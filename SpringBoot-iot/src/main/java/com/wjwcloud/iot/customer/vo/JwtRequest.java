package com.wjwcloud.iot.customer.vo;

import lombok.Data;

/**
 * @author JiaweiWu
 */
@Data
public class JwtRequest {
     private String mobilePhone;
     private String userName;
     private String password;
}
