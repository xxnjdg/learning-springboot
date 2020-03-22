package io.xxnjdg.learning.amqp.javaclientapi.exchange.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DirectTest {

    private static final String EXCHANGE_NAME = "direct_logs";
    private static final String ROUTING_KEY = "directRoutingKey";
    private static final String RABBITMQ_SERVER_IP_ADDRESS = "192.168.100.33";

    @Test
    public void producer() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RABBITMQ_SERVER_IP_ADDRESS);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            //声明了交换机类型direct
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");
            //使用了路由键
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, "hello world direct".getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + ROUTING_KEY + "':'" + "hello world direct" + "'");
        }
    }

    @Test
    public void consumer() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RABBITMQ_SERVER_IP_ADDRESS);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        String queueName = channel.queueDeclare().getQueue();

        channel.queueBind(queueName, EXCHANGE_NAME, ROUTING_KEY);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback  deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });

        Thread.sleep(10 * 1000);
    }


}
