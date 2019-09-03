package com.wen.tools.io.exception;

public class ToolsIOException extends RuntimeException {
    public ToolsIOException(String ression){
        super(ression);
    }
    public ToolsIOException(String ression ,Throwable cause){
        super(ression,cause);
    }
    public ToolsIOException(Exception e){
        super(e);
    }
}
