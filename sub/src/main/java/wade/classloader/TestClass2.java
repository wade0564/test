package wade.classloader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestClass2 {
	 
	 
	public void test(){
		
		System.out.println("This is a test method !!!");
	}
	
	
	
	
	public static void main(String[] args) throws SQLException {
	
		
		print(TestClass2.class.getClassLoader());
		
		print("==============Thread contextloader");
		
		print(Thread.currentThread().getContextClassLoader());
		
		
		 Connection conn;
		 
		 

			try {
				Class.forName("org.postgresql.Driver");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			conn= openConnection();
			 print("connection classloader");
			 print(conn.getClass().getClassLoader());
			 print(conn.getClass());

	}
	
	public static  Connection openConnection(){
		Connection conn =null;
		String user ="hermes";
		String password = "dangerous";
		String url = "jdbc:postgresql://10.110.178.92:5432/test";
		try {
			conn = DriverManager.getConnection(url, user, password);
			print("driver manager");
			print(DriverManager.class.getClassLoader());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	public static void print(Object o) {
		System.out.println(o);
		
	}
	
	

}
