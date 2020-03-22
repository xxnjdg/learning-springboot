package io.xxnjdg.learning.amqp.javaclientapi.ack;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AckTest {

    private static final Log log = LogFactory.getLog(AckTest.class);
    private static final String QUEUE_NAME = "test_ack_queue";
    private static final String RABBITMQ_SERVER_IP_ADDRESS = "192.168.100.33";
    private static final String EXCHANGE_NAME = "test_ack_exchange";
    private static final String ROUTING_KEY = "ack.save";
    private static final String BINDING_KEY = "ack.#";

    @Test
    public void producer() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RABBITMQ_SERVER_IP_ADDRESS);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, true,null, "hello world topic".getBytes());
            log.info(" [x] Sent '" + ROUTING_KEY + "':'" + "hello world topic" + "'");
            Thread.sleep(5 * 1000);
        }
    }

    @Test
    public void consumer() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RABBITMQ_SERVER_IP_ADDRESS);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, BINDING_KEY);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
            //手动确认
            // multiple:是否支持批量接受，true表示批量
            // requeue: 是否重回队列，true表示重回
//            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
            channel.basicNack(delivery.getEnvelope().getDeliveryTag(),false,true);
        };
        // 手工签收 必须要关闭 autoAck = false
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {
        });

        Thread.sleep(20 * 1000);
    }

}
