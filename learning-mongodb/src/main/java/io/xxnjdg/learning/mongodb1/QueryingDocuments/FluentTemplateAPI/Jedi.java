package io.xxnjdg.learning.mongodb1.QueryingDocuments.FluentTemplateAPI;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Jedi {
    @Id
    private String id;
    private String name;
    private int age;

    public Jedi(String name, int age) {
        this.name = name;
        this.age = age;
    }

}
