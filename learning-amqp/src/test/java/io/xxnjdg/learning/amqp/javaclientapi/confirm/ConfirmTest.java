package io.xxnjdg.learning.amqp.javaclientapi.confirm;

import com.rabbitmq.client.*;
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
public class ConfirmTest {

    private static final Log log = LogFactory.getLog(ConfirmTest.class);
    private static final String QUEUE_NAME = "test_confirm_queue";
    private static final String RABBITMQ_SERVER_IP_ADDRESS = "192.168.100.33";
    private static final String EXCHANGE_NAME = "test_confirm_exchange";
    private static final String ROUTING_KEY = "confirm.test";
    private static final String BINDING_KEY = "confirm.#";

    @Test
    public void producer() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RABBITMQ_SERVER_IP_ADDRESS);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            //指定我们的消息投递模式: 消息的确认模式
            channel.confirmSelect();
            //添加一个确认监听
            channel.addConfirmListener(new ConfirmListener() {
                @Override
                public void handleAck(long l, boolean b) throws IOException {
                    log.info("ddd ack " + l + " " + b);
                }

                @Override
                public void handleNack(long l, boolean b) throws IOException {
                    log.info("ddd nack " + l + " " + b);
                }
            });

            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, "hello world topic".getBytes());
            System.out.println(" [x] Sent '" + ROUTING_KEY + "':'" + "hello world topic" + "'");

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
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
        });

        Thread.sleep(20 * 1000);
    }

}
