package com.wen.tools.pool.exception;

/**
 * @author : WChen129
 * @date : 2018-05-30
 */
public class SessionFactoryException extends RuntimeException {
    public SessionFactoryException(String ression){
        super(ression);
    }
    public SessionFactoryException(String ression ,Throwable cause){
        super(ression,cause);
    }
}
