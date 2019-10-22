package com.xajiusuo.jpa.util;

import com.xajiusuo.jpa.param.e.Result;

/**
 * Created by sht on 2018/1/22.
 */
public class ResponseBean {


    private  int statusCode;

    private  String message;

    private Object data;

    private Object o;

    public ResponseBean() {
    }

    public ResponseBean(Result r) {
        this(r.getCode().getCode(),r.getMsg(),r.getData());
        o = r.getO();
    }

    public ResponseBean(int statusCode, String message, Object data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public ResponseBean(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public Object getO() {
        return o;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
