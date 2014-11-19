package com.emc.prometheus.parser.dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.emc.prometheus.parser.pojo.LOG_FILE_TYPE;
import com.emc.prometheus.parser.pojo.LOG_TYPE;
import com.emc.prometheus.parser.pojo.LogInfo;
import com.emc.prometheus.parser.pojo.CompositeLogInfo;
import com.google.common.base.Joiner;

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



	
	@Transactional
	public CompositeLogInfo getLogInfos(){
		
		//TODO getLastAsupId
		
		CompositeLogInfo compositeLogInfo  = new CompositeLogInfo();
		
		DB db ;
		Long lastAsupId = 1L;
		
		String geninfoSql = String.format("SELECT asupid, sn, epoch, chassis_sn, (CASE WHEN from_sub=TRUE THEN 'SUB' ELSE 'ASUP' END) as type, file_handler"
										+ " FROM asup.geninfo"
										+ " WHERE asupid >= %s %s and file_handler is not null and (from_sub=false or (from_sub=true and file_handler !~ 'autosupport')  )"
										+ " order by asupid asc LIMIT %s"
										,lastAsupId>minAsupId?lastAsupId:minAsupId
										,maxAsupId==null?"":"and asupid<="+maxAsupId
										,logSelectLimit);
		
		log.debug("Geninfo SQL: {}",geninfoSql);
		
		final Map<LogInfo, List<LogInfo>> subLogMap = new HashMap<LogInfo, List<LogInfo>>();
		
		//TODO exception Catch
		final List<LogInfo> logInfos = new ArrayList<>();
				getJdbcTemplate().query(geninfoSql,new RowCallbackHandler() {
					
					@Override
					public void processRow(ResultSet rs) throws SQLException {
						LogInfo logInfo = new LogInfo();
						logInfo.setAsupId(rs.getLong("asupid"));
						logInfo.setSn(rs.getString("sn"));
						logInfo.setEpoch(rs.getLong("epoch"));
						logInfo.setChassis_sn(rs.getString("chassis_sn"));
						logInfo.setType(LOG_FILE_TYPE.valueOf(rs.getString("type")));
						logInfo.setFile_handler(rs.getString("file_handler"));
						logInfos.add(logInfo);
						if(logInfo.getType()==LOG_FILE_TYPE.SUB){
							subLogMap.put(logInfo, new ArrayList<LogInfo>());
						}
					}
				});
		
		if(!subLogMap.isEmpty()){
			List<Long> subids = new ArrayList<>();
			for (LogInfo subLogInfo : subLogMap.keySet()) {
				subids.add(subLogInfo.getAsupId());
			}
			//filter the ddfs.info
			String subContentSql = String.format("SELECT subid , (CASE WHEN asupid IS NULL THEN subid ELSE asupid END) AS asupid, path AS file_handler "
													+ "FROM asup.sub_content "
													+ "WHERE subid in(%s) and path !~ 'ddfs.info' "
													+ "ORDER BY asupid"
													, Joiner.on(",").join(subids));
			
			log.debug("SubContent SQL: {}",subContentSql);
			
			getJdbcTemplate().query(subContentSql,new RowCallbackHandler() {
				@Override
				public void processRow(ResultSet rs) throws SQLException {
					
					Long subid = rs.getLong("subid");
					for (Entry<LogInfo, List<LogInfo>> subLogEntry : subLogMap.entrySet()) {
						LogInfo subLogInfo=subLogEntry.getKey() ;
						if(subid.equals(subLogInfo.getAsupId())){
							LogInfo subContentLogInfo = new LogInfo();
							subContentLogInfo.setAsupId(rs.getLong("asupid"));
							subContentLogInfo.setSn(subLogInfo.getSn());
							subContentLogInfo.setEpoch(subLogInfo.getEpoch());
							subContentLogInfo.setChassis_sn(subLogInfo.getChassis_sn());
							//select Type
							subContentLogInfo.setType(LOG_FILE_TYPE.getType(rs.getString("file_handler")));
							subContentLogInfo.setFile_handler(rs.getString("file_handler"));
							subLogEntry.getValue().add(subContentLogInfo);
						}
					}
				}});
			
		}
		
		
		if (!logInfos.isEmpty()) {
			compositeLogInfo.setLogInfos(logInfos);
			if (!subLogMap.isEmpty()) {
				compositeLogInfo.setSubLogMap(subLogMap);
			}

		}
		return compositeLogInfo;
	}
}
