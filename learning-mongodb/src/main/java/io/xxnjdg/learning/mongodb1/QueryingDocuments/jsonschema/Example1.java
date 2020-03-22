package io.xxnjdg.learning.mongodb1.QueryingDocuments.jsonschema;


import com.mongodb.client.MongoClients;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;
import org.springframework.data.mongodb.core.schema.MongoJsonSchema;

import static org.springframework.data.mongodb.core.schema.JsonSchemaProperty.*;

public class Example1 {
    public static void main(String[] args) {

        /**
         * 通过网关接口上的静态方法，已经有一些预定义的和强类型化的架构对象（JsonSchemaObject和JsonSchemaProperty）
         * 可用。但是，您可能需要构建可通过构建器API创建的定制属性验证规则，如以下示例所示：
         */
        // "birthdate" : { "bsonType": "date" }
//        JsonSchemaProperty.named("birthdate").ofType(Type.dateType());

        // "birthdate" : { "bsonType": "date", "description", "Must be a date" }
//        JsonSchemaProperty.named("birthdate").with(JsonSchemaObject.of(Type.dateType()).description("Must be a date"));

        // CollectionOptions为集合的模式支持提供入口点，如以下示例所示：

//        MongoJsonSchema schema = MongoJsonSchema.builder().required("firstname", "lastname").build();

        //template.createCollection(Person.class, CollectionOptions.empty().schema(schema));


        MongoTemplate mongoTemplate = new MongoTemplate(
                new SimpleMongoClientDbFactory(
                        MongoClients.create("mongodb://192.168.100.33:27017/"), "mydb"));


        //创建 a JSON schema
        MongoJsonSchema build = MongoJsonSchema.builder() //获取模式构建器以使用流畅的API配置模式。
                .required("lastname") // 如此处所示直接配置所需的属性，或者如第3步所述配置更多详细信息。
                .properties(
                        //配置必需的字符串类型的名字字段，仅允许使用luke和han值。属性可以输入或不输入。
                        // 使用静态导入的JsonSchemaProperty可使语法稍微紧凑一些，并获得诸如string（…）之类的入口点。
                        required(string("firstname").possibleValues("luke", "han")),
                        object("address")
                                .properties(string("postCode").minLength(4).maxLength(5)))
                //生成架构对象。使用该架构创建集合或查询文档。
                .build();

        //创建 集合
        mongoTemplate.createCollection(TestCollection.class, CollectionOptions.empty().schema(build));

        // 成功
        mongoTemplate.insert(new TestCollection("fd","luke",new Address("ddddd")));

        //失败
        mongoTemplate.insert(new TestCollection("fd","a",new Address("d")));


    }
}
