package io.xxnjdg.learning.mongodb.MongoDBsupport.AggregationFrameworkSupport;

import lombok.Data;

@Data
public class TagCount {
    private String _id;
    private String tag;
    private int n;
}