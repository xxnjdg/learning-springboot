package io.xxnjdg.learning.mongodb.MongoDBsupport.ScriptOperations;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ScriptOperations;
import org.springframework.data.mongodb.core.script.ExecutableMongoScript;
import org.springframework.data.mongodb.core.script.NamedMongoScript;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScriptOperationsTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    // TODO: 2020/2/5 执行不成功
    /**
     * MongoDB 4.2删除了对ScriptOperations使用的eval命令的支持。无法替代已删除的功能。
     * 执行不成功，api也过时
     */
    @Test
    public void Test(){
        ScriptOperations scriptOps = mongoTemplate.scriptOps();

        ExecutableMongoScript echoScript = new ExecutableMongoScript("function(x) { return x; }");
        //直接执行脚本，而无需将功能存储在服务器端。
        scriptOps.execute(echoScript, "directly execute script");

        //使用“ echo”作为名称存储脚本。给定名称标识脚本，并允许以后调用它。
        scriptOps.register(new NamedMongoScript("echo", echoScript));
        //使用提供的参数执行名称为“ echo”的脚本。
        scriptOps.call("echo", "execute script via name");
    }
}
