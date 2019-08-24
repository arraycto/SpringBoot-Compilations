package com.wjwcloud.oauth.model;

import lombok.Builder;
import lombok.Getter;
import com.wjwcloud.oauth.enums.AuthResponseStatus;

/**
 * JustAuth统一授权响应类
 */
@Getter
@Builder
public class AuthResponse<T> {
    /**
     * 授权响应状态码
     */
    private int code;

    /**
     * 授权响应信息
     */
    private String msg;

    /**
     * 授权响应数据，当且仅当 code = 2000 时返回
     */
    private T data;

    /**
     * 是否请求成功
     *
     * @return true or false
     */
    public boolean ok() {
        return this.code == AuthResponseStatus.SUCCESS.getCode();
    }
}
