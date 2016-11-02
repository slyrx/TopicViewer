package com.example.topicviewer;


import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class DBadapter {
	// 数据库名称为data
	//private static final String	DB_NAME			= "TopicViewer.db";
	private static String	DB_NAME			= Environment.getExternalStorageDirectory().toString() + "/aliuhongru/topicViewer/test.db";

	// 数据库版本
	private static final int	DB_VERSION		= 1;
	
	// 数据库表名
	private static final String	DB_TABLE		= "Questions";
	
	// 表中一条数据的名称
	public static final String	KEY_ID		= "QuestionID";	
	
	// 表中一条数据的内容
	public static final String	KEY_QUESTIONTEXT		= "QuestionText";	
	// 表中一条数据的id
	public static final String	KEY_QUESTIONINFO		= "QuestionInfo";
	// 表中一条数据的id
	public static final String	KEY_MEDIACONTENT		= "MediaContent";
	// 表中一条数据的id
	public static final String	KEY_MEDIA_WIDTH		= "Media_width";
	// 表中一条数据的id
	public static final String	KEY_MEIDA_HEIGHT		= "Media_height";
	// 表中一条数据的id
	public static final String	KEY_ANSWERTEXT		= "AnswerText";
	// 表中一条数据的id
	public static final String	KEY_ANSWERINFO		= "AnswerInfo";
	// 表中一条数据的id
	public static final String	KEY_ANSWERMEDIACONTENT		= "AnswerMediaContent";
	// 表中一条数据的id
	public static final String	KEY_ANSWERMEDIA_WIDTH		= "AnswerMedia_width";
	// 表中一条数据的id
	public static final String	KEY_ANSWERMEDIA_HEIGHT		= "AnswerMedia_height";
	// 表中一条数据的id
	public static final String	KEY_COMMENT		= "Comment";
	public static final String KEY_ISWRONG = "IsWrong";
	
	// 本地Context对象
	private Context				mContext		= null;
	
	//创建一个表
	private static final String	DB_CREATE		= "CREATE TABLE " + DB_TABLE + " (" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_QUESTIONTEXT + " TEXT,"
			+ KEY_QUESTIONINFO + " BLOB,"
			+ KEY_MEDIACONTENT + " BLOB,"
			+ KEY_MEDIA_WIDTH + " INTEGER,"
			+ KEY_MEIDA_HEIGHT + " INTEGER,"
			+ KEY_ANSWERTEXT + " TEXT,"
			+ KEY_ANSWERINFO + " BLOB,"
			+ KEY_ANSWERMEDIACONTENT + " BLOB,"
			+ KEY_ANSWERMEDIA_WIDTH + " INTEGER,"
			+ KEY_ANSWERMEDIA_HEIGHT + " INTEGER,"
			+ KEY_COMMENT + " TEXT,"
			+ KEY_ISWRONG + " BOOLEAN)";


	// 执行open（）打开数据库时，保存返回的数据库对象
	private SQLiteDatabase		mSQLiteDatabase	= null;

	// 由SQLiteOpenHelper继承过来
	private DatabaseHelper		mDatabaseHelper	= null;
	
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		/* 构造函数-创建一个数据库 */
		DatabaseHelper(Context context)
		{
			//当调用getWritableDatabase() 
			//或 getReadableDatabase()方法时
			//则创建一个数据库
			super(context, DB_NAME, null, DB_VERSION);
			
			
		}

		/* 创建一个表 */
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			// 数据库没有表时创建一个
			//db.execSQL(DB_CREATE);
		}

		/* 升级数据库 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			db.execSQL("DROP TABLE IF EXISTS notes");
			onCreate(db);
		}
	}

	/* 构造函数-取得Context */
	public DBadapter(Context context)
	{
		mContext = context;
	}
	
	//数据库名称自修改
	public void changeDBName(String newDBName) {
		DB_NAME = Environment.getExternalStorageDirectory().toString() + "/aliuhongru/topicViewer/" + newDBName + ".db";
	}
	
	/* 查询指定数据 */
	public Cursor fetchData(long rowId) throws SQLException
	{

		Cursor mCursor =

		mSQLiteDatabase.query(true, DB_TABLE, new String[] { KEY_ID, KEY_QUESTIONTEXT, KEY_QUESTIONINFO
				, KEY_MEDIACONTENT, KEY_MEDIA_WIDTH, KEY_MEIDA_HEIGHT
				, KEY_ANSWERTEXT, KEY_ANSWERINFO, KEY_ANSWERMEDIACONTENT
				, KEY_ANSWERMEDIA_WIDTH, KEY_ANSWERMEDIA_HEIGHT, KEY_COMMENT, KEY_ISWRONG }, 
				KEY_ID + "=" + rowId, null, null, null, null, null);

		if (mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;

	}
	
	/* 通过Cursor查询所有数据 */
	public Cursor fetchAllData()
	{
		return mSQLiteDatabase.query(DB_TABLE, new String[] { KEY_ID, KEY_QUESTIONTEXT, KEY_QUESTIONINFO
				, KEY_MEDIACONTENT, KEY_MEDIA_WIDTH, KEY_MEIDA_HEIGHT
				, KEY_ANSWERTEXT, KEY_ANSWERINFO, KEY_ANSWERMEDIACONTENT
				, KEY_ANSWERMEDIA_WIDTH, KEY_ANSWERMEDIA_HEIGHT, KEY_COMMENT, KEY_ISWRONG }, null, null, null, null, null);
	}
	// 打开数据库，返回数据库对象
	public void open() throws SQLException
	{
		mDatabaseHelper = new DatabaseHelper(mContext);
		mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
	}
	
	/* 更新一条数据 */
	public boolean updateData(long rowId, String data)
	{
		ContentValues args = new ContentValues();
		args.put(KEY_COMMENT, data);
		int result = mSQLiteDatabase.update(DB_TABLE, args, KEY_ID + "=" + rowId, null);
		return result > 0;
	}
	
	public boolean updateIsWrongTopicState(long rowId, Boolean data)
	{
		ContentValues args = new ContentValues();
		args.put(KEY_ISWRONG, data);
		int result = mSQLiteDatabase.update(DB_TABLE, args, KEY_ID + "=" + rowId, null);
		return result > 0;
	}
	
}
