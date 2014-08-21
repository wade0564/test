import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.emc.prometheus.asupqueuing.dao.GpUploadDao;

public class CheckAsupExistence {

	private final static Logger log = LoggerFactory
			.getLogger(CheckAsupExistence.class);

	public static void main(String[] args) throws SQLException {

		BaseDao dao = new BaseDao();

		int count = 1000;
//		int count = 1000000;
		String sql = "select asup.asupid ,external,filename from asup.asup LEFT JOIN asup.geninfo ON asup.geninfo.asupid=asup.asup.asupid WHERE TYPE = 'Autosupport' AND filename ~ '\\\\d{4}/\\\\d{2}/\\\\d{2}/\\\\d{4}-\\\\d{2}-\\\\d{2}_.*' limit "+count;

		System.out.println(sql);
		int maxAsupId;
		int external;
		String filename;
		String path = "/asuprdr10/asupmails/asup_archive/DD";

		while(true){
			maxAsupId= check(dao, sql, path);
			sql ="select asup.asupid ,external,filename from asup.asup LEFT JOIN asup.geninfo ON asup.geninfo.asupid=asup.asup.asupid WHERE TYPE = 'Autosupport' AND filename ~ '\\\\d{4}/\\\\d{2}/\\\\d{2}/\\\\d{4}-\\\\d{2}-\\\\d{2}_.*' limit "+count;
		}
		

	}

	private static int check(BaseDao dao, String sql, String path)
			throws SQLException {
		int maxAsupId =0;
		int external;
		String filename;
		ResultSet rs = dao.query(sql);
		while (rs.next()) {
			maxAsupId = rs.getInt("asupid");
			external = rs.getInt("external");
			filename = rs.getString("filename");
			if (external == 1) {
				filename = path + "/emails/" + filename;

			} else {
				filename = path + "internals/emails/" + filename;
			}

			File file = new File(filename);

			if (!file.exists()) {
				log.error(file.getAbsolutePath() +"is not exists");
				System.exit(-1);
			}
		}
		
		return maxAsupId; 
	}

}
