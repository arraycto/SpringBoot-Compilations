package com.geer2.nettyMqtt.server.channel;

import com.geer2.nettyMqtt.server.api.BaseAuthService;
import org.springframework.stereotype.Service;

@Service
public class BaseAuthServerImpl implements BaseAuthService {

    @Override
    public boolean authorized(String username, String password) {
        return true;
    }
}
