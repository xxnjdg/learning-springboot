package io.xxnjdg.learning.amqp.javaclientapi.dlx;

import com.rabbitmq.client.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DlxTest {

    private static final Log LOG = LogFactory.getLog(DlxTest.class);
    private static final String RABBITMQ_SERVER_IP_ADDRESS = "192.168.100.33";
    private static final String EXCHANGE_NAME = "test_dlx_exchange";
    private static final String ROUTING_KEY = "dlx.save";
    private static final String BINDING_KEY = "dlx.*";
    private static final String QUEUE_NAME = "test_dlx_queue";

    @Test
    public void producer() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RABBITMQ_SERVER_IP_ADDRESS);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .deliveryMode(2)
                    .contentEncoding("UTF-8")
                    .expiration("10000")
                    .build();

            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, properties, "hello world topic".getBytes("UTF-8"));
            LOG.info(" [x] Sent '" + ROUTING_KEY + "':'" + "hello world topic" + "'");
        }
        Thread.sleep(5*1000);
    }

    @Test
    public void consumer() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RABBITMQ_SERVER_IP_ADDRESS);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        // 这就是一个普通的交换机 和 队列 以及路由
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "dlx.exchange");
        //这个agruments属性，要设置到声明队列上
        channel.queueDeclare(QUEUE_NAME,true,false,false,arguments);
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,BINDING_KEY);

        //要进行死信队列的声明:
        channel.exchangeDeclare("dlx.exchange", "topic", true, false, null);
        channel.queueDeclare("dlx.queue", true, false, false, null);
        channel.queueBind("dlx.queue", "dlx.exchange", "#");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });

        Thread.sleep(20 * 1000);
    }

}
