package com.geer2.iot.spring;

import com.geer2.iot.bootstrap.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author JiaweiWu
 * @create 2019-11-13 17:02
 **/
@Component
public class springTest {

    @Autowired
    private Producer producer;
    /**
     *  系统初始化自动配置了，直接注入此类进行操作
     */

}
