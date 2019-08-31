package com.webserver.http;

/**
 * 空请求异常
 * 当HttpRequest在解析请求时,发现此次请求为空请求
 * 时会抛出该异常.
 *
 * @author Aijiang
 *
 */
public class EmptyRequestException extends Exception{
    private static final long serialVersionUID = 1L;

    EmptyRequestException() {
        super();
    }

    public EmptyRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public EmptyRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyRequestException(String message) {
        super(message);
    }

    public EmptyRequestException(Throwable cause) {
        super(cause);
    }

}
