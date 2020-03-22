package io.xxnjdg.learning.mongodb.MongoDBsupport.GroupOperations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("group_test_collection")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupTestCollection {
    @Id
    private String id;
    private int x;

    public GroupTestCollection(int x){
        this.x = x;
    }

}
