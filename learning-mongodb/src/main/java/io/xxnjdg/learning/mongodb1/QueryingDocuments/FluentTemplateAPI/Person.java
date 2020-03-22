package io.xxnjdg.learning.mongodb1.QueryingDocuments.FluentTemplateAPI;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
public class Person {
    @Id
    private String id;
    private String name;
    private String file1;
    private String file2;
    private Boolean jedi;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Person(String name, Boolean jedi, int age) {
        this.name = name;
        this.jedi = jedi;
        this.age = age;
    }

    public Person(String name, String file1, String file2, Boolean jedi, int age) {
        this.name = name;
        this.file1 = file1;
        this.file2 = file2;
        this.jedi = jedi;
        this.age = age;
    }
}