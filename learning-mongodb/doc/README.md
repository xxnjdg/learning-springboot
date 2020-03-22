 



https://docs.spring.io/spring-data/mongodb/docs/2.2.3.RELEASE/reference/html/#reference

```shell
wget https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-rhel62-4.2.3.tgz
tar zxvf mongodb-linux-x86_64-rhel62-4.2.3.tgz -C /opt/modele/
cd /opt/modele/mongodb-linux-x86_64-rhel62-4.2.3/

mkdir data
mkdir logs
mkdir etc

```



```
vi etc/mongodb.conf 
#数据库路径
dbpath=/opt/modele/mongodb-linux-x86_64-rhel62-3.2.7/data
#日志输出文件路径
logpath=/opt/modele/mongodb-linux-x86_64-rhel62-3.2.7/logs/mongodb.log
#错误日志采用追加模式
logappend=true
#启用日志文件，默认启用
journal=true
#这个选项可以过滤掉一些无用的日志信息，若需要调试使用请设置为false
quiet=true
#端口号 默认为27017
port=27017
#允许远程访问
bind_ip=0.0.0.0
#开启子进程
fork=true
#开启认证，必选先添加用户
#auth=false
```

 

```
bin/mongod --config /opt/modele/mongodb-linux-x86_64-rhel62-4.2.3/etc/mongodb.conf 
```



```
use examples
```

切换之前，无需创建数据库。当您第一次在数据库中存储数据时（例如在数据库中创建第一个集合），MongoDB会创建数据库。

MongoDB将文档存储在集合中。集合类似于关系数据库中的表。如果不存在集合，则在您第一次为该集合存储数据时，MongoDB会创建该集合。



https://www.jianshu.com/p/cf7416f4a036

位于org.springframework.data.mongodb.core程序包中的MongoTemplate类是Spring对MongoDB支持的中心类，并提供了丰富的功能集可与数据库进行交互。该模板提供了创建，更新，删除和查询MongoDB文档的便捷操作，并提供了域对象和MongoDB文档之间的映射。

MongoDB文档和域类之间的映射是通过委派MongoConverter接口的实现来完成的。Spring提供了MappingMongoConverter，但是您也可以编写自己的转换器。有关更多详细信息，请参见“自定义转换-覆盖默认映射”。

MongoTemplate类实现接口MongoOperations。MongoOperations上的方法尽可能以MongoDB驱动程序Collection对象上可用的方法命名，以使熟悉该驱动程序API的现有MongoDB开发人员熟悉该API。例如，您可以找到诸如find，findAndModify，findAndReplace，findOne，插入，删除，保存，更新和updateMulti之类的方法。设计目标是使在基本MongoDB驱动程序和MongoOperations的使用之间的转换尽可能容易。这两个API之间的主要区别在于，可以将MongoOperations传递给域对象，而不是传递给Document。另外，MongoOperations具有用于查询，条件和更新操作的流畅API，而不是填充文档来为这些操作指定参数。

MongoTemplate使用的默认转换器实现是MappingMongoConverter。虽然MappingMongoConverter可以使用其他元数据来指定对象到文档的映射，但是它也可以通过使用一些ID和集合名称的映射约定来转换不包含其他元数据的对象。这些约定以及映射注释的使用在“映射”一章中进行了说明。

MongoTemplate的另一个主要功能是将MongoDB Java驱动程序引发的异常转换为Spring的可移植数据访问异常层次结构。有关更多信息，请参见“异常转换”。

MongoTemplate提供了许多便利的方法来帮助您轻松执行常见任务。但是，如果您需要直接访问MongoDB驱动程序API，则可以使用几种Execute回调方法之一。execute回调为您提供了对com.mongodb.client.MongoCollection或com.mongodb.client.MongoDatabase对象的引用。有关更多信息，请参见“执行回调”部分。

**说了一堆废话，结论MongoTemplate很重要，可以crud**

创建MongoTemplate时可能要设置的其他可选属性是默认的WriteResultCheckingPolicy，WriteConcern和ReadPreference属性。



 WriteResultChecking  WriteConcernWrite ConcernResolver 略过

MongoConverter通过（通过约定）识别Id属性名称，导致在字符串和存储在数据库中的ObjectId之间进行隐式转换。

MongoDB要求所有文档都有一个_id字段。如果不提供，驱动程序会为ObjectId分配一个生成的值。当您使用MappingMongoConverter时，某些规则将控制Java类中的属性如何映射到此_id字段：

