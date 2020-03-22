package io.xxnjdg.learning.mongodb1.MapReduceOperations;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Example1 {
    private static Log log = LogFactory.getLog(Example1.class);

    public static void main(String[] args) {


        log.info("11111111");

//        MongoTemplate mongoTemplate = new MongoTemplate(
//                new SimpleMongoClientDbFactory(
//                        MongoClients.create("mongodb://192.168.100.33:27017/"), "mydb"));
//
//        mongoTemplate.insert(new Jmr("a","b"));
//        mongoTemplate.insert(new Jmr("b","c"));
//        mongoTemplate.insert(new Jmr("c","d"));


//        String map = null;
//        String reduce = null;
//        try {
//            String mapFile = Example1.class.getClassLoader().getResource("map.js").getFile();
//            String reduceFile = Example1.class.getClassLoader().getResource("reduce.js").getFile();
//            map = IOUtils.toString(new FileInputStream(mapFile), "utf-8");
//            reduce = IOUtils.toString(new FileInputStream(reduceFile), "utf-8");
//            System.out.println(map);
//            System.out.println(reduce);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }



//        MapReduceResults<ValueObject> results = mongoTemplate.mapReduce("jmr1", "classpath: map.js", "classpath: reduce.js", ValueObject.class);
//        for (ValueObject valueObject : results) {
//            System.out.println(valueObject);
//        }

//        mongoTemplate.dropCollection(Jmr.class);

    }
}
