package io.xxnjdg.learning.mongodb1.QueryingDocuments.jsonschema;

import com.mongodb.client.MongoClients;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoJsonSchemaCreator;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;
import org.springframework.data.mongodb.core.schema.MongoJsonSchema;

/**
 * Generate Json Schema from domain type
 */
public class Example2 {
    public static void main(String[] args) {
        MongoTemplate mongoTemplate = new MongoTemplate(
                new SimpleMongoClientDbFactory(
                        MongoClients.create("mongodb://192.168.100.33:27017/"), "mydb"));

        MongoJsonSchema schema = MongoJsonSchemaCreator.create(mongoTemplate.getConverter())
                .createSchemaFor(Person.class);

        mongoTemplate.createCollection(Person.class, CollectionOptions.empty().schema(schema));

        Person person = new Person("name", 1);
        mongoTemplate.insert(person);
    }
}
