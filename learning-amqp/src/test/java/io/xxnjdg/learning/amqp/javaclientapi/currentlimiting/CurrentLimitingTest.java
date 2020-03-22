package io.xxnjdg.learning.amqp.javaclientapi.currentlimiting;

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
public class CurrentLimitingTest {

    private static final Log log = LogFactory.getLog(CurrentLimitingTest.class);
    private static final String QUEUE_NAME = "test_qos_queue";
    private static final String RABBITMQ_SERVER_IP_ADDRESS = "192.168.100.33";
    private static final String EXCHANGE_NAME = "test_qos_exchange";
    private static final String ROUTING_KEY = "qos.save";
    private static final String BINDING_KEY = "qos.#";

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

        //1 限流方式  第一件事就是 autoAck设置为 false
        //prefetchSize: 0 还有 prefetchSize 这个参数表示消费者所能接收未确认消息的总体大小的上限，
        //单位为 B ，设置为 0 则表示没有上限
        //prefetchCount:会告诉RabbitMQ不要同时给一个消费者推送多于N个消息，即一旦有N个消息还没有ack,则该consumer将block掉，直到有消息ack
        //global: true\false是否将上面设置应用于channel简单点说，就是上面限制是channel级别的还是consumer级别
        channel.basicQos(0, 1, false);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
            //手动确认
            // multiple ：是否支持批量接受
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);
        };
        //prefetch_count在no_ask=false的情况下生效，即在自动应答的情况下这两个值是不生效的。
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {
        });

        Thread.sleep(20 * 1000);
    }

}
