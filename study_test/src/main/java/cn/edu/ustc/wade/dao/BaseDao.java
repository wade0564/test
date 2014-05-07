package cn.edu.ustc.wade.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.edu.ustc.wade.util.Params;

public class BaseDao {

	protected Connection conn = null;
	protected PreparedStatement pstmt = null;
	protected ResultSet rs = null;
	protected static String user;
	protected static String password;
	protected static String url;
	static {
		user = Params.GPDB_USERNAME.getValue();
		password = Params.GPDB_PASSWORD.getValue();
		url = "jdbc:postgresql://" + Params.GPDB_SERVER.getValue() + ":"
				+ Params.GPDB_PORT.getValue() + "/"
				+ Params.GPDB_DATABASE.getValue();

		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	protected void preparedSql(String sql, Object... params)
			throws SQLException {
		pstmt = conn.prepareStatement(sql);
		for (int i = 0; i < params.length; i++) {
			pstmt.setObject(i + 1, params[i]);
		}
	}


	public ResultSet query(String sql, Object... params) throws SQLException {
		openConn();
		preparedSql(sql, params);
		rs = pstmt.executeQuery();
		return rs;
	}

	

	public int update(String sql, Object... params) {
		openConn();
		int count = 0;
		try {
			preparedSql(sql, params);
			count = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();	
		}
		return count;
	}

	protected void openConn() {
		try {
			if(conn==null||conn.isClosed()){
				conn = DriverManager.getConnection(url, user, password);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	protected void close() {
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	protected void closeAll() {
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
