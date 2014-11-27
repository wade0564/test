package com.emc.prometheus.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.emc.prometheus.parser.dedupe.Range;
import com.emc.prometheus.parser.pojo.LOG_TYPE;
import com.emc.prometheus.parser.pojo.StoreFile;
import com.emc.prometheus.parser.util.DBUtils;
import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.Transaction;

import dnl.utils.text.table.TextTable;

/**
 * @author wade
 * @version Nov 25, 2014 4:15:28 PM
 */
public class PrintDB {
	final static Logger log = LoggerFactory.getLogger(DBUtils.class);

	public final static String LAST_ASUPID = "lastAsupid";

	private static String ENV_PATH;

	private static Environment environment;

	private static Database database;

	private static Database catalogDatabase;

	private static Transaction transaction;

	private static Map<Class, EntryBinding> classBindingMap;

	static {
		try {
			ENV_PATH = PropertiesLoaderUtils.loadAllProperties(
					"logParser.properties").getProperty(
					"LOG.PARSER.IN_MEMORY_DB.EVN_PATH");
			environment = createEnvironment(ENV_PATH);
			database = createDatabase(environment, "Binding Database");
			catalogDatabase = createDatabase(environment, "Catalog database");
			createClassBindingMap();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			log.error("Can't find the configuration files {}",
					"logParser.properties");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			log.error("Can't create In-Memory Database");
		}
	}

	public static void main(String[] args) {
		printAllDataBaseEntry();
	}

	private static void createClassBindingMap() {
		classBindingMap = new HashMap<Class, EntryBinding>();
		// Ranges
		StoredClassCatalog rangeCatalog = new StoredClassCatalog(
				catalogDatabase);
		EntryBinding<ArrayList> rangeBinding = new SerialBinding(rangeCatalog,
				ArrayList.class);
		// StoreFile
		StoredClassCatalog storeFileCatalog = new StoredClassCatalog(
				catalogDatabase);
		EntryBinding<StoreFile> storeFileBinding = new SerialBinding<StoreFile>(
				storeFileCatalog, StoreFile.class);
		// lastAsupid
		StoredClassCatalog asupIdCatalog = new StoredClassCatalog(
				catalogDatabase);
		EntryBinding<Long> asupIdBinding = new SerialBinding<Long>(
				asupIdCatalog, Long.class);
		classBindingMap.put(Range.class, rangeBinding);
		classBindingMap.put(StoreFile.class, storeFileBinding);
		classBindingMap.put(Long.class, asupIdBinding);

	}

	private static Environment createEnvironment(String envHome)
			throws Exception {
		EnvironmentConfig envCfg = new EnvironmentConfig();
		envCfg.setAllowCreate(true);
		envCfg.setReadOnly(false);
		envCfg.setTransactional(true);

		Environment mydbEnv = new Environment(new File(envHome), envCfg);
		System.out.println("Env Config: " + mydbEnv.getConfig());

		return mydbEnv;
	}

	private static Database createDatabase(Environment dbEnv,
			String databaseName) throws Exception {
		DatabaseConfig dbCfg = new DatabaseConfig();
		dbCfg.setAllowCreate(true);
		dbCfg.setReadOnly(false);
		dbCfg.setTransactional(true);

		Database database = dbEnv.openDatabase(null, databaseName, dbCfg);
		log.info("Database Name: {}", database.getDatabaseName());

		return database;
	}

	public static void printAllDataBaseEntry() {
		Cursor cursor = database.openCursor(transaction, null);

		// Cursors need a pair of DatabaseEntry objects to operate. These hold
		// the key and data found at any given position in the database.
		DatabaseEntry foundKey = new DatabaseEntry();
		DatabaseEntry foundData = new DatabaseEntry();

		// To iterate, just call getNext() until the last database record has
		// been
		// read. All cursor operations return an OperationStatus, so just read
		// until we no longer see OperationStatus.SUCCESS
		
		List<String[]> storeFileTableData = new ArrayList<>();
		List<String[]> rangeTableData = new ArrayList<>();
		boolean isStoreFileFirst = true;
		boolean isRangeFirst = true;
		while (cursor.getNext(foundKey, foundData, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
			String keyString = new String(foundKey.getData());

			LOG_TYPE[] types = LOG_TYPE.values();
			int i;
			for (i = 0; i < types.length; i++) {
				LOG_TYPE type = types[i];
				if (keyString.equals(type.toString())) {
					StoreFile storeFile = (StoreFile) classBindingMap.get(StoreFile.class).entryToObject(foundData);
						storeFileTableData.add(storeFile.getData(type.toString()));
					break;
				}
			}
			if (i < types.length) {
				continue;
			}

			if (keyString.endsWith(LAST_ASUPID)) {
				System.out.println("   ______________________________________________________________________________");
				System.out.println("   LAST_ASUPID............"+classBindingMap.get(Long.class).entryToObject(foundData));
				continue;
			}

			// Ranges
			List<Range> ranges = (List<Range>) classBindingMap.get(Range.class)
					.entryToObject(foundData);
			for (int j = 0; j < ranges.size(); j++) {
				if(j==0){
					rangeTableData.add(ranges.get(j).getData(keyString));
				}else {
					rangeTableData.add(ranges.get(j).getData(""));
				}
				
			}

		}
		
		String[]  storeFileTablecolumnNames  = {"log_type","storeFile","content","lastPos","currentPos"};
		String[]  rangeTableColumnNames  = {"sn_type","start:  ts   |            hash                |index",
				"end:    ts   |             hash               |index"};
		TextTable storeFileTable = new TextTable(storeFileTablecolumnNames, storeFileTableData.toArray(new String[][]{}));
		storeFileTable.setAddRowNumbering(true);
		storeFileTable.setSort(0);
		storeFileTable.printTable();
		TextTable rangeTable = new TextTable(rangeTableColumnNames, rangeTableData.toArray(new String[][]{}));
		rangeTable.setAddRowNumbering(true);
//		rangeTable.setSort(0);
		rangeTable.printTable();
	}
}
