package io.xxnjdg.learning.mongodb1.QueryingDocuments.FluentTemplateAPI;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.MongoClients;
import com.mongodb.client.result.UpdateResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.mongodb.core.*;
import org.springframework.data.mongodb.core.query.Update;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * FluentTemplateAPI
 * MongoOperations extends FluentMongoOperations
 * MongoOperations除了curd还扩展了其他功能，这个接口很强大，仔细看你会发现，crud是有很多重载的，这个接口定义了一部分
 * FluentMongoOperations 这个接口也定义了curd,mr的一部分，这个接口的设计有点像 java stream
 * 您可以通过终止方法：first（），one（），all（）或stream（）在检索单个实体和将多个实体作为列表或流检索之间切换。
 * query insert update remove 这4个方法都是以类类型为参数，之前使用是以实例对象为参数，有本质差别,
 * 这就是为什么我会说 mongoTemplate crud 有很多重载
 */
public class Example1 {

    private static Log log = LogFactory.getLog(Example1.class);

    public static void main(String[] args) {

        MongoTemplate mongoTemplate = new MongoTemplate(
                new SimpleMongoClientDbFactory(
                        MongoClients.create("mongodb://192.168.100.33:27017/"), "mydb"));


        List<Person> list1 = Arrays.asList(new Person("f", true, 2), new Person("b", true, 3),
                new Person("c", "fff", "ff", true, 3));
        List<Person> list2 = Arrays.asList(new Person("d", false,4),
                new Person("e",false, 5), new Person("f",false, 6));
        // 批量插入
        // BulkMode.UNORDERED:表示并行处理，遇到错误时能继续执行不影响其他操作；BulkMode.ORDERED：表示顺序执行，遇到错误时会停止所有执行
        BulkWriteResult bulk = mongoTemplate.insert(Person.class).withBulkMode(BulkOperations.BulkMode.ORDERED).bulk(list1);
        log.info(bulk);
        bulk = mongoTemplate.insert(Person.class).withBulkMode(BulkOperations.BulkMode.UNORDERED).bulk(list1);
        //insertedCount = 3
        log.info(bulk);

        //返回新值
        Collection<? extends Person> all = mongoTemplate.insert(Person.class).all(list2);
        for (Person person : all) {
            log.info(person);
        }

        // as 用于讲查询到的结果映射到Jedi类
        // 条件查询
        List<Jedi> jedis = mongoTemplate.query(Person.class).as(Jedi.class).matching(query(where("jedi").is(true))).all();
        for (Jedi jedi : jedis) {
            log.info(jedi);
        }

        //条件更新
        UpdateResult age = mongoTemplate.update(Person.class).matching(query(where("jedi").is(false)))
                .apply(Update.update("age", 21)).all();
        log.info(age);

        //
        Jedi value = mongoTemplate.update(Person.class)
                .matching(query(where("jedi").is("none"))) //条件查询
                .replaceWith(new Person("update", 1)).as(Jedi.class) //查询到替换新值，映射到 Jedi.class 类
                .withOptions(FindAndReplaceOptions.options().returnNew().upsert())//返回新值，查询不存在执行insert
                .findAndReplaceValue();//返回值
        log.info(value);

        // 使用 java stream api 查询
        Stream<Person> stream = mongoTemplate.query(Person.class).stream();
        stream.forEach(log::info);

        //全部删除，没有查询
        List<Person> remove = mongoTemplate.remove(Person.class).findAndRemove();
        remove.forEach(log::info);

        mongoTemplate.dropCollection(Person.class);

    }
}

/**
 * #config root logger
 * log4j.rootLogger = INFO,system.out
 * log4j.appender.system.out=org.apache.log4j.ConsoleAppender
 * log4j.appender.system.out.layout=org.apache.log4j.PatternLayout
 * log4j.appender.system.out.layout.ConversionPattern=MINAServer Logger-->%5p{%F:%L}-%m%n
 *
 * #config this Project.file logger
 * log4j.logger.thisProject.file=INFO,thisProject.file.out
 * log4j.appender.thisProject.file.out=org.apache.log4j.DailyRollingFileAppender
 * log4j.appender.thisProject.file.out.File=logContentFile.log
 * log4j.appender.thisProject.file.out.layout=org.apache.log4j.PatternLayout
 *
 * log4j.category.org.springframework.data.mongodb=DEBUG
 * log4j.appender.stdout.layout.ConversionPattern=%d{ABSOLUTE} %5p %40.40c:%4L - %m%n
 *
 * log4j1.properties
 */
