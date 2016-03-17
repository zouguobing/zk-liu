package com.bt.liu.support;

/**
 * Created by binglove on 16/3/13.
 */
public class BaseRes<T> {

    protected static final int FAIL_CODE = 1;

    protected static final int SUCC_CODE = 0;

    private int code = SUCC_CODE;
    
    private String error;

    private T data;

    public int getCode() {
        return code;
    }

    public String getError() {
        return error;
    }

    public BaseRes<T> setError(String error) {
        this.code = FAIL_CODE;
        this.error = error;
        return this;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSucccess() {
        return this.code == SUCC_CODE;
    }
    
}
