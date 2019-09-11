package com.wen.tools.pool.jdbc;



import com.wen.tools.pool.entry.DataSource;
import com.wen.tools.pool.exception.JDBCException;
import com.wen.tools.pool.domain.ToolsPoolIConstants;
import com.wen.tools.pool.exception.SessionFactoryException;
import com.wen.tools.cryption.CryptionUtil;
import com.wen.tools.domain.utils.GetValueUtils;
import com.wen.tools.domain.utils.ParameterUtils;
import com.wen.tools.log.utils.LogUtil;

import java.sql.*;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.LongAdder;

public class JDBCHelper {

    // 数据库连接池
    private static int maxNum = 20;
    private static String url;
    private static String user;
    private static String password;
    private static int GET_CONNECTION_MAX_RETEY_TIMES;
    private static int RETEY_SLEEP_TIMES;
    private LongAdder current_queue_size=new LongAdder();

    private ConcurrentLinkedQueue<Connection> connectionQueue = new ConcurrentLinkedQueue<>() ;
    private static Properties properties=null;
    static {
        try {
            properties=ParameterUtils.fromPropertiesFile(System.getProperty(ToolsPoolIConstants.CONFIG.JDBC_POOL_CONFIG_GILE)==null?ToolsPoolIConstants.CONFIG.JDBC_POOL_CONFIG_GILE:System.getProperty(ToolsPoolIConstants.CONFIG.JDBC_POOL_CONFIG_GILE)) .getProperties();
            String driver = properties.getProperty(ToolsPoolIConstants.JDBC.JDBC_DRIVER);
            maxNum = GetValueUtils.getIntegerOrElse(properties.getProperty(ToolsPoolIConstants.JDBC.JDBC_DATASOURCE_MAX_SIZE), 20);
            url = properties.getProperty(ToolsPoolIConstants.JDBC.JDBC_URL);
            user = properties.getProperty(ToolsPoolIConstants.JDBC.JDBC_USER);
            password = properties.getProperty(ToolsPoolIConstants.JDBC.JDBC_PASSWORD);
            password=CryptionUtil.getDecryptionString(password);
            LogUtil.getCoreLog().info("init JDBCHelper,driver:{},url:{},user:",driver,url,user);
            GET_CONNECTION_MAX_RETEY_TIMES = GetValueUtils.getIntegerOrElse(properties.getProperty(ToolsPoolIConstants.JDBC.JDBC_DATASOURCE_GET_CONNECTION_MAX_RETEY_TIMES), 3);
            RETEY_SLEEP_TIMES=GetValueUtils.getIntegerOrElse(properties.getProperty(ToolsPoolIConstants.JDBC.JDBC_DATASOURCE_GET_CONNECTION_RETEY_SLEEP_TIMES), 1000);
            Class.forName(driver);
        } catch (Exception e) {
            e.printStackTrace();
           throw  new RuntimeException(e);
        }
    }

    // 第二步，实现JDBCHelper的单例化
    // 为什么要实现代理化呢？因为它的内部要封装一个简单的内部的数据库连接池
    // 为了保证数据库连接池有且仅有一份，所以就通过单例的方式
    // 保证JDBCHelper只有一个实例，实例中只有一份数据库连接池
    private static volatile JDBCHelper instance = null;

    /**
     * 获取单例
     *
     * @return 单例
     */
    public static JDBCHelper getInstance() {
        return Singleton.INSTANCE.getInstance ();
    }

    private enum Singleton {
        INSTANCE;
        private JDBCHelper singleton;

        Singleton() {
            singleton = new JDBCHelper ();
        }

        public JDBCHelper getInstance() {
            return singleton;
        }
    }


    /**
     * 构造方法加载连接池
     */
    private JDBCHelper() {
        int datasourceSize = GetValueUtils.getIntegerOrElse(properties.getProperty(ToolsPoolIConstants.JDBC.JDBC_DATASOURCE_SIZE), 1);
        try {
            current_queue_size.increment();
            initDataSourcre(datasourceSize);
            LogUtil.getCoreLog().info("init connection successfully");
        }catch (Exception e){
            e.printStackTrace();
            throw new JDBCException (e);
        }
    }

    private synchronized void initDataSourcre(int datasourceSize) throws Exception {
        for (int i = 0; i < datasourceSize; i++) {
             try {
                 Connection conn = DriverManager.getConnection(url, user, password);
                 connectionQueue.add(conn);
             }catch (Exception e){
                 e.printStackTrace();
                 current_queue_size.decrement();
                 throw  new JDBCException("create connection error");
             }
            LogUtil.getCoreLog().info("create a new Connection successfully, now contain connection total:{},avaliable:{}",current_queue_size.longValue(),connectionQueue.size());
        }
    }

