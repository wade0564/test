package cn.edu.ustc.wade.util;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Hsql数据库连接工具类
 * 
 * @author Nanlei
 * 
 */
public class HsqldbConnection extends DatabaseConnection {
	/**
	 * 构造方法，加载HSQL数据库驱动并获取连接对象
	 * 
	 * @param databaseFileNamePrefix
	 * @throws SQLException
	 */
	public HsqldbConnection(String path, String prefix) throws SQLException {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
		} catch (ClassNotFoundException e) {
			throw new SQLException("HSQLDB database driver is not found");
		}
		conn = DriverManager.getConnection("jdbc:hsqldb:file:" + path + prefix,
				"ROOT", "123");
	}
	/**
	 * 关闭数据库和连接
	 */
	public void close() throws SQLException {
		Statement stat = createStatement();
		stat.execute("SHUTDOWN");
		super.close();
	}
}
