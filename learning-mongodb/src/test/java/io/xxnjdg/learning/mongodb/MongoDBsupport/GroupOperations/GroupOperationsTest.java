package io.xxnjdg.learning.mongodb.MongoDBsupport.GroupOperations;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GroupOperationsTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    // TODO: 2020/2/5 执行不成功
    @Test
    public void Test(){

        /**
         * 为了理解组操作如何工作，使用以下示例，该示例有些虚构。
         * 有关更实际的示例，请参阅《 MongoDB-权威指南》一书。使用以下行创建的名为group_test_collection的集合。
         *
         * 我们想对每行中的唯一字段x字段进行分组，并汇总每个x的特定值出现的次数。为此，我们需要创建一个初始文档，
         * 其中包含我们的count变量以及一个reduce函数，该函数将在每次遇到该变量时对其进行递增。执行组操作的Java代码如下所示
         */

        mongoTemplate.insert(new GroupTestCollection(1));
        mongoTemplate.insert(new GroupTestCollection(1));
        mongoTemplate.insert(new GroupTestCollection(2));
        mongoTemplate.insert(new GroupTestCollection(3));
        mongoTemplate.insert(new GroupTestCollection(3));
        mongoTemplate.insert(new GroupTestCollection(3));

        GroupByResults<XObject> group = mongoTemplate.group("group_test_collection",
                GroupBy.key("x").initialDocument("{ count: 0 }").reduceFunction("function(doc, prev) { prev.count += 1 }"),
                XObject.class);

        group.forEach(System.out::println);

        mongoTemplate.dropCollection(GroupTestCollection.class);
    }
}
