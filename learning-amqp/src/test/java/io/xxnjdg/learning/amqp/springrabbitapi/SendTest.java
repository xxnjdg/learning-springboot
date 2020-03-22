package io.xxnjdg.learning.amqp.springrabbitapi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SendTest {

    private static final Log LOG = LogFactory.getLog(SendTest.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void sendTest1(){
        //消息属性
        MessageProperties properties = new MessageProperties();
        properties.setExpiration("10000"); //过期时间
        properties.getHeaders().put("hello1","world1");

        Message message = MessageBuilder.withBody("hello world11".getBytes())
                .andProperties(properties)
                .setDeliveryMode(MessageDeliveryMode.PERSISTENT) //持久化
                .setHeader("hello","world")
                .build();

        //确认
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (correlationData != null){
                LOG.info("Confirm "+correlationData.getId());
            }
            LOG.info("Confirm "+ack);
            LOG.info("Confirm "+cause);
        });

        //返回
        rabbitTemplate.setReturnCallback((message1, replyCode, replyText, exchange, routingKey) -> {
            LOG.info("Return "+message1);
            LOG.info("Return "+replyCode);
            LOG.info("Return "+replyText);
            LOG.info("Return "+exchange);
            LOG.info("Return "+routingKey);
        });

        //保证消息id全局唯一
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId("123456789");

        //发送
        rabbitTemplate.convertAndSend("testRabbitExchange","test.test",message,correlationData);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void sendTest2(){

        //确认
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (correlationData != null){
                LOG.info("Confirm "+correlationData.getId());
            }
            LOG.info("Confirm "+ack);
            LOG.info("Confirm "+cause);
        });

        //返回
        rabbitTemplate.setReturnCallback((message1, replyCode, replyText, exchange, routingKey) -> {
            LOG.info("Return "+message1);
            LOG.info("Return "+replyCode);
            LOG.info("Return "+replyText);
            LOG.info("Return "+exchange);
            LOG.info("Return "+routingKey);
        });


        rabbitTemplate.convertAndSend("allArgsExchange","allArgsKey","hello");

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
