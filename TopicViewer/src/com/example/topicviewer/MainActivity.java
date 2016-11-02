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
        
        //�������ݿ�
        //String dbPath = "/sdcard/aliuhongru/topicViewer/test.db";
        //mSQLiteDatabase = this.openOrCreateDatabase("TopicViewer.db", MODE_PRIVATE, null);
        m_MyDataBaseAdapter = new DBadapter(this); 
        
        
        //��UI
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
		//��adapter��ӵ�m_Spinner��
		m_Spinner.setAdapter(adapter);
		//���Spinner�¼�����
		m_Spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				//m_TextView1.setText("���Ѫ���ǣ�" + m_Countries[arg2]);
				//������ʾ��ǰѡ�����
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
				//����ť�¼�
			}
		});
        
        m_WrongTopicCheckBox.setOnClickListener(new CheckBox.OnClickListener(){
			public void onClick(View v)
			{
				//��CheckBox��״̬�������ݿ� 
				SetIsWrongTopic(m_WrongTopicCheckBox.isChecked());
			}
        });
        
        m_ButtonShowAnswerPic.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v)
			{
				//����ť�¼�
				ShowAnswerImage();
			}
		});
        
        m_ButtonChangeDataBase.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v)
			{
				//����ť�¼�
				//�޸����ݿ�����
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
				//����ť�¼�
				//ֱ�Ӳ���Ŀ����Ŀ
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
				//����ť�¼�
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
				//����ť�¼�
				//���²���¼������ݣ���¼�����ݿ���
				if(myCur != null)
				{	//TODO
					int questionIDColumn = myCur.getColumnIndex(DBadapter.KEY_ID);
					int questionID = myCur.getInt(questionIDColumn);
					m_MyDataBaseAdapter.updateData(questionID, m_EditText.getText().toString());
					//�ر�myCurָ�룬���»�ã��ٴζ�λ����ǰλ��
					UpdateCursorAfterSet();
					
					//�����²�����״̬��ʾ
					UpdateCommentState();
				}
			}
		});

        m_ButtonTHV.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v)
			{
				//����ť�¼�
		        //��������intent
				
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
				//����ť�¼�
		        //��������intent
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
				//����ť�¼�
		        //��������intent
				
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
				//����ť�¼�
		        //��������intent
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
        
		//���ð�ť���¼�����
        m_ButtonShowTopic.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v)
			{
				//����ť�¼�
				//��ѯ���ݿ���Ŀ
		        //��ѯ���ݿ� 
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
				//����ť�¼�
				ShowAnswerInfo();
			}
		});

        m_ButtonLastTopic.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v)
			{
				//����ť�¼�
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
				//����ť�¼� TODO
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
		
		//�������ݿ⣬ʹ���ݿ�������ʾ TODO
		//�ر�myCurָ�룬���»�ã��ٴζ�λ����ǰλ��
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
			m_TextViewAnswerImageState.setText("��");
		}
		else {
			m_TextViewAnswerImageState.setText("��");
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
		
		//����Ƿ����²�
		UpdateCommentState();
    	/*int commentColumn = myCur.getColumnIndex(DBadapter.KEY_COMMENT);
    	String commentText = myCur.getString(commentColumn);
    	if((commentText != null)&&(commentText.length() > 0))
    	{
    		m_TextViewCommentState.setText("��");
    	}
    	else {
    		m_TextViewCommentState.setText("��");
		}*/
    	
    	}
    }
    
    private void UpdateCommentState() {
    	int commentColumn = myCur.getColumnIndex(DBadapter.KEY_COMMENT);
    	String commentText = myCur.getString(commentColumn);
    	if((commentText != null)&&(commentText.length() > 0))
    	{
    		m_TextViewCommentState.setText("��");
    	}
    	else {
    		m_TextViewCommentState.setText("��");
		}
	}
    
    private void ShowAnswerInfo()
    {
		//��ʾ��ǰ��Ŀ�Ĵ�����
    	if(myCur != null)
    	{
		int answerTextColumn = myCur.getColumnIndex(DBadapter.KEY_ANSWERTEXT);
    	String answerText = myCur.getString(answerTextColumn);
    	m_TextViewShowAnswer.setText(answerText);
    	}
    }
    
    private void ShowAnswerImage() {
		//��ʾ��ǰ��Ŀ�Ĵ�ͼƬ
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
			//��ImageView��ͼƬ��Դ
			Bitmap mediaContentBitMap = BitmapFactory.decodeByteArray(mediaContent, 0, mediaContent.length);
			imageMedia.setImageBitmap(mediaContentBitMap);
			mAttacher = new PhotoViewAttacher(imageMedia);
			
			if(1 == imageType)
			{
				m_TopicBitmap = mediaContentBitMap;
				//����ĿͼƬ��Ϊ��ʱ�ļ�
				
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
				//����ͼƬ��Ϊ��ʱ�ļ�
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
			//����Ϊ�޴�ͼƬ
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
