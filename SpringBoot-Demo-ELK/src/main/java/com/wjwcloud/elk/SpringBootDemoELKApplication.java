package com.wjwcloud.elk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author JiaweiWu
 */
@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan(basePackages = {"com.wjwcloud.elk"})
public class SpringBootDemoELKApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoELKApplication.class, args);
    }

}
