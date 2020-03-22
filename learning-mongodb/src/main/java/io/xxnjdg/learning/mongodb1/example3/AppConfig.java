package io.xxnjdg.learning.mongodb1.example3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;

/**
 * 以下示例显示了一个基于Java的bean元数据的示例，该元数据支持@Repository注释类上的异常转换：
 */
@Configuration
public class AppConfig {

    /*
     * Factory bean that creates the com.mongodb.MongoClient instance
     * MongoClientFactoryBean 都过时了。。。
     */
     public @Bean
     MongoClientFactoryBean mongo() {
          MongoClientFactoryBean mongo = new MongoClientFactoryBean();
          mongo.setHost("localhost");
          return mongo;
     }
}