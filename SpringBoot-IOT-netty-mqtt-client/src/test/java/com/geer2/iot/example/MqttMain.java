package com.geer2.iot.example;

import com.geer2.iot.auto.MqttListener;
import com.geer2.iot.bootstrap.bean.SubMessage;
import com.geer2.iot.bootstrap.producer.MqttProducer;
import com.geer2.iot.bootstrap.producer.Producer;
import com.geer2.iot.properties.ConnectOptions;
import io.netty.handler.codec.mqtt.MqttQoS;

/**
 * 测试
 *
 * @author JiaweiWu
 * @create 2019-11-25
 **/
public class MqttMain {

    public static void main(String[] strings){
        Producer producer = new MqttProducer();
        ConnectOptions connectOptions = new ConnectOptions();
        connectOptions.setBacklog(1024);
        connectOptions.setConnectTime(1000l);
        connectOptions.setSsl(false);
        connectOptions.setJksStorePassword("mu$tch8ng3");
        connectOptions.setJksFile("/securesocket.jks");
        connectOptions.setJksCertificatePassword("inc0rrect");
        connectOptions.setServerIp("10.0.75.1");
        connectOptions.setPort(1883);
        connectOptions.setBossThread(1);
        connectOptions.setWorkThread(8);
        connectOptions.setMinPeriod(10);
        connectOptions.setRevbuf(1024);
        connectOptions.setSndbuf(1024);
        connectOptions.setTcpNodelay(true);
        connectOptions.setKeepalive(true);
        connectOptions.setHeart(5);
        ConnectOptions.MqttOpntions mqttOpntions = new ConnectOptions.MqttOpntions();
        mqttOpntions.setHasCleanSession(true);
        mqttOpntions.setHasWillFlag(true);

        mqttOpntions.setClientIdentifier("mac-125-123|authType=online,random=12345,signmethod=hmacmd5|");

        mqttOpntions.setWillTopic("/lose/device");
        mqttOpntions.setWillQos(1);
        mqttOpntions.setHasWillRetain(false);
        mqttOpntions.setWillMessage("123123");
        mqttOpntions.setHasPassword(true);
        mqttOpntions.setHasPassword(true);
        mqttOpntions.setUserName("mac-125-123&7a84e627b816ccbd");
        mqttOpntions.setPassword("db18e537405ee406ae8e307ba6ef2d6a");
        mqttOpntions.setKeepAliveTime(5);
        connectOptions.setMqtt(mqttOpntions);
        producer.setMqttListener(new MqttListener() {
            @Override
            public void callBack(String topic, String msg) {
                        System.out.print("========================================"+topic+msg);
            }
            @Override
            public void callThrowable(Throwable e) {

            }
        });
        producer.connect(connectOptions);
        producer.sub(SubMessage.builder().qos(MqttQoS.AT_MOST_ONCE).topic("/7a84e627b816ccbd/mac-125-123/ext/register").build());
//        producer.pub("/test","hah",2);
    }

}
