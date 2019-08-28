package com.wjwcloud.auth.service.impl;

import com.wjwcloud.auth.commons.constant.CommonConstants;
import com.wjwcloud.auth.dao.BaseUserDao;
import com.wjwcloud.auth.domain.BaseUserEntity;
import com.wjwcloud.auth.service.BaseUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BaseUserServiceImpl implements BaseUserService{

    @Autowired
    private BaseUserDao baseUserDao;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(CommonConstants.STRENGTH);

    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
//    @Cache(key = "user{1}")
    @Override
    public BaseUserEntity getUserByUsername(String username) {
        BaseUserEntity baseUserEntity = new BaseUserEntity();
        baseUserEntity.setUsername(username);
        return baseUserDao.selectOne(baseUserEntity);
    }

    /**
     * 校验数据库用户数据
     * @param username
     * @param password
     * @return
     */
    @Override
    public BaseUserEntity validate(String username, String password) {
        BaseUserEntity info = this.getUserByUsername(username);
        if (info != null){
            if (encoder.matches(password, info.getPassword())) {
                return info;
            }
        }
        return new BaseUserEntity();
    }
}
