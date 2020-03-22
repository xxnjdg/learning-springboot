package io.xxnjdg.learning.mongodb1.example7;

import com.mongodb.client.MongoClients;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class FindAndModifyTest {

    private static final Log log = LogFactory.getLog(MongoApp.class);

    public static void main(String[] args) {

        /**
         * <T> T findAndModify(Query query, Update update, Class<T> entityClass);
         *
         * <T> T findAndModify(Query query, Update update, Class<T> entityClass, String collectionName);
         *
         * <T> T findAndModify(Query query, Update update, FindAndModifyOptions options, Class<T> entityClass);
         *
         * <T> T findAndModify(Query query, Update update, FindAndModifyOptions options, Class<T> entityClass, String collectionName);
         */
        MongoOperations mongoTemplate = new MongoTemplate(
                new SimpleMongoClientDbFactory(
                        MongoClients.create("mongodb://192.168.100.33:27017/"), "mydb"));

//        mongoTemplate.insert(new Person("Tom", 21));
//        mongoTemplate.insert(new Person("Dick", 22));
//        mongoTemplate.insert(new Person("Harry", 23));
//
//        Query query = new Query(Criteria.where("name").is("Harry"));
//        Update update = new Update().inc("age", 1);
//        Person p = mongoTemplate.findAndModify(query, update, Person.class); // return's old person object 返回旧值
//
//        log.info(p.getName().equals("Harry"));
//        log.info(p.getAge() == 23);
//        p = mongoTemplate.findOne(query, Person.class);
//        log.info(p.getAge() == 23);
//
//        // Now return the newly updated document when updating 返回新值
//        p = mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Person.class);
//        log.info(p.getAge() == 25);

        /**
         * 如果找不到与查询匹配的文档，则将执行插入操作
         * 使用FindAndModifyOptions方法可以设置returnNew，upsert和remove的选项。从前面的代码片段扩展来的示例如下：
         */
        Update update = new Update().inc("age", 1);
        Query query2 = new Query(Criteria.where("name").is("Mary"));
        Person p = mongoTemplate.findAndModify(query2, update, new FindAndModifyOptions().returnNew(true).upsert(true), Person.class);
        log.info(p.getAge());
        log.info(p.getName());
    }
}
