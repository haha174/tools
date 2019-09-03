package com.wen.tools.pool.exception;

public class JDBCException extends RuntimeException {
    public JDBCException(String ression){
        super(ression);
    }

    public JDBCException(String ression ,Throwable cause){
        super(ression,cause);
    }
    public JDBCException(Exception e){
        super(e);
    }
}
