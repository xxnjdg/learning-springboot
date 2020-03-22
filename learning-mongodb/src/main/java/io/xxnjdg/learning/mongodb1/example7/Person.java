package io.xxnjdg.learning.mongodb1.example7;

public class Person {

  /**
   * 用@Id（org.springframework.data.annotation.Id）注释的属性或字段映射到_id字段。
   *
   * 没有注释但名为id的属性或字段映射到_id字段。
   */
  private String id;
  private String name;
  private int age;

  public Person(String name, int age) {
    this.name = name;
    this.age = age;
  }

  public String getId() {
    return id;
  }
  public String getName() {
    return name;
  }
  public int getAge() {
    return age;
  }

  @Override
  public String toString() {
    return "Person [id=" + id + ", name=" + name + ", age=" + age + "]";
  }

}