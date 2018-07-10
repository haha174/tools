package com.wen.pool.exception;

public class JDBCException extends RuntimeException {
    public JDBCException(String ression){
        super(ression);
    }

    public JDBCException(String ression ,Throwable cause){
        super(ression,cause);
    }
}
