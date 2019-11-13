package com.wjwcloud.iot.model;

import java.io.Serializable;

public abstract class AbstructBaseVo<ID> implements Serializable {
    private static final long serialVersionUID = 1L;
    private String keyCode;

    public AbstructBaseVo() {
    }

    public abstract ID getId();

    public abstract void setId(ID var1);

    public String getKeyCode() {
        return this.keyCode;
    }

    public void setKeyCode(String keyCode) {
        this.keyCode = keyCode;
    }
}
