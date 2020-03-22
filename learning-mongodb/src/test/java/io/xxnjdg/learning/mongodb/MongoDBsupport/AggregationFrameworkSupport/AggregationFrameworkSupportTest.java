package io.xxnjdg.learning.mongodb.MongoDBsupport.AggregationFrameworkSupport;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AggregationFrameworkSupportTest {
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     *https://blog.csdn.net/itpika/article/details/80855452
     */

    @Test
    public void aggregationFrameworkExample1(){

        mongoTemplate.insert(new TagsCollection(1,2,3,4));
        mongoTemplate.insert(new TagsCollection(1,2,3,4));
        mongoTemplate.insert(new TagsCollection(1,2,3,4));
        mongoTemplate.insert(new TagsCollection(1,2,3,4));

        Aggregation agg = newAggregation(
                project("tags"),
                unwind("tags"),
                group("tags").count().as("n"),
                project("n").and("tag").previousOperation(),
                sort(DESC, "n")
        );


        AggregationResults<TagCount> results = mongoTemplate.aggregate(agg, "tags", TagCount.class);
        List<TagCount> tagCount = results.getMappedResults();

        tagCount.forEach(System.out::println);

        mongoTemplate.dropCollection(TagsCollection.class);
    }

    @Test
    public void aggregationFrameworkExample2(){

        mongoTemplate.insert(new ZipInfo("gz","gds",1000));
        mongoTemplate.insert(new ZipInfo("sz","gds",2000));
        mongoTemplate.insert(new ZipInfo("dg","gds",3000));
        mongoTemplate.insert(new ZipInfo("fs","gds",4000));

        TypedAggregation<ZipInfo> aggregation = newAggregation(ZipInfo.class,
                group("state", "city")
                        .sum("population").as("pop"),
                sort(ASC, "pop", "state", "city"),
                group("state")
                        .last("city").as("biggestCity")
                        .last("pop").as("biggestPop")
                        .first("city").as("smallestCity")
                        .first("pop").as("smallestPop"),
                project()
                        .and("state").previousOperation()
                        .and("biggestCity")
                        .nested(bind("name", "biggestCity").and("population", "biggestPop"))
                        .and("smallestCity")
                        .nested(bind("name", "smallestCity").and("population", "smallestPop")),
                sort(ASC, "state")
        );

        AggregationResults<ZipInfoStats> result = mongoTemplate.aggregate(aggregation, ZipInfoStats.class);
        ZipInfoStats firstZipInfoStats = result.getMappedResults().get(0);

        System.out.println(firstZipInfoStats);

        mongoTemplate.dropCollection(ZipInfo.class);
    }
}
