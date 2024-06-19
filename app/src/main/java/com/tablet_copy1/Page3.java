package com.tablet_copy1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bm_tablet_copy1.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//匯入輸入值檢驗相關
//匯入資料庫相關

public class Page3 extends Activity implements OnClickListener, OnFocusChangeListener, android.content.DialogInterface.OnClickListener {

    InputMethodManager mInputMethodManager = null; // 系統鍵盤

    // -----------------------Button-------------------------------------
    Button SB = null; // 存檔確認鈕
    Button PPB = null; // 上一頁鈕
    Button N1 = null; // 數字鈕
    Button N2 = null; // 數字鈕
    Button N3 = null; // 數字鈕
    Button N4 = null; // 數字鈕
    Button N5 = null; // 數字鈕
    Button N6 = null; // 數字鈕
    Button N7 = null; // 數字鈕
    Button N8 = null; // 數字鈕
    Button N9 = null; // 數字鈕
    Button N0 = null; // 數字鈕
    Button ND = null; // 倒退鈕
    Button Np = null; // 小數點紐
    Button up = null; // 上一筆紐
    Button next = null; // 下一筆紐
    Button listDialog; // 親見備註
    // -----------------------Button-------------------------------------

    // -----------------------EditText-----------------------------------
    EditText PANTNUM_CET = null; // 機械表度數欄
    // -----------------------EditText-----------------------------------

    // -----------------------TextView-----------------------------------
    TextView userid, username, address, phonenumber, poset, type, lamet, lampt, fccet, test;
    TextView PANTNUMtext = null; // PANTNUM限制值
    // -----------------------TextView-----------------------------------

