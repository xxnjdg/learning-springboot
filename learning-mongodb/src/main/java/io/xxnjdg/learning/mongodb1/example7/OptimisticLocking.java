package io.xxnjdg.learning.mongodb1.example7;

import com.mongodb.client.MongoClients;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * @Version批注提供的语法类似于MongoDB上下文中的JPA语法，
 * 并确保更新仅适用于具有匹配版本的文档。因此，将version属性的实际值添加到更新查询中，
 * 使得如果与此同时其他操作更改了文档，则更新不会产生任何影响。在这种情况下，将引发OptimisticLockingFailureException。
 * 以下示例显示了这些功能：
 */
public class OptimisticLocking {
    public static void main(String[] args) {

        MongoOperations template = new MongoTemplate(
                new SimpleMongoClientDbFactory(
                        MongoClients.create("mongodb://192.168.100.33:27017/"), "mydb"));


        // 最初插入文档。版本设置为0。
        User daenerys = template.insert(new User("Daenerys"));

        //加载刚插入的文档。版本仍为0。
        User tmp = template.findOne(query(where("id").is(daenerys.getId())), User.class);

        daenerys.setLastname("Targaryen");
        template.save(daenerys); //使用version = 0更新文档。将姓氏和凹凸版本设置为1。

        /**
         * 尝试更新以前加载的文档，该文档的版本仍为0。由于当前版本为1，操作失败，并显示OptimisticLockingFailureException。
         * 幂等操作 乐观锁
         */
        template.save(tmp); // throws OptimisticLockingFailureException

        /**
         * 乐观锁定要求将WriteConcern设置为ACKNOWLEDGED。否则，可以静默吞下OptimisticLockingFailureException。
         * 从2.2版开始，从数据库中删除实体时，MongoOperations还包括@Version属性。
         * 要删除不带版本的文档，请使用MongoOperations＃remove（Query，...）而不是MongoOperations＃remove（Object）。
         *
         * 从版本2.2开始，存储库在删除版本控制的实体时检查确认删除的结果。如果无法通过CrudRepository.delete（Object）
         * 删除版本化的实体，则会引发OptimisticLockingFailureException。在这种情况下，同时会更改版本或删除对象。
         * 使用CrudRepository.deleteById（ID）绕开乐观锁定功能并删除对象，无论其版本如何。
         */
    }
}
