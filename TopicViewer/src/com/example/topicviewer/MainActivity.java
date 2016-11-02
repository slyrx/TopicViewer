package com.example.topicviewer;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import uk.co.senab.photoview.PhotoViewAttacher;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract.Directory;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;



public class MainActivity extends Activity implements OnTouchListener {

	DBadapter m_MyDataBaseAdapter;
	Button m_ButtonShowTopic, m_ButtonShowAnswer, m_ButtonLastTopic, m_ButtonNextTopic, m_ButtonTemp, m_ButtonBiggerTipic, m_ButtonBiggerAnswer,
	m_ButtonCommnentUpdate, m_ButtonShowComment, m_ButtonMoveToPosition, m_ButtonChangeDataBase,m_ButtonShowAnswerPic,
	m_ButtonTHV, m_ButtonAHV;
	TextView m_TextViewShowTopic, m_TextViewShowAnswer, m_TextViewTopicNum, m_TextViewCommentState, m_TextViewAnswerImageState, m_TextViewTodayTopicNum;
	ImageView m_ImageViewShowTopicMedia, m_ImageViewShowAnswerMedia;
	Cursor myCur;
	PhotoViewAttacher mAttacher;
	RelativeLayout m_Layout1;
	EditText m_EditText , m_EditText2, m_EditText3;
	CheckBox m_WrongTopicCheckBox;
    final int RIGHT = 0;  
    final int LEFT = 1;  
    private float m_LastMotionX;
    private GestureDetector gestureDetector;
    private Bitmap m_TopicBitmap, m_AnswerBitmap;
    private int m_CurPosition = -1;
	private static final String[]	m_Countries	= { "20", "5", "10", "30", "40"};
	private ArrayAdapter<String>	adapter;
	Spinner m_Spinner;
	private static int TODAYTOPNUM = 20;
	private String m_StartTopicNum = "1";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //访问数据库
        //String dbPath = "/sdcard/aliuhongru/topicViewer/test.db";
        //mSQLiteDatabase = this.openOrCreateDatabase("TopicViewer.db", MODE_PRIVATE, null);
        m_MyDataBaseAdapter = new DBadapter(this); 
        
        
        //绑定UI
        gestureDetector = new GestureDetector(MainActivity.this,onGestureListener);  
        m_Layout1 = (RelativeLayout) findViewById(R.id.layout1);
        m_Layout1.setOnTouchListener(this);
        m_ButtonShowTopic = (Button)findViewById(R.id.button4);
        m_ButtonShowAnswer = (Button)findViewById(R.id.button1);
        m_ButtonLastTopic = (Button)findViewById(R.id.button2);
        m_ButtonNextTopic = (Button)findViewById(R.id.button3);
        m_ButtonTemp = (Button)findViewById(R.id.button5);
        m_ButtonBiggerTipic = (Button)findViewById(R.id.button6);
        m_ButtonBiggerAnswer = (Button)findViewById(R.id.button7);
        m_ButtonCommnentUpdate = (Button)findViewById(R.id.button8);
        m_ButtonShowComment = (Button)findViewById(R.id.button9);
        m_ButtonMoveToPosition = (Button)findViewById(R.id.button10);
        m_ButtonChangeDataBase = (Button)findViewById(R.id.button11);
        m_ButtonShowAnswerPic = (Button)findViewById(R.id.button12);
        m_ButtonTHV = (Button)findViewById(R.id.button13);
        m_ButtonAHV = (Button)findViewById(R.id.button14);
        m_TextViewShowTopic = (TextView)findViewById(R.id.textView1);
        m_TextViewShowAnswer = (TextView)findViewById(R.id.textView2);
        m_TextViewTopicNum = (TextView)findViewById(R.id.textView4);
        m_TextViewCommentState = (TextView)findViewById(R.id.textView5);
        m_TextViewAnswerImageState = (TextView)findViewById(R.id.textView7);
        m_TextViewTodayTopicNum = (TextView)findViewById(R.id.textView8);
        
        m_ImageViewShowTopicMedia = (ImageView)findViewById(R.id.imageView1);
        m_ImageViewShowAnswerMedia = (ImageView)findViewById(R.id.imageView2);
        m_EditText = (EditText)findViewById(R.id.editText1);
        m_EditText2 = (EditText)findViewById(R.id.editText2);
        
        m_EditText3 = (EditText)findViewById(R.id.editText3);
        m_WrongTopicCheckBox = (CheckBox)findViewById(R.id.checkBox1);
        m_WrongTopicCheckBox.setChecked(false);
        