**用@Id（org.springframework.data.annotation.Id）注释的属性或字段映射到_id字段。**

**没有注释但名为id的属性或字段映射到_id字段。**

下面概述了使用MappingMongoConverter（MongoTemplate的默认值）时，对映射到_id文档字段的属性进行的类型转换（如果有）。

如果要避免将整个Java类名写为类型信息，而是想使用键，则可以在实体类上使用@TypeAlias批注。如果您需要进一步自定义映射，请查看TypeInformationMapper接口。可以在DefaultMongoTypeMapper上配置该接口的实例，然后可以在MappingMongoConverter上对其进行配置。以下示例显示如何为实体定义类型别名：

```java
@TypeAlias("pers")
class Person {

}
```



MongoTemplate上有几种方便的方法可以保存和插入对象。要对转换过程进行更细粒度的控制，可以在MappingMongoConverter中注册Spring转换器，例如Converter <Person，Document>和Converter <Document，Person>。

**insert**和**save**操作之间的区别在于，如果对象尚不存在，则save操作将执行insert操作。

 难道对象不存在，就不能插入？？？

使用保存操作的简单情况是保存POJO。在这种情况下，**集合名称由类的名称**（不完全限定）确定。您也可以使用特定的集合名称调用保存操作。您可以使用映射元数据覆盖存储对象的集合。

有两种方法来管理用于**文档的集合名称**。使用的默认集合名称是更改为**以小写字母开头的类**名称。因此，com.test.Person类存储在person集合中。您可以通过在**@Document注解**中**提供其他集合名**称来**自定义此名称**。您还可以通过提供自己的集合名称作为所选**MongoTemplate方法调用的最后一个参数来覆盖集合名称**。

insert：插入一个对象。如果存在具有**相同ID**的现有文档，则会**生成错误**。

insertAll：将对象的集合作为第一个参数。此方法根据先前指定的规则检查每个对象并将其插入到适当的集合中。

save：保存对象，**覆盖**可能具有**相同ID**的所有对象。



mongodb server update 函数查询可以查到多个，但只会更新第一个，这是**默认行为**

可以设置参数，更新多个

对于更新，您可以使用MongoOperation.updateFirst更新找到的第一个文档，或者可以使用MongoOperation.updateMulti方法更新找到的所有与查询匹配的文档。以下示例显示了所有SAVINGS帐户的更新，其中，我们使用$ inc运算符向余额中添加了一笔$ 50.00的一次性奖金：

updateFirst：使用更新的文档更新与查询文档条件匹配的第一个文档。

updateMulti：使用更新的文档更新所有与查询文档条件匹配的对象。

```java
Update addToSet (String key, Object value) Update using the $addToSet update modifier

Update currentDate (String key) Update using the $currentDate update modifier

Update currentTimestamp (String key) Update using the $currentDate update modifier with $type timestamp

Update inc (String key, Number inc) Update using the $inc update modifier

Update max (String key, Object max) Update using the $max update modifier

Update min (String key, Object min) Update using the $min update modifier

Update multiply (String key, Number multiplier) Update using the $mul update modifier

Update pop (String key, Update.Position pos) Update using the $pop update modifier

Update pull (String key, Object value) Update using the $pull update modifier

Update pullAll (String key, Object[] values) Update using the $pullAll update modifier

Update push (String key, Object value) Update using the $push update modifier

Update pushAll (String key, Object[] values) Update using the $pushAll update modifier

Update rename (String oldName, String newName) Update using the $rename update modifier

Update set (String key, Object value) Update using the $set update modifier

Update setOnInsert (String key, Object value) Update using the $setOnInsert update modifier

Update unset (String key) Update using the $unset update modifier
```



与执行updateFirst操作相关，您还可以执行“ upsert”操作，如果找不到与查询匹配的文档，则将执行插入操作。插入的文档是查询文档和更新文档的组合。下面的示例显示如何使用upsert方法：

```java
template.upsert(query(where("ssn").is(1111).and("firstName").is("Joe").and("Fraizer").is("Update")), update("address", addr), Person.class);
```



upsert不支持ordering。请使用findAndModify来应用Sort。

remove

