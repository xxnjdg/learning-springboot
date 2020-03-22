package io.xxnjdg.learning.mongodb1.example4;

import com.mongodb.client.MongoDatabase;
import org.springframework.dao.DataAccessException;

/**
 * 虽然com.mongodb.MongoClient是MongoDB驱动程序API的入口点，但连接到特定的MongoDB数据库实例需要其他信息，
 * 例如数据库名称以及可选的用户名和密码。利用这些信息，您可以获得com.mongodb.client.MongoDatabase对象，
 * 并访问特定MongoDB数据库实例的所有功能。Spring提供了org.springframework.data.mongodb.core.MongoDbFactory接口，
 * 如下面的清单所示，以引导到数据库的连接
 */
public interface MongoDbFactory {

  MongoDatabase getDb() throws DataAccessException;

  MongoDatabase getDb(String dbName) throws DataAccessException;
}