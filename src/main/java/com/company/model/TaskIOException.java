package com.company.model;

public class TaskIOException extends Exception { //exc in TaskOI.java supposedly
    public TaskIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskIOException(Throwable cause) {
        super(cause);
    }

}
