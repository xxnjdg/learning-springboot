package io.xxnjdg.learning.mongodb.MongoDBsupport.AggregationFrameworkSupport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("tags")
public class TagsCollection {
    @Id
    private String id;
    private List<Integer> tags = new ArrayList<>();

    public TagsCollection(Integer ...tag){
        this.tags.addAll(Arrays.asList(tag));
    }
}
