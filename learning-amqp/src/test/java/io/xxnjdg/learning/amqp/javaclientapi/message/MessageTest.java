package io.xxnjdg.learning.amqp.javaclientapi.message;

import com.rabbitmq.client.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageTest {

    private static final String QUEUE_NAME = "hello";
    private static final String RABBITMQ_SERVER_IP_ADDRESS = "192.168.100.33";

    @Test
    public void producer() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(RABBITMQ_SERVER_IP_ADDRESS);
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = "hello world";

            //消息头
            Map<String, Object> headers = new HashMap<>();
            headers.put("my1", "111");
            headers.put("my2", "222");

            // 消息属性
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .deliveryMode(2) //消息持久化
                    .contentEncoding("UTF-8") //编码
                    .expiration("10000") //过期时间
                    .headers(headers) //设置 headers
                    .build();

            channel.basicPublish("", QUEUE_NAME, properties, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
    }


    @Test
    public void consumer() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(RABBITMQ_SERVER_IP_ADDRESS);
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
            Map<String, Object> headers = delivery.getProperties().getHeaders();
            System.out.println("headers get my1 value: " + headers.get("my1"));
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
        });
    }

}
