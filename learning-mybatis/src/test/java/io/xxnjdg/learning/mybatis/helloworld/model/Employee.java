package io.xxnjdg.learning.mybatis.helloworld.model;

import lombok.Data;

/**
 * @author djr
 * @version 1.0
 * @date 2020/3/12 11:09
 */
@Data
public class Employee {
    private int id;
    private String lastName;
    private String qender;
    private String email;
}
