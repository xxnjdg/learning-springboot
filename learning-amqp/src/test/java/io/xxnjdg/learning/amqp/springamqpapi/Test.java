package io.xxnjdg.learning.amqp.springamqpapi;

import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Test {


    public void test(){
        // 可以传参 com.rabbitmq.client.ConnectionFactory
        CachingConnectionFactory factory = new CachingConnectionFactory();
        //缓存chanel
        factory.setCacheMode(CachingConnectionFactory.CacheMode.CHANNEL);
        //缓存连接 默认的缓存模式是CHANNEL，
        factory.setCacheMode(CachingConnectionFactory.CacheMode.CONNECTION);
        //限制允许的连接总数
        factory.setConnectionLimit(5);
        //channelCacheSize成为对可以在连接上创建的通道数的限制。
        // 如果达到限制，则调用线程将阻塞，直到某个通道可用或达到此超时为止，在这种情况下，将抛出AmqpTimeoutException。
        //设置通道缓存 默认25
        factory.setChannelCacheSize(2);
        //如果达到限制，则使用channelCheckoutTimeLimit等待连接变为空闲。如果超过了时间，则抛出AmqpTimeoutException。
        factory.setChannelCheckoutTimeout(1000);
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setHost("192.168.100.33");
        factory.setPort(5672);
        factory.setAddresses("host1:5672,host2:5672"); //另外，如果在集群环境中运行，则可以使用addresses属性，如下所示：
        //生成的名称用于目标RabbitMQ连接的特定于应用程序的标识
        factory.setConnectionNameStrategy(connectionFactory -> "MY_CONNECTION");
        //每当建立新连接时，基础连接工厂将尝试依次连接到每个主机。从版本2.1.8开始，
        // 可以通过将shuffleAddresses属性设置为true来使连接顺序随机。shuffle 将在创建任何新连接之前应用。
        factory.setShuffleAddresses(true);

        //通过将CachingConnectionFactory属性PublisherConfirmType设置为ConfirmType.CORRELATED并将PublisherReturns属性设置为'true'，
        // 可以支持已确认（具有相关性）和返回的消息。
        //客户端可以向该频道注册PublisherCallbackChannel.Listener
        factory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        factory.setPublisherReturns(true);

        //对于第二种情况，该消息将被丢弃，并且不会生成返回。基础通道异常关闭。默认情况下，
        // 会记录此异常，但是您可以在CachingConnectionFactory中注册ChannelListener以获得此类事件的通知。
        factory.addConnectionListener(connection -> {});
        factory.addChannelListener((channel, b) -> {});

        Connection connection = factory.createConnection();

    }

    public void Test1(){
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setReturnCallback((message, i, s, s1, s2) -> {});
        rabbitTemplate.setConfirmCallback((correlationData, b, s) -> {});
    }

    public void Test2(){

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setTaskExecutor();

    }

}
