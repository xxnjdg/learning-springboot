package io.xxnjdg.learning.mongodb1.example1;

import com.mongodb.client.MongoClients;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import static org.springframework.data.mongodb.core.query.Criteria.where;

public class MongoApp {

  private static final Log log = LogFactory.getLog(MongoApp.class);

  public static void main(String[] args) throws Exception {

    /**
     * 您可以使用标准的com.mongodb.MongoClient对象和要使用的数据库名称来实例化Spring Mongo的中央帮助程序类MongoTemplate。
     * 如果构造函数参数名称与存储文档的字段名称匹配，则将它们用于实例化对象
     */
    MongoOperations mongoOps = new MongoTemplate(MongoClients.create(), "database");
    mongoOps.insert(new Person("Joe", 34));

    log.info(mongoOps.findOne(new Query(where("name").is("Joe")), Person.class));

    mongoOps.dropCollection("person");
  }
}