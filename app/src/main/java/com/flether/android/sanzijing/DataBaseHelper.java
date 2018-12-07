package com.flether.android.sanzijing;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;


public class DataBaseHelper extends SQLiteOpenHelper{
	 
    //The Android's default system path of your application database.
    private static final String DB_PATH = "/data/data/com.flether.android.sanzijing/databases/";
    private static final String DB_NAME = "sanzijing.db";
    private static final String DB_TABLE = "sanzijing";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_PINYIN = "pinYin";
    public static final String KEY_YUANWEN = "yuanWen";    
    public static final String KEY_ZHUSHI = "zhuShi";
    public static final String KEY_YIWEN = "yiWen";
    public static final String KEY_QISHI = "qiShi";
    public static final String KEY_ISFAVOR = "isFavor";
    
   
    private final Context myContext;
    private static SQLiteDatabase myDataBase; 
 
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) { 
    	super(context, DB_NAME, null, 1);
        this.myContext = context;
    }	
 
  
    @Override
	public void onCreate(SQLiteDatabase db) {
 
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
	}
    
    
    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{ 
    	boolean dbExist = checkDataBase();
 
    	if(dbExist){
    		//do nothing - database already exist
    	}else{ 
    		//By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
        	this.getReadableDatabase();
 
        	try { 
    			copyDataBase(); 
    		} catch (IOException e) { 
        		throw new Error("Error copying database"); 
        	}
    	} 
    }
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){ 
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY); 
    	}catch(SQLiteException e){ 
    		//database does't exist yet. 
    	}
 
    	if(checkDB != null){ 
    		checkDB.close(); 
    	}
 
    	return checkDB != null ? true : false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{ 
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DB_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
  
    public void openDataBase() throws SQLException{  
    	try {    		 
			createDataBase(); 
		} catch (IOException ioe) { 
			throw new Error("Unable to create database"); 
		}
    	
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
 
    }
 
    @Override
	public synchronized void close() { 
    	    if(myDataBase != null)
    		    myDataBase.close();
 
    	    super.close();
 
	}  
    
    /**
     * Return a Cursor over the list of all data in the database
     * 
     */
    public Cursor fetchAllData() {

        return myDataBase.query(DB_TABLE, new String[] {KEY_ROWID, KEY_YUANWEN, KEY_PINYIN, KEY_ZHUSHI,
        		KEY_YIWEN, KEY_QISHI, KEY_ISFAVOR}, null, null, null, null, null);
    }
	 
    
    /**
     * Return a Cursor over the list of all favorite data in the database
     * 
     */
    public Cursor fetchSearchData(String serachKey) {

    	Cursor mCursor =
    		myDataBase.query(DB_TABLE, new String[] {KEY_ROWID, KEY_YUANWEN, KEY_PINYIN, KEY_ZHUSHI,
            		KEY_YIWEN, KEY_QISHI, KEY_ISFAVOR},  KEY_YUANWEN + " like '%" + serachKey + "%'", null, null, null, null);
    		if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    /**
     * Return a Cursor over the list of all favorite data in the database
     * 
     */
    public Cursor fetchFavorData() {

    	Cursor mCursor =
    		myDataBase.query(DB_TABLE, new String[] {KEY_ROWID, KEY_YUANWEN, KEY_PINYIN, KEY_ZHUSHI,
            		KEY_YIWEN, KEY_QISHI, KEY_ISFAVOR},  KEY_ISFAVOR, null, null, null, null);
    		if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    /**
     * Return a Cursor positioned at the note that matches the given rowId
     */
    public Cursor fetchData(long rowId) throws SQLException {

        Cursor mCursor =
        	myDataBase.query(true, DB_TABLE, new String[] {KEY_ROWID, KEY_YUANWEN, KEY_PINYIN, KEY_ZHUSHI,
            		KEY_YIWEN, KEY_QISHI, KEY_ISFAVOR}, KEY_ROWID + "=" + rowId, null,
                    null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    /**
     * Update the note using the details provided. The note to be updated is
     * specified using the rowId, and it is altered to use the title and body
     * values passed 
     */
    public boolean updateData(long rowId, boolean favor) {
        ContentValues args = new ContentValues();
        args.put(KEY_ISFAVOR, favor);
        return myDataBase.update(DB_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }    
  
}