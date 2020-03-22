package io.xxnjdg.learning.mongodb.MongoDBsupport.AggregationFrameworkSupport;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
class ZipInfo {
   String id;
   String city;
   String state;
   @Field("pop") int population;
   @Field("loc") double[] location;

   public ZipInfo(String city, String state, int population) {
      this.city = city;
      this.state = state;
      this.population = population;
   }
}