package com.wjwcloud.elk.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

//@Entity
//@Table(name = "t_logger_infos")
@Data
public class LoggerEntity implements Serializable {

    /**
     * 应用程序名
     */
    private String applicationName;

    /**
     * spring.profiles.active
     */
    private String profileActive;

    /**
     * 客户端请求ip
     */
    private String clientIp;

    /**
     * 客户端请求路径
     */
    private String uri;

    /**
     * 客户端请求完整路径
     */
    private String url;

    /**
     * 请求方法名
     */
    private String methodName;

    /**
     * 请求类名
     */
    private String className;

    /**
     * 终端请求方式,普通请求,ajax请求
     */
    private String requestType;

    /**
     * 请求方式method,post,get等
     */
    private String requestMethod;

    /**
     * 请求参数内容,json
     */
    private String requestParamData;

    /**
     * 请求body参数内容,json
     */
    private String requestBodyData;

    /**
     * 请求接口唯一session标识
     */
    private String sessionId;

    /**
     * 请求时间
     */
    private Date requestDateTime;

    /**
     * 接口返回时间
     */
    private Date responseDateTime;

    /**
     * 接口返回数据json
     */
    private String responseData;

    /**
     * 请求时httpStatusCode代码，如：200,400,404等
     */
    private String httpStatusCode;

    /**
     * 请求耗时秒单位
     */
    private long spentTime;

}