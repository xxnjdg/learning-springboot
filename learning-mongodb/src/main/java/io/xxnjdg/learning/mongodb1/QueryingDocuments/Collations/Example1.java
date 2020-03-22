package io.xxnjdg.learning.mongodb1.QueryingDocuments.Collations;

import org.springframework.data.mongodb.core.query.Collation;

/**
 * Collations基于ICU Collations定义字符串比较规则
 * 简单说根据语言排序
 */
public class Example1 {
    public static void main(String[] args) {
        //排序规则需要创建语言环境。这可以是语言环境的字符串表示形式，
        // 语言环境（考虑语言，国家和地区）或Collat​​ionLocale。语言环境对于创建是必需的。
        Collation collation = Collation.of("fr")

                //	Collation strength 定义了表示字符之间差异的比较级别。
                // 您可以根据所选强度配置各种选项（区分大小写，区分大小写和其他）。
                .strength(Collation.ComparisonLevel.secondary()
                        .includeCase())

                //指定是将数字字符串比较为数字还是字符串。
                .numericOrderingEnabled()

                //指定排序规则是否应将空格和标点符号视为基本字符以进行比较
                .alternate(Collation.Alternate.shifted().punct())

                //指定带有变音符号的字符串是否从字符串的后面开始排序，例如使用某些法语词典排序。
                .forwardDiacriticSort()

                //指定是否检查文本是否需要规范化以及是否执行规范化。
                .normalizationEnabled();

//
//        Collation french = Collation.of("fr");
//        Collation german = Collation.of("de");
//
//        template.createCollection(Person.class, CollectionOptions.just(collation));
//
//        template.indexOps(Person.class).ensureIndex(new Index("name", Direction.ASC).collation(german));
//
//        Collation collation = Collation.of("de");
//
//        Query query = new Query(Criteria.where("firstName").is("Amél")).collation(collation);
//
//        List<Person> results = template.find(query, Person.class);
//
//        Collation collation = Collation.of("de");
//
//        AggregationOptions options = AggregationOptions.builder().collation(collation).build();
//
//        Aggregation aggregation = newAggregation(
//                project("tags"),
//                unwind("tags"),
//                group("tags")
//                        .count().as("count")
//        ).withOptions(options);
//
//        AggregationResults<TagCount> results = template.aggregate(aggregation, "tags", TagCount.class);
    }
}
