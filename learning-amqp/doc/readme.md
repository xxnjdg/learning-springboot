# Reference

##  Using Spring AMQP

### AMQP Abstractions

#### Message

0-9-1 AMQP规范未定义消息类或接口。而是在执行诸如basicPublish（）之类的操作时，将内容作为字节数组参数传递，并将其他属性作为单独的参数传递。Spring AMQP将Message类定义为更通用的AMQP域模型表示形式的一部分。Message类的目的是将主体和属性封装在一个实例中，以便使API更加简单。下面的示例显示Message类的定义：

```java
public class Message {

    private final MessageProperties messageProperties;

    private final byte[] body;

    public Message(byte[] body, MessageProperties messageProperties) {
        this.body = body;
        this.messageProperties = messageProperties;
    }

    public byte[] getBody() {
        return this.body;
    }

    public MessageProperties getMessageProperties() {
        return this.messageProperties;
    }
}
```

MessageProperties接口定义了几个常用属性，例如“ messageId”，“ timestamp”，“ contentType”等。您还可以通过调用setHeader（String key，Object value）方法，使用用户定义的“标题”扩展这些属性。

#### Exchange

Exchange接口表示一个AMQP Exchange，这是消息生产者发送到的对象。代理的虚拟主机中的每个Exchange都有唯一的名称以及其他一些属性。以下示例显示了Exchange接口：

```java
public interface Exchange {

    String getName();

    String getExchangeType();

    boolean isDurable();

    boolean isAutoDelete();

    Map<String, Object> getArguments();

}
```

如您所见，Exchange还具有一个由ExchangeTypes中定义的常量表示的“类型”。基本类型为：直接，主题，扇出和标题。在核心程序包中，可以找到每种类型的Exchange接口的实现。这些Exchange类型在处理队列绑定方面的行为各不相同。例如，直接交换可让队列由固定的路由键（通常是队列名称）绑定。主题交换支持具有路由模式的绑定，这些路由模式可能分别包括“正一”和“零或更多”的“ *”和“＃”通配符。Fanout交换发布到与其绑定的所有队列，而无需考虑任何路由键。有关这些和其他Exchange类型的更多信息，请参见其他资源。

AMQP规范还要求任何经纪人提供没有名称的“默认”直接交换。所有声明的队列都以其名称作为路由键绑定到该默认Exchange。您可以在AmqpTemplate中了解有关Spring AMQP中默认Exchange使用情况的更多信息。



