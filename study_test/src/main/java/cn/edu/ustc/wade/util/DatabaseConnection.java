package cn.edu.ustc.wade.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 数据库连接工具类基类
 * 
 * @author Nanlei
 * 
 */
public abstract class DatabaseConnection {
	protected Connection conn;
	/**
	 * 关闭数据库连接
	 * 
	 * @throws SQLException
	 */
	public void close() throws SQLException {
		conn.close();
	}
	/**
	 * 创建语句对象
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Statement createStatement() throws SQLException {
		return conn.createStatement();
	}
	/**
	 * 创建预处理SQL语句对象
	 * 
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public PreparedStatement createPreparedStatement(String sql)
			throws SQLException {
		return conn.prepareStatement(sql);
	}
}
