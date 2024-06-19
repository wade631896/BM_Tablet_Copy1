package com.tablet_copy1;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.*;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.TabHost.TabSpec;
import com.bm_tablet_copy1.R;
//匯入資料庫相關
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.inputmethod.InputMethodManager;

public class Page5 extends Activity implements OnClickListener,OnItemClickListener,android.content.DialogInterface.OnClickListener {
	//初始化各變數---------------------------------------------------------------
	String DataTitle=null;						//定義資訊欄標頭
	CheckBox   CB1=null;						//使用中壓力勾選
	CheckBox   CB2=null;						//未使用中壓力勾選
	CheckBox   CB3=null;						//減壓器未封鉛勾選
	CheckBox   USECB=null;						//使用中勾選
	CheckBox   CB4=null;						//其他勾選
	
	ImageView  TAB2IMG1=null;
	ImageView  TAB2IMG2=null;
	ImageView  TAB2IMG3=null;
	ImageView  TAB2USEIMG=null;
	ImageView  TAB2IMG4=null;
	ImageView  TAB3IMG1=null;
	ImageView  TAB3IMG2=null;
	ImageView  TAB3IMG3=null;
	ImageView  TAB3USEIMG=null;
	ImageView  TAB3IMG4=null;
	EditText   EDT1=null;						//使用中壓力填寫
	EditText   EDT2=null;						//未使用中壓力填寫
	EditText   TAB2EDT1=null;
	EditText   TAB2EDT2=null;
	EditText   TAB3EDT1=null;
	EditText   TAB3EDT2=null;
	ImageButton EB=null;						//結束鈕
	ImageButton PPB=null;						//上一頁鈕
	ImageButton SB=null;						//確認紐
	
