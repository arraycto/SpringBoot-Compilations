package com.wjwcloud.iot.model;


import java.io.Serializable;

public class ResultResponse implements Serializable {
    private String code;
    private String message;
    private Object data;

    public static ResultResponse SUCCESSFUL() {
        return new ResultResponse(ResultCode.SUCCESSFUL, ResultCode.SUCCESSFUL_MESSAGE);
    }

    public static ResultResponse SUCCESSFUL(Object data) {
        return new ResultResponse(ResultCode.SUCCESSFUL, ResultCode.SUCCESSFUL_MESSAGE, data);
    }

    public static ResultResponse SUCCESSFUL(Object data, String message) {
        return new ResultResponse(ResultCode.SUCCESSFUL, message, data);
    }

    public static ResultResponse FAILED() {
        return new ResultResponse(ResultCode.FAILED, ResultCode.FAILED_MESSAGE);
    }

    public static ResultResponse FAILED(String message) {
        return new ResultResponse(ResultCode.FAILED, message);
    }

    public static ResultResponse FAILED(Object data, String message) {
        return new ResultResponse(ResultCode.FAILED, message, data);
    }

    public ResultResponse() {
        this.code = ResultCode.SUCCESSFUL_MESSAGE;
        this.message = ResultCode.SUCCESSFUL_MESSAGE;
    }

    public ResultResponse(String code) {
        this.code = code;
        this.message = code;
    }

    public ResultResponse(String code, String message) {
        this.message = message;
        this.code = code;
    }

    public ResultResponse(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String toString() {
        return "{code='" + this.code + "', message='" + this.message + "'}";
    }
}

