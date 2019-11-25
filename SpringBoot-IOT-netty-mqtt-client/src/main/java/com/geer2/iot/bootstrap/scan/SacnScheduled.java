package com.geer2.iot.bootstrap.scan;

import com.geer2.iot.bootstrap.producer.Producer;
import com.geer2.iot.bootstrap.bean.SendMqttMessage;
import com.geer2.iot.pool.Scheduled;
import io.netty.handler.codec.mqtt.MqttMessageType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 扫描消息确认
 *
 * @author JiaweiWu
 * @create 2019-11-25 19:22
 **/
@Data
@Slf4j
public class SacnScheduled extends AbstractScanRunnable {

    private Producer producer;

    private  ScheduledFuture<?> submit;

    private  int  seconds;

    public SacnScheduled(Producer producer,int seconds) {
        this.producer=producer;
        this.seconds=seconds;
    }

    public  void start(){
        Scheduled  scheduled = new ScheduledPool();
        this.submit = scheduled.submit(this);
    }

    public  void close(){
        if(submit!=null && !submit.isCancelled()){
            submit.cancel(true);
        }
    }


    @Override
    public void doInfo(SendMqttMessage poll) {
        if(producer.getChannel().isActive()){
            if(checkTime(poll)){
                poll.setTimestamp(System.currentTimeMillis());
                switch (poll.getConfirmStatus()){
                    case PUB:
                        poll.setDup(true);
                        pubMessage(producer.getChannel(),poll);
                        break;
                    case PUBREC:
                        sendAck(MqttMessageType.PUBREC,true,producer.getChannel(),poll.getMessageId());
                        break;
                    case PUBREL:
                        sendAck(MqttMessageType.PUBREL,true,producer.getChannel(),poll.getMessageId());
                        break;
                    default:break;
                }

            }
        }
        else
        {
            log.info("channel is not alived");
            submit.cancel(true);
        }
    }

    private boolean checkTime(SendMqttMessage poll) {
        return System.currentTimeMillis()-poll.getTimestamp()>=seconds*1000;
    }

    private class ScheduledPool implements Scheduled {
        private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        @Override
        public ScheduledFuture<?> submit(Runnable runnable){
            return scheduledExecutorService.scheduleAtFixedRate(runnable,2,2, TimeUnit.SECONDS);
        }


    }

}
