package io.xxnjdg.learning.amqp.springrabbitapi;

import com.rabbitmq.client.Channel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import java.io.IOException;
import java.util.concurrent.Executor;

@Configuration
public class ReceiveTest {

    private static Log log = LogFactory.getLog(ReceiveTest.class);


    //必须是org.springframework.core.task.TaskExecutor的子类
    @Bean
    public Executor myThreadPool(){
        return new SimpleAsyncTaskExecutor("myThreadPool" + "-");
    }

    @Bean
    public RabbitListenerErrorHandler myErrorHandler(){
        return (amqpMessage, message, exception) -> {
            log.info(amqpMessage);
            log.info(message);
            log.info(exception);
            //允许ack和nack
            message.getHeaders().get(AmqpHeaders.CHANNEL,Channel.class)
                    .basicNack(message.getHeaders().get(AmqpHeaders.DELIVERY_TAG,Long.class),false,false);
//                    .basicReject(message.getHeaders().get(AmqpHeaders.DELIVERY_TAG,Long.class),true);
            //往上抛异常
            throw exception;
        };
    }

    //concurrency 线程数
    //executor 线程池
    //ackMode 确认方式
    //errorHandler 异常方法
    @RabbitListener(queues = "hello", concurrency = "2",
            executor = "myThreadPool",ackMode = "MANUAL",errorHandler = "myErrorHandler")
    public void test(Message data, Channel channel) {
        try {
            String s = new String(data.getBody());
            log.info(s);
            if(s.equals("hello")){
                throw new RuntimeException("my custom exception");
            }
            String name = Thread.currentThread().getName();
            long deliveryTag = data.getMessageProperties().getDeliveryTag();
            log.info(name + " " + data);
            channel.basicAck(deliveryTag,false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //自动声明
    //bindings 绑定
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "testRabbitQueue",durable = "true"),
            exchange = @Exchange(name = "testRabbitExchange",type = ExchangeTypes.TOPIC),
            key = "test.#"
    ))
    public void test1(Message data, Channel channel) {
        log.info(new String(data.getBody()));
        log.info(data);
    }



}
