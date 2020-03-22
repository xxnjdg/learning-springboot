package io.xxnjdg.learning.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRedis {

    private Logger logger = LoggerFactory.getLogger(TestRedis.class);

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Test
    public void test(){
        for (int i = 0; i < 10; i++) {
            redisTemplate.opsForValue().set("mekey_"+i,"mykey"+i);
        }
    }

    @Test
    public void test1(){
        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                Long aLong = redisConnection.dbSize();
                ((StringRedisConnection)redisConnection).set("myKey_connection","value");
                logger.info(aLong.toString());
                return null;
            }
        });
    }

    @Test
    public void test2(){
        Object execute = redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                Boolean set = ((StringRedisConnection) redisConnection)
                        .set("key_test2", "key_test2", Expiration.seconds(6000), RedisStringCommands.SetOption.ifAbsent());
                logger.info(set.toString());
                return set;
            }
        });

        logger.info(execute == null?"yes":execute.toString());
    }



}
