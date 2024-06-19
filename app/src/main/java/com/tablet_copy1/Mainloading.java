package com.tablet_copy1;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.ExifInterface;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemClickListener;
import com.bm_tablet_copy1.R;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import static android.content.ContentValues.TAG;

public class Mainloading extends Activity
		implements OnClickListener, OnItemClickListener, android.content.DialogInterface.OnClickListener {
	public ProgressDialog PDialog = null;
	Button StartButton = null;
	Button EndButton = null;
	ProgressBar PR = null;
	Button BackBt = null;
	TextView message = null;
	CheckBox CB = null;
	String Page = null;
	String[] SendData = new String[7];
	BufferedReader br;
	SoundPool sp;// 声明一个SoundPool
	String Fullnm; // 用戶名稱
	String DataWatnum;
	int MusicButton;// 按鈕音
	int MusicButton1;
	int voice = 1;
	private AudioManager am;
	private int maxVol;
	public String MusicVoice = "2";// 音效開關 2=開 1=關

	Button uploadButton = null;
	Button downButton = null;
	boolean end=false;//傳輸結果
	String m_Text;
	String file3,file4;
	int dwup=0;

	String uploaddate,uploadimg,uploaduid; //上傳日期、圖base64編碼、用戶編號

	//SD卡路徑
	private String sdURL = "";
	SharedPreferences setSDPath;
	String sdPath="";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.showexit);//表示此頁面對應showexit
		FindView();
		BuildListener();
		InitData();
		init();

		try {
			//內建儲存
			//確定讀寫
			if (Environment.getExternalStorageState()// 確定SD卡可讀寫
					.equals(Environment.MEDIA_MOUNTED)) {
				File sdFile = android.os.Environment.getExternalStorageDirectory();
				String path = sdFile.getPath() + File.separator + "DB3backup";
				File dirFile = new File(path);
				//如果資料夾不存在
				if (!dirFile.exists()) {
					// 建立資料夾
					dirFile.mkdir();
				}
			}
			//平板位置
			String file1 = "/mnt/sdcard/DB3/copy.db3";
			//外插sd卡
			String file2 = "/mnt/sdcard/DB3backup/copybackup1.db3";
			copyFile(file1, file2);

			//SD儲存
			setSDPath = getSharedPreferences("SDPATH",0);
			sdPath= setSDPath.getString("SDPATH","/Removable/MicroSD/");
			sdURL=sdURL+sdPath;

			//建立文件檔儲存路徑
			File mFile = new File(sdURL);
			//如果資料夾不存在
			if (!mFile.exists()) {
				// 建立資料夾
				mFile.mkdirs();
			}

			//紀錄當下時間至分鐘
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String date = sDateFormat.format(new java.util.Date());

			//複製到ＳＤ卡
			File[] myfiles= getApplicationContext().getExternalFilesDirs(null);
			sdURL=myfiles[1].toString();
			copyFile(file1, sdURL + "/BM1output" + date + ".db3");


		}catch (Exception e) {
			e.printStackTrace();
		}

		CB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if ( isChecked )
				{
					downButton.setEnabled(true);
				}else{
					downButton.setEnabled(false);
				}
			}
		});

	}

	private void init() { // 音效檔
		// TODO Auto-generated method stub
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		maxVol = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

		// sp= new SoundPool(10, AudioManager.STREAM_SYSTEM,
		// 100);//第一个参数为同时播放数据流的最大个数，第二数据流类型，第三为声音质量
		sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0); // 音量鍵調節音效音量!
		AssetManager assetManager = getAssets();
		// AssetFileDescriptor descriptor =
		// assetManager.openFd("explosion.ogg");
		// //游戏开发通常把音效图片等资源放到assets中，便于用分级文件夹管理

		// int explosionId = sp.load(descriptor, 1);

		MusicButton = sp.load(this, R.raw.button, 1); // 把你的声音素材放到res/raw里，第2个参数即为资源文件，第3个为音乐的优先级
		MusicButton1 = sp.load(this, R.raw.button1, 1);

	}

	void copyFile(String file1, String file2) throws IOException {
	    InputStream in = new FileInputStream(file1); //讀取
	    try {
	        OutputStream out = new FileOutputStream(file2); //寫入
	        try {
				PR.setProgress(0);
				PR.setMax(100);
	            // Transfer bytes from in to out
	            byte[] buf = new byte[1024];
	            int len;
	            while ((len = in.read(buf)) > 0) {
	                out.write(buf, 0, len);
	                PR.incrementProgressBy(10); //動態進度條
	            }
	        } finally {
	            out.close();
	        }
	    } finally {
	        in.close();
	    }
	}

	private void FindView() {//將每個可能改變內容的物件進行FINDVIEW
		BackBt = (Button) findViewById(R.id.backbutton);
		StartButton = (Button) findViewById(R.id.okbutton);//OKButton對應到showexit.xml中的備份
		EndButton = (Button) findViewById(R.id.Cancelbutton);
		PR = (ProgressBar) findViewById(R.id.progressbar1);
		message = (TextView) findViewById(R.id.message); // 訊息
		message.setText("1.請先執行備份才能上傳\n2.上傳時請輸入正確的路線檔名\n3.下載時會清除資料務必留意");
		uploadButton = (Button) findViewById(R.id.upbutton);
		downButton = (Button) findViewById(R.id.downbutton);
		CB = (CheckBox) findViewById(R.id.cb_flex);
	}

	private void BuildListener() {//傾聽以下三個Button，以下面onclick進行回應
		StartButton.setOnClickListener(this);
		EndButton.setOnClickListener(this);
		BackBt.setOnClickListener(this);
		uploadButton.setOnClickListener(this);
		downButton.setOnClickListener(this);
	}

	private void VoiceSwitch(int arg1) { // 檢查開關
		if ("2".equals(MusicVoice)) {
			sp.play(arg1, 0.5f, 0.5f, 1, 0, 1f);
		}
		// sp.unload(arg1);

		// sp.release();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		VoiceSwitch(MusicButton1);
		if (v.equals(StartButton)) { // ||v.equals(WSB)
			m_Text = "";
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("請輸入抄表路線別 [例：A01]／[例：Z02]");
			final EditText input = new EditText(this);
			input.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD | InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
			builder.setView(input);
			final CharSequence strDialogTitle = getString(R.string.str_dialog_title);
			final CharSequence strDialogBody = getString(R.string.str_dialog_body);
			final String file1 = "/mnt/sdcard/DB3/copy.db3"; // 平板位置
			final String file2 = "/mnt/sdcard/DB3backup/copybackup.db3"; // 外插sd卡

			if (Environment.getExternalStorageState()// 確定SD卡可讀寫
					.equals(Environment.MEDIA_MOUNTED)) {
				File sdFile = android.os.Environment.getExternalStorageDirectory();
				String path = sdFile.getPath() + File.separator + "DB3backup";
				File dirFile = new File(path);
				if (!dirFile.exists()) {// 如果資料夾不存在
					dirFile.mkdir();// 建立資料夾
				}
			}

			builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					m_Text = input.getText().toString();
					if (!m_Text.equals("")){
						PDialog = ProgressDialog.show(Mainloading.this, strDialogTitle, strDialogBody, true);
						new Thread() {
							public void run() {
								try {
									SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMdd");
									String date = sDateFormat.format(new java.util.Date());
									file3 = "/mnt/sdcard/DB3backup/COPY" + m_Text.trim().toString().toUpperCase() + "-" + (Integer.valueOf(date.substring(0,4)) - 1911) + date.substring(4,8) + ".db3"; // 外插sd卡
									file4 = "COPY" + m_Text.trim().toString().toUpperCase() + "-" + (Integer.valueOf(date.substring(0,4)) - 1911) + date.substring(4,8) + ".mdb";
									//String file3 = "/storage/extSdCard/DB3backup/copybackup.db3";
									copyFile(file1, file2);
									copyFile(file1, file3);
								} catch (Exception e) {
									e.printStackTrace();
								} finally {
									PDialog.dismiss();
								}
							}
						}.start();
						openDialog((String) getText(R.string.DialogMessage11));

						uploadButton.setVisibility(View.VISIBLE );
						//downButton.setVisibility(View.VISIBLE );
					}else{
						Toast.makeText(Mainloading.this,"請輸入抄表路線別 [例：A01]／[例：Z02]",Toast.LENGTH_SHORT).show();
						uploadButton.setVisibility(View.INVISIBLE );
						//downButton.setVisibility(View.INVISIBLE );
					}
				}
			});
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					uploadButton.setVisibility(View.INVISIBLE );
					//downButton.setVisibility(View.INVISIBLE );
					PDialog = ProgressDialog.show(Mainloading.this, strDialogTitle, strDialogBody, true);
					new Thread() {
						public void run() {
							try {
								copyFile(file1, file2);
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								PDialog.dismiss();
							}
						}
					}.start();
					openDialog((String) getText(R.string.DialogMessage11));
				}
			});
			builder.show();
		} else if (v.equals(EndButton)) {
			// StartSend(true,true);
			finish();
		} else if (v.equals(BackBt)) {
			StartSend(true, false);
		}else if (v.equals(uploadButton)) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if(mNetworkInfo != null && mNetworkInfo.isConnected())//判斷網路是否連線
			{
				dwup = 1;
				myAsyncTask mRequest = new myAsyncTask();
				mRequest.execute();
			}
			else {
				Toast.makeText(this,"請檢查網路連線",Toast.LENGTH_SHORT).show();
			}
		}else if (v.equals(downButton)) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if(mNetworkInfo != null && mNetworkInfo.isConnected())//判斷網路是否連線
			{
				final AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("下載後會清除資料，確定下載?");
				builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dwup = 2;
						myAsyncTask mRequest = new myAsyncTask();
						mRequest.execute();
					}
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				builder.show();
			}
			else {
				Toast.makeText(this,"請檢查網路連線",Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void InitData() { // 接收資料
		// 初始化資料傳送
		SendData[0] = (String) getText(R.string.NonCopyedData); // 全部/已抄/已抄代碼之初值 (可能為姓名)
		SendData[1] = ""; // 抄表區代碼之初值
		SendData[2] = ""; // 預選道路代碼之初值
		SendData[3] = ""; // 用戶編號之初值
		SendData[4] = ""; // 地址之初值
		SendData[5] = ""; // 資料庫位置
		Page = "";
		try {
			Intent GetIntent = getIntent();
			Bundle GetBundle = GetIntent.getExtras();

			SendData[0] = GetBundle.getString("D1");
			SendData[1] = GetBundle.getString("D2");
			SendData[2] = GetBundle.getString("D3");
			SendData[3] = GetBundle.getString("D4");
			SendData[4] = GetBundle.getString("D5");
			SendData[5] = GetBundle.getString("D6");
			Page = GetBundle.getString("Page_");
			SendData[6] = GetBundle.getString("D7");//?
			Fullnm = GetBundle.getString("D8");
			DataWatnum = GetBundle.getString("D9");
			MusicVoice = GetBundle.getString("M1");
		} catch (Exception e) {
		}
	}

	private void StartSend(boolean arg1, boolean arg2) { // 傳輸資料
		Intent SendIntent = new Intent();
		if (arg1 == true) {

			if (arg2 == true) {
				SendIntent.setClass(this, Tablet_Copy.class);
			} else {
				if ("page1".equals(Page)) {
					SendIntent.setClass(this, Tablet_Copy.class);
				} else if ("page2".equals(Page)) {
					SendIntent.setClass(this, Page2.class);
				} else if ("page3".equals(Page)) {
					SendIntent.setClass(this, Page3.class);
				}
			}

			Bundle SendBundle = new Bundle();
			SendBundle.putString("D1", SendData[0]);
			SendBundle.putString("D2", SendData[1]);
			SendBundle.putString("D3", SendData[2]);
			SendBundle.putString("D4", SendData[3]);
			SendBundle.putString("D5", SendData[4]);
			SendBundle.putString("D6", SendData[5]);
			SendBundle.putString("D7", SendData[6]); //?
			SendBundle.putString("Page_", Page);
			SendBundle.putString("D8", Fullnm);
			SendBundle.putString("D9", DataWatnum);
			SendBundle.putString("M1", MusicVoice);
			SendIntent.putExtras(SendBundle);

			startActivity(SendIntent);
			finish();
		} else {

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

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	private Bitmap getResizedBitmap(String imagePath) {
		// 取得原始圖檔的bitmap與寬高
		BitmapFactory.Options options = new BitmapFactory.Options();
		Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
		int width = options.outWidth, height = options.outHeight;

		// 將圖檔等比例縮小至寬度為1024
		final int MAX_WIDTH = 1024;
		float resize = 1; // 縮小值 resize 可為任意小數
		if(width>MAX_WIDTH){
			resize = ((float) MAX_WIDTH) / width;
		}

		// 產生縮圖需要的參數 matrix
		Matrix matrix = new Matrix();
		matrix.postScale(resize, resize); // 設定寬高的縮放比例

		// 產生縮小後的圖
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		return resizedBitmap;
	}
	public static Bitmap rotateToDegrees(Bitmap tmpBitmap, float degrees) {
		Matrix matrix = new Matrix();
		matrix.reset();
		matrix.setRotate(degrees);
		return tmpBitmap =
				Bitmap.createBitmap(tmpBitmap, 0, 0, tmpBitmap.getWidth(), tmpBitmap.getHeight(), matrix,
						true);
	}
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation =
					exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
							ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
	public static String bitmapToBase64(Bitmap bitmap) {

		String result = null;
		ByteArrayOutputStream baos = null;
		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);

				baos.flush();
				baos.close();

				byte[] bitmapBytes = baos.toByteArray();
				result =android.util.Base64.encodeToString(bitmapBytes, android.util.Base64.DEFAULT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private class myAsyncTask extends AsyncTask<Void,Void,Void> {
		@Override
		protected Void doInBackground(Void... voids) {
			//資料接回
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMdd");
			ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			String date = sDateFormat.format(new java.util.Date());

			if (dwup == 1){ //上傳
				File f = new File(file3);
				byte[] byteArray = null;
				try {
					byteArray = FileUtils.readFileToByteArray(f);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String strBase64 = new String(Base64.encode(byteArray, 0));
				if(f.exists())
				{
					String uploadfileName =f.getName().toString().trim();
					updownservice.FileUploadByBase64String("FileUploadByBase64String",strBase64,uploadfileName);
					try {
						Thread.sleep(1000);

						updownservice.CreateDB("CreateDB",file4);
						try {
							Thread.sleep(2000);

							updownservice.DataTrans("DataTrans",uploadfileName,file4);
							try {
								Thread.sleep(1000);

								File pic = new File("/mnt/sdcard/copypictures/"  + date.substring(0,6));
								if(!pic.exists()){
									pic.mkdirs();
								}
								String[] list = pic.list();
								for(int i = 0; i< list.length;i++) {
									// 取得原始圖檔的bitmap與寬高
									BitmapFactory.Options options = new BitmapFactory.Options();
									Bitmap widthbitmap = BitmapFactory.decodeFile(pic + "/" + list[i], options);
									int width = options.outWidth;

									//Log.i(TAG, "MyClass.getView() — get item number" + widthbitmap +  "___" + width);

									if (width != -1){
										//String uploadpicName =pic.getName().toString().trim();
										Bitmap thumbnail = getResizedBitmap(pic + "/" + list[i]);
										thumbnail = rotateToDegrees(thumbnail, readPictureDegree(pic + "/" + list[i]));
										ByteArrayOutputStream bytes = new ByteArrayOutputStream();
										thumbnail.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
										String s = bitmapToBase64(thumbnail).toString();//將圖片編碼後丟到server，不儲存本地端
										uploadimg = s;
										updownservice.uploadImages("uploadImages", uploadimg,list[i]);
									}
								}
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}else if(dwup == 2){ //下載
				try{
					String downloadUrl = "http://www.shng.com.tw/ComputerCopy_Webservice/M_download/copy.db3";
					URL url = new URL(downloadUrl);
					//打开连接
					URLConnection conn = url.openConnection();
					//打开输入流
					InputStream is = conn.getInputStream();
					//获得长度
					int contentLength = conn.getContentLength();
					Log.e(TAG, "contentLength = " + contentLength);
					//创建文件夹 MyDownLoad，在存储卡下
					//String dirName = Environment.getExternalStorageDirectory() + "/MyDownLoad/";
					File file = new File("/mnt/sdcard/DB3/");
					//不存在创建
					if (!file.exists()) {
						file.mkdir();
					}
					//下载后的文件名
					String fileName = "/mnt/sdcard/DB3/copy.db3";
					File file1 = new File(fileName);
					if (file1.exists()) {
						file1.delete();
					}
					//创建字节流
					byte[] bs = new byte[1024];
					int len;
					OutputStream os = new FileOutputStream(fileName);
					//写数据
					while ((len = is.read(bs)) != -1) {
						os.write(bs, 0, len);
					}
					//完成后关闭流
					Log.e(TAG, "download-finish");
					os.close();
					is.close();
				}catch (Exception e){
					e.printStackTrace();
				}
			}
			if(mNetworkInfo != null && mNetworkInfo.isConnected())
			{
				end=true;
			}
			return null;
		}
		@Override
		protected void onPreExecute() {
			final CharSequence strDialogTitle = getString(R.string.str_dialog_title);
			final CharSequence strDialogBody = getString(R.string.str_dialog_body);
			PDialog = ProgressDialog.show(Mainloading.this, strDialogTitle, strDialogBody, true);

			super.onPreExecute();
		}
		@Override
		protected void onPostExecute(Void aVoid) {
			if (dwup == 1){
				if(end) {
					PDialog.dismiss();
					//Toast.makeText(Mainloading.this, "上傳成功", Toast.LENGTH_SHORT).show();
					openDialog((String) "上傳成功");
					end=false;
				}
				else Toast.makeText(Mainloading.this,"傳輸失敗，請檢查網路後再試一次",Toast.LENGTH_SHORT).show();
			}else if(dwup == 2){
				if(end) {
					PDialog.dismiss();
					//Toast.makeText(Mainloading.this, "下載成功", Toast.LENGTH_SHORT).show();
					openDialog((String) "下載成功");
					end=false;
				}
				else Toast.makeText(Mainloading.this,"傳輸失敗，請檢查網路後再試一次",Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(aVoid);
		}
	}
}