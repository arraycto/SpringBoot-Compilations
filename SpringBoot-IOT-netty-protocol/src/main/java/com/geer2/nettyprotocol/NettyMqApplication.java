package com.geer2.nettyprotocol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author JiaweiWu
 */
@SpringBootApplication
@ComponentScan("com.geer2")
public class NettyMqApplication {
	public static void main(String[] args) {
		SpringApplication.run(NettyMqApplication.class, args);
	}

}
