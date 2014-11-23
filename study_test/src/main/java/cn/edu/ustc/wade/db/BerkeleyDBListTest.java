/*
 * More about Oracle Berkeley DB at
 * http://www.oracle.com/technology/software/products/berkeley-db/htdocs/jeoslicense.html
 */

package cn.edu.ustc.wade.db;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import java.io.File;
import java.util.ArrayList;

/**
 * @author tpumapa3m
 */
public class BerkeleyDBListTest {

     public static void main(String[] args) {
        //system variables
        Environment myDbEnv = null;
        Database myDatabase = null;
        Database mySerDatabase = null;

        //starting
        try {
            //instantiating an EnvironmentConfig
            EnvironmentConfig envConfig = new EnvironmentConfig();
            envConfig.setAllowCreate(true);
            //creating an Environment
            myDbEnv = new Environment(new File("c:/berkeley_db/"),
                                      envConfig);
            //instantiating DataBaseConfig for sampleDatabase
            DatabaseConfig dbConfig = new DatabaseConfig();
            dbConfig.setAllowCreate(true);
            //option for writing at last moment, for reducing disk I/O
            //dbConfig.setDeferredWrite(true);
            //option for creating in-cache database, the fastest type
            //dbConfig.setTemporary(true);
            //opening Database
            myDatabase = myDbEnv.openDatabase(null,
                                              "sampleDatabase",
                                              dbConfig);
            //instantiating DataBaseConfig for serializeInfoDatabase
            dbConfig = new DatabaseConfig();
            dbConfig.setAllowCreate(true);
            mySerDatabase = myDbEnv.openDatabase(null,
                                                 "serializeInfoDatabase",
                                                 dbConfig);
        } catch (DatabaseException dbe) {
            System.err.println(dbe.toString());
        }

        //Entries
        DatabaseEntry theKey = null;
        DatabaseEntry theData = null;

        //***catalog for class-data
        StoredClassCatalog classCatalog = new StoredClassCatalog(mySerDatabase);

        //***Bindings
        EntryBinding myBinding = new SerialBinding(classCatalog, ArrayList.class);

        //data
        String aKey = "myList";
        ArrayList aList = new ArrayList();
        aList.add(1);
        aList.add(2);
        aList.add(3);

        //writing
        try {
            theKey = new DatabaseEntry(aKey.getBytes("UTF-8"));
            theData = new DatabaseEntry();
            myBinding.objectToEntry(aList, theData);
            //Perform the put
            myDatabase.put(null, theKey, theData);
        } catch (Exception e) {
            System.err.println(e.toString());
        }

        //synchronizing cache with hdd
        myDbEnv.sync();

        //reading
        try {
            theKey = new DatabaseEntry(aKey.getBytes("UTF-8"));
            theData = new DatabaseEntry();
            // Perform the get.
            if (myDatabase.get(null, theKey, theData, LockMode.DEFAULT) ==
                OperationStatus.SUCCESS) {
                System.out.println("For key: '" + aKey + "' found data: '" +
                                    (ArrayList) myBinding.entryToObject(theData) +
                                    "'.");
            } else {
                System.out.println("No record found for key '" + aKey + "'.");
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }

        //deleting
        try {
            theKey = new DatabaseEntry(aKey.getBytes("UTF-8"));
            myDatabase.delete(null, theKey);
        } catch (Exception e) {
            System.err.println(e.toString());
        }

        //synchronizing cache with hdd
        myDbEnv.sync();

        //ending
        try {
            //closing Database
            if (myDatabase != null) {
                myDatabase.close();
            }
            //closing Database
            if (mySerDatabase != null) {
                mySerDatabase.close();
            }
            //closing Environment
            if (myDbEnv != null) {
                myDbEnv.close();
            }
        } catch (DatabaseException dbe) {
            System.err.println(dbe.toString());
        }
    }

}