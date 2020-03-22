package io.xxnjdg.learning.mongodb.MongoDBsupport.MapReduceOperations;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.mapreduce.MapReduceResults;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MapReduceOperationsTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void ExampleUsage() {

        // 先插入三条文档
        mongoTemplate.insert(new Jmr("a","b"));
        mongoTemplate.insert(new Jmr("b","c"));
        mongoTemplate.insert(new Jmr("c","d"));

        //从输入集合 jmr 集合导入数据，经过 mr 后映射到 ValueObject.class
//        MapReduceResults<ValueObject> results = mongoTemplate.mapReduce("jmr",
//                "classpath:map.js", "classpath:reduce.js", ValueObject.class);

        //支持条件查询，在输入集合查询
        Query query = new Query(where("x").ne(new String[] { "a", "b" }));

        MapReduceResults<ValueObject> results = mongoTemplate.mapReduce(query,"jmr",
                "classpath:map.js", "classpath:reduce.js",
                new MapReduceOptions().outputCollection("jmr_out"), //结果可以输出到 jmr_out 集合
                ValueObject.class);

        for (ValueObject valueObject : results) {
            System.out.println(valueObject);
        }

        // 删除 Jmr.class 集合
        mongoTemplate.dropCollection(Jmr.class);
        /**
         * 结果
         *ValueObject [id=a, value=1.0]
         * ValueObject [id=b, value=2.0]
         * ValueObject [id=c, value=2.0]
         * ValueObject [id=d, value=1.0]
         */

    }

}