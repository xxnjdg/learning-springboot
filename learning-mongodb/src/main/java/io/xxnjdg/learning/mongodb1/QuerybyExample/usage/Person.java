package io.xxnjdg.learning.mongodb1.QuerybyExample.usage;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Person {

  @Id
  private String id;
  private String firstname;
  private String lastname;
//  private Address address;

  // … getters and setters omitted
}