package com.emc.prometheus.parser.dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import com.emc.prometheus.parser.pojo.LOG_FILE_TYPE;
import com.emc.prometheus.parser.pojo.LogInfo;

@Component
public class LogDao extends JdbcDaoSupport {
	
	@Autowired
	public LogDao( DataSource dataSource){
		super.setDataSource(dataSource);
	}
	

	final static Logger log = LoggerFactory.getLogger(LogDao.class);
	
	private @Value("${LOG.SELECT.LIMIT}") Long logSelectLimit; 
	private @Value("${LOG.MIN.ASUPID}") Long minAsupId; 
	private @Value("${LOG.MAX.ASUPID}") Long maxAsupId; 



	
	public List<LogInfo> getLogInfos(){
		
		//TODO getLastAsupId
		
		DB db ;
		Long lastAsupId = 1L;
		
		//
		String sql = String.format("SELECT asupid, sn, chassis_sn, (CASE WHEN from_sub=TRUE THEN 'SUB' ELSE 'ASUP' END) as type, file_handler"
										+ " FROM asup.geninfo"
										+ " WHERE asupid >= %s %s and file_handler is not null"
										+ " order by asupid asc LIMIT %s",lastAsupId>minAsupId?lastAsupId:minAsupId,maxAsupId==null?"":"and asupid<="+maxAsupId,logSelectLimit);
		
		//TODO exception Catch
		List<LogInfo> logInfos = getJdbcTemplate().query(sql,new RowMapper<LogInfo>(){
			@Override
			public LogInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				LogInfo logInfo = new LogInfo();
				logInfo.setAsupId(rs.getLong("asupid"));
				logInfo.setSn(rs.getString("sn"));
				logInfo.setChassis_sn(rs.getString("chassis_sn"));
				logInfo.setType(LOG_FILE_TYPE.valueOf(rs.getString("type")));
				logInfo.setFile_handler(rs.getString("file_handler"));
				return logInfo;
			}
		});
		
		return logInfos;
		
		
	}
	
	
	
	

}
