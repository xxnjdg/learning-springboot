package io.xxnjdg.learning.mybatis.helloworld.mapper;

import io.xxnjdg.learning.mybatis.helloworld.model.Employee;

/**
 * @author djr
 * @version 1.0
 * @date 2020/3/12 11:30
 */
public interface EmployeeMapper {
    Employee queryEmployeeById(int id);
}
