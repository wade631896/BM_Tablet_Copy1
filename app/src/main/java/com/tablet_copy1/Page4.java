package com.tablet_copy1;

import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.*;
import android.view.KeyEvent;
import android.view.View;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;

import com.bm_tablet_copy1.R;
//匯入資料庫相關
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.media.SoundPool;

public class Page4 extends Activity  implements OnClickListener,OnItemClickListener,android.content.DialogInterface.OnClickListener {
	ImageButton QCB=null;						//查詢已抄鈕
	ImageButton QNCB=null;						//查詢未抄鈕
	ImageButton CB=null;						//取消鈕
	EditText ILET=null;							//用戶表/番號欄
	ListView DLV=null;							//用戶資料欄
	Button N1=null;								//數字鈕
	Button N2=null;								//數字鈕
	Button N3=null;								//數字鈕
	Button N4=null;								//數字鈕
	Button N5=null;								//數字鈕
	Button N6=null;								//數字鈕
	Button N7=null;								//數字鈕
	Button N8=null;								//數字鈕
	Button N9=null;								//數字鈕
	Button N0=null;								//數字鈕
	Button NH=null;								//數字鈕
	Button NS=null;								//數字鈕
	Button ND=null;								//數字鈕
    SoundPool sp;//声明一个SoundPool
    int MusicButton;//按鈕音
    int backspace;//數字鍵音效
    private AudioManager am; 
    private int maxVol;  
    public String MusicVoice = "2";//音效開關   2=開  1=關
	CharSequence[] CustDataList=null;
	CharSequence[] CustDataNo=null;
	String[] SendData=new String[6];			//0:全部/已抄/已抄代碼    1:抄表區代碼
												//2:預選道路代碼                   3:用編代碼
    											//4:用戶地址			  5.資料庫位置
	String QueryType=null;						//替代陣列0
	//----------------------------------------------------------------
    //-----------------------------------------------------------------
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page4);
        
        FindView();
        BuildListener();
        InitData();
        init();
    }
    private void init() {
        // TODO Auto-generated method stub
    	setVolumeControlStream(AudioManager.STREAM_MUSIC); 
        am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);  
        maxVol = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
      //  sp= new SoundPool(10, AudioManager.STREAM_SYSTEM, 1);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);  //音量鍵調節音效音量!
        MusicButton = sp.load(this, R.raw.button, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
        backspace = sp.load(this, R.raw.backspace, 1);
        
    }
    private void VoiceSwitch(int arg1){   // 檢查開關
    	if ("2".equals(MusicVoice)){
			sp.play(arg1, 1, 1, 0, 0, 1);
			}
    }
    private void FindView(){
       	QCB=(ImageButton)findViewById(R.id.QueryCopyedButton);
       	QNCB=(ImageButton)findViewById(R.id.QueryNonCopyedButton);
       	CB=(ImageButton)findViewById(R.id.CancelButton);  
       	ILET=(EditText)findViewById(R.id.InputLampNoEditText);
       	DLV=(ListView)findViewById(R.id.DataListView);
       	N1=(Button)findViewById(R.id.N1);
       	N2=(Button)findViewById(R.id.N2);
       	N3=(Button)findViewById(R.id.N3);
       	N4=(Button)findViewById(R.id.N4);
       	N5=(Button)findViewById(R.id.N5);
       	N6=(Button)findViewById(R.id.N6);
       	N7=(Button)findViewById(R.id.N7);
       	N8=(Button)findViewById(R.id.N8);
       	N9=(Button)findViewById(R.id.N9);
       	N0=(Button)findViewById(R.id.N0);
       	NH=(Button)findViewById(R.id.Nh);
       	NS=(Button)findViewById(R.id.Ns);
       	ND=(Button)findViewById(R.id.Nd);
    }

    private void BuildListener(){
    	QCB.setOnClickListener(this);   	
    	QNCB.setOnClickListener(this);  
    	CB.setOnClickListener(this);  
    	N1.setOnClickListener(this);
    	N2.setOnClickListener(this);
    	N3.setOnClickListener(this);
    	N4.setOnClickListener(this);
    	N5.setOnClickListener(this);
    	N6.setOnClickListener(this);
    	N7.setOnClickListener(this);
    	N8.setOnClickListener(this);
    	N9.setOnClickListener(this);
    	N0.setOnClickListener(this);
    	NH.setOnClickListener(this);
    	NS.setOnClickListener(this);
    	ND.setOnClickListener(this);
    	DLV.setOnItemClickListener(this);
    }     
    
    private void InitData(){
  		//初始化資料傳送
		Intent GetIntent = getIntent();
		Bundle GetBundle = GetIntent.getExtras();
		SendData[0] = GetBundle.getString("D1");
		SendData[1] = GetBundle.getString("D2");
		SendData[2] = "";
		SendData[3] = "";
		SendData[4] = "";
		SendData[5] = GetBundle.getString("D6");
		MusicVoice = GetBundle.getString("M1");
    }
    
    private void GetData(){
    	MyDBConnection DBConnection=null;
    	SQLiteDatabase DB=null;
    	Cursor Cursor=null;
 		int Dco=0;
		
		String CountTable = 						//定義資料庫連線字串
			"select count(*) from "+			
			(String)getText(R.string.TableName1)+","+
			(String)getText(R.string.TableName2)+" "+
			"where "+(String)getText(R.string.TableName2)+".RoadNo="+
			(String)getText(R.string.TableName1)+".RoadNo "+
			"and "+(String)getText(R.string.TableName2)+".Watnum like '%"+
			ILET.getText().toString() + "%' ";
		
		String DetailTable = 						//定義資料庫連線字串
			"select "+(String)getText(R.string.TableName2)+".Watnum,"+
			(String)getText(R.string.TableName2)+".CustLoc1,"+
			(String)getText(R.string.TableName2)+".Custno,"+
			(String)getText(R.string.TableName1)+".RoadNm "+
			"from "+(String)getText(R.string.TableName2)+","+
			(String)getText(R.string.TableName1)+" "+
			"where "+(String)getText(R.string.TableName2)+".RoadNo="+
			(String)getText(R.string.TableName1)+".RoadNo "+
			"and "+(String)getText(R.string.TableName2)+".Watnum like '%"+
			ILET.getText().toString() + "%' ";
		
		if (((String)getText(R.string.CopyedData)).equals(QueryType)) {
			CountTable = 
				CountTable + "and "+(String)getText(R.string.ResultFieldCopyedMk)+
				"='"+ (String)getText(R.string.CopyedMk) +"' ";
			DetailTable = 
				DetailTable + "and "+(String)getText(R.string.ResultFieldCopyedMk)+
				"='"+ (String)getText(R.string.CopyedMk) +"' ";
		} else if (((String)getText(R.string.NonCopyedData)).equals(QueryType)) {
			CountTable = 
				CountTable + "and "+(String)getText(R.string.ResultFieldCopyedMk)+
				"<>'"+ (String)getText(R.string.CopyedMk) +"' ";
			DetailTable = 
				DetailTable + "and "+(String)getText(R.string.ResultFieldCopyedMk)+
				"<>'"+ (String)getText(R.string.CopyedMk) +"' ";			
		};

		DetailTable = DetailTable + "Order by " + (String)getText(R.string.OrderKeyWatnum);

		
 		//---------------------------------------------------------------
 		try {			
 			DBConnection=new MyDBConnection(this,SendData[5],null,1);
			//DB=DBConnection.getWritableDatabase();	//儲存空間不足時會有錯誤
 			DB=DBConnection.getReadableDatabase();	
			
			Cursor=DB.rawQuery(CountTable, null);
			while (Cursor.moveToNext()){
				Dco=Cursor.getInt(0);
			}
        	Cursor.close();
        	
        	if (Dco!=0){
        		CustDataList = new CharSequence[Dco];
        		CustDataNo = new CharSequence[Dco];
        		Dco=0;
        		Cursor=DB.rawQuery(DetailTable, null);
        		while (Cursor.moveToNext()){
        			CustDataList[Dco]=
        				AddStrLength(Cursor.getString(0),10,false," ")+
        				AddStrLength(Cursor.getString(1),15,false,"　")+
        				AddStrLength(Cursor.getString(2),10,false," ");
        			CustDataNo[Dco]=Cursor.getString(2);
         			Dco+=1;
        		}
        		Cursor.close();       	
	
        		//設定列表內容
        		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,R.layout.gridrow,CustDataList);
        		//設定列數
        		DLV.setAdapter(adapter);
        	} else {
        		CustDataList = new CharSequence[1];
           		CustDataNo = new CharSequence[1];   
           		
    			CustDataList[0]=(String)getText(R.string.NoGridData);
     			CustDataNo[0]="";
           		
        		//設定列表內容
        		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,R.layout.gridrow,CustDataList);
        		//設定列數
        		DLV.setAdapter(adapter);        		
        	}
			DB.close();
			DBConnection.close();
		} catch (SQLException e){
			Cursor.close();
			DB.close();
			DBConnection.close();
			Toast.makeText(Page4.this,e.getMessage(),Toast.LENGTH_LONG).show();
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

	private void FillData(Integer arg){
		switch (arg){
			case R.id.Nd:
				if (ILET.length()!=0){ILET.setText(String.valueOf(ILET.getText()).substring(0,ILET.length()-1));}
				break;		
			case R.id.Nh:
				ILET.setText(ILET.getText()+"H");
				break;
			case R.id.Ns:
				ILET.setText(ILET.getText()+"S");
				break;				
			case R.id.N1:
				ILET.setText(ILET.getText()+"1");
				break;				
			case R.id.N2:
				ILET.setText(ILET.getText()+"2");
				break;
			case R.id.N3:
				ILET.setText(ILET.getText()+"3");
				break;
			case R.id.N4:
				ILET.setText(ILET.getText()+"4");
				break;
			case R.id.N5:
				ILET.setText(ILET.getText()+"5");
				break;
			case R.id.N6:
				ILET.setText(ILET.getText()+"6");
				break;
			case R.id.N7:
				ILET.setText(ILET.getText()+"7");
				break;
			case R.id.N8:
				ILET.setText(ILET.getText()+"8");
				break;
			case R.id.N9:
				ILET.setText(ILET.getText()+"9");
				break;
			case R.id.N0:
				ILET.setText(ILET.getText()+"0");
				break;
	    }
	}
	
	@Override
	public void onClick(View v) {
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        long[] pattern = {0,40}; // OFF/ON/OFF/ON...
        vibrator.vibrate(pattern, -1);
        
		if (v.equals(QCB) || v.equals(QNCB)||v.equals(CB)){
			
			VoiceSwitch(MusicButton); 
			
		
		} else if (v.equals(N1) || v.equals(N2) || v.equals(N3) || v.equals(N4) || v.equals(N5) || v.equals(N6) || v.equals(N7) || v.equals(N8) || v.equals(N9) || v.equals(N0) || v.equals(NH) || v.equals(NS) || v.equals(ND)){
			
			VoiceSwitch(backspace); 
			
		}
		
		
		if (v.equals(QCB) || v.equals(QNCB)){
			if ("".equals(ILET.getText().toString())){
				StartSend(false,false);		
			} else {
				if (v.equals(QCB)){
					QueryType=(String)getText(R.string.CopyedData);
				} else {
					QueryType=(String)getText(R.string.NonCopyedData);
				}
				GetData();
			}
		} else if (v.equals(CB)){
			StartSend(true,false);
		} else if (v.equals(N1) || v.equals(N2) || v.equals(N3) || v.equals(N4) || v.equals(N5) || v.equals(N6) || v.equals(N7) || v.equals(N8) || v.equals(N9) || v.equals(N0) || v.equals(NH) || v.equals(NS) || v.equals(ND)){
			FillData(v.getId());
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (!"".equals((String)CustDataNo[arg2])){
			SendData[3]=(String)CustDataNo[arg2];
			StartSend(true,true);
		}
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
                                    StartSend(true,false);
                           
                    }                 
            return false;         
    }
}