    /**
     * 第四步，提供获取数据库连接的方法
     * 有可能，你去获取的时候，这个时候，连接都被用光了，你暂时获取不到数据库连接
     * 所以我们要自己编码实现一个简单的等待机制，去等待获取到数据库连接
     */
    public  DataSource getConnection() {
        try {
            int time = 0;
            while (connectionQueue.size() == 0&&time < GET_CONNECTION_MAX_RETEY_TIMES) {
                if (current_queue_size.longValue() < maxNum) {
                    current_queue_size.increment();
                    initDataSourcre(1);
                } else {
                    time++;
                    Thread.sleep(RETEY_SLEEP_TIMES);
                }
            }
            if(time>=GET_CONNECTION_MAX_RETEY_TIMES){
                throw new SessionFactoryException("get connection time out");
            }
            Connection connection = connectionQueue.poll();
            if (connection==null||connection.isClosed()) {
                current_queue_size.decrement();
                return getConnection();
            }
            connection.setAutoCommit(true);
            LogUtil.getCoreLog().info("get a connection successfully,now contain connection total:{},avaliable:{}",current_queue_size.longValue(),connectionQueue.size());
            return new DataSource(connection);
        } catch (Exception e) {
           e.printStackTrace();
           throw new SessionFactoryException("get connection error");
        }
    }

    /**
     * 归还数据库连接
     * @param dataSource
     */
    public synchronized void backConnection(DataSource dataSource) {
        connectionQueue.add(dataSource.getConnection());
        dataSource.setConnection(null);
        LogUtil.getCoreLog().info("back a connection successfully, now contain connection total:{},avaliable:{}",current_queue_size.longValue(),connectionQueue.size());
    }


    /**
     * 执行增删改SQL语句
     * 自动事务执行 执行update
     * @param sql
     * @return 影响的行数
     */
    public int executeUpdate(String sql, Object[] params) {
        int rtn=0;
        DataSource source=getConnection();
        try {
            executeUpdate(source,sql,params);
        }catch (JDBCException e){
            e.printStackTrace();
            throw new JDBCException (e.getMessage(),e.getCause());
        }finally {
            backConnection(source);
        }
        return rtn;
    }

    /**
     * 执行增删改SQL语句
     * 手动事务执行 执行update
     * @param sql
     * @return 影响的行数
     */
    public int executeUpdate( DataSource source,String sql, Object[] params) {
        int rtn=0;
        try {
            PreparedStatement  pstmt =getPrepareStatementSql(source.getConnection(),sql,params);
            rtn = pstmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
            throw new JDBCException(e.getMessage(),e.getCause());
        }
        return rtn;
    }

    /**
     * 执行查询SQL语句
     * 自动事务检索
     * @param sql
     * @param params
     */
    public ResultSet executeQuery(String sql, Object[] params){
        DataSource source=getConnection();
        Connection connection=source.getConnection();
        ResultSet rs=null;
        try {
            PreparedStatement  pstmt =getPrepareStatementSql(connection,sql,params);
             rs=pstmt.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            backConnection(source);
        }
        return rs;
    }



    /**
     *
     * @param connection
     * @param sql
     * @param params
     * @return 带事务的编译
     * @throws SQLException
     */
    private PreparedStatement getPrepareStatementSql(Connection connection,String sql, Object[] params)throws SQLException{
        PreparedStatement  pstmt = connection.prepareStatement(sql);
        if(params!=null&&params.length>0){
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i+1, params[i]);
            }
        }
        return pstmt;
    }

    /**
     * 批量执行SQL语句
     * <p>
     * 批量执行SQL语句，是JDBC中的一个高级功能
     * 默认情况下，每次执行一条SQL语句，就会通过网络连接，向MySQL发送一次请求
     * <p>
     * 但是，如果在短时间内要执行多条结构完全一模一样的SQL，只是参数不同
     * 虽然使用PreparedStatement这种方式，可以只编译一次SQL，提高性能，但是，还是对于每次SQL
     * 都要向MySQL发送一次网络请求
     * <p>
     * 可以通过批量执行SQL语句的功能优化这个性能
     * 一次性通过PreparedStatement发送多条SQL语句，比如100条、1000条，甚至上万条
     * 执行的时候，也仅仅编译一次就可以
     * 这种批量执行SQL语句的方式，可以大大提升性能
     *
     * @param sql
     * @param paramsList
     * @return 每条SQL语句影响的行数
     */

    public int[] executeBatch( Connection conn,String sql, List<Object[]> paramsList) throws SQLException{
        int[] rtn = null;
        PreparedStatement pstmt = null;
        // 第一步：使用Connection对象，取消自动提交
        pstmt = conn.prepareStatement(sql);
        // 第二步：使用PreparedStatement.addBatch()方法加入批量的SQL参数
        for (Object[] params : paramsList) {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            pstmt.addBatch();
        }
        // 第三步：使用PreparedStatement.executeBatch()方法，执行批量的SQL语句
        rtn = pstmt.executeBatch();
        // 最后一步：使用Connection对象，提交批量的SQL语句
        return rtn;
    }


}
