package cn.edu.ustc.wade.db;

import java.io.File;
import java.io.Serializable;
import java.util.Iterator;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.Transaction;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.StoreConfig;
import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

public class JEtest {
	
	private static final String ENV_PATH = "c:/berkeley_db/";

	public static void main(String[] args) throws Exception {
		
		JEtest je = new JEtest();

		//打开Database环境
		Environment mydbEnv = je.testEnvironment(ENV_PATH);
		
		//打开Database
		Database mytestdb = je.testDatabase(mydbEnv);
		
		//简单的CRUD
		je.testCRUD(mytestdb);

		//不同的数据写入方式
		je.testUpdateMore(mytestdb);
		
		//不同的数据读取方式
		je.testGetMore(mytestdb);
		
		//对象的读写
		je.testObject(mytestdb);
		
		//直接持久层应用（DPL：Direct Persistence Layer）
		je.testDPL(mytestdb);
		
		//事务处理
		je.testTransaction(mytestdb);
		
		//游标操作
		je.testCursor(mytestdb);

		//关闭database
		if (mytestdb != null) {
			mytestdb.close();
			mytestdb = null;
		}
		
		//关闭database环境
		if (mydbEnv != null) {
			mydbEnv.sync();//把数据同步到磁盘中去
			mydbEnv.cleanLog();//清理日志
			mydbEnv.close();
			mydbEnv = null;
		}
	}
	
	public Environment testEnvironment(String envHome) throws Exception {
		EnvironmentConfig envCfg = new EnvironmentConfig();
		//当数据库环境不存在的时候，创建一个数据库环境，默认为false.
		envCfg.setAllowCreate(true);
		//以只读方式打开，默认为false.
		envCfg.setReadOnly(false);
		//事务支持，默认为false.
		envCfg.setTransactional(true);

		Environment mydbEnv = new Environment(new File(envHome), envCfg);
		
		System.out.println("Env Config: " + mydbEnv.getConfig());
		
		return mydbEnv;
	}
	
	public Database testDatabase(Environment mydbEnv) throws Exception {
		DatabaseConfig dbCfg = new DatabaseConfig();
		dbCfg.setAllowCreate(true);
		dbCfg.setReadOnly(false);
		dbCfg.setTransactional(true);
		
		Database mytestdb = mydbEnv.openDatabase(null, "VendorDB", dbCfg);
			
		System.out.println("Database Name: " + mytestdb.getDatabaseName());
		
		return mytestdb;
	}
		
	public void testCRUD(Database mytestdb) throws Exception {
		//C
		String key = "key-rensanning";
		String value = "This is a test!(000)";
		
		DatabaseEntry keyEntry = new DatabaseEntry(key.getBytes("utf-8"));
//		DatabaseEntry valEntry = new DatabaseEntry(value.getBytes("utf-8"));
		OperationStatus status =null; 
//		status= mytestdb.put(null, keyEntry, valEntry);
//		System.out.println("Put Status: " + status);
		
		//R
		DatabaseEntry valGet = new DatabaseEntry();
		status = mytestdb.get(null, keyEntry, valGet, LockMode.DEFAULT);
		if (status == OperationStatus.SUCCESS) {
			value = new String(valGet.getData(), "utf-8");
			System.out.println("Read Value:" + value);
		}
		
		//U
		value = "This is a test!(111)";
		status = mytestdb.put(null, keyEntry, new DatabaseEntry(value.getBytes("utf-8")));
		
		status = mytestdb.get(null, keyEntry, valGet, LockMode.DEFAULT);
		if (status == OperationStatus.SUCCESS) {
			value = new String(valGet.getData(), "utf-8");
			System.out.println("Update Value:" + value);
		}
		
		//D
		status = mytestdb.delete(null, keyEntry);
		System.out.println("Delete Status: " + status);
	}
		
