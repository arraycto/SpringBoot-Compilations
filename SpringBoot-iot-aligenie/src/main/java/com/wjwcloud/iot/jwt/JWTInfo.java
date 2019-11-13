package com.wjwcloud.iot.jwt;

import java.io.Serializable;


public class JWTInfo implements Serializable,IJWTInfo {
    /**
     * 唯一名称，可是是用来登陆
     */
    private String username;
    private String userId;
    /**
     * 用户昵称
     */
    private String name;
    private String comId;
    public JWTInfo(String username, String userId, String name, String comId) {
        this.username = username;
        this.userId = userId;
        this.name = name;
        this.comId = comId;
    }

    @Override
    public String getUniqueName() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setComId(String comId){
        this.comId = comId;
    }
    @Override
    public String getComId() {
        return comId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JWTInfo jwtInfo = (JWTInfo) o;

        if (username != null ? !username.equals(jwtInfo.username) : jwtInfo.username != null) {
            return false;
        }
        if (comId != null ? !comId.equals(jwtInfo.comId) : jwtInfo.comId != null) {
            return false;
        }
        return userId != null ? userId.equals(jwtInfo.userId) : jwtInfo.userId == null;

    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
