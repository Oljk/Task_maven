package com.company.model;

/**
 * Исключение, которое выбрасывает TaskIO методы при работе с файлами
 * @see com.company.model.TaskIO
 * @see java.lang.Exception
 * @author olga
 * @version 1.0
 */
public class TaskIOException extends Exception {
    public TaskIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskIOException(Throwable cause) {
        super(cause);
    }

}