	EditText TAB1=null;							//TAB1
	EditText TAB2=null;							//TAB2
	EditText TAB3=null;							//TAB3
	String Remark="";							//備註
	String Remark1="";							//前期備註
	String Remark2="";							//前前期備註
	String DataWatnum=null;						//儲存表號資料
    SoundPool sp;//声明一个SoundPool
    int MusicButton;//按鈕音
    private AudioManager am; 
    private int maxVol;  
    public String MusicVoice = "2";//音效開關   2=開  1=關
    public Boolean Find1 = false;//辨別 表號查詢
    public Boolean Find2 = false;
	CharSequence[] CustDataList=null;			//存放Grid資料
	CharSequence[] CustDataNo=null;				//存放用編資料
	CharSequence[] CustFullnm=null;				//存放用戶名稱
	String[] SendData=new String[7];			//0:全部/已抄/已抄代碼    1:抄表區代碼
												//2:預選道路代碼                    3:用編代碼
    											//4:用戶地址			   5.資料庫位置
	String Fullnm; //用戶名稱
	//----------------------------------------------------------------
    //-----------------------------------------------------------------
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page5);
        
        FindView();
        BuildListener();
        InitData();               
        GetData();
        init();

  
        EDT1.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {  
            @Override  
            public void onFocusChange(View v, boolean hasFocus) {  
            	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if(hasFocus) {
                	if(CB1.isChecked()){
        				EDT1.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);    //限制只能輸入數字及小數點
        		
        				imm.showSoftInput(EDT1, InputMethodManager.SHOW_FORCED);
        			}else{
        				EDT1.setInputType(InputType.TYPE_NULL);
        				imm.hideSoftInputFromWindow(EDT1.getWindowToken(), 0);
        			}
                	// 此处为得到焦点时的处理内容
                } else {     	
                	// 此处为失去焦点时的处理内容
                		EDT1.setInputType(InputType.TYPE_NULL);
                		imm.hideSoftInputFromWindow(EDT1.getWindowToken(), 0);		
                	}
            	}
        	});
        EDT2.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {  
            @Override  
            public void onFocusChange(View v, boolean hasFocus) {  
            	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if(hasFocus) {
                	if(CB2.isChecked()){
        				EDT2.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);    //限制只能輸入數字及小數點
        				imm.showSoftInput(EDT2, InputMethodManager.SHOW_FORCED);			
        			}else{
        				EDT2.setInputType(InputType.TYPE_NULL);
        	    		imm.hideSoftInputFromWindow(EDT2.getWindowToken(), 0);
        			}
                	// 此处为得到焦点时的处理内容
                } else {     	
                	// 此处为失去焦点时的处理内容
                	EDT2.setInputType(InputType.TYPE_NULL);
                	imm.hideSoftInputFromWindow(EDT2.getWindowToken(), 0);
                	}
            	}
        	});
        TAB1.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {  
            @Override  
            public void onFocusChange(View v, boolean hasFocus) {  
            	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                if(hasFocus) {
                	if(CB4.isChecked()){
        				TAB1.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        				TAB1.setGravity(Gravity.TOP);  
        		        //改变默认的单行模式  
        		    	TAB1.setSingleLine(false);  
        		        //水平滚动设置为False  
        		    	TAB1.setHorizontallyScrolling(false);
        				imm.showSoftInput(TAB1, InputMethodManager.SHOW_FORCED);
        			}else{
        				TAB1.setInputType(InputType.TYPE_NULL);
        	    		imm.hideSoftInputFromWindow(TAB1.getWindowToken(), 0);
        			}
                	// 此处为得到焦点时的处理内容
                } else {    	
                	// 此处为失去焦点时的处理内容
                	TAB1.setInputType(InputType.TYPE_NULL);
                	imm.hideSoftInputFromWindow(TAB1.getWindowToken(), 0);
                	}
            	}
        	});
    }
    private void init() {
        // TODO Auto-generated method stub
    	setVolumeControlStream(AudioManager.STREAM_MUSIC); 
        am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);  
        maxVol = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
       // sp= new SoundPool(10, AudioManager.STREAM_SYSTEM, 100);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);  //音量鍵調節音效音量!
        MusicButton = sp.load(this, R.raw.button, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
     
       
    
    }
    
    private void FindView(){
    	CB1=(CheckBox)findViewById(R.id.checkBox1);
    	CB2=(CheckBox)findViewById(R.id.checkBox2);
    	CB3=(CheckBox)findViewById(R.id.checkBox3);
    	USECB=(CheckBox)findViewById(R.id.usecheckBox);
    	CB4=(CheckBox)findViewById(R.id.checkBox4);
    	EDT1=(EditText)findViewById(R.id.checkedt1);
    	EDT2=(EditText)findViewById(R.id.checkedt2);
    	TAB2IMG1=(ImageView)findViewById(R.id.TAB2Image1);
    	TAB2IMG2=(ImageView)findViewById(R.id.TAB2Image2);
    	TAB2IMG3=(ImageView)findViewById(R.id.TAB2Image3);
    	TAB2USEIMG=(ImageView)findViewById(R.id.TAB2usecheckbox);
    	TAB2IMG4=(ImageView)findViewById(R.id.TAB2Image4);
    	TAB3IMG1=(ImageView)findViewById(R.id.TAB3Image1);
    	TAB3IMG2=(ImageView)findViewById(R.id.TAB3Image2);
    	TAB3IMG3=(ImageView)findViewById(R.id.TAB3Image3);
    	TAB3USEIMG=(ImageView)findViewById(R.id.TAB3usecheckbox);
    	TAB3IMG4=(ImageView)findViewById(R.id.TAB3Image4);
    	TAB2EDT1=(EditText)findViewById(R.id.TAB2checkedt1);
    	TAB2EDT2=(EditText)findViewById(R.id.TAB2checkedt2);
    	TAB3EDT1=(EditText)findViewById(R.id.TAB3checkedt1);
    	TAB3EDT2=(EditText)findViewById(R.id.TAB3checkedt2);
    	TAB1=(EditText)findViewById(R.id.tab1edt);
    	TAB2=(EditText)findViewById(R.id.tab2edt);
    	TAB3=(EditText)findViewById(R.id.tab3edt);
    	PPB=(ImageButton)findViewById(R.id.PreviousPageButton);
    	SB=(ImageButton)findViewById(R.id.SureButton);
    	TabHost tabHost = (TabHost)findViewById(R.id.tabHost);
    	tabHost.setup();
    	
    	TabSpec spec1 = tabHost.newTabSpec("tab1");
    	spec1.setContent(R.id.tab1);
    	spec1.setIndicator("今期備註",getResources().getDrawable(android.R.drawable.ic_menu_edit));
    	
    	TabSpec spec2 = tabHost.newTabSpec("tab2");
    	spec2.setContent(R.id.tab2);
    	spec2.setIndicator("前期備註",getResources().getDrawable(android.R.drawable.ic_menu_agenda));
    	
    	TabSpec spec3 = tabHost.newTabSpec("tab3");
    	spec3.setContent(R.id.tab3);
    	spec3.setIndicator("前前期備註",getResources().getDrawable(android.R.drawable.ic_menu_agenda));
    	
    	tabHost.addTab(spec1);
    	tabHost.addTab(spec2);
    	tabHost.addTab(spec3);
    	tabHost.setCurrentTab(0);
    	
        //EditText editText = new EditText(this);  
        //设置EditText的显示方式为多行文本输入  
    	TAB1.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);  
        //文本显示的位置在EditText的最上方  
    	TAB1.setGravity(Gravity.TOP);  
        //改变默认的单行模式  
    	TAB1.setSingleLine(false);  
        //水平滚动设置为False  
    	TAB1.setHorizontallyScrolling(false);  
    	
    	TAB2.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);  
        //文本显示的位置在EditText的最上方  
    	TAB2.setGravity(Gravity.TOP);  
        //改变默认的单行模式  
    	TAB2.setSingleLine(false);  
        //水平滚动设置为False  
    	TAB2.setHorizontallyScrolling(false);  
    	
    	TAB3.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);  
        //文本显示的位置在EditText的最上方  
    	TAB3.setGravity(Gravity.TOP);  
        //改变默认的单行模式  
    	TAB3.setSingleLine(false);  
        //水平滚动设置为False  
    	TAB3.setHorizontallyScrolling(false);  
    	/*
    	CB1=(CheckBox)findViewById(R.id.checkBox1);
    	CB2=(CheckBox)findViewById(R.id.checkBox2);
    	CB3=(CheckBox)findViewById(R.id.checkBox3);
    	CB4=(CheckBox)findViewById(R.id.checkBox4);
    	EDT1=(EditText)findViewById(R.id.checkedt1);
    	EDT2=(EditText)findViewById(R.id.checkedt2);
    	*/
    
    	TAB2EDT1.setEnabled(false);
    	TAB2EDT2.setEnabled(false);
    	
    	TAB3EDT1.setEnabled(false);
    	TAB3EDT2.setEnabled(false);
    	
    	TAB2.setEnabled(false);
    	TAB3.setEnabled(false);
    	EDT1.setInputType(InputType.TYPE_NULL);  
    	EDT2.setInputType(InputType.TYPE_NULL);
    	TAB1.setInputType(InputType.TYPE_NULL);
    	EDT1.setBackgroundResource(R.color.silver);
		EDT2.setBackgroundResource(R.color.silver);
		TAB1.setBackgroundResource(R.color.silver);
		
		TAB2IMG1.setImageResource(R.drawable.c2);
		TAB2IMG2.setImageResource(R.drawable.c2);
		TAB2IMG3.setImageResource(R.drawable.c2);
		TAB2USEIMG.setImageResource(R.drawable.c2);
		TAB2IMG4.setImageResource(R.drawable.c2);
		TAB3IMG1.setImageResource(R.drawable.c2);
		TAB3IMG2.setImageResource(R.drawable.c2);
		TAB3IMG3.setImageResource(R.drawable.c2);
		TAB3USEIMG.setImageResource(R.drawable.c2);
		TAB3IMG4.setImageResource(R.drawable.c2);
		/*
		if (v.equals(SB)){       //備註存檔
			if(CB1.isChecked()){
				Remark=Remark+"●(1)" + EDT1.getText().toString().trim()+"○(1)";      //字串前後增加的 ●(1)和 ○(1)是為了抓取字串。
			}else if(CB2.isChecked()){
				Remark=Remark+"●(2)" + EDT2.getText().toString().trim()+"○(2)";
			}else if(CB3.isChecked()){
				Remark=Remark+"●(3)";
			}else if(CB4.isChecked()){
				Remark=Remark+"●(4)" + TAB1.getText().toString().trim()+"○(4)";
			}
		 */
	
		
    }
    private void GetData(){
    	MyDBConnection DBConnection=null;
    	SQLiteDatabase DB=null;
    	Cursor Cursor=null;
    	
 		//---------------------------------------------------------------
 		try {			
 			//-----------------------------------------------------------------Remark變數取得備註資料
 			DBConnection=new MyDBConnection(this,SendData[5],null,1);
 			//DB=DBConnection.getWritableDatabase();	//儲存空間不足時會有錯誤
 				DB=DBConnection.getReadableDatabase();	
 				String RemarkTable = "select Remark1,Remark2,Remark from "+(String)getText(R.string.TableName2)+
 		    			" where Custno='"+SendData[3] + "' ";
 				Cursor=DB.rawQuery(RemarkTable, null);
 				while (Cursor.moveToNext()){
 					Remark1=Cursor.getString(0).trim();
 					Remark2=Cursor.getString(1).trim();
 					Remark=Cursor.getString(2).trim();
 					
 				}
 				Cursor.close();
 				DB.close();
 				DBConnection.close(); 
 			//-----------------------------------------------------------------
 			int cb1index1 = Remark.indexOf("●(1)"),cb1index2 = Remark.indexOf("○(1)");
 	 		int cb2index1 = Remark.indexOf("●(2)"),cb2index2 = Remark.indexOf("○(2)");
 	 		int cb3index1 = Remark.indexOf("●(3)");
 	 		int usecbindex1 = Remark.indexOf("●(use)");	
 	 		int cb4index1 = Remark.indexOf("●(4)"),cb4index2 = Remark.indexOf("○(4)");	
 	 		
 			int TAB2cb1index1 = Remark1.indexOf("●(1)"),TAB2cb1index2 = Remark1.indexOf("○(1)");
 			int TAB2cb2index1 = Remark1.indexOf("●(2)"),TAB2cb2index2 = Remark1.indexOf("○(2)");
 			int TAB2cb3index1 = Remark1.indexOf("●(3)");
 			int TAB2usecbindex1 = Remark1.indexOf("●(use)");
 			int TAB2cb4index1 = Remark1.indexOf("●(4)"),TAB2cb4index2 = Remark1.indexOf("○(4)");
 			int TAB3cb1index1 = Remark2.indexOf("●(1)"),TAB3cb1index2 = Remark2.indexOf("○(1)");
 			int TAB3cb2index1 = Remark2.indexOf("●(2)"),TAB3cb2index2 = Remark2.indexOf("○(2)");
 			int TAB3cb3index1 = Remark2.indexOf("●(3)");
 			int TAB3usecbindex1 = Remark2.indexOf("●(use)");
 			int TAB3cb4index1 = Remark2.indexOf("●(4)"),TAB3cb4index2 = Remark2.indexOf("○(4)");
 			if(!"".equals(Remark)){
 				if(cb1index1 >= 0){
 					EDT1.setText(Remark.substring(cb1index1+4, cb1index2));
 					EDT1.setBackgroundResource(R.color.s);
 					CB1.setChecked(true);
 					//TAB2IMG1.setImageResource(R.drawable.c1);
 				}
 				if(cb2index1 >= 0){
 					EDT2.setText(Remark.substring(cb2index1+4, cb2index2));
 					EDT2.setBackgroundResource(R.color.s);
 					CB2.setChecked(true);
 					//TAB2IMG2.setImageResource(R.drawable.c1);
 				}
 				if(cb3index1 >= 0){
 					CB3.setChecked(true);
 					//TAB2IMG3.setImageResource(R.drawable.c1);
 				}
 				if(usecbindex1 >= 0){
 					USECB.setChecked(true);
 					
 				}
 				if(cb4index1 >= 0){
 					TAB1.setText(Remark.substring(cb4index1+4, cb4index2));
 					TAB1.setBackgroundResource(R.color.s);
 					CB4.setChecked(true);
 					//TAB2IMG4.setImageResource(R.drawable.c1);
 				}
 			}
 			if(!"".equals(Remark1)){
 				if(TAB2cb1index1 >= 0){ 
 					TAB2EDT1.setText(Remark1.substring(TAB2cb1index1+4, TAB2cb1index2));
 					TAB2IMG1.setImageResource(R.drawable.c1);
 				}
 				if(TAB2cb2index1 >= 0){
 					TAB2EDT2.setText(Remark1.substring(TAB2cb2index1+4, TAB2cb2index2));
 					TAB2IMG2.setImageResource(R.drawable.c1);
 				}
 				if(TAB2cb3index1 >= 0){
 					TAB2IMG3.setImageResource(R.drawable.c1);
 				}
 				if(TAB2usecbindex1 >= 0){
 					TAB2USEIMG.setImageResource(R.drawable.c1);
 				}
 				if(TAB2cb4index1 >= 0){
 					TAB2.setText(Remark1.substring(TAB2cb4index1+4, TAB2cb4index2));
 					TAB2IMG4.setImageResource(R.drawable.c1);
 				}
 			}
 			if(!"".equals(Remark2)){
 				if(TAB3cb1index1 >= 0){  
 					TAB3EDT1.setText(Remark2.substring(TAB3cb1index1+4, TAB3cb1index2));
 					TAB3IMG1.setImageResource(R.drawable.c1);
 				}
 				if(TAB3cb2index1 >= 0){
 					TAB3EDT2.setText(Remark2.substring(TAB3cb2index1+4, TAB3cb2index2));
 					TAB3IMG2.setImageResource(R.drawable.c1);
 				}
 				if(TAB3cb3index1 >= 0){
 					TAB3IMG3.setImageResource(R.drawable.c1);
 				}
 				if(TAB3usecbindex1 >= 0){
 					TAB3USEIMG.setImageResource(R.drawable.c1);
 				}
 				if(TAB3cb4index1 >= 0){
 					TAB3.setText(Remark2.substring(TAB3cb4index1+4, TAB3cb4index2));
 					TAB3IMG4.setImageResource(R.drawable.c1);
 				}
 			}
 			
		} catch (SQLException e){
			Cursor.close();
			DB.close();
			DBConnection.close();
			Toast.makeText(Page5.this,e.getMessage(),Toast.LENGTH_LONG).show();
		} 
    }
    private void BuildListener(){
    	PPB.setOnClickListener(this);
    	SB.setOnClickListener(this);
    	CB1.setOnClickListener(this);
    	CB2.setOnClickListener(this);
    	CB3.setOnClickListener(this);
    	USECB.setOnClickListener(this);
    	CB4.setOnClickListener(this);
    	EDT1.setOnClickListener(this);
    	EDT2.setOnClickListener(this);
    	TAB1.setOnClickListener(this);
	
     }
    
    private void InitData(){
  		//初始化資料傳送
		Intent GetIntent = getIntent();
		Bundle GetBundle = GetIntent.getExtras();
		SendData[0] = GetBundle.getString("D1");
		SendData[1] = GetBundle.getString("D2");
		SendData[2] = GetBundle.getString("D3");
		SendData[3] = GetBundle.getString("D4");
		//SendData[3] = "";
		SendData[4] = GetBundle.getString("D5");
		SendData[5] = GetBundle.getString("D6");
		SendData[6] = GetBundle.getString("D7");
		Fullnm = GetBundle.getString("D8");
		DataWatnum = GetBundle.getString("D9");
		MusicVoice = GetBundle.getString("M1");
		
	
		

		
    }
    private void VoiceSwitch(int arg1){   // 檢查開關
    	if ("2".equals(MusicVoice)){
			sp.play(arg1, 1, 1, 0, 0, 1);
			}
    }
  
   
    
    private void StartSend(boolean agr1,boolean arg2){
    	if (agr1==true){
    		Intent SendIntent=new Intent();
    		if (arg2==true){
    		
           		SendIntent.setClass(this, Page3.class); 
           		
    		} else {
    			
        		SendIntent.setClass(this, Tablet_Copy.class); 
        		
    		}

    		Bundle SendBundle=new Bundle();
    		SendBundle.putString("D1", SendData[0]);
    		SendBundle.putString("D2", SendData[1]);
    		SendBundle.putString("D3", SendData[2]);
    		SendBundle.putString("D4", SendData[3]);
    		SendBundle.putString("D5", SendData[4]);
    		SendBundle.putString("D6", SendData[5]);
    		SendBundle.putString("D7", SendData[6]);
    		SendBundle.putString("D8", Fullnm);
    		SendBundle.putString("D9", DataWatnum);
    		SendBundle.putString("M1", MusicVoice);
    		SendIntent.putExtras(SendBundle);
		
    		startActivity(SendIntent);
    		finish();
    	} else {
			AlertDialog Alert=null;
			
		
			
			AlertDialog.Builder Builder=new AlertDialog.Builder(this);
			Builder.setTitle(getText(R.string.DialogTitle));
			Builder.setMessage(getText(R.string.DialogMessage1));
			Builder.setCancelable(true);
			Builder.setNegativeButton(getText(R.string.DialogButton1), this);
			Alert=Builder.create();
			Alert.show();
    	}
    }
    
	private String AddStrLength(String string, int i, boolean b, String string2) {
		while(string.length()<i){
			if (b==true){
				string=string2+string;
			} else {
				string=string+string2;
			}
		} 
    	return string;
	}
	private void UpdateData(){
		MyDBConnection DBConnection=new MyDBConnection(this,SendData[5],null,1);
		SQLiteDatabase DB=DBConnection.getWritableDatabase();	
		
		try{
			String Str=
				"update "+(String)getText(R.string.TableName2)+" set "+
				"Remark='"+ Remark+"' "+
				"where Custno='" + SendData[3] +"' ";
			DB.execSQL(Str);
		
			DB.close();
			DBConnection.close();
			
			AlertDialog Alert=null;
			AlertDialog.Builder Builder=new AlertDialog.Builder(this);
			Builder.setTitle(getText(R.string.TcTitle));
			Builder.setMessage(getText(R.string.notesave));
			Builder.setCancelable(true);
			Builder.setNegativeButton(getText(R.string.Button), this);
			Alert=Builder.create();
			Alert.show();
			
			//StartSend(true,true);
		} catch (SQLException e){
			DB.close();
			DBConnection.close();
			Toast.makeText(Page5.this,e.getMessage(),Toast.LENGTH_LONG).show();
			StartSend(false,false);
		}
	}
	@Override
	public void onClick(View v) {
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        long[] pattern = {0,40}; // OFF/ON/OFF/ON...
        
        vibrator.vibrate(pattern, -1);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
        if (v.equals(CB1)||v.equals(CB2)||v.equals(CB3)||v.equals(USECB)||v.equals(CB4)||v.equals(PPB)||v.equals(SB)){
        	VoiceSwitch(MusicButton);
        }
		
		EDT1.setInputType(InputType.TYPE_NULL);
		EDT2.setInputType(InputType.TYPE_NULL);
		TAB1.setInputType(InputType.TYPE_NULL);
		
		imm.hideSoftInputFromWindow(EDT1.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(EDT2.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(TAB1.getWindowToken(), 0);
		
		EDT1.setBackgroundResource(R.color.silver);
		EDT2.setBackgroundResource(R.color.silver);
		TAB1.setBackgroundResource(R.color.silver);
		
		if(CB1.isChecked()){
			EDT1.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);    //限制只能輸入數字及小數點
			imm.showSoftInput(EDT1, InputMethodManager.SHOW_FORCED); 
			EDT1.setBackgroundResource(R.color.s);
		}else{
			EDT1.setText("");  // 取消勾選清空欄位
		}
		if(CB2.isChecked()){
			EDT2.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);    //限制只能輸入數字及小數點
			imm.showSoftInput(EDT2, InputMethodManager.SHOW_FORCED);
			EDT2.setBackgroundResource(R.color.s);
		}else{
			EDT2.setText("");  // 取消勾選清空欄位
		}
		if(CB4.isChecked()){
			TAB1.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
			TAB1.setGravity(Gravity.TOP);  
	        //改变默认的单行模式  
	    	TAB1.setSingleLine(false);  
	        //水平滚动设置为False  
	    	TAB1.setHorizontallyScrolling(false);
			imm.showSoftInput(TAB1, InputMethodManager.SHOW_FORCED);
			TAB1.setBackgroundResource(R.color.s);
		}else{
			TAB1.setText("");  // 取消勾選清空欄位
		}
		
		

		Remark="";
		
		if (v.equals(SB)){       //備註存檔
			if(CB1.isChecked()){
				Remark=Remark+"●(1)" + EDT1.getText().toString().trim()+"○(1)";      //字串前後增加的 ●(1)和 ○(1)是為了抓取字串。
			}
			if(CB2.isChecked()){
				Remark=Remark+"●(2)" + EDT2.getText().toString().trim()+"○(2)";
			}
			if(CB3.isChecked()){
				Remark=Remark+"●(3)";
			}
			if(USECB.isChecked()){
				Remark=Remark+"●(use)";
			}
			if(CB4.isChecked()){
				Remark=Remark+"●(4)" + TAB1.getText().toString().trim()+"○(4)";
			}
			
			UpdateData();
			imm.hideSoftInputFromWindow(EDT1.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(EDT2.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(TAB1.getWindowToken(), 0);
			
		} else if (v.equals(PPB)){
			StartSend(true,true);
			imm.hideSoftInputFromWindow(EDT1.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(EDT2.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(TAB1.getWindowToken(), 0);
			
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		SendData[3]=(String)CustDataNo[arg2];
		Fullnm=(String)CustFullnm[arg2];
		DataWatnum=null;
		arg1.setBackgroundResource(R.color.view);
		//SendData[4]=String.valueOf(DLV.getFirstVisiblePosition());
		StartSend(true,true);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which){
			case -1:
				finish();
				break;
		}
	}
	private static Boolean isExit =false;     
    private static Boolean hasTask =false;
    Timer tExit =new Timer();
    TimerTask task =new TimerTask() {
            @Override         
            public void run() {             
                    isExit =false;             
                    hasTask =true;         
                    }     
            };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
                    // TODO Auto-generated method stub
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                            
                                    finish();                                 
                                    StartSend(true,true);
                           
                    }                 
            return false;         
    }
    
}