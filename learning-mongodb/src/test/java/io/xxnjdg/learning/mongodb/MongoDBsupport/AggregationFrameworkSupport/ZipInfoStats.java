package io.xxnjdg.learning.mongodb.MongoDBsupport.AggregationFrameworkSupport;

import lombok.Data;

@Data
class ZipInfoStats {
   String id;
   String state;
   City biggestCity;
   City smallestCity;
}