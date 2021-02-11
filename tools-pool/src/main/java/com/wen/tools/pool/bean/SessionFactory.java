package com.wen.tools.pool.bean;

import com.alibaba.fastjson.JSON;
import com.wen.tools.pool.domain.ToolsPoolIConstants;
import com.wen.tools.pool.entry.CommonColumns;
import com.wen.tools.pool.entry.DataSource;
import com.wen.tools.pool.exception.SessionFactoryException;
import com.wen.tools.pool.jdbc.JDBCHelper;
import com.wen.tools.pool.utils.BeanUtil;
import com.wen.tools.log.utils.LogUtil;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SessionFactory {
    /**
     * 连接信息
     */
    private DataSource dataSource;
    /**
     * 标注当前是否是手动事务的还是自动事务的
     */
    private boolean transaction = false;
    /**
     * 获得对象
     */
    private static JDBCHelper jdbcHelper = JDBCHelper.getInstance();

    private SessionFactory(DataSource dataSource, boolean transaction) {
        this.dataSource = dataSource;
        this.transaction = transaction;
    }

    private SessionFactory() {
    }

    public static SessionFactory getSessionFactory() {
        return new SessionFactory(null, false);
    }

    /**
     * 注意 当取得手动事务的session  需要手动去提交事务  和close   sessionFactory  不然会导致 增删改失败和丢失连接数
     *
     * @return
     */
    public static SessionFactory getTransactionSessionFactory() {
        SessionFactory sessionFactory = new SessionFactory(jdbcHelper.getConnection(), true);
        sessionFactory.setAutoCommit(false);
        return sessionFactory;
    }

    /**
     * 提交事务
     *
     * @return
     */
    public boolean commit() {
        try {
            dataSource.getConnection().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 回滚事务
     *
     * @return
     */
    public boolean rollback() {
        try {
            dataSource.getConnection().rollback();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 设置自动提交  默认的是true
     *
     * @return
     */
    public boolean setAutoCommit(boolean flag) {
        try {
            dataSource.getConnection().setAutoCommit(flag);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 增删改方法
     *
     * @param sql
     * @param params
     * @return
     */

    public int executeUpdate(String sql, Object[] params) {
        if (transaction == true) {
            return jdbcHelper.executeUpdate(dataSource, sql, params);
        } else {
            return jdbcHelper.executeUpdate(sql, params);
        }
    }

    /**
     * @param sql
     * @param params
     * @return
     */
    public Map<String, Object> queryForMap(String sql, Object[] params) {
        try {
            ResultSet set = jdbcHelper.executeQuery(sql, params);
            List<Map<String, Object>> list = ResultSetToMap(set);
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param sql
     * @return
     */
    public Map<String, Object> queryForMap(String sql) {
        return queryForMap(sql, null);
    }

    /**
     * 返回一个map  形式的查找结果
     *
     * @param rs
     * @return
     */
    private List<Map<String, Object>> ResultSetToMap(ResultSet rs) {
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int count = rsmd.getColumnCount();
            String[] name = new String[count];
            for (int i = 0; i < count; i++) {
                name[i] = rsmd.getColumnName(i + 1).toLowerCase();
            }
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            Map<String, Object> map = null;
            while (rs.next()) {
                map = new HashMap<>();
                for (int i = 0; i < count; i++) {
                    try {
                        map.put(name[i], rs.getObject(name[i]));
                    } catch (SQLException e) {
                        LogUtil.getCoreLog().warn("meet error when get value by using column name:{} will use index:{} to get data", name[i], (i + 1));
                        map.put(name[i], rs.getObject(i + 1));
                    }
                }
                result.add(map);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SessionFactoryException(e.getMessage());
        }
    }


    public <T> T queryForObject(String sql, Class<T> clazz) {
        return queryForObject(sql, null, clazz);
    }

    /**
     * 返回一个对象形式的查找结果
     *
     * @param sql
     * @param params
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T queryForObject(String sql, Object[] params, Class<T> clazz) {
        Map<String, Object> map = queryForMap(sql, params);
        if (null != map) {
            try {
                return BeanUtil.mapToBean(clazz, map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public <T> List<T> queryForList(String sql, Class<T> clazz) {
        return queryForList(sql, null, clazz);
    }

    public <T> List<T> queryForList(String sql, Object[] params, Class<T> clazz) {
        try {
            ResultSet set = jdbcHelper.executeQuery(sql, params);
            List<Map<String, Object>> list = ResultSetToMap(set);
            if (clazz.getName().equals("java.util.Map") || clazz.newInstance() instanceof Map) {
                return (List<T>) list;
            }
            if (clazz.newInstance() instanceof String) {
                List<String> stringList = new ArrayList<>();
                for (Map<String, Object> map : list) {
                    stringList.add(JSON.toJSONString(map));
                }
                return (List<T>) stringList;
            }
            return mapsToObjects(list, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SessionFactoryException(e.getMessage());
        }
    }


    /**
     * 根据sql  获取String
     *
     * @param sql
     * @return
     */
    public String queryForString(String sql) {
        return queryForString(sql, null);
    }

    /**
     * @param sql
     * @param params
     * @return
     */
    public String queryForString(String sql, Object[] params) {
        try {
            ResultSet set = jdbcHelper.executeQuery(sql, params);
            List<Map<String, Object>> list = ResultSetToMap(set);
            if (list != null && list.size() > 0) {
                return com.alibaba.fastjson.JSON.toJSONString(list.get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SessionFactoryException(e.getMessage());
        }
        return "";
    }


    private <T> List<T> mapsToObjects(List<Map<String, Object>> maps, Class<T> clazz) throws Exception {
        List<T> list = new ArrayList<>();
        if (maps != null && maps.size() > 0) {
            T bean = null;
            for (Map<String, Object> map : maps) {
                bean = BeanUtil.mapToBean(clazz, map);
                list.add(bean);
            }
        }
        return list;
    }


    /**
     * get direct original data
     *
     * @param sql
     * @return
     */
    public ResultSet getOriginalData(String sql) {
        return jdbcHelper.executeQuery(sql, null);
    }

    /**
     * get all table columns by table name
     *
     * @param tableName
     * @return
     */
    public List<CommonColumns> getTableColumnsByTableName(String tableName) {
        if(StringUtils.isBlank(tableName)){
            LogUtil.getCoreLog().error("table name is blank when get table columns");
            throw new IllegalArgumentException("table name is blank:"+tableName);
        }
        String[] str = new String[1];
        str[0] = tableName;
        return queryForList(ToolsPoolIConstants.DYNAMIC_SQL.GET_COLUMNS_SQL,str,CommonColumns.class);
    }
    public List<CommonColumns> getTablePrimaryKeyByTableName(String tableName) {
        if(StringUtils.isBlank(tableName)){
            LogUtil.getCoreLog().error("table name is blank when get table primary key");
            throw new IllegalArgumentException("table name is blank:"+tableName);
        }
        String[] str = new String[1];
        str[0] = tableName;
        return queryForList(ToolsPoolIConstants.DYNAMIC_SQL.GET_PRIMARY_KEY_SQL,str,CommonColumns.class);
    }

    /**
     * 关闭连接
     * if use transaction connection need back connecttion
     */
    public void close() {
        if (dataSource != null) {
            JDBCHelper.getInstance().backConnection(dataSource);
        }
        this.transaction = true;
        this.dataSource = null;
    }
}
