package io.xxnjdg.learning.mongodb1.example5;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import static java.util.Collections.singletonList;

/**
 * 连接到数据库时，MongoDB Server第三代更改了身份验证模型。因此，一些可用于身份验证的配置选项不再有效。
 * 您应该使用特定于MongoClient的选项来通过MongoCredential设置凭据以提供身份验证数据，如以下示例所示：
 */
@Configuration
public class ApplicationContextEventTestsAppConfig extends AbstractMongoConfiguration {

  @Override
  public String getDatabaseName() {
    return "database";
  }

  @Override
  @Bean
  public MongoClient mongoClient() {
    return new MongoClient(singletonList(new ServerAddress("127.0.0.1", 27017)),
      singletonList(MongoCredential.createCredential("name", "db", "pwd".toCharArray())));
  }
}