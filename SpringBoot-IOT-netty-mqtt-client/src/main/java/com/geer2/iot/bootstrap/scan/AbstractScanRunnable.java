package com.geer2.iot.bootstrap.scan;

import com.geer2.iot.bootstrap.producer.MqttApi;
import com.geer2.iot.bootstrap.bean.SendMqttMessage;
import com.geer2.iot.commons.enums.ConfirmStatus;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 扫描未确认信息
 *
 * @author JiaweiWu
 * @create 2019-11-25 16:50
 **/

@Slf4j
public abstract class AbstractScanRunnable extends MqttApi implements Runnable {



    private ConcurrentLinkedQueue<SendMqttMessage> queue  = new ConcurrentLinkedQueue<>();


    public  boolean addQueue(SendMqttMessage t){
        return queue.add(t);
    }

    public  boolean addQueues(List<SendMqttMessage> ts){
        return queue.addAll(ts);
    }



    @Override
    public void run() {
        if(!queue.isEmpty()) {
            SendMqttMessage poll;
            List<SendMqttMessage> list = new LinkedList<>();
            for (; (poll = queue.poll()) != null; ) {
                if (poll.getConfirmStatus() != ConfirmStatus.COMPLETE) {
                    list.add(poll);
                    doInfo(poll);
                }
                break;
            }
            addQueues(list);
        }
    }

    /**
     *抽象方法 消息推送线程池
     * @param poll
     */
    public  abstract  void  doInfo( SendMqttMessage poll);


}
