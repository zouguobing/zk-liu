package com.bt.liu.exception;

/**
 * Created by binglove on 16/1/29.
 */
public class ConvertRuntimeException extends RuntimeException {

    private static final long serialVersionUID = -78382390820930996L;

    public ConvertRuntimeException() {
        super();
    }

    public ConvertRuntimeException(String message) {
        super(message);
    }

    public ConvertRuntimeException(Throwable cause) {
        super(cause);
    }

    public ConvertRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

}
