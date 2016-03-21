package com.bt.liu.dto;

/**
 * Created by binglove on 16/3/17.
 */
public class LiuConfig {

    //key键
    private String key;

    //value值
    private String value;

    //操作人
    private String operator;

    //描述
    private String memo;

    //最后操作时间
    private String updateTime;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
