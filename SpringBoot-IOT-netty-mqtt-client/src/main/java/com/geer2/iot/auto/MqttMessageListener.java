package com.geer2.iot.auto;

import io.netty.handler.codec.mqtt.MqttQoS;

import java.lang.annotation.*;

/**
 * @author JiaweiWu
 * 消费者配置注解类
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MqttMessageListener {

    String[] topic() ;

    MqttQoS qos() default MqttQoS.AT_MOST_ONCE;

}
