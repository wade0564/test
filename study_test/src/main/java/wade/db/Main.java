package wade.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {

	public static void main(String[] args) {
		try {
			//加载HSQLDB的JDBC驱动
			Class.forName("org.hsqldb.jdbcDriver");
			//在内存中建立数据库memdb,用户名为sa,密码为空
//			Connection conn = DriverManager.getConnection("jdbc:hsqldb:mem:memdb","sa",null);
			Connection conn = DriverManager.getConnection("jdbc:hsqldb:file:testdb","sa","");
			System.out.println("connect to memdb OK");
			
			Statement stat = conn.createStatement();
			//新建数据表
			stat.executeUpdate("create text table person(NAME VARCHAR(20), AGE INTEGER)");
			stat.executeUpdate("SET TABLE person SOURCE \"wade.db\"");
			
//			stat.executeUpdate("create table person(NAME VARCHAR(20), AGE INTEGER)");
//			System.out.println("create TABLE:person OK");
			
			//插入数据
			stat.executeUpdate("INSERT INTO person VALUES('wade',22)");
			stat.executeUpdate("INSERT INTO person VALUES('amos','25')");
			System.out.println("insert data into TABLE:person OK!");

//			conn.close();
			
//			stat.execute("SHUTDOWN");
//			System.out.println("SHUTDOWN");
			
			Connection conn2 = DriverManager.getConnection("jdbc:hsqldb:file:testdb","sa","");
			
			//查询数据
			PreparedStatement pstmt = conn2.prepareStatement("SELECT * FROM person");
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				String s = null;
				s = rs.getString(1) + "," + rs.getString(2);
				System.out.println(s);
			}
			System.out.println("select data OK");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
