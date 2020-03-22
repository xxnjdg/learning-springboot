package io.xxnjdg.learning.mongodb1.QueryingDocuments.jsonschema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestCollection {
    private String lastname;
    private String firstname;
    private Address address;
}
