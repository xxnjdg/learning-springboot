package io.xxnjdg.learning.amqp.springrabbitapi;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfiguringBroker {

    @Bean
    public Queue allArgsQueue() {
        return QueueBuilder.nonDurable("allArgsQueue")
                .ttl(6000) //消息过期时间
                .expires(2000_000) //队列过期时间
                .maxLength(42) //队列最大长度
                .maxLengthBytes(10_000) //队列最大字节长度
                .overflow(QueueBuilder.Overflow.rejectPublish) //设置队列溢出行为。 这确定了达到队列的最大长度时消息将发生什么情况。 有效值为drop-head，reject-publish或reject-publish-dlx。 仲裁队列类型仅支持首写。
                .deadLetterExchange("dlxAllArgsExchange") //死信交换机，交换机不会自动声明
                .deadLetterRoutingKey("dlxAllArgsKey") //死信交换机路由键
                .maxPriority(4) //优先级
                .lazy() //lazy模式
                .masterLocator(QueueBuilder.MasterLocator.minMasters) //不清楚
                .singleActiveConsumer() //只允许单一消费者
                .build();
    }

    @Bean
    public DirectExchange allArgsExchange() {
        return ExchangeBuilder.directExchange("allArgsExchange")
                .durable(true)
                .alternate("alternate")
                .build();
    }

    @Bean
    public Binding allArgsBinding() {
        return BindingBuilder.bind(
                allArgsQueue()).to(allArgsExchange()).with("allArgsKey");
    }

    @Bean
    public DirectExchange dlxAllArgsExchange() {
        return ExchangeBuilder.directExchange("dlxAllArgsExchange")
                .durable(true)
                .build();
    }

    @Bean
    public Queue dlxAllArgsQueue() {
        return QueueBuilder.durable("dlxAllArgsQueue")
                .build();
    }

    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(
                dlxAllArgsQueue()).to(dlxAllArgsExchange()).with("dlxAllArgsKey");
    }

}
