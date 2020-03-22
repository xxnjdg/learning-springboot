package io.xxnjdg.learning.amqp.javaclientapi.returnlistener;

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
public class ReturnListenerTest {

    private static final Log log = LogFactory.getLog(ReturnListenerTest.class);
    private static final String QUEUE_NAME = "test_return_queue";
    private static final String RABBITMQ_SERVER_IP_ADDRESS = "192.168.100.33";
    private static final String EXCHANGE_NAME = "test_return_exchange";
    private static final String ROUTING_KEY = "return.save";
    private static final String ERROR_ROUTING_KEY = "abc.save";
    private static final String BINDING_KEY = "return.#";

    @Test
    public void producer() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RABBITMQ_SERVER_IP_ADDRESS);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.addReturnListener((i, s, s1, s2, basicProperties, bytes) -> {
                log.info(i+" " + s+" " +s1+ " " +s2+ " " +
                        basicProperties+ " " + new String(bytes));
            });

            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            channel.basicPublish(EXCHANGE_NAME, ERROR_ROUTING_KEY, true,null, "hello world topic".getBytes());
            System.out.println(" [x] Sent '" + ERROR_ROUTING_KEY + "':'" + "hello world topic" + "'");

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
