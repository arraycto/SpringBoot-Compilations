package com.geer2.nettyprotocol.server.mqtt.channel;

import com.geer2.nettyprotocol.server.mqtt.api.BaseAuthService;
import org.springframework.stereotype.Service;

@Service
public class BaseAuthServerImpl implements BaseAuthService {

    @Override
    public boolean authorized(String username, String password) {
        return true;
    }
}
