package com.wen.pool;

import com.wen.pool.bean.SessionFactory;
import org.junit.Test;

import java.util.List;

/**
 * @author : WChen129
 * @date : 2018-07-10
 */
public class test {
    @Test
    public void testSessionFactory(){
        SessionFactory sessionFactory=SessionFactory.getSessionFactory ();
        List<String> result=   sessionFactory.queryForList ("select * from user",String.class);
        System.out.println (result);
    }
}
