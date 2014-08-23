package wade.exception;

import java.sql.SQLException;

public class ExceptionTest {
	
	public static void main(String[] args) {
		
		
		
		try {
			
			throw  new  Exception("exception");
//			throw  new  SQLException("wade");
		} catch (SQLException e) {
			System.out.println("sql");
		}catch (Exception e) {
			System.out.println("exception");
		}
		
		
		
	}
	

}
