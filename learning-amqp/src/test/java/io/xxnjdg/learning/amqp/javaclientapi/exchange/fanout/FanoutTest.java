package io.xxnjdg.learning.amqp.javaclientapi.exchange.fanout;

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
public class FanoutTest {

    private static final String EXCHANGE_NAME = "logs";
    private static final String RABBITMQ_SERVER_IP_ADDRESS = "192.168.100.33";

    @Test
    public void producer() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RABBITMQ_SERVER_IP_ADDRESS);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            //重载方法也挺多，选了参数最多的解释，其他重载参数和这个一致
            //exchangeDeclare​(String exchange, BuiltinExchangeType type, boolean durable, boolean autoDelete, boolean internal, Map<String,​Object> arguments)
            //Declare an exchange, via an interface that allows the complete set of arguments.
            // 通过允许完整参数集的接口声明交换。 这里和队列一样，如果不存在就创建，参数相同允许重复声明，但只会创建一次，因此消费者和生产者和重复声明
            //Parameters:
            //exchange - the name of the exchange  交换机名字
            //type - the exchange type  交换机类型，其他重载这个参数类型为String,这里是BuiltinExchangeType，只能选择4中类型 direct  fanout topic headers
            //durable - true if we are declaring a durable exchange (the exchange will survive a server restart) 是否持久化
            //autoDelete - true if the server should delete the exchange when it is no longer in use 是否自动删除
            //internal - true if the exchange is internal, i.e. can't be directly published to by a client. 是否内部使用
            //arguments - other properties (construction arguments) for the exchange 交换机参数
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

            String message = "info: Hello World!";

            //fanout 不需要路由键
            //生产者必须声明交互机，不然会报错，队列可以不声明，当队列不存在时，消息会丢失
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }

    @Test
    public void consumer() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RABBITMQ_SERVER_IP_ADDRESS);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        //这里生成了无名队列
        String queueName = channel.queueDeclare().getQueue();
        //AMQP.Queue.BindOk queueBind​(String queue, String exchange, String routingKey, Map<String,​Object> arguments) throws IOException
        //Bind a queue to an exchange.  将队列绑定到交换。
        //Parameters:
        //queue - the name of the queue  队列名字
        //exchange - the name of the exchange 交换机名字
        //routingKey - the routing key to use for the binding 路由键名字
        //arguments - other properties (binding parameters) 参数
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });

        Thread.sleep(20 * 1000);
    }

}
