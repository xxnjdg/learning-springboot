package io.xxnjdg.learning.mongodb1.example2;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 通过这种方法，您可以将标准的com.mongodb.MongoClient实例与使用Spring的
 * MongoClientFactoryBean的容器一起使用。与直接实例化com.mongodb.MongoClient实例相比，
 * FactoryBean还具有为容器提供ExceptionTranslator实现的附加优点，
 * 该实现将MongoDB异常转换为Spring的可移植DataAccessException层次结构中用于
 * @Repository 注释的数据访问类的异常。Spring的DAO支持功能介绍了这种层次结构和@Repository的用法。
 */
@Configuration
public class AppConfig {

    /*
     * Use the standard Mongo driver API to create a com.mongodb.MongoClient instance.
     * 使用基于Java的Bean元数据注册com.mongodb.MongoClient对象
     */
    @Bean
    public MongoClient mongoClient() {
        return new MongoClient("192.168.100.33");
    }
}