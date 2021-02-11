package com.wen.tools.pool;

import com.wen.tools.pool.bean.SessionFactory;

public class Test {
    public static void main(String[] args) {
     SessionFactory sessionFactory=   SessionFactory.getSessionFactory();
        System.out.println( sessionFactory.queryForMap("select * from dw_clsfd.clsfd_sap_directio_ebayk"));
    ;
    }
}
