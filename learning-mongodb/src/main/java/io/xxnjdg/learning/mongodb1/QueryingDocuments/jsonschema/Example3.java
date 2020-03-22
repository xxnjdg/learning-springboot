package io.xxnjdg.learning.mongodb1.QueryingDocuments.jsonschema;

import com.mongodb.client.MongoClients;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;
import org.springframework.data.mongodb.core.schema.MongoJsonSchema;

import static org.springframework.data.mongodb.core.schema.JsonSchemaProperty.encrypted;
import static org.springframework.data.mongodb.core.schema.JsonSchemaProperty.string;

// TODO: 2020/2/4 不成功
public class Example3 {
    public static void main(String[] args) {
        MongoTemplate mongoTemplate = new MongoTemplate(
                new SimpleMongoClientDbFactory(
                        MongoClients.create("mongodb://192.168.100.33:27017/"), "mydb"));

        MongoJsonSchema schema = MongoJsonSchema.builder()
                .properties(
                        encrypted(string("ssn"))
                                .algorithm("AEAD_AES_256_CBC_HMAC_SHA_512-Deterministic")
                                .keyId("*key0_id")
                ).build();

        mongoTemplate.createCollection(Ssn.class, CollectionOptions.empty().schema(schema));

        mongoTemplate.insert(new Ssn("abc","123"));
    }
}
