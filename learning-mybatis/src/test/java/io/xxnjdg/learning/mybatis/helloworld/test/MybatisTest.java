package io.xxnjdg.learning.mybatis.helloworld.test;


import io.xxnjdg.learning.mybatis.helloworld.mapper.EmployeeMapper;
import io.xxnjdg.learning.mybatis.helloworld.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author djr
 * @version 1.0
 * @date 2020/3/12 11:11
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MybatisTest {


    public SqlSessionFactory getSqlSessionFactory() throws IOException {
        String resource = "helloworld/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        return new SqlSessionFactoryBuilder().build(inputStream);
    }

    @Test
    public void test() throws IOException {

        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

        try (SqlSession session = sqlSessionFactory.openSession()) {
            Employee employee = session.selectOne("io.xxnjdg.learning.mybatis.helloworld.mapper.EmployeeMapper.queryEmployeeById", 1);
            log.info(employee.toString());
        }
    }

    @Test
    public void test1() throws IOException {

        SqlSessionFactory sqlSessionFactory = getSqlSessionFactory();

        try (SqlSession session = sqlSessionFactory.openSession()) {
            EmployeeMapper mapper = session.getMapper(EmployeeMapper.class);
            Employee employee = mapper.queryEmployeeById(1);
            log.info(employee.toString());
        }
    }

}
