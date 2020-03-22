package io.xxnjdg.learning.mongodb1.QueryingDocuments;

import com.mongodb.client.MongoClients;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public class QueryTest {
    private static Log log = LogFactory.getLog(QueryTest.class);

    public static void main(String[] args) {

        MongoTemplate mongoTemplate = new MongoTemplate(
                new SimpleMongoClientDbFactory(
                        MongoClients.create("mongodb://192.168.100.33:27017/"), "mydb"));


        mongoTemplate.insert(new Person("ys", 20, new Account(10000.00)));

        //List<Account> accounts = Arrays.asList(new Account(500.00), new Account(300.00), new Account(500.00));
        //accounts 可以为类 或 数组
        // 数组项有 balance 字段即可，只要有一项满足就为找到
        List<Person> result = mongoTemplate.find(query(where("age").lt(50)
                .and("accounts.balance").gt(1000.00d)), Person.class);

        log.info(result);

        mongoTemplate.dropCollection(Person.class);
    }
}
