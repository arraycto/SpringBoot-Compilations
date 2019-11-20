package com.geer2.nettyprotocol.server.queue;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

/**
 * @author JiaweiWu
 */
@Component
public class DisruptorMessageStarter implements  MessageStarter<MessageEvent> , DisposableBean {


    /**
     * //创建disruptor, 泛型参数:传递的事件的类型
     *         // 第一个参数: 产生Event的工厂类, Event封装生成-消费的数据
     *         // 第二个参数: RingBuffer的缓冲区大小 （设置RingBuffer大小, 需为2的N次方(能将求模运算转为位运算提高效率 ), 否则影响性能）
     *         // 第三个参数: 线程池
     *         // 第四个参数: SINGLE单个生产者, MULTI多个生产者
     *         // 第五个参数: WaitStrategy 当消费者阻塞在SequenceBarrier上, 消费者如何等待的策略.
     *             //BlockingWaitStrategy 使用锁和条件变量, 效率较低, 但CPU的消耗最小, 在不同部署环境下性能表现比较一致
     *             //SleepingWaitStrategy 多次循环尝试不成功后, 让出CPU, 等待下次调度; 多次调度后仍不成功, 睡眠纳秒级别的时间再尝试. 平衡了延迟和CPU资源占用, 但延迟不均匀.
     *             //YieldingWaitStrategy 多次循环尝试不成功后, 让出CPU, 等待下次调度. 平衡了延迟和CPU资源占用, 延迟也比较均匀.
     *             //BusySpinWaitStrategy 自旋等待，类似自旋锁. 低延迟但同时对CPU资源的占用也多.
     */
    private Disruptor<MessageEvent> disruptor = new Disruptor<>(MessageEvent::new,
            1024, DaemonThreadFactory.INSTANCE, ProducerType.MULTI,
            new BlockingWaitStrategy());

    private final EventHandler<MessageEvent> eventHandler;

    public DisruptorMessageStarter(EventHandler<MessageEvent> eventHandler) {
        this.eventHandler = eventHandler;
        // 注册事件消费处理器, 也即消费者. 可传入多个EventHandler ...
        disruptor.handleEventsWith(eventHandler);
        // 启动
        disruptor.start();
    }


    @Override
    public RingBuffer<MessageEvent> getRingBuffer() {
        return disruptor.getRingBuffer();
    }

    @Override
    public void shutdown() {
        disruptor.shutdown();
    }


    @Override
    public void destroy() throws Exception {
        shutdown();
    }
}
