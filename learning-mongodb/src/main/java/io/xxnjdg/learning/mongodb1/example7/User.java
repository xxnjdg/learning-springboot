package io.xxnjdg.learning.mongodb1.example7;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

@Data
public class User {
    @Id
    String id;
    String lastname;
    @Version
    Long version;

    public User(String lastname){
        this.lastname = lastname;
    }
}