```java
template.remove(tywin, "GOT");                                              

template.remove(query(where("lastname").is("lannister")), "GOT");           

template.remove(new Query().limit(3), "GOT");                               

template.findAllAndRemove(query(where("lastname").is("lannister"), "GOT");  

template.findAllAndRemove(new Query().limit(3), "GOT"); 
```





从3.6版开始，MongoDB支持根据提供的JSON Schema验证文档的集合。可以在创建集合时定义架构本身以及验证操作和级别，如以下示例所示：



```json
{
  "type": "object",                                                        

  "required": [ "firstname", "lastname" ],                                 

  "properties": {                                                          

    "firstname": {                                                         
      "type": "string",
      "enum": [ "luke", "han" ]
    },
    "address": {                                                           
      "type": "object",
      "properties": {
        "postCode": { "type": "string", "minLength": 4, "maxLength": 5 }
      }
    }
  }
}
```

JSON模式文档始终从其根开始描述整个文档。模式是一个模式对象本身，可以包含描述属性和子文档的嵌入式模式对象。

required是描述文档中哪些属性是必需的属性。可以选择指定它，以及其他模式约束。有关可用关键字，请参阅MongoDB文档。

属性与描述对象类型的架构对象有关。它包含特定于属性的架构约束。

firstname为文档中的firsname字段指定约束。在这里，它是一个基于字符串的属性元素，声明可能的字段值。

address是一个子文档，该子文档在其postCode字段中定义了值的架构。

您可以通过指定模式文档（即通过使用Document API解析或构建文档对象）或通过使用org.springframework.data.mongodb.core.schema中的Spring Data的JSON模式实用程序来构建模式来提供模式。。MongoJsonSchema是所有与JSON模式相关的操作的入口。以下示例显示如何使用MongoJsonSchema.builder（）创建JSON模式：



设置模式可能是一项耗时的工作，我们鼓励每个决定这样做的人都花点时间。重要的是，架构更改可能很难。但是，有时可能不想让它停滞不前，这就是JsonSchemaCreator发挥作用的地方。

JsonSchemaCreator及其默认实现从映射基础结构提供的域类型元数据中生成MongoJsonSchema。这意味着，将考虑带注释的属性以及潜在的自定义转换。



当与MongoDB进行更底层的交互时，MongoOperations接口是核心组件之一。它提供了广泛的方法，涵盖了从集合创建，索引创建和CRUD操作到更高级的功能（如Map-Reduce和聚合）的需求。您可以为每种方法找到多个重载。其中大多数涵盖了API的可选或可为空的部分。



FluentMongoOperations为MongoOperations的常用方法提供了更狭窄的接口，并提供了更具可读性的流利API,入口点（插入（…），查找（…），更新（…）和其他）基于要运行的操作遵循自然的命名模式。从入口点开始，API旨在仅提供上下文相关的方法，这些方法导致终止方法调用实际的MongoOperations对应对象-在以下示例中为all方法：



归类可用于创建集合和索引。如果创建指定排序规则的集合，则除非指定其他排序规则，否则该排序规则将应用于索引创建和查询。排序规则对整个操作有效，不能在每个字段中指定。

像其他元数据一样，归类可以通过@Document注释的归类属性从域类型派生，并将在执行查询，创建集合或索引时直接应用

当MongoDB在首次交互时自动创建集合时，将不使用带注释的排序规则。这将需要额外的商店交互来延迟整个过程。在这种情况下，请使用MongoOperations.createCollection

如果未指定排序规则（Collation.simple（）），则MongoDB使用简单的二进制比较。

仅当用于该操作的排序规则与索引排序规则匹配时，才使用索引。

好处

按示例查询非常适合几种用例：

使用一组静态或动态约束来查询数据存储。

频繁重构域对象，而不必担心破坏现有查询。

独立于基础数据存储API进行工作。

局限性

不支持嵌套或分组属性约束，例如firstname =？0或（firstname =？1和lastname =？2）。

仅支持字符串的开始/包含/结束/正则表达式匹配，以及其他属性类型的完全匹配。



前面的示例显示了一个简单的域对象。您可以使用它来创建一个示例。默认情况下，具有空值的字段将被忽略，并且使用商店特定的默认值来匹配字符串。可以使用工厂方法或使用ExampleMatcher构建示例。例子是一成不变的。以下清单显示了一个简单的示例：

示例不限于默认设置。您可以使用ExampleMatcher为字符串匹配，空值处理和特定于属性的设置指定自己的默认值，如以下示例所示：












