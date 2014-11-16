package cn.edu.ustc.wade.db;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import junit.framework.TestCase;
import cn.edu.ustc.wade.util.HsqldbConnection;
public class DBTest extends TestCase {
	private HsqldbConnection conn;
	public DBTest() throws SQLException {
		conn = new HsqldbConnection(
				"src/main/resources/org/ourpioneer/vehicle/db/", "vehicle");
	}
	public void testInsert() throws SQLException {
		String sql = "insert into vehicle(PLATE,CHASSIS,COLOR,WHEEL,SEAT) values(?,?,?,?,?)";
		PreparedStatement pstat = conn.createPreparedStatement(sql);
		pstat.setString(1, "辽BTEST");
		pstat.setString(2, "1234567890");
		pstat.setString(3, "BLACK");
		pstat.setInt(4, 4);
		pstat.setInt(5, 6);
		int execute = pstat.executeUpdate();
		pstat.close();
		conn.close();
		assertEquals(1, execute);
	}
}
