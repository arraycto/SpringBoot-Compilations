package com.wjwcloud.auth.domain;

import lombok.Data;

import java.sql.Date;

@Data
public class BaseEntity {
    private Date createTime;
    private Date updateTime;
    private String updatePerson;
    private String createPerson;
    private String isDelete;
}
