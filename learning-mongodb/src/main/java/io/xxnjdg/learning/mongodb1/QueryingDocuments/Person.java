package io.xxnjdg.learning.mongodb1.QueryingDocuments;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Person {
    private String id;
    private String name;
    private int age;
    private Account accounts;

    public Person(String name, int age, Account account) {
        this.name = name;
        this.age = age;
        this.accounts = account;
    }
}