	public void testUpdateMore(Database mytestdb) throws Exception {
		String key = "key-rensanning";
		DatabaseEntry keyEntry = new DatabaseEntry(key.getBytes("utf-8"));
		DatabaseEntry valGet = new DatabaseEntry();
		
		String value = "This is a test!(by 'put' method)";
		//Database.put()： 向数据库写入数据，如果不支持重复记录，则会覆盖更新key对应的已有记录
		OperationStatus status = mytestdb.put(null, keyEntry, new DatabaseEntry(value.getBytes("utf-8")));
		
		status = mytestdb.get(null, keyEntry, valGet, LockMode.DEFAULT);
		if (status == OperationStatus.SUCCESS) {
			value = new String(valGet.getData(), "utf-8");
			System.out.println("Put Value:" + value);
		}
		
		//Database.putNoOverwrite():向数据库写入数据，但是如果key已经存在，不会覆盖已有数据（即使数据库支持重复key）
		value = "This is a test!(by 'putNoOverwrite' method)";
		status = mytestdb.putNoOverwrite(null, keyEntry, new DatabaseEntry(value.getBytes("utf-8")));
		
		status = mytestdb.get(null, keyEntry, valGet, LockMode.DEFAULT);
		if (status == OperationStatus.SUCCESS) {
			value = new String(valGet.getData(), "utf-8");
			System.out.println("PutNoOverwrite Value:" + value);
		}

		//*****设置一个key是否允许存储多个值*****
		DatabaseConfig dbCfg = new DatabaseConfig();
		dbCfg.setAllowCreate(true);
		dbCfg.setReadOnly(false);
		dbCfg.setTransactional(true);
		dbCfg.setSortedDuplicates(true);
		
		Database mytestdb2 = mytestdb.getEnvironment().openDatabase(null, "DuplicatesDB", dbCfg);
		
		//Database.putNoDupData():向数据库写入数据（该方法仅用于支持重复key的数据库），如果key和value对应的记录已经存在，那么操作结果是：OperationStatus.KEYEXIST
		value = "This is a test!(by 'putNoDupData' method)";
		status = mytestdb2.putNoDupData(null, keyEntry, new DatabaseEntry(value.getBytes("utf-8")));
		if (status == OperationStatus.SUCCESS) {
			value = new String(valGet.getData(), "utf-8");
			System.out.println("PutNoOverwrite Value:" + value);
		} else if (status == OperationStatus.KEYEXIST) {
			System.out.println("putNoDupData KEYEXIST:" + key);
		}
		
		status = mytestdb2.get(null, keyEntry, valGet, LockMode.DEFAULT);
		if (status == OperationStatus.SUCCESS) {
			value = new String(valGet.getData(), "utf-8");
			System.out.println("putNoDupData Value:" + value);
		}
		
		if (null != mytestdb2) {
			mytestdb2.close();
			mytestdb2 = null;
		}
	}
	
	public void testGetMore(Database mytestdb) throws Exception {
		String key = "key-rensanning";
		String value = "This is a test!(000)";
		DatabaseEntry keyEntry = new DatabaseEntry(key.getBytes("utf-8"));
		DatabaseEntry valEntry = new DatabaseEntry(value.getBytes("utf-8"));
		OperationStatus status = mytestdb.put(null, keyEntry, valEntry);
		System.out.println("Put Status: " + status);
		
		DatabaseEntry valGet = new DatabaseEntry();
		
		//Database.get() ：检索key对应的记录
		status = mytestdb.get(null, keyEntry, valGet, LockMode.DEFAULT);
		if (status == OperationStatus.SUCCESS) {
			value = new String(valGet.getData(), "utf-8");
			System.out.println("Read Value(get):" + value);
		}
		
		//Database.getSearchBoth() ：根据key和value 检索数据库记录
		status = mytestdb.getSearchBoth(null, keyEntry, valGet, LockMode.DEFAULT);
		if (status == OperationStatus.SUCCESS) {
			value = new String(valGet.getData(), "utf-8");
			System.out.println("Read Value(getSearchBoth):" + value);
		}
	}	
		
	public void testObject(Database mytestdb) throws Exception {
		//C
		String key = "key-rensanning-Object";
		Person value = new Person(9527, "rensanning", true);
		
		DatabaseEntry keyEntry = new DatabaseEntry(key.getBytes("utf-8"));
		DatabaseEntry valEntry = new DatabaseEntry();
		PersonTupleBinding personBinding = new PersonTupleBinding();
		personBinding.objectToEntry(value, valEntry);
		
		OperationStatus status = mytestdb.put(null, keyEntry, valEntry);
		System.out.println("Put Person Status: " + status);
		
		//R
		DatabaseEntry valGet = new DatabaseEntry();
		status = mytestdb.get(null, keyEntry, valGet, LockMode.DEFAULT);
		if (status == OperationStatus.SUCCESS) {
			value = personBinding.entryToObject(valGet);
			System.out.println("Read Person Value:" + value.getId() + "\t" + value.getName() + "\t" + value.isSex());
		}
	}

