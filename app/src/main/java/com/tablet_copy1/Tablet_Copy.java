package com.tablet_copy1;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.*;
import android.view.KeyEvent;
import android.view.View;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.view.View.OnClickListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.ListAdapter;  
import android.widget.ListView; 
import android.app.ProgressDialog;
import java.io.*;
import com.bm_tablet_copy1.R;
//匯入資料庫相關
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.inputmethod.InputMethodManager;

import org.w3c.dom.Text;

public class Tablet_Copy extends Activity implements OnClickListener,OnItemClickListener,android.content.DialogInterface.OnClickListener {   //去掉,OnItemLongClickListener

	InputMethodManager mInputMethodManager = null; // 系統鍵盤
	String DataTitle=null;
	public ProgressDialog PDialog = null;
	ImageButton EB=null;						//結束鈕
	ImageButton NPB=null;						//下一頁鈕
	//ImageButton RSB=null;						//道路選定鈕
	//ImageButton WSB=null;						//表查詢鈕
	ImageButton VOI=null;						//聲音開關
	RadioButton RB1=null;						//全部選項鈕
	RadioButton RB2=null;						//已抄選項鈕
	RadioButton RB3=null;						//未抄選項鈕
	TextView TTV=null;							//相關資料欄
	TextView CHV=null;							//總抄表區
	TextView test;
	ListView DLV=null;							//道路分佈資料欄
	ImageButton FID=null;
	EditText FIV=null;
	String strNum="";
	String NowDate="";
	BufferedReader br;
    SoundPool sp; //声明一个SoundPool
    int MusicButton; //按鈕音
    int MusicButton1;
    int voice=1;
    private AudioManager am; 
    private int maxVol;
	int change=0;
    public String MusicVoice = "2"; //音效開關   2=開  1=關
	SQLiteDatabase sqldb;  
	public String DB_TABLE = "num";  
	public View view1=null;
	public String DB_NAME = "DB.sqlite"; public int DB_VERSION = 1; 
	final DataHelper helper = new DataHelper(this, DB_NAME, null, DB_VERSION);  
	CharSequence[] RoadData=null;
	CharSequence[] RoadData1=null;
	String Page=null;
	String[] SendData=new String[7]; //0:全部/已抄/已抄代碼 1:抄表區代碼2:預選道路代碼 3:用編代碼 4:用戶地址 5.資料庫位置
	//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page1);//page1 layout
        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE); //系統鍵盤
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); //不自動彈出鍵盤
		getWindow().setWindowAnimations(0);
        FindView();
        BuildListener();
        InitData();  //接收資料
        init(); //音效檔
        Page = "page1";

    	if (CheckDB()) GetData();
     	DLV.setOnScrollListener(new OnScrollListener() {     //滑動listview時觸發事件
             //滚动状态改变时调用
			@Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {  
                // 不滚动时保存当前滚动到的位置
            	 if (view1!=null){
            		 view1.setBackgroundResource(R.color.white);
             	}
            }
            @Override  
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {  
            }  
        });

		FIV.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				FIV.removeTextChangedListener(this);//解除文字改變事件
				FIV.setText(s.toString().toUpperCase());//轉換
				FIV.setSelection(s.toString().length());//重新設定游標位置
				FIV.addTextChangedListener(this);//重新綁
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}
			public void afterTextChanged(Editable arg0) {

			}
		});

		FIV.requestFocus();
		RB1.setChecked(false);
		RB2.setChecked(false);
		RB3.setChecked(true);
		onClick(RB3);
    }

    private void init() {  //音效檔
    	setVolumeControlStream(AudioManager.STREAM_MUSIC); 
        am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);  
        maxVol = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
       	// sp= new SoundPool(10, AudioManager.STREAM_SYSTEM, 100);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);  //音量鍵調節音效音量! 
        AssetManager assetManager = getAssets(); 
   		//     AssetFileDescriptor descriptor = assetManager.openFd("explosion.ogg"); //游戏开发通常把音效图片等资源放到assets中，便于用分级文件夹管理
    	//    int explosionId = sp.load(descriptor, 1);
        MusicButton = sp.load(this, R.raw.button, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
        MusicButton1 = sp.load(this, R.raw.button1, 1);
        if ("2".equals(MusicVoice)){
    		VOI.setImageResource(R.drawable.voice_open);
    	} else {
    	VOI.setImageResource(R.drawable.voice_close);
    	}
    }
 
    private void FindView(){
    	EB=(ImageButton)findViewById(R.id.EndButton);
    	NPB=(ImageButton)findViewById(R.id.NextPageButton);
    	//RSB=(ImageButton)findViewById(R.id.RoadSelectedButton);
    	//WSB=(ImageButton)findViewById(R.id.SearchButton);
    	VOI=(ImageButton)findViewById(R.id.voice);
    	RB1=(RadioButton)findViewById(R.id.AllDataRadioButton);
    	RB2=(RadioButton)findViewById(R.id.CopyedRadioButton);
    	RB3=(RadioButton)findViewById(R.id.NonCopyedRadioButton);
    	TTV=(TextView)findViewById(R.id.TitleTextView);
		CHV=(TextView)findViewById(R.id.CHVTextView);
    	DLV=(ListView)findViewById(R.id.DataListView);
		FID=(ImageButton)findViewById(R.id.WatnumButton);
		FIV=(EditText)findViewById(R.id.WatnumEditText);
		test=(TextView)findViewById(R.id.test);
    }
    
    private void exitDialog(String Message) {
    	new AlertDialog.Builder(this)
		.setTitle(getText(R.string.TcTitle))
		.setMessage(Message)
		.setPositiveButton(getText(R.string.DialogButton1),
			new DialogInterface.OnClickListener() {
 
				@Override
				public void onClick(DialogInterface dialog, int which) {  //點選確定
					// TODO Auto-generated method stub
					//按下按鈕後執行的動作，沒寫則退出Dialog
					finish();                                  
                    System.exit(0);			//退出系統
				}
			}
		)
		.show();
    }
    
    private void openDialog(String Message) {
    	new AlertDialog.Builder(this)
		.setTitle(getText(R.string.TcTitle))
		.setMessage(Message)
		.setPositiveButton(getText(R.string.DialogButton1),
			new DialogInterface.OnClickListener() {
 
				@Override
				public void onClick(DialogInterface dialog, int which) {  //點選確定
					// TODO Auto-generated method stub
					//按下按鈕後執行的動作，沒寫則退出Dialog
					//finish();                                  
                   
				}
			}
		)
		.show();
    }
    
    public String getDateTime(){
    	SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
    	Date date = new Date();
    	String strDate = sdFormat.format(date);
    	//System.out.println(strDate);
    	return strDate;
    }
    
    private void BuildListener(){
    	EB.setOnClickListener(this);
    	NPB.setOnClickListener(this);
    	//RSB.setOnClickListener(this);
    	//WSB.setOnClickListener(this);
    	VOI.setOnClickListener(this);
    	RB1.setOnClickListener(this);
    	RB2.setOnClickListener(this);
    	RB3.setOnClickListener(this);
    	DLV.setOnItemClickListener(this);
    	//DLV.setOnItemLongClickListener(this);
		FID.setOnClickListener(this);
    }
    
    private void InitData(){ //接收資料
    	SendData[0]=(String)getText(R.string.NonCopyedData);	//全部/已抄/已抄代碼之初值 預設為未抄
        SendData[1]="";											//抄表區代碼之初值
        SendData[2]="";											//預選道路代碼之初值
        SendData[3]="";											//用戶編號之初值
        SendData[4]="";											//地址之初值
        SendData[5]="";											//資料庫位置
        Page="";
 		try {
 			Intent GetIntent = getIntent();
			Bundle GetBundle = GetIntent.getExtras();
			SendData[0] = GetBundle.getString("D1");
			SendData[1] = GetBundle.getString("D2");
			SendData[2] = GetBundle.getString("D3");
			SendData[3] = GetBundle.getString("D4");
			SendData[4] = GetBundle.getString("D5");
			SendData[5] = GetBundle.getString("D6");
			SendData[6] = GetBundle.getString("D7");
			Page = GetBundle.getString("Page_");
			MusicVoice = GetBundle.getString("M1");
			change = Integer.valueOf(GetBundle.getString("change"));
			SetOptionChecked(String.valueOf(change));
 		} catch (Exception e){
  		}
    }

	public boolean dispatchKeyEvent(KeyEvent event) { //鍵盤的完成按鈕
		if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() != KeyEvent.ACTION_UP){
			onClick(FID);

			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			if(inputMethodManager.isActive()){
				inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
			}

			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	private void VoiceSwitch(int arg1){   // 檢查開關
    	if ("2".equals(MusicVoice)){
			sp.play(arg1, 0.5f, 0.5f, 1, 0, 1f);  
			}
    	//sp.unload(arg1); 

    	//sp.release();
    }
    
    private boolean CheckDB(){
       	// 供日後取得資料庫存放之外部路徑
    	//取得SD Card路徑
     	//SendData[5]="/mnt/extsd/copy.db3";	//land
    	//SendData[5]="/sdcard2/test/copy.db3";		//htc j
    	SendData[5]="/mnt/sdcard/DB3/copy.db3";     // (本機記憶體)
    	
    	//SendData[5]="/mnt/sdcard2/DB3/copy.db3";     // (外插SD卡)   ●
    	//SendData[5]="/storage/sdcard1/db3/copy.db3";		//dell
    	//SendData[5]="/mnt/sdcard/copy.db3";	//dell sen  	
    	//SendData[5] = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+(String)getText(R.string.DataBaseName_Sort);
       	File tFile = new File(SendData[5]);    
    	if (tFile.exists() == true) {   
    		return true;
    	} else {
    		TTV.setText("無檔案："+SendData[5]);
 			return false;
    	}
    }
    
    private void GetData(){
    	MyDBConnection DBConnection=null;
    	SQLiteDatabase DB=null;
    	Cursor Cursor=null;
 		int Dco=0;
		String Str=null;
		String CHVStr=null;

 		final String CountTable=						//定義資料庫連線字串	
 			"select count(*) from "+(String)getText(R.string.TableName1);	
 		 String DetailTable=							//定義資料庫連線字串
 			"select * from "+(String)getText(R.string.TableName1)+" ";
		 String resultCount=							//定義資料庫連線字串
				"select count(*) from "+(String)getText(R.string.TableName2)+" ";
		/*final String QueryTable=						//定義資料庫連線字串
			"select a."+(String)getText(R.string.ResultFieldRoadno)+",count(b."+(String)getText(R.string.ResultFieldRoadno)+") from "+
			(String)getText(R.string.TableName1)+" AS a LEFT OUTER JOIN "+
			"(SELECT RoadNo FROM Result WHERE "+(String)getText(R.string.ResultFieldCopyedMk)+"<>'"+
			(String)getText(R.string.CopyedMk)+"'"+") AS b "+
			"ON a."+(String)getText(R.string.ResultFieldRoadno)+" = b."+(String)getText(R.string.ResultFieldRoadno)+
			" GROUP BY a."+(String)getText(R.string.ResultFieldRoadno);*/

 		try {
 			DBConnection=new MyDBConnection(this,SendData[5],null,1);
			DB=DBConnection.getWritableDatabase();	//儲存空間不足時會有錯誤
 			DB=DBConnection.getReadableDatabase();	

 			//重整資料庫抄表註記
			/*Cursor=DB.rawQuery(QueryTable, null);
			while (Cursor.moveToNext()){
				Str=
					"update "+(String)getText(R.string.TableName1)+" "+
					"set "+(String)getText(R.string.RoadFieldDco)+"="+
					(String)getText(R.string.RoadFieldRco)+"-"+String.valueOf(Cursor.getInt(1))+" "+
					"where RoadNo='"+
					(String)Cursor.getString(0)+"' ";
				DB.execSQL(Str);
			}
        	Cursor.close();*/

			switch(change)
			{
				case 0:
					DetailTable=DetailTable+"order by RoadNo ";
					break;
				case 1:
					DetailTable=DetailTable+"order by RoadNo ";  //全部
					break;
				case 2:
					DetailTable=DetailTable+"where CAST([Dco] as integer) > 0"+" order by RoadNo ";  //已抄
					break;
				case 3:
					DetailTable=DetailTable+"where CAST([Rco] as integer) <> CAST([Dco] as integer)"+" order by RoadNo "; //未抄
					break;
			}

			Cursor=DB.rawQuery(CountTable, null);
			while (Cursor.moveToNext()){
				Dco=Cursor.getInt(0);
				CHVStr = getText(R.string.DataTotal)+Cursor.getString(0);
				//CHV.setText(getText(R.string.DataTotal)+Cursor.getString(0));	//顯示抄表區總數
			}
        	Cursor.close();

			Cursor=DB.rawQuery(resultCount, null);
			while (Cursor.moveToNext()){
				CHV.setText(CHVStr + "\n" + getText(R.string.DataTota2)+Cursor.getString(0));	//顯示抄表戶總數
			}
			Cursor.close();

        	if (Dco!=0){
            	RoadData = new CharSequence[Dco];
        		Dco=0;
        		int Cc=0;
        		int Tc=0;
        		Cursor=DB.rawQuery(DetailTable, null);
        		while (Cursor.moveToNext()){
        			RoadData[Dco]=
        					Cursor.getString(1)+"   "+
                	        Cursor.getString(2)+"       "+
                	        Cursor.getString(4)+"/"+
                	        Cursor.getString(3);
        			Cc+=Cursor.getFloat(4);
        			Tc+=Cursor.getFloat(3);
        			Dco+=1;
        		}
        		while (Cursor.moveToPrevious()){
        			SendData[1]=Cursor.getString(0);
        		}
        		Cursor.close();

        		DataTitle=
        			//"("+getText(R.string.AllDataCount)+"："+ String.valueOf(Tc)+ getText(R.string.Unit)+"，"+ getText(R.string.CopyedData)+"："+ String.valueOf(Cc)+ getText(R.string.Unit)+")";
					"("+ getText(R.string.CopyedData)+"："+ String.valueOf(Cc)+ getText(R.string.Unit)+")";
        		if ("".equals(SendData[2])){
        			TTV.setText(getText(R.string.DataTitle1)+SendData[1]+DataTitle);
        		} else {
        			TTV.setText(getText(R.string.DataTitle1)+SendData[1]+DataTitle+"　"+getText(R.string.DataTitle2)+SendData[2]);
        		}

    			Cursor=DB.rawQuery(DetailTable, null);

        		String _id = Cursor.getColumnName(1);
    	        String roadnm = Cursor.getColumnName(2);
    	        String dco = Cursor.getColumnName(3);
    	        String rco = Cursor.getColumnName(4);
    	        String[] ColumnNames = { _id, roadnm, dco, rco };

				//利用adapter將資料灌入DLV(DataListView)
    	        ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.listviewlayout, Cursor, ColumnNames, new int[] { R.id.roadno, R.id.roadnm, R.id.rco, R.id.dco });
        	
            	//設定列表內容
        		//ListAdapter	adapter = new ArrayAdapter<CharSequence>(this,R.layout.gridrow,RoadData);
            	//設定列數
           		DLV.setAdapter(adapter);
    	        //Cursor.close();
        	}
        	DB.close();
			DBConnection.close();
		} catch (SQLException e){
			//Cursor.close();
			DB.close();
			DBConnection.close();
			Toast.makeText(Tablet_Copy.this,e.getMessage(),Toast.LENGTH_LONG).show();
		}
    }

    private void SetOptionChecked(String arg){
    	if (arg.equals((String)getText(R.string.AllData))){
    		RB1.setChecked(true);
    		RB2.setChecked(false);
    		RB3.setChecked(false);
    	} else if (arg.equals((String)getText(R.string.CopyedData))){
    		RB1.setChecked(false);
    		RB2.setChecked(true);
    		RB3.setChecked(false);
    	} else if (arg.equals((String)getText(R.string.NonCopyedData))) {
			RB1.setChecked(false);
			RB2.setChecked(false);
			RB3.setChecked(true);
		}
    	}

	private void StartSend(boolean arg1,boolean arg2){   // 傳輸資料
    	Intent SendIntent=new Intent();
    	if (arg1==true){
    		if (arg2==true){
    			SendIntent.setClass(this, Page2.class);
    		} else {
    			SendIntent.setClass(this, Page4.class);
    		}
    		Bundle SendBundle=new Bundle();
    		SendBundle.putString("D1", SendData[0]);
    		SendBundle.putString("D2", SendData[1]);
    		SendBundle.putString("D3", SendData[2]);
    		SendBundle.putString("D4", SendData[3]);
    		SendBundle.putString("D5", SendData[4]);
    		SendBundle.putString("D6", SendData[5]);
			SendBundle.putString("D7", SendData[6]);
    		SendBundle.putString("Page_", Page);
    		SendBundle.putString("M1", MusicVoice);
			SendBundle.putString("change", String.valueOf(change));
			SendBundle.putString("FIV", FIV.getText().toString());
			SendIntent.putExtras(SendBundle);
    		startActivity(SendIntent);
    		finish();//是否於此Tablet_Copy關閉 ?
    	} else {
    		if (arg2==true){
    			
    			SendIntent.setClass(this, Mainloading.class);
    			Bundle SendBundle=new Bundle();
        		SendBundle.putString("D1", SendData[0]);
        		SendBundle.putString("D2", SendData[1]);
        		SendBundle.putString("D3", SendData[2]);
        		SendBundle.putString("D4", SendData[3]);
        		SendBundle.putString("D5", SendData[4]);
        		SendBundle.putString("D6", SendData[5]);
				SendBundle.putString("D7", SendData[6]);
        		SendBundle.putString("Page_", Page);
        		SendBundle.putString("M1", MusicVoice); 
        		SendIntent.putExtras(SendBundle);
        		startActivity(SendIntent);
        		finish();
    		}else {
    			openDialog((String) getText(R.string.DialogMessage1));
    		}
			
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

	@Override
	public void onClick(View v) {
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        long[] pattern = {0,40}; // OFF/ON/OFF/ON...
        vibrator.vibrate(pattern, -1);
		
        // TODO Auto-generated method stub
		if (v.equals(VOI)) {
			if ("1".equals(MusicVoice)) {
			 MusicVoice = "2";
			} else {
				MusicVoice = "1";			
			}
		}
		if ("2".equals(MusicVoice)){
    		VOI.setImageResource(R.drawable.voice_open);
    	} else {
    		VOI.setImageResource(R.drawable.voice_close);
    	}
		if (v.equals(EB) || v.equals(NPB)) {   // || v.equals(WSB)
			VoiceSwitch(MusicButton);
			if (v.equals(NPB)){
				//一般用戶檢核資料是否齊全(商區不用)
				if (!"".equals(SendData[1])){
					if (SendData[1].substring(0, 1).equals(getText(R.string.NormalCust1)) || SendData[1].substring(0, 1).equals(getText(R.string.NormalCust2))){
						if ("".equals(SendData[2])){
							StartSend(false,false);
						} else {
							if ("1".equals(SendData[6])){
								openDialog((String) getText(R.string.DialogMessage6));
							}else StartSend(true,true);
						}
					} else {
						if ("".equals(SendData[2])){
							openDialog((String) getText(R.string.DialogMessage5));
						} else {
							/*if (!NowDate.equals(strNum)){  //假如不等於電腦年月則跳出提醒視窗
									exitDialog((String) getText(R.string.DialogMessage9));
							}else if("".equals(strNum) && "".equals(NowDate)){    //假如平板的date筆記本遺失 則跳出視窗
									exitDialog((String) getText(R.string.DialogMessage10));
							}
							else{
								StartSend(true,true);	   //平板時間等於電腦年月時間則可換頁
							}*/
							StartSend(true,true);
						}
					}
				} else {
					StartSend(false,false);					
				}
			} else {
				AlertDialog Alert=null;
				// else if (v.equals(WSB)){
				//		StartSend(true,false);		
				//	}
				AlertDialog.Builder Builder=new AlertDialog.Builder(this);
				Builder.setTitle(getText(R.string.DialogTitle));
				Builder.setMessage(getText(R.string.DialogMessage12));
				Builder.setCancelable(true);
				Builder.setPositiveButton(getText(R.string.DialogButton4), this);
				Builder.setNegativeButton(getText(R.string.DialogButton3), this);
				Alert=Builder.create();
				Alert.show();
				//●●
				//showCustomDialog();
				//StartSend(false,true);
			}
		} else if (v.equals(RB1)|| v.equals(RB2) || v.equals(RB3)){
			VoiceSwitch(MusicButton1);
			if (v.equals(RB1)){
				RB1.setChecked(true);
				RB2.setChecked(false);
				RB3.setChecked(false);
				change=1;
				GetData();
				SendData[0]=(String)getText(R.string.AllData);
			} else if (v.equals(RB2)){
				RB1.setChecked(false);
				RB2.setChecked(true);			
				RB3.setChecked(false);
				change=2;
				GetData();
				SendData[0]=(String)getText(R.string.CopyedData);
			} else if (v.equals(RB3)){
				RB1.setChecked(false);
				RB2.setChecked(false);
				RB3.setChecked(true);
				change=3;
				GetData();
				SendData[0]=(String)getText(R.string.NonCopyedData);
			}
		} else if (v.equals(FID)) {
			SelectTable();
		}
	}

	public void SelectTable(){
		MyDBConnection DBConnection = null;
		SQLiteDatabase DB = null;
		Cursor Cursor = null;
		int Dco=0;

		//定義資料庫連線字串
		String FindTable = "select Result.Roadno from " + (String) getText(R.string.TableName2) + " where TRIM(" + (String) getText(R.string.TableName2) + ".Watnum) = '" + FIV.getText().toString() + "'";

		String CountTable = "select count(Result.Roadno) from " + (String) getText(R.string.TableName2) + " where TRIM(" + (String) getText(R.string.TableName2) + ".Watnum) = '" + FIV.getText().toString() + "'";

		// 已抄、未抄、全部 的查詢條件
		if (((String) getText(R.string.CopyedData)).equals(SendData[0])) {
			CountTable = CountTable + " and " + (String) getText(R.string.ResultFieldCopyedMk) + " = '" + (String) getText(R.string.CopyedMk) + "'";
		} else if (((String) getText(R.string.NonCopyedData)).equals(SendData[0])) {
			CountTable = CountTable + " and " + (String) getText(R.string.ResultFieldCopyedMk) + " <> '" + (String) getText(R.string.CopyedMk) + "'";
		}

		try {
			DBConnection=new MyDBConnection(this,SendData[5],null,1);
			//DB=DBConnection.getWritableDatabase();	//儲存空間不足時會有錯誤
			DB=DBConnection.getReadableDatabase();

			Cursor=DB.rawQuery(CountTable, null);

			while (Cursor.moveToNext()){
				Dco=Cursor.getInt(0);
			}
			Cursor.close();
			if (Dco==0){
				if (FIV.getText().toString().length()==0){
					openDialog((String) getText(R.string.DialogMessage8));
				}else{
					openDialog((String) getText(R.string.DialogMessage7));
				}

			} else{
				Cursor=DB.rawQuery(FindTable, null);
				while (Cursor.moveToNext()){
					SendData[2]=Cursor.getString(0);
				}
				StartSend(true,true);

			}
			Cursor.close();
			DB.close();
			DBConnection.close();
		} catch (SQLException e){
			Cursor.close();
			DB.close();
			DBConnection.close();
			Toast.makeText(Tablet_Copy.this,e.getMessage(),Toast.LENGTH_LONG).show();
		}
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		SendData[2]=(String)RoadData[arg2].subSequence(0, 3);
		SendData[3]="";
		SendData[4]="";
		//ListView listView = (ListView) arg0;
		if (view1!=null){
			view1.setBackgroundResource(R.color.white);
		} 
		//	view.setBackgroundResource(R.color.white);
			arg1.setBackgroundResource(R.color.view);
		
		view1 = arg1;
		
		showToast((String)RoadData[arg2]);
		//Toast.makeText(Tablet_Copy.this,""+(String)RoadData[arg2],Toast.LENGTH_SHORT).show();
		TTV.setText(getText(R.string.DataTitle1)+SendData[1]+DataTitle+"　"+getText(R.string.DataTitle2)+SendData[2]);
	}
	
	private Toast mToast;
    public void showToast(String roadData2) {// Toast顯示第二次時，假如前一次Toast還沒關閉，則會關閉前一個Toast。
        if(mToast == null) {  
            mToast = Toast.makeText(Tablet_Copy.this,""+roadData2,Toast.LENGTH_SHORT);  
        } else {  
            mToast.setText(roadData2);    
            mToast.setDuration(Toast.LENGTH_SHORT);  
        }  
        mToast.show();  
    }  
     
    public void cancelToast() {  
            if (mToast != null) {  
                mToast.cancel();  
            }  
        }  

	public void onNothingSelected(AdapterView<?> arg0) {
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which){
			case -1:
				//finish();
				finish();
				break;
			case -2:
				
				StartSend(false,true);
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

    final void setPriority(int streamID, int priority) {
            
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                            if(isExit ==false ) {
                                    isExit =true;
                                    showToast("兩秒內再按一次退出程序");
                                    //Toast.makeText(this, "兩秒內再按一次退出程序", Toast.LENGTH_SHORT).show();
                                    if(!hasTask) {
                                            tExit.schedule(task, 2000); 
                                    }
                            } else {
                                    finish();
                                    System.exit(0);
                            }
             //               return false; 
                    }

             //       if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                    	//SoundPool.setVolume(sp.play(paiMap.get(resId), 1, 1, 1,0, 1f), soundPoolVolume, soundPoolVolume);
            //        	voice+=1;
            //        	setPriority(MusicButton, voice);
            //        	setPriority(MusicButton1, voice);
            //        	   return true;
           //         }

            //        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            //        	voice-=1;
            //        	setPriority(MusicButton, voice);
           //         	setPriority(MusicButton1, voice);
                    	
           //         	   return true;
          //          }

        //            else {
                    //return super.onKeyDown(
                    //keyCode, event);
                    return false; 
           //         }

                    	//  return false;      
              
    }

}
