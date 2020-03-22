package io.xxnjdg.learning.mongodb1.QuerybyExample.usage;

import org.springframework.data.domain.Example;

public class Example1 {
    public static void main(String[] args) {


        Person person = new Person();//创建域对象的新实例。
        person.setFirstname("Dave");//设置要查询的属性。
        Example<Person> example = Example.of(person);//创建示例。


//        ExampleMatcher matcher = ExampleMatcher.matching() //创建一个ExampleMatcher以期望所有值都匹配。即使没有进一步的配置，它也可以在此阶段使用。
//                .withIgnorePaths("lastname")//构造一个新的ExampleMatcher以忽略lastname属性路径。
//                .withIncludeNullValues()//	Construct a new ExampleMatcher to ignore the lastname property path and to include null values
//                .withStringMatcherEnding();

//        ExampleMatcher.matching().withIgnoreNullValues()

//        Example<Person> example = Example.of(person, matcher);

        //默认情况下，ExampleMatcher期望探针上设置的所有值都匹配。如果要获取与隐式定义的任何谓词匹配的结果，请使用ExampleMatcher.matchingAny（）。


    }
}
