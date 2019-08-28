package com.wjwcloud.auth.service.impl;

import com.wjwcloud.auth.commons.exception.UserInvalidException;
import com.wjwcloud.auth.domain.vo.JwtAuthenticationRequest;
import com.wjwcloud.auth.domain.BaseUserEntity;
import com.wjwcloud.auth.jwt.JWTInfo;
import com.wjwcloud.auth.jwt.utils.JwtTokenUtil;
import com.wjwcloud.auth.service.AuthService;
import com.wjwcloud.auth.service.BaseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AuthServiceImpl implements AuthService {

    private JwtTokenUtil jwtTokenUtil;
    private BaseUserService baseUserService;

    @Autowired
    public AuthServiceImpl(JwtTokenUtil jwtTokenUtil,BaseUserService baseUserService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.baseUserService = baseUserService;
    }

    /**
     * 用户登录
     * @param authenticationRequest
     * @return
     * @throws Exception
     */
    @Override
    public String login(JwtAuthenticationRequest authenticationRequest) throws Exception {
        BaseUserEntity info = baseUserService.validate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        if (!StringUtils.isEmpty(info.getId())) {
            return jwtTokenUtil.generateToken(new JWTInfo(info.getUsername(), info.getId() + "", info.getName(), info.getComId() + ""));
        }
        throw new UserInvalidException("用户不存在或账户密码错误!");
    }

    /**
     * token校验
     * @param token
     * @throws Exception
     */
    @Override
    public void validate(String token) throws Exception {
        jwtTokenUtil.getInfoFromToken(token);
    }

    /**
     * token刷新
     * @param oldToken
     * @return
     * @throws Exception
     */
    @Override
    public String refresh(String oldToken) throws Exception {
        return jwtTokenUtil.generateToken(jwtTokenUtil.getInfoFromToken(oldToken));
    }
}