        m_Spinner = (Spinner)findViewById(R.id.spinner1);
        
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, m_Countries);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//将adapter添加到m_Spinner中
		m_Spinner.setAdapter(adapter);
		//添加Spinner事件监听
		m_Spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				//m_TextView1.setText("你的血型是：" + m_Countries[arg2]);
				//设置显示当前选择的项
				arg0.setVisibility(View.VISIBLE);
				
				TODAYTOPNUM = Integer.parseInt(m_Countries[arg2].toString());
				m_TextViewTodayTopicNum.setText("0/" + TODAYTOPNUM);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
				// TODO Auto-generated method stub
			}

		});
        
        m_ButtonTemp.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v)
			{
				//处理按钮事件
			}
		});
        
        m_WrongTopicCheckBox.setOnClickListener(new CheckBox.OnClickListener(){
			public void onClick(View v)
			{
				//将CheckBox的状态存入数据库 
				SetIsWrongTopic(m_WrongTopicCheckBox.isChecked());
			}
        });
        
        m_ButtonShowAnswerPic.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v)
			{
				//处理按钮事件
				ShowAnswerImage();
			}
		});
        
        m_ButtonChangeDataBase.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v)
			{
				//处理按钮事件
				//修改数据库名称
				m_MyDataBaseAdapter.changeDBName(m_EditText3.getText().toString());
				m_MyDataBaseAdapter.open();
		        myCur = 
		        m_MyDataBaseAdapter.fetchAllData();
		        
		        if(myCur.moveToFirst()){
		        	ShowTopicInfo();
		        	ShowTopicNum();
		        	GetIsWrongTopic();
		        }
			}
		});
        
        m_ButtonMoveToPosition.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v)
			{
				//处理按钮事件
				//直接查找目标题目
				if(myCur != null){
			        myCur.moveToPosition(Integer.parseInt(m_EditText2.getText().toString()) - 1);
		        	ShowTopicInfo();
		        	ShowTopicNum();
		        	m_StartTopicNum = m_EditText2.getText().toString();
				}
			}
		});
        
        m_ButtonShowComment.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v)
			{
				//处理按钮事件
				if(myCur != null)
				{
			    	int commentColumn = myCur.getColumnIndex(DBadapter.KEY_COMMENT);
			    	String commentText = myCur.getString(commentColumn);
			    	m_EditText.setText(commentText);
				}
			}
		});
        
        m_ButtonCommnentUpdate.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v)
			{
				//处理按钮事件
				//将吐槽中录入的内容，记录到数据库中
				if(myCur != null)
				{	//TODO
					int questionIDColumn = myCur.getColumnIndex(DBadapter.KEY_ID);
					int questionID = myCur.getInt(questionIDColumn);
					m_MyDataBaseAdapter.updateData(questionID, m_EditText.getText().toString());
					//关闭myCur指针，重新获得，再次定位到当前位置
					UpdateCursorAfterSet();
					
					//更新吐槽内容状态显示
					UpdateCommentState();
				}
			}
		});

        m_ButtonTHV.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v)
			{
				//处理按钮事件
		        //测试启动intent
				
				//Bitmap image= m_TopicBitmap;

				Intent intent = new Intent(MainActivity.this, ImageViewerActivity.class);
				intent.putExtra("imageType", 1);
				intent.putExtra("imageRotate", 0);
				startActivity(intent);
			}
		});
        
        m_ButtonAHV.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v)
			{
				//处理按钮事件
		        //测试启动intent
				//Bitmap image= m_AnswerBitmap;

				//Bundle extras = new Bundle();
				//extras.putParcelable("imagebitmap", image);
				Intent intent = new Intent(MainActivity.this, ImageViewerActivity.class);
				//intent.putExtras(extras);
				intent.putExtra("imageType", 2);
				intent.putExtra("imageRotate", 0);
				startActivity(intent);
			}
		});
    
        
        m_ButtonBiggerTipic.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v)
			{
				//处理按钮事件
		        //测试启动intent
				
				//Bitmap image= m_TopicBitmap;

				Intent intent = new Intent(MainActivity.this, ImageViewerActivity.class);
				intent.putExtra("imageType", 1);
				intent.putExtra("imageRotate", 3);
				startActivity(intent);
			}
		});
        
        m_ButtonBiggerAnswer.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v)
			{
				//处理按钮事件
		        //测试启动intent
				//Bitmap image= m_AnswerBitmap;

				//Bundle extras = new Bundle();
				//extras.putParcelable("imagebitmap", image);
				Intent intent = new Intent(MainActivity.this, ImageViewerActivity.class);
				//intent.putExtras(extras);
				intent.putExtra("imageType", 2);
				intent.putExtra("imageRotate", 3);
				startActivity(intent);
			}
		});
        
		//设置按钮的事件监听
        m_ButtonShowTopic.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v)
			{
				//处理按钮事件
				//查询数据库题目
		        //查询数据库 
				m_MyDataBaseAdapter.open();
		        myCur = 
		        m_MyDataBaseAdapter.fetchAllData();
		        
		        if(myCur.moveToFirst()){
		        	ShowTopicInfo();
		        	ShowTopicNum();
		        	GetIsWrongTopic();
		        }

			}
		});
        
        m_ButtonShowAnswer.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v)
			{
				//处理按钮事件
				ShowAnswerInfo();
			}
		});

        m_ButtonLastTopic.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v)
			{
				//处理按钮事件
				if(myCur != null)
				{
		        if(myCur.moveToPrevious()){
		        	int curPos = myCur.getPosition();
		        	ShowTopicInfo();
		        	ShowTopicNum();
		        	m_ImageViewShowAnswerMedia.setImageResource(R.drawable.ic_launcher);
		        	ClearAnswerInfo();
		        	GetIsWrongTopic();
		        }
				}
			}
		});
        
        m_ButtonNextTopic.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v)
			{
				//处理按钮事件 TODO
				if(myCur != null)
				{
		        if(myCur.moveToNext()){
		        	int curPos = myCur.getPosition();
		        	ShowTopicInfo();
		        	ShowTopicNum();
		        	m_ImageViewShowAnswerMedia.setImageResource(R.drawable.ic_launcher);
		        	ClearAnswerInfo();
		        	GetIsWrongTopic();
		        }
				}
			}
		});
    }
    
    private void SetIsWrongTopic(Boolean isWrongTopic)
    {
		int questionIDColumn = myCur.getColumnIndex(DBadapter.KEY_ID);
		int questionID = myCur.getInt(questionIDColumn);
		m_MyDataBaseAdapter.updateIsWrongTopicState(questionID, isWrongTopic);
		
		//更新数据库，使数据库立即显示 TODO
		//关闭myCur指针，重新获得，再次定位到当前位置
		UpdateCursorAfterSet();
    }
    
    private void UpdateCursorAfterSet() {
		m_CurPosition = myCur.getPosition();
		myCur.close();
		myCur = m_MyDataBaseAdapter.fetchAllData();
		myCur.moveToPosition(m_CurPosition);
	}
    
    private void GetIsWrongTopic()
    {
		int questionIsWrongTopicColumn = myCur.getColumnIndex(DBadapter.KEY_ISWRONG);
		Boolean questionIsWrongTopic = myCur.getInt(questionIsWrongTopicColumn) > 0;
		m_WrongTopicCheckBox.setChecked(questionIsWrongTopic);
    }
    
    private void ShowTopicNum()
    {
		if(myCur != null)
		{
    	int questionIDColumn = myCur.getColumnIndex(DBadapter.KEY_ID);
		int questionID = myCur.getInt(questionIDColumn);
    	m_TextViewTopicNum.setText(String.valueOf(questionID));
    	
    	if(null != m_StartTopicNum)
    	{
        	int diff = questionID - Integer.parseInt(m_StartTopicNum);
        	m_TextViewTodayTopicNum.setText(diff + "/" + TODAYTOPNUM);
    	}
    	

    	int questionMediaColumn = myCur.getColumnIndex(DBadapter.KEY_ANSWERMEDIACONTENT);
		byte[] questionMedia = myCur.getBlob(questionMediaColumn);
		
		if(questionMedia != null)
		{
			m_TextViewAnswerImageState.setText("有");
		}
		else {
			m_TextViewAnswerImageState.setText("无");
		}
    	
		}
    }
    
    private GestureDetector.OnGestureListener onGestureListener =   
            new GestureDetector.SimpleOnGestureListener() {  
            @Override  
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,  
                    float velocityY) {  
                float x = e2.getX() - e1.getX();  
                float y = e2.getY() - e1.getY();  
      
                if (x > 0) {  
                    doResult(RIGHT);  
                } else if (x < 0) {  
                    doResult(LEFT);  
                }  
                return true;  
            }  
        };  
    
    public void doResult(int action) {  
        	  
            switch (action) {  
            case RIGHT:  
                System.out.println("go right");  
                break;  
      
            case LEFT:  
                System.out.println("go right");  
                break;  
      
            }  
        } 
    
    private int deltaX = 0;
    private boolean xMoved = false;
	public boolean onTouch(View v, MotionEvent event) {
		//return gestureDetector.onTouchEvent(event);
		final int action = event.getAction();
		final float x = event.getX();
		final ViewConfiguration configuration = ViewConfiguration
				.get(v.getContext());
		int touchSlop = configuration.getScaledTouchSlop();
		
		switch(action){
			case MotionEvent.ACTION_DOWN:
				m_LastMotionX = x;
				break;
			case MotionEvent.ACTION_MOVE:
				deltaX = (int)(m_LastMotionX - x);
				xMoved = Math.abs(deltaX) > touchSlop;
				break;
			case MotionEvent.ACTION_UP:				
				if(xMoved && (deltaX > 0)){
			        if((myCur!=null) && (myCur.moveToPrevious())){
			        	int curPos = myCur.getPosition();
			        	ShowTopicInfo();
			        	ShowTopicNum();
			        	m_ImageViewShowAnswerMedia.setImageResource(R.drawable.ic_launcher);
			        	ClearAnswerInfo();
			        	GetIsWrongTopic();
			        }
				}else if (xMoved && (deltaX < 0))
				{
			        if((myCur!=null) && myCur.moveToNext()){
			        	int curPos = myCur.getPosition();
			        	ShowTopicInfo();
			        	ShowTopicNum();
			        	m_ImageViewShowAnswerMedia.setImageResource(R.drawable.ic_launcher);
			        	ClearAnswerInfo();
			        	GetIsWrongTopic();
			        }
				}else {
					//do nothing
				}
				break;		
		}
		
		return true;
	}
	
    private void ShowTopicInfo()
    {
    	if(myCur != null)
    	{
    	int questionTextColumn = myCur.getColumnIndex(DBadapter.KEY_QUESTIONTEXT);
    	int mediaContentColumn = myCur.getColumnIndex(DBadapter.KEY_MEDIACONTENT);
    	String questionText = myCur.getString(questionTextColumn);
    	byte[] mediaContent = myCur.getBlob(mediaContentColumn);
		m_TextViewShowTopic.setText(questionText);
		ShowImageContent(mediaContent, m_ImageViewShowTopicMedia, 1);
		
		//检查是否有吐槽
		UpdateCommentState();
    	/*int commentColumn = myCur.getColumnIndex(DBadapter.KEY_COMMENT);
    	String commentText = myCur.getString(commentColumn);
    	if((commentText != null)&&(commentText.length() > 0))
    	{
    		m_TextViewCommentState.setText("有");
    	}
    	else {
    		m_TextViewCommentState.setText("无");
		}*/
    	
    	}
    }
    
    private void UpdateCommentState() {
    	int commentColumn = myCur.getColumnIndex(DBadapter.KEY_COMMENT);
    	String commentText = myCur.getString(commentColumn);
    	if((commentText != null)&&(commentText.length() > 0))
    	{
    		m_TextViewCommentState.setText("有");
    	}
    	else {
    		m_TextViewCommentState.setText("无");
		}
	}
    
    private void ShowAnswerInfo()
    {
		//显示当前题目的答案文字
    	if(myCur != null)
    	{
		int answerTextColumn = myCur.getColumnIndex(DBadapter.KEY_ANSWERTEXT);
    	String answerText = myCur.getString(answerTextColumn);
    	m_TextViewShowAnswer.setText(answerText);
    	}
    }
    
    private void ShowAnswerImage() {
		//显示当前题目的答案图片
    	if(myCur != null)
    	{
		int answerMediaContentColumn = myCur.getColumnIndex(DBadapter.KEY_ANSWERMEDIACONTENT);
    	byte[] answerMediaContent = myCur.getBlob(answerMediaContentColumn);
    	ShowImageContent(answerMediaContent, m_ImageViewShowAnswerMedia, 2);
    	}
	}
    
    private void ClearAnswerInfo() {
    	m_TextViewShowAnswer.setText("");
    	byte[] answerMediaContent = null;
    	ShowImageContent(answerMediaContent, m_ImageViewShowAnswerMedia, 2);
	}
    
    private void ShowImageContent(byte[] mediaContent, ImageView imageMedia, int imageType )
    {
		if(mediaContent != null)
		{
			//给ImageView绑定图片资源
			Bitmap mediaContentBitMap = BitmapFactory.decodeByteArray(mediaContent, 0, mediaContent.length);
			imageMedia.setImageBitmap(mediaContentBitMap);
			mAttacher = new PhotoViewAttacher(imageMedia);
			
			if(1 == imageType)
			{
				m_TopicBitmap = mediaContentBitMap;
				//将题目图片存为临时文件
				
				try {
					
					FileOutputStream fos = openFileOutput("tempTopic.tmp", Context.MODE_PRIVATE);
					try {
						fos.write(mediaContent);
						fos.flush();
						fos.close();
					} catch (IOException e) {
						// Auto-generated catch block
						e.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					// Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if(2 == imageType)
			{
				m_AnswerBitmap = mediaContentBitMap;
				//将答案图片存为临时文件
				String answerFileName = "tempAnswer.tmp";
				try {
					FileOutputStream fos = openFileOutput("tempAnswer.tmp", Context.MODE_PRIVATE);
					try {
						fos.write(mediaContent);
						fos.flush();
						fos.close();
					} catch (IOException e) {
						// Auto-generated catch block
						e.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					// Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				//do nothing
			}
		}
		else {
			//设置为无答案图片
			imageMedia.setImageResource(R.drawable.ic_launcher);
		}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
