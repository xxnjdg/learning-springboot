package io.xxnjdg.learning.amqp.javaclientapi.helloworld;

import com.rabbitmq.client.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloWorldTest {

    private static final String QUEUE_NAME = "hello";
    private static final String RABBITMQ_SERVER_IP_ADDRESS = "192.168.100.70";

    @Test
    public void producer() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置主机名,不设置就为默认值 用户guest 密码guest 主机 localhost 端口5672 虚拟主机 /
        connectionFactory.setHost(RABBITMQ_SERVER_IP_ADDRESS);
        //创建连接和通道
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
//            Declare a queue
//            声明一个队列，如果队列不存在会创建队列 ，如果参数一样重复声明不会报错，因此消费者和生产者可以重复声明
//            Parameters:
//            queue - the name of the queue 队列名字
//            durable - true if we are declaring a durable queue (the queue will survive a server restart)
//            如果我们声明一个持久队列，则为true（该队列将在服务器重启后继续存在）
//            exclusive - true if we are declaring an exclusive queue (restricted to this connection)
//            如果我们声明一个独占队列，则为true（仅限此connection） 一般false
//            autoDelete - true if we are declaring an autodelete queue (server will delete it when no longer in use)
//            如果我们声明一个自动删除队列，则为true（服务器将在不再使用它时将其删除） 一般为false
//            arguments - other properties (construction arguments) for the queue
//            参数 可设置 消息过期时间 等参数
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = "hello world";

//          有三个重载方法 都用于发送消息
//            void	basicPublish​(String exchange, String routingKey, boolean mandatory, boolean immediate, AMQP.BasicProperties props, byte[] body) Publish a message.
//            Publish a message. Publishing to a non-existent exchange will result in a channel-level protocol exception, which closes the channel. Invocations of Channel#basicPublish will eventually block if a resource-driven alarm is in effect.
//            发布消息。 发布到不存在的交换机将导致通道级协议异常，从而关闭通道。 如果资源驱动的警报生效，则Channel＃basicPublish的调用最终将被阻止。
//            Parameters:
//            exchange - the exchange to publish the message to   交换机名字
//            routingKey - the routing key   路由键
//            mandatory - true if the 'mandatory' flag is to be set
//            如果要设置“强制性”标志，则为true  用于 returnCallback 设置为true，当交换机不存在或路由不到，会在监听事件中捕获失败的消息，false 会删除消息，默认不设置为false
//            immediate - true if the 'immediate' flag is to be set. Note that the RabbitMQ server does not support this flag. 请注意，RabbitMQ服务器不支持此标志。
//            props - other properties for the message - routing headers etc  设置参数，例如消息是否持久化，设置消息头，属性
//            body - the message body 消息体
//            void	basicPublish​(String exchange, String routingKey, boolean mandatory, AMQP.BasicProperties props, byte[] body)Publish a message. 同上
//            void	basicPublish​(String exchange, String routingKey, AMQP.BasicProperties props, byte[] body)Publish a message.
//            这里官方例子交换机用""，会使用默认的交换机 AMQP default  这是一个direct类型，路由键为队列名字
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
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
        };
        //重载太多就解释官方例子
        //这个函数会用异步的方式一直监听消息
        //Start a non-nolocal, non-exclusive consumer, with a server-generated consumerTag. Provide access only to basic.deliver and basic.cancel AMQP methods (which is sufficient for most cases). See methods with a Consumer argument to have access to all the application callbacks.
        //Parameters:
        //queue - the name of the queue  队列名字
        //autoAck - true if the server should consider messages acknowledged once delivered; false if the server should expect explicit acknowledgements
        //为true是自动确认，为false是手动确认，不返回确认，rabbitmq 不会删除消息
        //deliverCallback - callback when a message is delivered   消息处理回调
        //cancelCallback - callback when the consumer is cancelled  删除处理回调
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
        });

    }

}