	public void testDPL(Database mytestdb) throws Exception {
		
		Environment env = mytestdb.getEnvironment();
		
		StoreConfig storeConfig = new StoreConfig();
		storeConfig.setAllowCreate(true);
		storeConfig.setTransactional(true);
		
		EntityStore store = new EntityStore(env, "StoreDB", storeConfig);
		
		PrimaryIndex<String, UserInfo> pIndex = store.getPrimaryIndex(String.class, UserInfo.class);
		
		//C
		pIndex.put(new UserInfo("001", "user001"));
		pIndex.put(new UserInfo("002", "user002"));
		pIndex.put(new UserInfo("003", "user003"));
		pIndex.put(new UserInfo("004", "user004"));
		pIndex.put(new UserInfo("005", "user005"));

		//R
		String myKey = "001";
		UserInfo getData = pIndex.get(myKey);
		System.out.println("Read User 001:" + getData);
		
		//U
		pIndex.put(new UserInfo("002", "user002222"));
		
		//Read ALL
		EntityCursor<UserInfo> cursor = pIndex.entities();
		try {
			Iterator<UserInfo> i = cursor.iterator();
			while (i.hasNext()) {
				System.out.println("Cursor data:" + i.next());
			}
		} finally {
			cursor.close();
		}
		
		//D
		String pkey = "003";
		boolean flag = pIndex.delete(pkey);
		System.out.println("delete object :" + pkey + " result:" + flag);

		//关闭store
		if (store != null) {
			store.close();
			store = null;
		}
	}
	
	public void testTransaction(Database mytestdb) throws Exception {
		Transaction txn = mytestdb.getEnvironment().beginTransaction(null, null);
		try {
			for(int i = 0; i < 5; i++) {
				mytestdb.put(txn, new DatabaseEntry(("TXN-KEY"+(i+1)).getBytes("utf-8")), new DatabaseEntry(("TXN-VALUE"+(i+1)).getBytes("utf-8")));
			}
		} catch (DatabaseException e) {
			if (txn != null) {
				txn.abort();
				txn = null;
			}
			throw e;
		} finally {
			if (txn != null) {
				txn.commit();
			}
		}
	}
	
	public void testCursor(Database mytestdb) throws Exception {
		//C
		for(int i = 0; i < 5; i++) {
			mytestdb.put(null, new DatabaseEntry(("KEY"+(i+1)).getBytes("utf-8")), new DatabaseEntry(("VALUE"+(i+1)).getBytes("utf-8")));
		}

		DatabaseEntry key = new DatabaseEntry();
		DatabaseEntry value = new DatabaseEntry();
		
		//D (by Cursor)
		Transaction txn = mytestdb.getEnvironment().beginTransaction(null, null);
		Cursor cursor1 = mytestdb.openCursor(txn, null);
		OperationStatus result1 = cursor1.getFirst(key, value, null);
		while (result1 == OperationStatus.SUCCESS) {
			if ("VALUE3".equals(new String(value.getData(), "utf-8"))) {
				cursor1.delete();
			}		
			result1 = cursor1.getNext(key, value, null);
		}
		
		if (cursor1 != null) {
			cursor1.close();
		}
		if (txn != null) {
			txn.commit();
		}
		
		//R (by Cursor)
		Cursor cursor2 = mytestdb.openCursor(null, null);
		OperationStatus result2 = cursor2.getFirst(key, value, null);

		while (result2 == OperationStatus.SUCCESS) {
			System.out.println("Cursor Read Value:" + new String(value.getData(), "utf-8"));			
			result2 = cursor2.getNext(key, value, null);
		}
		
		if (cursor2 != null) {
			cursor2.close();
		}
	}
	
}

class Person {
	int id;
	String name;
	boolean sex;

	public Person() {
	}
	
	public Person(int id, String name, boolean sex) {
		this.id = id;
		this.name = name;
		this.sex = sex;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isSex() {
		return sex;
	}
	public void setSex(boolean sex) {
		this.sex = sex;
	}
}

class PersonTupleBinding extends TupleBinding<Person> {

	@Override
	public Person entryToObject(TupleInput input) {
		Person p = new Person();
		p.setId(input.readInt());
		p.setName(input.readString());
		p.setSex(input.readBoolean());
		return p;
	}

	@Override
	public void objectToEntry(Person p, TupleOutput output) {		
		output.writeInt(p.getId());
		output.writeString(p.getName());
		output.writeBoolean(p.isSex());
	}
}

@Entity
@SuppressWarnings("serial")
class UserInfo implements Serializable {
	
	@PrimaryKey
	private String userId;
	private String userName;

	public UserInfo() {
	}

	public UserInfo(String userId, String userName) {
		this.userId = userId;
		this.userName = userName;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "UserInfo [userId=" + userId + ", userName=" + userName + "]";
	}
}
