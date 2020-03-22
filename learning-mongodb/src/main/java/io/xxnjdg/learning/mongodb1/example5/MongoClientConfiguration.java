package io.xxnjdg.learning.mongodb1.example5;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

/**
 * 从MongoDB Java驱动程序3.7.0开始，通过mongodb-driver-sync工件向MongoClient提供了一个替代入口点。
 * com.mongodb.client.MongoClient与com.mongodb.MongoClient不兼容，并且不再支持旧版DBObject编解码器。
 * 因此，它不能与Querydsl一起使用，并且需要不同的配置。您可以使用AbstractMongoClientConfiguration来利用新的MongoClients构建器API。
 */
@Configuration
public class MongoClientConfiguration extends AbstractMongoClientConfiguration {

	@Override
	protected String getDatabaseName() {
		return "database";
	}

	@Override
	public MongoClient mongoClient() {
		return MongoClients.create("mongodb://localhost:27017/?replicaSet=rs0&w=majority");
	}
}