    CheckBox USECB = null; // 使用中勾選
    private AudioManager am;
    SoundPool sp; // 声明一个SoundPool
    private int maxVol;
    int MusicButton; // 按鈕音
    int MusicBackspace; // 倒退數字鍵音效
    int MusicButton1; // 按鈕音效
    int MusicEnter;  // 確認數字鍵音效
    int Music0; // 數字鍵0音效
    int Music1; // 數字鍵1音效
    int Music2; // 數字鍵2音效
    int Music3; // 數字鍵3音效
    int Music4; // 數字鍵4音效
    int Music5; // 數字鍵5音效
    int Music6; // 數字鍵6音效
    int Music7; // 數字鍵7音效
    int Music8; // 數字鍵8音效
    int Music9; // 數字鍵9音效
    int change;
    String UseRemark = ""; // 使用中備註
    String DataWatnum = null; // 儲存表號資料
    public String MusicVoice = "2"; // 音效開關 2=開 1=關
    String Page = null;
    String[] SendData = new String[6]; // 0:全部/已抄/已抄代碼 1:抄表區代碼 2:預選道路代碼 3:用編代碼 4:用戶地址 5:資料庫位置
    String[] Data = new String[11]; // 給Data[0~7]塞資料使用
    String Fullnm, nextFullnm; // 用戶名稱
    String PANTNUMOrgDeg = null; // 供記錄原先度數
    String CVOrgDeg = null; // 供記錄原先度數
    String OrgRemark = null; // 供記錄原先備註
    String id, nextid; // 用戶id,下一個用戶id
    String nextCusno, nextText; // 下一個用戶資料
    CharSequence[] CustDataList = null;
    CharSequence[] CustDataNo = null;
    public Boolean Find1 = false; // 辨別 表號查詢
    public Boolean Find2 = false;
    Boolean Nonext = false;
    boolean Remark = false; // 是否有備註
    boolean note = false; // 備註
    List<String> lunch; // ListDialog塞入親見備註資料
    Toast toast = null; //提醒視窗
    // -----------------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page3);
        // 先取得系統鍵盤服務
        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        FindView();
        BuildListener();
        InitData();
        GetData();
        init();
        Page = "page3";
    }

    private void init() {
        // TODO Auto-generated method stub
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVol = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC); // sp= new SoundPool(10, AudioManager.STREAM_SYSTEM,1);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0); // 音量鍵調節音效音量!
        MusicButton = sp.load(this, R.raw.button, 1); // 把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
        MusicBackspace = sp.load(this, R.raw.backspace, 1);
        MusicButton1 = sp.load(this, R.raw.button1, 1);
        MusicEnter = sp.load(this, R.raw.enter, 1);
        Music0 = sp.load(this, R.raw.number0, 1);
        Music1 = sp.load(this, R.raw.number1, 1);
        Music2 = sp.load(this, R.raw.number2, 1);
        Music3 = sp.load(this, R.raw.number3, 1);
        Music4 = sp.load(this, R.raw.number4, 1);
        Music5 = sp.load(this, R.raw.number5, 1);
        Music6 = sp.load(this, R.raw.number6, 1);
        Music7 = sp.load(this, R.raw.number7, 1);
        Music8 = sp.load(this, R.raw.number8, 1);
        Music9 = sp.load(this, R.raw.number9, 1);
    }

    private void VoiceSwitch(int arg1) { // 檢查開關
        if ("2".equals(MusicVoice)) {
            sp.play(arg1, 1, 1, 0, 0, 1);
        }
    }

    private void FindView() {
        USECB = (CheckBox) findViewById(R.id.checkOkBox);
        SB = (Button) findViewById(R.id.SureButton);
        PPB = (Button) findViewById(R.id.PreviousPageButton);
        up = (Button) findViewById(R.id.Up);
        next = (Button) findViewById(R.id.Next);
        N1 = (Button) findViewById(R.id.N1);
        N2 = (Button) findViewById(R.id.N2);
        N3 = (Button) findViewById(R.id.N3);
        N4 = (Button) findViewById(R.id.N4);
        N5 = (Button) findViewById(R.id.N5);
        N6 = (Button) findViewById(R.id.N6);
        N7 = (Button) findViewById(R.id.N7);
        N8 = (Button) findViewById(R.id.N8);
        N9 = (Button) findViewById(R.id.N9);
        N0 = (Button) findViewById(R.id.N0);
        ND = (Button) findViewById(R.id.Nd);
        Np = (Button) findViewById(R.id.Np);
        listDialog = (Button) findViewById(R.id.list_dialog);
        listDialog.setOnClickListener(new View.OnClickListener() { //ListDialog按鈕事件
            @Override
            public void onClick(View view) {
                listDialog();
            }
        });
        userid = (TextView) findViewById(R.id.userid);
        username = (TextView) findViewById(R.id.username);
        address = (TextView) findViewById(R.id.address);
        phonenumber = (TextView) findViewById(R.id.phonenumber);
        poset = (TextView) findViewById(R.id.poset);
        lamet = (TextView) findViewById(R.id.lamet);
        lampt = (TextView) findViewById(R.id.lampt);
        fccet = (TextView) findViewById(R.id.fccet);
        type = (TextView) findViewById(R.id.type);
        test = (TextView) findViewById(R.id.test);
        PANTNUMtext = (TextView) findViewById(R.id.PANTNUM_TextView);
        PANTNUM_CET = (EditText) findViewById(R.id.PANTNUM_EditText);
    }

    private void BuildListener() {
        SB.setOnClickListener(this);
        PPB.setOnClickListener(this);
        USECB.setOnClickListener(this);
        up.setOnClickListener(this);
        next.setOnClickListener(this);
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
        ND.setOnClickListener(this);
        Np.setOnClickListener(this);
        PANTNUM_CET.setOnFocusChangeListener(this);
    }

    private void InitData() {   //接收資料
        Intent GetIntent = getIntent();
        Bundle GetBundle = GetIntent.getExtras();
        SendData[0] = GetBundle.getString("D1");
        SendData[1] = GetBundle.getString("D2");
        SendData[2] = GetBundle.getString("D3");
        SendData[3] = GetBundle.getString("D4");
        SendData[4] = GetBundle.getString("D5");
        SendData[5] = GetBundle.getString("D6");
        Page = GetBundle.getString("Page_");
        Fullnm = GetBundle.getString("D8");
        DataWatnum = GetBundle.getString("D9");
        MusicVoice = GetBundle.getString("M1");
        id = GetBundle.getString("id");
        change = Integer.valueOf(GetBundle.getString("change"));
    }

    private void GetData() { //取得相關資料
        MyDBConnection DBConnection = null;
        SQLiteDatabase DB = null;
        Cursor Cursor = null;
        String RemarkTable = "select 'ya' from "
                + (String) getText(R.string.TableName2)
                + " where (trim(Remark)<>'' or trim(Remark1)<>'' or trim(Remark2)<>'') and Custno='"
                + SendData[3] + "' ";
        String DetailTable = // 定義資料庫連線字串
                "select " + (String) getText(R.string.TableName2) + ".CustLoc1,"
                        + (String) getText(R.string.TableName2) + ".Tel1,"
                        + (String) getText(R.string.TableName2) + ".Tel2,"
                        + (String) getText(R.string.TableName2) + ".Lamploc,"
                        + (String) getText(R.string.TableName2) + ".PlantNm,"
                        + (String) getText(R.string.TableName2) + ".Watnum,"
                        + (String) getText(R.string.TableName2) + ".LatPantnum,"
                        + (String) getText(R.string.TableName2) + ".LatPantnum2,"
                        + (String) getText(R.string.TableName2) + ".Pantnum,"
                        + (String) getText(R.string.TableName2) + ".Remark,"
                        + (String) getText(R.string.TableName2) + ".MPRE,"
                        + (String) getText(R.string.TableName2) + ".PDADT,"
                        + (String) getText(R.string.TableName2) + ".PRESCOEF,"
                        + (String) getText(R.string.TableName2) + ".Yel_Remark "
                        + "from "
                        + (String) getText(R.string.TableName2) + ","
                        + (String) getText(R.string.TableName1) + " " + "where "
                        + (String) getText(R.string.TableName2) + ".RoadNo="
                        + (String) getText(R.string.TableName1) + ".RoadNo " + "and "
                        + (String) getText(R.string.TableName2) + ".Custno='"
                        + SendData[3] + "' " + " group by " + (String) getText(R.string.TableName2) + ".Custno" + " order by " + (String) getText(R.string.TableName2) + ".Seq";
        String WatnumTable = // 查詢表號用
                "select " + (String) getText(R.string.TableName2) + ".CustLoc1,"
                        + (String) getText(R.string.TableName2) + ".Tel1,"
                        + (String) getText(R.string.TableName2) + ".Tel2,"
                        + (String) getText(R.string.TableName2) + ".Lamploc,"
                        + (String) getText(R.string.TableName2) + ".PlantNm,"
                        + (String) getText(R.string.TableName2) + ".Watnum,"
                        + (String) getText(R.string.TableName2) + ".LatPantnum,"
                        + (String) getText(R.string.TableName2) + ".LatPantnum2,"
                        + (String) getText(R.string.TableName2) + ".Pantnum,"
                        + (String) getText(R.string.TableName2) + ".Remark,"
                        + (String) getText(R.string.TableName2) + ".MPRE,"
                        + (String) getText(R.string.TableName2) + ".PDADT,"
                        + (String) getText(R.string.TableName2) + ".PRESCOEF,"
                        + (String) getText(R.string.TableName2) + ".Yel_Remark "
                        + "from "
                        + (String) getText(R.string.TableName2) + ","
                        + (String) getText(R.string.TableName1) + " " +
                        "where " + (String) getText(R.string.TableName2) + ".RoadNo = " + "'" + SendData[2] + "'"
                        + "and TRIM(" + (String) getText(R.string.TableName2)
                        + ".Watnum)='" + DataWatnum + "' ";
        /*if (Find1==true) {
			int Dco=0;
			String FindTable =                        //定義資料庫連線字串
					"select Result.Fullnm," + (String) getText(R.string.TableName2) + ".CustLoc," +
							(String) getText(R.string.TableName2) + ".Watnum," +
							(String) getText(R.string.TableName2) + ".Custno," +
							(String) getText(R.string.TableName1) + ".RoadNm " +
							",Result._id from " + (String) getText(R.string.TableName2) + "," +
							(String) getText(R.string.TableName1) + " " +
							"where "+(String)getText(R.string.TableName2)+".RoadNo = "+ "'"+SendData[2]+"'"+
							" and TRIM(" + (String) getText(R.string.TableName2) + ".Watnum) = " + "'" + DataWatnum + "'";
			String CountTable;

			CountTable =                        //定義資料庫連線字串
					"select count(*) from " +
							(String) getText(R.string.TableName2) + "," +
							(String) getText(R.string.TableName1) + " " +
							"where "+(String)getText(R.string.TableName2)+".RoadNo = "+ "'"+SendData[2]+"'"+
							" and TRIM(" + (String) getText(R.string.TableName2) + ".Watnum) = " + "'" + DataWatnum + "'";
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
					if (DataWatnum.length()==0){
						openDialog((String) getText(R.string.DialogMessage8));
						Find2=false;
					}else{
						openDialog((String) getText(R.string.DialogMessage7));
						Find2=false;
					}

				} else{
					Find2=true;
					Cursor=DB.rawQuery(FindTable, null);
					while (Cursor.moveToNext()){
						Fullnm=Cursor.getString(0);
						SendData[3]=Cursor.getString(3);
					}
				}
				Cursor.close();
				DB.close();
				DBConnection.close();
			} catch (SQLException e){
				Cursor.close();
				DB.close();
				DBConnection.close();
				Toast.makeText(Page3.this,e.getMessage(),Toast.LENGTH_LONG).show();
			}
		}
*/
        // ---------------------------------------------------------------
        try {
            DBConnection = new MyDBConnection(this, SendData[5], null, 1);
            // DB=DBConnection.getWritableDatabase(); //儲存空間不足時會有錯誤
            DB = DBConnection.getReadableDatabase();

            if (DataWatnum == null) { // 辨別表號查詢和點選查詢
                Cursor = DB.rawQuery(DetailTable, null);
            } else {
                Cursor = DB.rawQuery(WatnumTable, null);
            }

            while (Cursor.moveToNext()) { //DetailTable
                UseRemark = Cursor.getString(9).trim();//取得是否使用中

                if (!"".equals(Cursor.getString(1))//用戶資料備註
                        && !"".equals(Cursor.getString(2))) {
                    userid.setText(SendData[3].toString());
                    username.setText(Fullnm.trim().toString());
                    address.setText(Cursor.getString(0).trim().toString());
                    phonenumber.setText(Cursor.getString(1).trim().toString());
                    poset.setText(Cursor.getString(3).trim().toString());
                    lamet.setText(Cursor.getString(5).trim().toString());
                    lampt.setText(Cursor.getString(10).trim().toString());
                    fccet.setText(Cursor.getString(12).trim().toString());
                    type.setText(Cursor.getString(4).trim().toString());
                    //test.setText(id);
                } else if (!"".equals(Cursor.getString(1))) {
                    userid.setText(SendData[3].toString());
                    username.setText(Fullnm.trim().toString());
                    address.setText(Cursor.getString(0).trim().toString());
                    phonenumber.setText(Cursor.getString(1).trim().toString());
                    poset.setText(Cursor.getString(3).trim().toString());
                    lamet.setText(Cursor.getString(5).trim().toString());
                    lampt.setText(Cursor.getString(10).trim().toString());
                    fccet.setText(Cursor.getString(12).trim().toString());
                    type.setText(Cursor.getString(4).trim().toString());
                    //test.setText(id);
                } else if (!"".equals(Cursor.getString(2))) {
                    userid.setText(SendData[3].toString());
                    username.setText(Fullnm.trim().toString());
                    address.setText(Cursor.getString(0).trim().toString());
                    phonenumber.setText(Cursor.getString(1).trim().toString());
                    poset.setText(Cursor.getString(3).trim().toString());
                    lamet.setText(Cursor.getString(5).trim().toString());
                    lampt.setText(Cursor.getString(10).trim().toString());
                    fccet.setText(Cursor.getString(12).trim().toString());
                    type.setText(Cursor.getString(4).trim().toString());
                    //test.setText(id);
                }

                //顯示已抄數值在EditText上
                if (!"".equals(Cursor.getString(8).trim())) { //避免機械表度數空值錯誤
                    PANTNUM_CET.setText(Cursor.getString(8).trim());
                    //PRCET.setText(Cursor.getString(15).trim());
					/*openDialog((String) Cursor.getString(11).trim() + " "
							+ getText(R.string.CopyedData) + "!!");*/
                }

                int lastindex = ((String) Cursor.getString(6).trim()).indexOf("("); //抓取符號為 "(" 的索引值
                Data[8] = ((String) Cursor.getString(6).trim()).substring(0, lastindex); //只抓符號為 "(" 前面的值,上期表度數

                PANTNUMtext.setText(Data[8]); //上期表度數
                PANTNUM_CET.requestFocus(); //聚焦指針度數輸入欄位
                //mInputMethodManager.showSoftInput(PANTNUM_CET, 0);//自動彈出鍵盤
                PANTNUMOrgDeg = (String) Cursor.getString(8).trim();
                CVOrgDeg = (String) Cursor.getString(8).trim();
                OrgRemark = (String) Cursor.getString(9).trim();

                //用戶相關掛錶資料
                Data[0] = getResources().getString(R.string.LampPostion) + Cursor.getString(3).trim();        //表位置
                Data[1] = Cursor.getString(5).trim();                     //表／番號
                Data[2] = getResources().getString(R.string.LampMpre) + Cursor.getString(10).trim();     //供氣壓力
                Data[3] = getResources().getString(R.string.LampPrescoef) + Cursor.getString(12).trim();     //壓力係數
                Data[4] = getResources().getString(R.string.LampType) + Cursor.getString(4).trim();        //燈表別

                //sCustomDialog上期&上上期
                Data[5] = Cursor.getString(6).trim();     //上期度數
                Data[6] = Cursor.getString(7).trim();     //上上期度數
                Data[7] = Cursor.getString(12).trim();    //壓力係數
                Data[10] = Cursor.getString(13).trim();	 //親見備註

                //塞入ArrayList陣列,後續給ListDialog使用
                lunch = new ArrayList<String>();
                lunch.add(Data[10]);

                if (!"".equals(Data[10])) {
                    listDialog.setVisibility(View.VISIBLE);
                }else{
                    listDialog.setVisibility(View.INVISIBLE);
                }

            }
            int usecbindex1 = UseRemark.indexOf("●(use)");
            if (!"".equals(UseRemark)) {
                if (usecbindex1 >= 0) {
                    USECB.setChecked(true);
                }
            }else{
                USECB.setChecked(false);
            }
            Cursor.close();
            DB.close();
            DBConnection.close();
        } catch (SQLException e) {
            Cursor.close();
            DB.close();
            DBConnection.close();
            Toast.makeText(Page3.this, e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
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
                            }
                        }
                )
                .show();
    }

    private void StartSend(boolean agr1, boolean arg2) {
        Intent SendIntent = new Intent();
        if (agr1 == true) {
            // SendData[3]="";
            if (arg2 == true) {
                SendIntent.setClass(this, Page2.class);
            } else {
                if (!"".equals(SendData[2])) {
                    if (note == true) {
                        SendIntent.setClass(this, Page5.class);
                    } else if (note == false) {
                        SendIntent.setClass(this, Page2.class);
                    }
                } else {
                    if (SendData[1].substring(0, 1).equals(
                            getText(R.string.BusinessCust))
                            && SendData[1].substring(0, 1).equals(
                            getText(R.string.BusinessCust))) {
                        SendIntent.setClass(this, Page2.class);
                    } else {
                        SendIntent.setClass(this, Page4.class);
                    }
                }
            }
            Bundle SendBundle = new Bundle();
            SendBundle.putString("D1", SendData[0]);
            SendBundle.putString("D2", SendData[1]);
            SendBundle.putString("D3", SendData[2]);
            SendBundle.putString("D4", SendData[3]);
            SendBundle.putString("D5", SendData[4]);
            SendBundle.putString("D6", SendData[5]);
            SendBundle.putString("D8", Fullnm);
            SendBundle.putString("D9", DataWatnum);
            SendBundle.putString("M1", MusicVoice);
            SendBundle.putString("Page_", Page);
            SendBundle.putString("change", String.valueOf(change));
            //SendBundle.putString("id", id);
            SendIntent.putExtras(SendBundle);
            startActivity(SendIntent);
            finish();
        } else {
            if (arg2 == true) {
                SendIntent.setClass(this, Mainloading.class);
                Bundle SendBundle = new Bundle();
                SendBundle.putString("D1", SendData[0]);
                SendBundle.putString("D2", SendData[1]);
                SendBundle.putString("D3", SendData[2]);
                SendBundle.putString("D4", SendData[3]);
                SendBundle.putString("D5", SendData[4]);
                SendBundle.putString("D6", SendData[5]);
                SendBundle.putString("D8", Fullnm);
                SendBundle.putString("D9", DataWatnum);
                SendBundle.putString("M1", MusicVoice);
                SendBundle.putString("Page_", Page);
                SendIntent.putExtras(SendBundle);
                startActivity(SendIntent);
                finish();
            } else {
                //openDialog((String) getText(R.string.DialogMessage1));
            }
        }
    }

    public void SelectNextCusno() {
        MyDBConnection DBConnection = null;
        SQLiteDatabase DB = null;
        Cursor Cursor = null;
        String FindTable =                        //定義資料庫連線字串
                "select " + (String) getText(R.string.TableName2) + ".Fullnm,"
                        + (String) getText(R.string.TableName2) + ".Watnum,"
                        + (String) getText(R.string.TableName2) + ".CustLoc,"
                        + (String) getText(R.string.TableName2) + ".Custno,"
                        + (String) getText(R.string.TableName1) + ".RoadNm,"
                        + (String) getText(R.string.TableName2) + "._id,"
                        + (String) getText(R.string.TableName2) + ".Pantnum "
                        + "from "
                        + (String) getText(R.string.TableName2) + ","
                        + (String) getText(R.string.TableName1) + " " +
                        "where " + (String) getText(R.string.TableName2) + ".RoadNo = " + "'" + SendData[2] + "'";


        if (((String) getText(R.string.CopyedData)).equals(SendData[0])) {    // 已抄、未抄、全部 的查詢條件
            FindTable =
                    FindTable + "and " + (String) getText(R.string.ResultFieldCopyedMk) +
                            "='" + (String) getText(R.string.CopyedMk) + "' ";
        } else if (((String) getText(R.string.NonCopyedData)).equals(SendData[0])) {
            FindTable =
                    FindTable + "and " + (String) getText(R.string.ResultFieldCopyedMk) +
                            "<>'" + (String) getText(R.string.CopyedMk) + "' ";
        }
        FindTable = FindTable + " group by " + (String) getText(R.string.TableName2) + ".Custno";
        FindTable = FindTable + " order by " + (String) getText(R.string.TableName2) + ".Seq";

        try {
            DBConnection = new MyDBConnection(this, SendData[5], null, 1);
            //DB=DBConnection.getWritableDatabase();	//儲存空間不足時會有錯誤
            DB = DBConnection.getReadableDatabase();
            Cursor = DB.rawQuery(FindTable, null);

            Cursor.moveToNext();
            while (!id.equals(Cursor.getString(5))) {
                Cursor.moveToNext();
            }
            if (Cursor.moveToNext()) {
                nextid = Cursor.getString(5);
                nextFullnm = Cursor.getString(0);
                nextCusno = Cursor.getString(3);
                nextText = Cursor.getString(6).trim();
                Cursor.moveToPrevious();
            } else {
                Nonext = true;
            }
            Cursor.close();
            DB.close();
            DBConnection.close();
        } catch (SQLException e) {
            Cursor.close();
            DB.close();
            DBConnection.close();
            Toast.makeText(Page3.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public String getDateTime() {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String strDate = sdFormat.format(date);
        // System.out.println(strDate);
        return strDate;
    }

    private void UpdateData() { //更新資料
        MyDBConnection DBConnection = new MyDBConnection(this, SendData[5],
                null, 1);
        SQLiteDatabase DB = DBConnection.getWritableDatabase();
        Cursor Cursor=null;
        String[] temp = new String[2];
        SelectNextCusno();
        String Str2=null;

        try {
            String Str = "update " + (String) getText(R.string.TableName2)
                    + " set "
                    + "PDADT='" + getDateTime() + "',"
                    + "pantnum='" + PANTNUM_CET.getText().toString() + "',"
                    //+ "RTUPR='" + PRCET.getText().toString() + "',"
                    + "Remark='" + UseRemark + "',"
                    + "CopyRemark=" + "0" + " "
                    + "where Custno='" + SendData[3] + "' ";
            DB.execSQL(Str);

            if ("".equals(PANTNUM_CET.getText().toString())) {
                Str = "update " + (String) getText(R.string.TableName2)
                        + " set " + "CopyMk='"
                        + (String) getText(R.string.NonCopyedMk) + "',"
                        + "CopyRemark=" + "0" + " " +
                        //",Remark='"+UseRemark+"' "+
                        "where Custno='" + SendData[3] + "' ";
                DB.execSQL(Str);
            } else {
                Str = "update " + (String) getText(R.string.TableName2)
                        + " set " + "CopyMk='"
                        + (String) getText(R.string.CopyedMk) + "',"
                        + "CopyRemark=" + "0" + " " +
                        //",Remark='"+UseRemark+"' "+
                        "where Custno='" + SendData[3] + "' ";
                DB.execSQL(Str);

                final String QueryTable=  //找出Road的總數和已抄數
                        "select count(a."+(String)getText(R.string.ResultFieldRoadno)+") from "+(String)getText(R.string.TableName2)+" as a " +
                                "where a."+(String)getText(R.string.ResultFieldCopyedMk)+" = '"+(String)getText(R.string.CopyedMk)+"' " +
                                "and a."+(String)getText(R.string.ResultFieldRoadno)+" = '" + SendData[2] + "'";

                Cursor=DB.rawQuery(QueryTable, null);
                while (Cursor.moveToNext()){
                    Str2=
                        "update "+(String)getText(R.string.TableName1)+" "+
                                "set "+(String)getText(R.string.RoadFieldDco)+" = "
                                +String.valueOf(Cursor.getInt(0))+" "+
                                "where RoadNo='" + SendData[2] + "'";
                    DB.execSQL(Str2);
                }
                Cursor.close();

            }
            //DB.execSQL(Str);
            DB.close();
            DBConnection.close();
            if (Nonext == true) {
                StartSend(true, false);
            } else {
                id = nextid;
                Fullnm = nextFullnm;
                SendData[3] = nextCusno;
                PANTNUM_CET.setText(nextText);
                GetData();
            }
        } catch (SQLException e) {
            DB.close();
            DBConnection.close();
            Toast.makeText(Page3.this, e.getMessage(), Toast.LENGTH_LONG)
                    .show();
            StartSend(false, false);
        }
    }

    private void UpdateData2() {
        MyDBConnection DBConnection = new MyDBConnection(this, SendData[5],
                null, 1);
        SQLiteDatabase DB = DBConnection.getWritableDatabase();
        String[] temp = new String[2];
        SelectNextCusno();

        try {
            String Str = "update " + (String) getText(R.string.TableName2)
                    + " set "
                    + "CopyRemark='1' " //記錄為未抄表用戶
                    + "where Custno='" + SendData[3] + "' ";
            DB.execSQL(Str);
            DB.close();
            DBConnection.close();
            if (Nonext == true) {
                StartSend(true, false);
            } else {
                id = nextid;
                Fullnm = nextFullnm;
                SendData[3] = nextCusno;
                PANTNUM_CET.setText(nextText);
                GetData();
            }
        } catch (SQLException e) {
            DB.close();
            DBConnection.close();
            Toast.makeText(Page3.this, e.getMessage(), Toast.LENGTH_LONG)
                    .show();
            StartSend(false, false);
        }
    }

    private void FillData(Integer arg) {
        selkeydown(arg,PANTNUM_CET);
    }

    private void selkeydown(Integer arg,Object arg1) { //按鈕資料鍵入EditText
        if (((EditText) arg1).findFocus() != null) {
            switch (arg) {
                case R.id.Nd:
                    if (((EditText) arg1).length() != 0) {
                        ((EditText) arg1).setText(String.valueOf(((EditText) arg1).getText()).substring(0,
                                ((EditText) arg1).length() - 1));
                    }
                    break;
                case R.id.N1:
                    ((EditText) arg1).setText(((EditText) arg1).getText() + "1");
                    break;
                case R.id.N2:
                    ((EditText) arg1).setText(((EditText) arg1).getText() + "2");
                    break;
                case R.id.N3:
                    ((EditText) arg1).setText(((EditText) arg1).getText() + "3");
                    break;
                case R.id.N4:
                    ((EditText) arg1).setText(((EditText) arg1).getText() + "4");
                    break;
                case R.id.N5:
                    ((EditText) arg1).setText(((EditText) arg1).getText() + "5");
                    break;
                case R.id.N6:
                    ((EditText) arg1).setText(((EditText) arg1).getText() + "6");
                    break;
                case R.id.N7:
                    ((EditText) arg1).setText(((EditText) arg1).getText() + "7");
                    break;
                case R.id.N8:
                    ((EditText) arg1).setText(((EditText) arg1).getText() + "8");
                    break;
                case R.id.N9:
                    ((EditText) arg1).setText(((EditText) arg1).getText() + "9");
                    break;
                case R.id.N0:
                    ((EditText) arg1).setText(((EditText) arg1).getText() + "0");
                    break;
                case R.id.Np:
                    if(!"".equals(((EditText) arg1).getText().toString().trim())){
                        if(StrP(((EditText) arg1).getText().toString())){
                            ((EditText) arg1).setText(((EditText) arg1).getText() + ".");
                        }
                    }
                    break;
            }
        }
    }

    public boolean StrP(String arg) {  //判斷是否有小數點
        if (arg.indexOf(".") != -1) {
            return false;
        }
        return true;
    }

    public void Move(Boolean Next) {
        MyDBConnection DBConnection = null;
        SQLiteDatabase DB = null;
        Cursor Cursor = null;
        String FindTable =                        //定義資料庫連線字串
                "select " + (String) getText(R.string.TableName2) + ".Fullnm,"
                        + (String) getText(R.string.TableName2) + ".Watnum,"
                        + (String) getText(R.string.TableName2) + ".CustLoc,"
                        + (String) getText(R.string.TableName2) + ".Custno,"
                        + (String) getText(R.string.TableName1) + ".RoadNm,"
                        + (String) getText(R.string.TableName2) + "._id,"
                        + (String) getText(R.string.TableName2) + ".Pantnum "
                        + "from "
                        + (String) getText(R.string.TableName2) + ","
                        + (String) getText(R.string.TableName1) + " " +
                        "where " + (String) getText(R.string.TableName2) + ".RoadNo = " + "'" + SendData[2] + "'";


        if (((String) getText(R.string.CopyedData)).equals(SendData[0])) {    // 已抄、未抄、全部 的查詢條件
            FindTable =
                    FindTable + "and " + (String) getText(R.string.ResultFieldCopyedMk) +
                            "='" + (String) getText(R.string.CopyedMk) + "' ";
        } else if (((String) getText(R.string.NonCopyedData)).equals(SendData[0])) {
            FindTable =
                    FindTable + "and " + (String) getText(R.string.ResultFieldCopyedMk) +
                            "<>'" + (String) getText(R.string.CopyedMk) + "' ";
        }
        FindTable = FindTable + " group by " + (String) getText(R.string.TableName2) + ".Custno";
        FindTable = FindTable + " order by " + (String) getText(R.string.TableName2) + ".Seq";

        try {
            DBConnection = new MyDBConnection(this, SendData[5], null, 1);
            //DB=DBConnection.getWritableDatabase();	//儲存空間不足時會有錯誤
            DB = DBConnection.getReadableDatabase();
            Cursor = DB.rawQuery(FindTable, null);

            Cursor.moveToNext();
            while (!id.equals(Cursor.getString(5))) {
                Cursor.moveToNext();
            }
            if (Next == true) {
                if (Cursor.moveToNext()) {
                    id = Cursor.getString(5);
                    Fullnm = Cursor.getString(0);
                    SendData[3] = Cursor.getString(3);
                    PANTNUM_CET.setText(Cursor.getString(6).trim());
                }
            } else {
                if (Cursor.moveToPrevious()) {
                    id = Cursor.getString(5);
                    Fullnm = Cursor.getString(0);
                    SendData[3] = Cursor.getString(3);
                    PANTNUM_CET.setText(Cursor.getString(6).trim());
                }
            }
            Cursor.close();
            DB.close();
            DBConnection.close();
        } catch (SQLException e) {
            Cursor.close();
            DB.close();
            DBConnection.close();
            Toast.makeText(Page3.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void onClick(View v) {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        long[] pattern1 = {0, 40}; // OFF/ON/OFF/ON...
        vibrator.vibrate(pattern1, -1);

        if (v.equals(SB) || v.equals(PPB)) {
            VoiceSwitch(MusicButton);
        } else if (v.equals(N1) || v.equals(N2) || v.equals(N3) || v.equals(N4)
                || v.equals(N5) || v.equals(N6) || v.equals(N7) || v.equals(N8)
                || v.equals(N9) || v.equals(N0) || v.equals(ND) || v.equals(Np)) {
            if (v.equals(N1)) {
                VoiceSwitch(Music1);
            } else if (v.equals(N2)) {
                VoiceSwitch(Music2);
            } else if (v.equals(N3)) {
                VoiceSwitch(Music3);
            } else if (v.equals(N4)) {
                VoiceSwitch(Music4);
            } else if (v.equals(N5)) {
                VoiceSwitch(Music5);
            } else if (v.equals(N6)) {
                VoiceSwitch(Music6);
            } else if (v.equals(N7)) {
                VoiceSwitch(Music7);
            } else if (v.equals(N8)) {
                VoiceSwitch(Music8);
            } else if (v.equals(N9)) {
                VoiceSwitch(Music9);
            } else if (v.equals(N0)) {
                VoiceSwitch(Music0);
            } else if (v.equals(ND)) {
                VoiceSwitch(MusicBackspace);
            } else if (v.equals(Np)) {
                VoiceSwitch(MusicEnter);
            }
        }
        if (v.equals(next)) { //下一筆紐
            Move(true);
            GetData();
        }
        if (v.equals(up)) { //上一筆紐
            Move(false);
            GetData();
        }
        if (v.equals(PPB)) { //上一頁紐
            StartSend(true, false);
        }
        if (       v.equals(N1) || v.equals(N2) || v.equals(N3) || v.equals(N4)
                || v.equals(N5) || v.equals(N6) || v.equals(N7) || v.equals(N8)
                || v.equals(N9) || v.equals(N0) || v.equals(ND) || v.equals(Np)) {
            FillData(v.getId());
        }
        if (v.equals(SB)) { //確認存檔紐
            if ("".equals(PANTNUM_CET.getText().toString().trim())) { //判別度數是否有輸入值
                showCustomDialog_Noncopy();
            } else if ("0".equals(PANTNUM_CET.getText().toString().trim())) {
                showCustomDialog_o();
            } else {
                double TrueData7 = Double.valueOf(Data[7]).doubleValue(); //轉成DOUBLE型態 ,壓力係數
                double ThisPANTNUM_CET = Double.valueOf(PANTNUM_CET.getText().toString()).doubleValue();//轉成DOUBLE型態  ,本期表度數
                int lastindex = (Data[5].toString().trim()).indexOf("(");
                double lastPANTNUM_CET = Double.valueOf((Data[5].toString().trim()).substring(0, lastindex)).doubleValue(); //轉成DOUBLE型態  ,只抓符號為 "(" 前面的值,上期表度度
                int lastindex2 = (Data[6].toString().trim()).indexOf("(");//抓取符號為 "(" 的索引值
                double lastPANTNUM_CET2 = Double.valueOf((Data[6].toString().trim()).substring(0, lastindex2)).doubleValue(); //轉成DOUBLE型態  ,只抓符號為 "(" 前面的值,上上期表度數
                double ThisTruePANTNUM_CET = (ThisPANTNUM_CET - lastPANTNUM_CET) * (TrueData7);//本期實用度數
                double RlRec = ThisPANTNUM_CET - lastPANTNUM_CET;//本期使用量
                if(RlRec >= 70 || ThisPANTNUM_CET <= lastPANTNUM_CET){
                    showCustomDialog();
                }else{
                    VoiceSwitch(MusicButton);//音效
                    if (USECB.isChecked()) {
                        UseRemark = UseRemark + "●(use)";
                    } else {
                        int usecbindex1 = UseRemark.indexOf("●(use)");
                        if (!"".equals(UseRemark)) {
                            if (usecbindex1 >= 0) {
                                UseRemark = UseRemark.replace("●(use)", "");  //消掉●(use)
                            }
                        }
                    }
                    if ("".equals(PANTNUM_CET.getText().toString())) {//本次抄表度數等於空白
                        if (!PANTNUMOrgDeg.equals(PANTNUM_CET.getText().toString())) {  //不等於上次抄表度數
                            UpdateData();
                        } else {
                            StartSend(false, false);
                        }
                    } else { //本次抄表度數不等於空白
                        UpdateData();
                    }
                }
            }
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case -1:
                finish();
                break;
            case -2:
                StartSend(false, true);
                break;
            case -3: // Nat_Button
                UpdateData();
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
		/*
		 * if (v.equals(REMET)){ if(v.isFocused()){
		 * mInputMethodManager.showSoftInput(v, 0); } else {
		 * mInputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0); }
		 * } else {
		 * mInputMethodManager.hideSoftInputFromWindow(REMET.getWindowToken(),
		 * 0); }
		 */
    }

    public void onTouchEvent(View V) {

    }

    private static Boolean isExit = false;
    private static Boolean hasTask = false;
    Timer tExit = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            isExit = false;
            hasTask = true;
        }
    };

    private File currentImageFile = null;
    //在按钮点击事件处写上这些东西，这些是在SD卡创建图片文件的:
    public void onCamera(View v) {

        ToCamera();
/*        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMdd");
        String date = sDateFormat.format(new java.util.Date());
        SimpleDateFormat sDateFormat2 = new SimpleDateFormat("yyyyMMddHHmmss");
        String date2 = sDateFormat2.format(new java.util.Date());
        File dir = new File(Environment.getExternalStorageDirectory(),"copypictures/" + date.substring(0,6));
        if(!dir.exists()){
            dir.mkdirs();
        }
        currentImageFile = new File(dir,  date2 + "-" + SendData[3].toString() + ".jpg");
        if(!currentImageFile.exists()){
            try {
                currentImageFile.createNewFile();

                if (currentImageFile.length() == 0){
                    currentImageFile.delete();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        it.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentImageFile));
        startActivityForResult(it, Activity.DEFAULT_KEYS_DIALER);*/
    }

    void ToCamera()
    {
        toast = Toast.makeText(this,"正在準備相機", Toast.LENGTH_LONG);

        Intent i = new Intent();
        i.setClass(Page3.this, getcamera.class);
        //i.putExtra("ordersn", order_sn);
        i.putExtra("D4", SendData[3]);
        if(toast != null)
            toast.show();
        startActivity(i);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            StartSend(true, false);
        }
        return false;
    }

    protected void showCustomDialog() { //已抄彈跳視窗

        final Dialog dialog = new Dialog(Page3.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.customdailog); //連結customdailog.XML
        final EditText editText1 = (EditText) dialog.findViewById(R.id.editText1); //上上期
        final EditText editText2 = (EditText) dialog.findViewById(R.id.editText2); //上期
        final EditText editText3 = (EditText) dialog.findViewById(R.id.editText3); //本期
        final EditText message = (EditText) dialog.findViewById(R.id.message); //訊息
        message.setEnabled(false);//關閉訊息EditText的小鍵盤，不能點選
        final EditText nomremark = (EditText) dialog.findViewById(R.id.nomremark); //輸入備註
        String showmessage = null;//訊息內容
        Button OKB = (Button) dialog.findViewById(R.id.okbutton);   //確認鍵
        Button CAB = (Button) dialog.findViewById(R.id.Cancelbutton);  //取消鍵

        editText1.setText(Data[6]); //上上期
        editText2.setText(Data[5]); //上期

        double TrueData7 = Double.valueOf(Data[7]).doubleValue(); //轉成DOUBLE型態 ,壓力係數
        double ThisPANTNUM_CET = Double.valueOf(PANTNUM_CET.getText().toString()).doubleValue();//轉成DOUBLE型態  ,本期表度數

        int lastindex = (editText2.getText().toString().trim()).indexOf("(");
        double lastPANTNUM_CET = Double.valueOf((editText2.getText().toString().trim()).substring(0, lastindex)).doubleValue(); //轉成DOUBLE型態  ,只抓符號為 "(" 前面的值,上期表度度

        int lastindex2 = (editText1.getText().toString().trim()).indexOf("(");//抓取符號為 "(" 的索引值
        double lastPANTNUM_CET2 = Double.valueOf((editText1.getText().toString().trim()).substring(0, lastindex2)).doubleValue(); //轉成DOUBLE型態  ,只抓符號為 "(" 前面的值,上上期表度數

        double ThisTruePANTNUM_CET = (ThisPANTNUM_CET - lastPANTNUM_CET) * (TrueData7);//本期實用度數
        editText3.setText(PANTNUM_CET.getText().toString().trim() + " 實用" + (long) ThisTruePANTNUM_CET + "度");

        double RlRec = ThisPANTNUM_CET - lastPANTNUM_CET;//本期使用量
        double PrevRlRec = lastPANTNUM_CET - lastPANTNUM_CET2;//上期使用量

        if (RlRec >= 70) {  //本期使用量超過70度以上
            showmessage = "(本期表度數(" + (long) ThisPANTNUM_CET + ") - 上期表度數(" + (long) lastPANTNUM_CET + ")) * 壓力係數(" + TrueData7 + ") = 本期實用度(" + (long) ThisTruePANTNUM_CET + ")" + "\n本期使用量超過70度以上,請注意!!";
            message.setTextColor(Color.rgb(255, 0, 0)); //訊息改成紅色字體
        } else {
            showmessage = "(本期表度數(" + (long) ThisPANTNUM_CET + ") - 上期表度數(" + (long) lastPANTNUM_CET + ")) * 壓力係數(" + TrueData7 + ") = 本期實用度(" + (long) ThisTruePANTNUM_CET + ")";
        }

        message.setText(showmessage); //message視窗解說算式詳細內容
        String PDC = null;
        Pattern pattern = Pattern.compile("[0-9]");
        Matcher matcher = pattern.matcher(PANTNUM_CET.getText().toString());
        if (matcher.find()) {
            int Po = editText2.getText().toString().indexOf("(");
            if (Po == -1) {
                PDC = editText2.getText().toString();
            } else {
                PDC = editText2.getText().toString().substring(0, Po)
                        .toString();
            }
            Matcher matcher1 = pattern.matcher(PDC);
            if (!matcher1.find()) {
                message.setTextColor(Color.rgb(255, 0, 0)); //訊息改成紅色字體
                message.setText(getText(R.string.DialogMessage4)); //前期度數為:推定/新掛/新換/n您確定本次度數是正確的嗎？
            } else {
                if (Integer.valueOf(PANTNUM_CET.getText().toString()) <= Integer.valueOf(PDC)) { //本次度數低於前期度數
                    message.setTextColor(Color.rgb(255, 0, 0)); //訊息改成紅色字體
                    message.setText(getText(R.string.DialogMessage3));
                }
            }
        } else {
            StartSend(false, false); //關閉
        }

        OKB.setOnClickListener(new View.OnClickListener() { //確認紐
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                VoiceSwitch(MusicButton);//音效
                if (USECB.isChecked()) {
                    UseRemark = UseRemark + "●(use)";
                } else {
                    int usecbindex1 = UseRemark.indexOf("●(use)");
                    if (!"".equals(UseRemark)) {
                        if (usecbindex1 >= 0) {
                            UseRemark = UseRemark.replace("●(use)", "");  //消掉●(use)
                        }
                    }
                }
                if ("".equals(PANTNUM_CET.getText().toString())) {//本次抄表度數等於空白
                    if (!PANTNUMOrgDeg.equals(PANTNUM_CET.getText().toString())) {  //不等於上次抄表度數
                        UpdateData();
                    } else {
                        StartSend(false, false);
                    }
                } else { //本次抄表度數不等於空白
                    UseRemark =  nomremark.getText().toString();
                    UpdateData();
                }
                dialog.dismiss();
            }
        });

        CAB.setOnClickListener(new View.OnClickListener() {   //取消鈕
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                VoiceSwitch(MusicButton); //音效
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    protected void showCustomDialog_Noncopy() { //未抄的彈跳視窗
        final Dialog dialog = new Dialog(Page3.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.customdailog2); //連結customdailog2.XML
        final EditText message = (EditText) dialog.findViewById(R.id.message); //訊息
        message.setEnabled(false);//關閉訊息EditText的小鍵盤，不能點選

        Button OKB = (Button) dialog.findViewById(R.id.okbutton);   //確認鍵
        Button CAB = (Button) dialog.findViewById(R.id.Cancelbutton);  //取消鍵

        String showmessage = (String) getText(R.string.DialogMessage15);//訊息內容
        message.setText(showmessage);//message內容
        message.setTextColor(Color.rgb(255, 0, 0)); //訊息改成紅色字體

        OKB.setOnClickListener(new View.OnClickListener() { //確認紐
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                VoiceSwitch(MusicButton);//音效
                UpdateData2();
                dialog.dismiss();
            }
        });

        CAB.setOnClickListener(new View.OnClickListener() {   //取消鈕
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                VoiceSwitch(MusicButton); //音效
                dialog.dismiss();
            }
        });
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.show();
    }

    protected void showCustomDialog_o() { //度數0的彈跳視窗
        final Dialog dialog = new Dialog(Page3.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.customdailog3); //連結customdailog3.XML
        final EditText message = (EditText) dialog.findViewById(R.id.message); //訊息
        message.setEnabled(false);//關閉訊息EditText的小鍵盤，不能點選
        final EditText nomremark = (EditText) dialog.findViewById(R.id.nomremark); //輸入備註
        final Spinner nomspinner = (Spinner) dialog.findViewById(R.id.nomspinner); //異常選項
        Button OKB = (Button) dialog.findViewById(R.id.okbutton);   //確認鍵
        Button CAB = (Button) dialog.findViewById(R.id.Cancelbutton);  //取消鍵
        String showmessage = (String) getText(R.string.DialogMessage16);//訊息內容
        message.setText(showmessage);//message內容
        message.setTextColor(Color.rgb(255, 0, 0)); //訊息改成紅色字體

        OKB.setOnClickListener(new View.OnClickListener() { //確認紐
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                VoiceSwitch(MusicButton);//音效
                UseRemark = (String) nomspinner.getSelectedItem()  + nomremark.getText().toString();
                UpdateData();
                dialog.dismiss();
            }
        });

        CAB.setOnClickListener(new View.OnClickListener() {   //取消鈕
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                VoiceSwitch(MusicButton); //音效
                dialog.dismiss();
            }
        });
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.show();
    }
    @SuppressLint("NewApi")
    private void listDialog(){ //listDialog呈現方法
        new AlertDialog.Builder(Page3.this,R.style.AlertDialogTheme)
                .setTitle("親見備註")
                .setItems(lunch.toArray(new String[lunch.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = lunch.get(which);
                        Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show(); //點下去跑出Toast顯示資料
                    }
                })
                .show();
    }
}