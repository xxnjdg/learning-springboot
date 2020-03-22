package io.xxnjdg.learning.mongodb1.example6;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;


@Configuration
public class AppConfig {

    public @Bean
    MongoClient mongoClient() {
        return new MongoClient("localhost");
    }

    /**
     * MongoTemplate（MongoClient mongo，字符串databaseName）：获取MongoClient对象和要操作的默认数据库名称。 构造方法过时。。
     * MongoTemplate（MongoDbFactory mongoDbFactory）：获取一个封装了MongoClient对象，数据库名称以及用户名和密码的MongoDbFactory对象。
     * MongoTemplate（MongoDbFactory mongoDbFactory，MongoConverter mongoConverter）：添加一个MongoConverter以用于映射。 springboot自动配置用了这个
     *
     * @return
     */
    public @Bean
    MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), "mydatabase");
    }


}