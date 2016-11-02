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
	// ���ݿ�����Ϊdata
	//private static final String	DB_NAME			= "TopicViewer.db";
	private static String	DB_NAME			= Environment.getExternalStorageDirectory().toString() + "/aliuhongru/topicViewer/test.db";

	// ���ݿ�汾
	private static final int	DB_VERSION		= 1;
	
	// ���ݿ����
	private static final String	DB_TABLE		= "Questions";
	
	// ����һ�����ݵ�����
	public static final String	KEY_ID		= "QuestionID";	
	
	// ����һ�����ݵ�����
	public static final String	KEY_QUESTIONTEXT		= "QuestionText";	
	// ����һ�����ݵ�id
	public static final String	KEY_QUESTIONINFO		= "QuestionInfo";
	// ����һ�����ݵ�id
	public static final String	KEY_MEDIACONTENT		= "MediaContent";
	// ����һ�����ݵ�id
	public static final String	KEY_MEDIA_WIDTH		= "Media_width";
	// ����һ�����ݵ�id
	public static final String	KEY_MEIDA_HEIGHT		= "Media_height";
	// ����һ�����ݵ�id
	public static final String	KEY_ANSWERTEXT		= "AnswerText";
	// ����һ�����ݵ�id
	public static final String	KEY_ANSWERINFO		= "AnswerInfo";
	// ����һ�����ݵ�id
	public static final String	KEY_ANSWERMEDIACONTENT		= "AnswerMediaContent";
	// ����һ�����ݵ�id
	public static final String	KEY_ANSWERMEDIA_WIDTH		= "AnswerMedia_width";
	// ����һ�����ݵ�id
	public static final String	KEY_ANSWERMEDIA_HEIGHT		= "AnswerMedia_height";
	// ����һ�����ݵ�id
	public static final String	KEY_COMMENT		= "Comment";
	public static final String KEY_ISWRONG = "IsWrong";
	
	// ����Context����
	private Context				mContext		= null;
	
	//����һ����
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


	// ִ��open���������ݿ�ʱ�����淵�ص����ݿ����
	private SQLiteDatabase		mSQLiteDatabase	= null;

	// ��SQLiteOpenHelper�̳й���
	private DatabaseHelper		mDatabaseHelper	= null;
	
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		/* ���캯��-����һ�����ݿ� */
		DatabaseHelper(Context context)
		{
			//������getWritableDatabase() 
			//�� getReadableDatabase()����ʱ
			//�򴴽�һ�����ݿ�
			super(context, DB_NAME, null, DB_VERSION);
			
			
		}

		/* ����һ���� */
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			// ���ݿ�û�б�ʱ����һ��
			//db.execSQL(DB_CREATE);
		}

		/* �������ݿ� */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			db.execSQL("DROP TABLE IF EXISTS notes");
			onCreate(db);
		}
	}

	/* ���캯��-ȡ��Context */
	public DBadapter(Context context)
	{
		mContext = context;
	}
	
	//���ݿ��������޸�
	public void changeDBName(String newDBName) {
		DB_NAME = Environment.getExternalStorageDirectory().toString() + "/aliuhongru/topicViewer/" + newDBName + ".db";
	}
	
	/* ��ѯָ������ */
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
	
	/* ͨ��Cursor��ѯ�������� */
	public Cursor fetchAllData()
	{
		return mSQLiteDatabase.query(DB_TABLE, new String[] { KEY_ID, KEY_QUESTIONTEXT, KEY_QUESTIONINFO
				, KEY_MEDIACONTENT, KEY_MEDIA_WIDTH, KEY_MEIDA_HEIGHT
				, KEY_ANSWERTEXT, KEY_ANSWERINFO, KEY_ANSWERMEDIACONTENT
				, KEY_ANSWERMEDIA_WIDTH, KEY_ANSWERMEDIA_HEIGHT, KEY_COMMENT, KEY_ISWRONG }, null, null, null, null, null);
	}
	// �����ݿ⣬�������ݿ����
	public void open() throws SQLException
	{
		mDatabaseHelper = new DatabaseHelper(mContext);
		mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
	}
	
	/* ����һ������ */
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
