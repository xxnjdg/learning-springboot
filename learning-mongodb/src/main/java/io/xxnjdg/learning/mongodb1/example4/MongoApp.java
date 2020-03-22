package io.xxnjdg.learning.mongodb1.example4;

import com.mongodb.MongoClient;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.Query;

import static org.springframework.data.mongodb.core.query.Criteria.where;

public class MongoApp {

    /**
     * 插入后数据库找不到？？？
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        /**
         * SimpleMongoDbFactory 都过时了。。。
         */
        MongoOperations mongoOps = new MongoTemplate(new SimpleMongoDbFactory(new MongoClient("192.168.100.33"), "database"));

        mongoOps.insert(new Person("Joe", 34));

        Person one = mongoOps.findOne(new Query(where("name").is("Joe")), Person.class);
        if (one != null) {
            System.out.println(one);
        }

        mongoOps.dropCollection("person");
    }
}