package com.wjwcloud.auth.service;

import com.wjwcloud.auth.domain.BaseUserEntity;

public interface BaseUserService {
    BaseUserEntity getUserByUsername(String username);
    BaseUserEntity validate(String username,String password);

}
