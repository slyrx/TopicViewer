package com.example.topicviewer;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import uk.co.senab.photoview.PhotoViewAttacher;
import android.R.anim;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class ImageViewerActivity extends Activity {
	ImageView m_ImageViewOnlyImage;
	PhotoViewAttacher mAttacher;
	private float m_LastMotionX;
    private int deltaX = 0;
    private boolean xMoved = false;
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
        // Be sure to call the super class.
        super.onCreate(savedInstanceState);
        
        // See assets/res/any/layout/dialog_activity.xml for this
        // view layout definition, which is being set here as
        // the content of our screen.
        setContentView(R.layout.activity_imageviewer);
        m_ImageViewOnlyImage = (ImageView)findViewById(R.id.imageView1);
        
        //�ƽ�ָ�
        //m_ImageViewOnlyImage.setImageResource(R.drawable.ic_launcher);
        
        
        Bundle extras = getIntent().getExtras();
        int data = 0;
        int rotateType = 0;
        if(extras != null)
        {
        	data = extras.getInt("imageType");
        	rotateType = extras.getInt("imageRotate");
        }
        FileInputStream fis = null;
        if(1 == data)
        {
        	try {
        		String topicFileName = "tempTopic.tmp";
        		fis = openFileInput(topicFileName);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        else if(2 == data)
        {
        	try {
        		String answerFileName = "tempAnswer.tmp";
        		fis = openFileInput(answerFileName);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        else {
			//do nothing
		}
        
        
        Bitmap bmp = BitmapFactory.decodeStream(fis);

        if(rotateType == 3)
        {
            //��תͼƬ
            //��������ͼƬ���õ�matrix����
              Matrix matrix=new Matrix();
            //��תͼƬ����
              matrix.postRotate(90,50,100);//������50��100 ��ת30��
            //������ͼƬ
              Bitmap resizedBitmap=Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),bmp.getHeight(),matrix,true);
            //�����洴����bitmapת����drawable����ʹ�����ʹ����ImageView,ImageButton��
              BitmapDrawable bmd=new BitmapDrawable(resizedBitmap);
              m_ImageViewOnlyImage.setAdjustViewBounds(true);
              m_ImageViewOnlyImage.setImageDrawable(bmd);
        }
        else {
        	m_ImageViewOnlyImage.setImageBitmap(bmp );
		}
        
        mAttacher = new PhotoViewAttacher(m_ImageViewOnlyImage);
    }

}
