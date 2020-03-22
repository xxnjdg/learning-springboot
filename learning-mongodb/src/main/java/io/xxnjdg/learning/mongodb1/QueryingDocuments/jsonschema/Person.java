package io.xxnjdg.learning.mongodb1.QueryingDocuments.jsonschema;

import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Field;

import static org.springframework.data.mongodb.core.mapping.FieldType.SCRIPT;

@Data
public class Person {

    //简单对象属性被视为常规属性。
    private final String firstname;
    //基本类型被视为必需的属性
    private final int age;
    //枚举限制为可能的值。
    private Species species;
    //检查对象类型属性并将其表示为嵌套文档
    private Address address;
    //转换器将其转换为Code的字符串类型属性。
    private @Field(targetType=SCRIPT) String theForce;
    // 生成schema时，将忽略@Transient属性。
    private @Transient Boolean useTheForce;


    public Person(String firstname, int age) {

        this.firstname = firstname;
        this.age = age;
    }

    // gettter / setter omitted
}
