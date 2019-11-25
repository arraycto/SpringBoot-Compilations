package com.geer2.iot.bootstrap.producer;

import com.geer2.iot.auto.MqttListener;
import com.geer2.iot.bootstrap.Bean.SubMessage;
import com.geer2.iot.properties.ConnectOptions;
import io.netty.channel.Channel;

import java.util.List;

/**
 * 生产者
 *
 * @author JiaweiWu
 * @create 2019-11-04 17:17
 **/
public interface Producer {

    Channel getChannel();

    Producer connect(ConnectOptions connectOptions);

    void  close();

    void setMqttListener(MqttListener mqttListener);

    void pub(String topic,String message,boolean retained,int qos);

    void pub(String topic,String message);

    void pub(String topic,String message,int qos);

    void pub(String topic,String message,boolean retained);

    void sub(SubMessage... subMessages);

    void unsub(List<String> topics);

    void unsub();

    void disConnect();

}
