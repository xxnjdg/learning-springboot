package io.xxnjdg.learning.mongodb1.example5;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

/**
 * 要向该容器注册MongoDbFactory实例，您需要编写类似于上一代码清单中突出显示的代码。以下清单显示了一个简单的示例：
 */
@Configuration
public class MongoConfiguration {

  public @Bean
  MongoDbFactory mongoDbFactory() {
    return new SimpleMongoDbFactory(new MongoClient(), "database");
  }
}