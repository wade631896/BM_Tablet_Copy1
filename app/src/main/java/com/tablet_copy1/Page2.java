package com.tablet_copy1;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
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

public class Page2 extends Activity implements OnClickListener, OnItemClickListener, android.content.DialogInterface.OnClickListener {

    String DataTitle = null;                      //定義資訊欄標頭
    ImageButton EB = null;                        //結束鈕
    ImageButton PPB = null;                       //上一頁鈕
    ImageButton NPB = null;                       //下一頁鈕
    TextView TTV = null;                          //相關資料欄
    ListView DLV = null;                          //用戶分佈資料欄
    ImageButton FID = null;
    EditText FIV = null;
    String DataWatnum = null;                     //儲存表號資料
    SoundPool sp; //声明一个SoundPool
    int MusicButton; //按鈕音
    int SelectNum;
    int change=0;
    private AudioManager am;
    private int maxVol;
    public String MusicVoice = "2"; //音效開關   2=開  1=關
    public View view1 = null;
    CharSequence[] CustDataList = null;               //存放Grid資料
    CharSequence[] CustDataNo = null;                //存放用編資料
    CharSequence[] CustFullnm = null;                //存放用戶名稱
    CharSequence[] Cusid = null;
    String Page = null;
    String id;
    int now = 0, num;
    String[] SendData = new String[7]; //0:全部/已抄/已抄代碼  1:抄表區代碼 2:預選道路代碼  3:用編代碼 4:用戶地址 5.資料庫位置
    String Fullnm; //用戶名稱
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page2);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //不自動彈出鍵盤
        FindView();
        BuildListener();
        InitData();
        GetData();
        init();
        Page = "page2";

        DLV.setOnScrollListener(new AbsListView.OnScrollListener() {     //滑動listview時觸發事件
            //滚动状态改变时调用
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 不滚动时保存当前滚动到的位置
            }
            //滚动时调用
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                //TTV.setText(String.valueOf(firstVisibleItem)+","+String.valueOf(visibleItemCount)+","+String.valueOf(totalItemCount));

                //if (firstVisibleItem != 0) {
               //     getViewByPosition((firstVisibleItem + visibleItemCount - 1), DLV).setBackgroundResource(R.color.white);
                //}
               // getViewByPosition(firstVisibleItem, DLV).setBackgroundResource(R.color.white);
               for(int i=firstVisibleItem;i<firstVisibleItem+visibleItemCount;i++) getViewByPosition(i,DLV).setBackgroundResource(R.color.white);
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
    }

    private void init() {
        // TODO Auto-generated method stub
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVol = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        // sp= new SoundPool(10, AudioManager.STREAM_SYSTEM, 100);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);  //音量鍵調節音效音量!
        MusicButton = sp.load(this, R.raw.button, 1); //把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
    }

    private void FindView() {
        EB = (ImageButton) findViewById(R.id.EndButton);
        PPB = (ImageButton) findViewById(R.id.MainPageButton);
        NPB = (ImageButton) findViewById(R.id.NextPageButton);
        TTV = (TextView) findViewById(R.id.TitleTextView);
        DLV = (ListView) findViewById(R.id.DataListView);
        FID = (ImageButton) findViewById(R.id.WatnumButton);
        FIV = (EditText) findViewById(R.id.WatnumEditText);
    }

    private void BuildListener() {
        EB.setOnClickListener(this);
        PPB.setOnClickListener(this);
        NPB.setOnClickListener(this);
        DLV.setOnItemClickListener(this);
        FID.setOnClickListener(this);
    }

    private void InitData() { //接收資料
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
        Fullnm = GetBundle.getString("D8");
        DataWatnum = GetBundle.getString("D9");
        MusicVoice = GetBundle.getString("M1");
        //change= Integer.valueOf(GetBundle.getString("change"));
        FIV.setText(GetBundle.getString("FIV"));
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

    public boolean dispatchTouchEvent(MotionEvent ev) { //點擊其他地方
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isShouldHideKeyboard(View v, MotionEvent event) { //根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    private void hideKeyboard(IBinder token) { //获取InputMethodManager，隐藏软键盘
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void VoiceSwitch(int arg1) {   // 檢查開關
        if ("2".equals(MusicVoice)) {
            sp.play(arg1, 1, 1, 0, 0, 1);
        }
    }

    private void GetData() {
        MyDBConnection DBConnection = null;
        SQLiteDatabase DB = null;
        Cursor Cursor = null;
        int Dco = 0;

        String CountTable =                        //定義資料庫連線字串   select count(*) from Road,Result where Result.RoadNo=Road.RoadNo
                "select count(*) from " +
                        (String) getText(R.string.TableName1) + "," +
                        (String) getText(R.string.TableName2) + " " +
                        "where " + (String) getText(R.string.TableName2) + ".RoadNo=" +
                        (String) getText(R.string.TableName1) + ".RoadNo ";

        if (SendData[1].substring(0, 1).equals(getText(R.string.NormalCust1)) || SendData[1].substring(0, 1).equals(getText(R.string.NormalCust2))) {
            CountTable =
                    CountTable + "and " + (String) getText(R.string.TableName2) + ".RoadNo='" + SendData[2] + "' ";
        }

        String DetailTable =     //定義資料庫連線字串select Result.Fullnm,Result.CustLoc,Result.Watnum,Result.Custno,Road.RoadNm ,Result._id from Result,Road where Result.RoadNo=Road.RoadNo
                "select Result.Fullnm," + (String) getText(R.string.TableName2) + ".CustLoc," +
                        (String) getText(R.string.TableName2) + ".Watnum," +
                        (String) getText(R.string.TableName2) + ".Custno," +
                        (String) getText(R.string.TableName1) + ".RoadNm " +
                        ",Result._id from " + (String) getText(R.string.TableName2) + "," +
                        (String) getText(R.string.TableName1) + " " +
                        "where " + (String) getText(R.string.TableName2) + ".RoadNo=" +
                        (String) getText(R.string.TableName1) + ".RoadNo ";

        String TestTable = "select * from Result";
        DetailTable =
                DetailTable + "and " + (String) getText(R.string.TableName2) + ".RoadNo='" + SendData[2] + "' ";
        CountTable =
                CountTable + "and " + (String) getText(R.string.TableName2) + ".RoadNo='" + SendData[2] + "' ";


        if (((String) getText(R.string.CopyedData)).equals(SendData[0])) {    // 已抄、未抄、全部 的查詢條件
            CountTable =
                    CountTable + "and " + (String) getText(R.string.ResultFieldCopyedMk) +
                            "='" + (String) getText(R.string.CopyedMk) + "' ";
            DetailTable =
                    DetailTable + "and " + (String) getText(R.string.ResultFieldCopyedMk) +
                            "='" + (String) getText(R.string.CopyedMk) + "' ";
        } else if (((String) getText(R.string.NonCopyedData)).equals(SendData[0])) {
            CountTable =
                    CountTable + "and " + (String) getText(R.string.ResultFieldCopyedMk) +
                            "<>'" + (String) getText(R.string.CopyedMk) + "' ";
            DetailTable =
                    DetailTable + "and " + (String) getText(R.string.ResultFieldCopyedMk) +
                            "<>'" + (String) getText(R.string.CopyedMk) + "' ";
        }
        DetailTable = DetailTable + " order by " + (String) getText(R.string.TableName2) + ".CopyRemark asc,Seq asc"; //以路順排列,未抄的排到最下面

        try {

            DBConnection = new MyDBConnection(this, SendData[5], null, 1);
            //DB=DBConnection.getWritableDatabase();	//儲存空間不足時會有錯誤
            DB = DBConnection.getReadableDatabase();

            Cursor = DB.rawQuery(CountTable, null);
            while (Cursor.moveToNext()) {
                Dco = Cursor.getInt(0);
            }
            Cursor.close();

            if (Dco != 0) {
                CustDataList = new CharSequence[Dco];
                CustDataNo = new CharSequence[Dco];
                CustFullnm = new CharSequence[Dco];
                Cusid = new CharSequence[Dco];
                Dco = 0;
                Cursor = DB.rawQuery(DetailTable, null);
                while (Cursor.moveToNext()) {
                    CustDataList[Dco] =
                            AddStrLength(Cursor.getString(0), 14, false, "　") +
                                    AddStrLength(Cursor.getString(1), 15, false, " ") +
                                    AddStrLength(Cursor.getString(2), 10, false, " ");
                    CustDataNo[Dco] = Cursor.getString(3);
                    CustFullnm[Dco] = Cursor.getString(0);
                    Cusid[Dco] = Cursor.getString(5);
                    Dco += 1;
                }
                while (Cursor.moveToPrevious()) {
                    DataTitle = Cursor.getString(4);

                }

                //Cursor.close();
                String Custno = Cursor.getColumnName(3);
                String Fullnm = Cursor.getColumnName(0);
                String CustLoc = Cursor.getColumnName(1);
                String Watnum = Cursor.getColumnName(2);
                String[] ColumnNames = {Custno, Fullnm, CustLoc, Watnum};

                ListAdapter adapter = new SimpleCursorAdapter(this,
                        R.layout.listviewlayout1, Cursor, ColumnNames, new int[]{R.id.Custno, R.id.Fullnm, R.id.CustLoc, R.id.Watnum});
                DataTitle =
                        SendData[2] + " " + DataTitle +
                                "(" + getText(R.string.AllDataCount) + "：" +
                                String.valueOf(Dco) +
                                getText(R.string.Unit) + ")";
                TTV.setText(DataTitle);

                //設定列表內容
                //ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,R.layout.gridrow,CustDataList);
                //設定列數
                DLV.setAdapter(adapter);
                DB.close();
                DBConnection.close();

                MoveGridRow();
                SendData[6] = "2";
            } else {
                DB.close();
                DBConnection.close();

                SendData[2] = "";
                SendData[3] = "";
                SendData[4] = "";
                SendData[6] = "1";
                StartSend(true, false);
            }
        } catch (SQLException e) {
            Cursor.close();
            DB.close();
            DBConnection.close();
            Toast.makeText(Page2.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void MoveGridRow() {
        //移動grid row至指定位置
        if (!"".equals(SendData[4])) {    // 之前的人寫的，現在不會用到
            if (Integer.valueOf(SendData[4]) <= CustDataNo.length) {
                DLV.setSelection(Integer.valueOf(SendData[4]));
            } else {
                return;
            }
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
            if (arg2 == true) {
                SendIntent.setClass(this, Page3.class);
            } else {
                SendIntent.setClass(this, Tablet_Copy.class);
            }
            Bundle SendBundle = new Bundle();
            SendBundle.putString("D1", SendData[0]);
            SendBundle.putString("D2", SendData[1]);
            SendBundle.putString("D3", SendData[2]);
            SendBundle.putString("D4", SendData[3]);
            SendBundle.putString("D5", SendData[4]);
            SendBundle.putString("D6", SendData[5]);
            SendBundle.putString("D7", SendData[6]);
            SendBundle.putString("Page_", Page);
            SendBundle.putString("D8", Fullnm);
            SendBundle.putString("D9", DataWatnum);
            SendBundle.putString("M1", MusicVoice);
            SendBundle.putString("id", id);
            SendBundle.putString("change",String.valueOf(change));
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
                SendBundle.putString("D7", SendData[6]);
                SendBundle.putString("Page_", Page);
                SendBundle.putString("D8", Fullnm);
                SendBundle.putString("D9", DataWatnum);
                SendBundle.putString("M1", MusicVoice);
                SendBundle.putString("id", id);
                SendIntent.putExtras(SendBundle);
                startActivity(SendIntent);
                finish();
            } else {
                openDialog((String) getText(R.string.DialogMessage1));
            }
        }
    }

    private String AddStrLength(String string, int i, boolean b, String string2) {
        while (string.length() < i) {
            if (b == true) {
                string = string2 + string;
            } else {
                string = string + string2;
            }
        }
        return string;
    }

    @Override
    public void onClick(View v) {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        long[] pattern = {0, 40}; // OFF/ON/OFF/ON...
        vibrator.vibrate(pattern, -1);

        VoiceSwitch(MusicButton);

        if (v.equals(NPB)) {
            if (!"".equals(SendData[3])) {
                StartSend(true, true);
            } else {
                StartSend(false, false);
            }
        } else if (v.equals(PPB)) {
            StartSend(true, false);
        } else if (v.equals(FID)) {
            if (FIV.getText().length() == 0) {
                openDialog((String) getText(R.string.DialogMessage8));
            } else {
                if(MoveToSelect()==true)
                {
                    setListViewPos(SelectNum);
                    now = SelectNum;
                }
            }
            //DLV.getChildAt(SelectNum).setBackgroundResource(R.color.view);
            //test.setText(String.valueOf(view1));
        } else {
            AlertDialog Alert = null;
            AlertDialog.Builder Builder = new AlertDialog.Builder(this);
            Builder.setTitle(getText(R.string.DialogTitle));
            Builder.setMessage(getText(R.string.DialogMessage12));
            Builder.setCancelable(true);
            Builder.setPositiveButton(getText(R.string.DialogButton4), this);
            Builder.setNegativeButton(getText(R.string.DialogButton3), this);

            Alert = Builder.create();
            Alert.show();
        }
    }

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    private void setListViewPos(final int pos) {
        /*if (android.os.Build.VERSION.SDK_INT >= 8) {*/
            DLV.setSelection(pos);
            //DLV.performItemClick(DLV.getAdapter().getView(pos, null, null), pos, DLV.getAdapter().getItemId(pos));
            //view1 = getViewByPosition(pos, DLV);
            //getViewByPosition(pos, DLV).setBackgroundResource(R.color.view);

            DLV .setOnScrollListener( new AbsListView.OnScrollListener() {
                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                        for(int i=firstVisibleItem;i<firstVisibleItem+visibleItemCount;i++){
                            if (pos != i){
                                getViewByPosition(i,DLV).setBackgroundResource(R.color.white);
                            }else{
                                view1 = getViewByPosition(pos, DLV);
                                getViewByPosition(pos, DLV).setBackgroundResource(R.color.view);
                            }
                        }

                    }
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }
            });
        /*} else {
            DLV.setSelection(pos);
            //DLV.performItemClick(DLV.getAdapter().getView(pos, null, null), pos, DLV.getAdapter().getItemId(pos));
            DLV.getAdapter().getView(pos, null, null).setBackgroundResource(R.color.view);
            view1 = getViewByPosition(pos, DLV);
            getViewByPosition(pos, DLV).setBackgroundResource(R.color.view);
        }*/
    }

    public Boolean MoveToSelect() {
        MyDBConnection DBConnection = null;
        SQLiteDatabase DB = null;
        Cursor Cursor = null;
        int Dco = 0;

        //定義資料庫連線字串select Result.Fullnm,Result.CustLoc,Result.Watnum,Result.Custno,Road.RoadNm ,Result._id from Result,Road where Result.RoadNo=Road.RoadNo
        String FindTable =  "select Result._id from " + (String) getText(R.string.TableName2) + "," + (String) getText(R.string.TableName1) + " where " + (String) getText(R.string.TableName2) + ".RoadNo = '" + SendData[2] + "' and TRIM(" + (String) getText(R.string.TableName2) + ".Watnum) = '" + FIV.getText().toString() + "'";

        String DetailTable = "select Result._id from " + (String) getText(R.string.TableName2) + " where " + (String) getText(R.string.TableName2) + ".RoadNo = '" + SendData[2] + "'";

        String CountTable = "select count(Result._id) from " + (String) getText(R.string.TableName2) + "," + (String) getText(R.string.TableName1) + " where " + (String) getText(R.string.TableName2) + ".RoadNo = '" + SendData[2] + "' and TRIM(" + (String) getText(R.string.TableName2) + ".Watnum) = '" + FIV.getText().toString() + "'";

        // 已抄、未抄、全部 的查詢條件
        if (((String) getText(R.string.CopyedData)).equals(SendData[0])) {
            DetailTable = DetailTable + " and " + (String) getText(R.string.ResultFieldCopyedMk) + " = '" + (String) getText(R.string.CopyedMk) + "'";
            CountTable = CountTable + " and " + (String) getText(R.string.ResultFieldCopyedMk) + " = '" + (String) getText(R.string.CopyedMk) + "'";
        } else if (((String) getText(R.string.NonCopyedData)).equals(SendData[0])) {
            DetailTable = DetailTable + " and " + (String) getText(R.string.ResultFieldCopyedMk) + " <> '" + (String) getText(R.string.CopyedMk) + "'";
            CountTable = CountTable + " and " + (String) getText(R.string.ResultFieldCopyedMk) + " <> '" + (String) getText(R.string.CopyedMk) + "'";
        }
        DetailTable = DetailTable + " order by " + (String) getText(R.string.TableName2) + ".CopyRemark asc,Seq asc"; //以路順排列,未抄的排到最下面

        try {

            DBConnection = new MyDBConnection(this, SendData[5], null, 1);
            //DB=DBConnection.getWritableDatabase();	//儲存空間不足時會有錯誤
            DB = DBConnection.getReadableDatabase();

            Cursor = DB.rawQuery(CountTable, null);

            while (Cursor.moveToNext()) {
                Dco = Cursor.getInt(0);
            }
            Cursor.close();
            if (Dco == 0) {
                openDialog((String) getText(R.string.DialogMessage7));
                return false;
            } else {
                Cursor = DB.rawQuery(FindTable, null);
                Cursor.moveToNext();
                id = Cursor.getString(0);
                Cursor.close();
                //DB=DBConnection.getWritableDatabase();	//儲存空間不足時會有錯誤

                Cursor = DB.rawQuery(DetailTable, null);
                SelectNum = 0;
                Cursor.moveToNext();
                while (!id.equals(Cursor.getString(0))) {
                    Cursor.moveToNext();
                    SelectNum++;
                }
                Cursor.close();
                DB.close();
                DBConnection.close();
                return true;
            }

        } catch (SQLException e) {
            Cursor.close();
            DB.close();
            DBConnection.close();
            Toast.makeText(Page2.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

return false;
    }

    public class ViewHolder {
        public TextView textView;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

        //TTV.setText(String.valueOf());
        SendData[3] = (String) CustDataNo[arg2];
        id = (String) Cusid[arg2];
        Fullnm = (String) CustFullnm[arg2];
        DataWatnum = null;
        if (view1 != null) {
            view1.setBackgroundResource(R.color.white);
        }
        //	view.setBackgroundResource(R.color.white);
        arg1.setBackgroundResource(R.color.view);
        now = arg2;
        view1 = arg1;

        StartSend(true,true);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case -1:
                //finish();
                finish();
                break;
            case -2:
                StartSend(false, true);
                break;
        }
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();
            StartSend(true, false);

        }
        return false;
    }
}