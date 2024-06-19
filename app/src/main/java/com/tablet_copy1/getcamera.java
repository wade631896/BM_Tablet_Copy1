package com.tablet_copy1;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.bm_tablet_copy1.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

//import android.support.design.widget.NavigationView;

public class getcamera extends Activity implements View.OnClickListener {
    //資料庫
    private SQLiteDatabase myapp2;
    //介面
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private RelativeLayout cameraLayout; //相機介面
    //相機
    private Camera mCamera;
    //圖片
    private ImageView iv_show,img_show;
    //控制項
    private Button btn_start, btn_stop, btn_view, btn_up, btn_next, btn_recamera;
    private TextView txt2save;
    //放大縮小
    private ZoomControls zoomControlsX ;
    //字串、數值
    //String ordersn="";
    String thenewfilepathUpload ;
    String imagebyte;
    String[] SendData = new String[6]; // 0:全部/已抄/已抄代碼 1:抄表區代碼 2:預選道路代碼 3:用編代碼 4:用戶地址 5:資料庫位置
    int ciq,p;
    private int viewWidth, viewHeight; //mSurfaceView的寬和高
    //檔案
    File [] f1;
    //提醒視窗
    Toast toast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_camera);

        btn_start = (Button) findViewById(R.id.btn_start);
        btn_stop = (Button) findViewById(R.id.btn_stop);
        btn_view = (Button) findViewById(R.id.btn_view);

        btn_start.setOnClickListener(click);
        btn_stop.setOnClickListener(click);
        btn_view.setOnClickListener(click);

        InitData();
        initView();
        enableZoom();
    }

    //按鈕監聽事件
    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_start:
                    savepicture();
                    break;
                case R.id.btn_stop:
                    take_pic_agnain();
                    break;
                case R.id.btn_view:
                    viewimage();
                    break;
                case R.id.btn_recamera:
                    take_pic_agnain();
                    img_show.setVisibility(View.GONE);
                    btn_view.setVisibility(View.VISIBLE);
                    btn_up.setVisibility(View.GONE);
                    btn_next.setVisibility(View.GONE);
                    btn_recamera.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    };

    //點擊回饋
    @Override
    public void onClick(View v) {
        if (mCamera == null) return;
        //自動對焦後拍照
        mCamera.autoFocus(autoFocusCallback);
    }

    //自動對焦進行拍照
    Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {//對焦成功
                camera.takePicture(new Camera.ShutterCallback() { //按下快門
                    @Override
                    public void onShutter() { //按下快門瞬間的操作
                    }
                }, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {//是否保存原始圖片的資訊

                    }
                }, pictureCallback);
            }
        }
    };

    //最佳預覽，拉伸問題
    private Camera.Size getBestPreviewSize(int width, int height) {
        Camera.Size result = null;
        final Camera.Parameters p = mCamera.getParameters();
        //特別注意此處需要規定rate的比是大的比小的，不然有可能出現rate = height/width，但是後面遍歷的時候，current_rate = width/height,所以我們限定都為大的比小的。
        float rate = (float) Math.max(width, height)/ (float)Math.min(width, height);
        float tmp_diff;
        float min_diff = -1f;
        for (Camera.Size size : p.getSupportedPreviewSizes()) {
            float current_rate = (float) Math.max(size.width, size.height)/ (float)Math.min(size.width, size.height);
            tmp_diff = Math.abs(current_rate-rate);
            if( min_diff < 0){
                min_diff = tmp_diff ;
                result = size;
            }
            if( tmp_diff < min_diff ){
                min_diff = tmp_diff ;
                result = size;
            }
        }
        return result;
    }

    //獲取圖片
    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            final Bitmap resource = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (resource == null) {
                Toast.makeText(getcamera.this, "拍照失敗", Toast.LENGTH_SHORT).show();
            }
            final Matrix matrix = new Matrix();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                matrix.setRotate(90);
            } else{
                matrix.setRotate(90);
            }
            final Bitmap bitmap = Bitmap.createBitmap(resource, 0, 0, resource.getWidth(), resource.getHeight(), matrix, true);
            if (bitmap != null && iv_show != null && (iv_show.getVisibility() == View.GONE || iv_show.getVisibility() == View.INVISIBLE)) {
                mCamera.stopPreview();
                iv_show.setVisibility(View.VISIBLE);
                btn_start.setVisibility(View.VISIBLE);
                btn_stop.setVisibility(View.VISIBLE);
                txt2save.setVisibility(View.VISIBLE);
                mSurfaceView.setVisibility(View.GONE);
                zoomControlsX.setVisibility(View.INVISIBLE);
                //Toast.makeText(getcamera.this, "拍照", Toast.LENGTH_SHORT).show();
                iv_show.setImageBitmap(bitmap);
            }
        }
    };

    //讀取SDCard圖片，型態為Bitmap
    private static Bitmap getBitmap(String file)
    {
        try
        {
            Bitmap bitmap = BitmapFactory.decodeFile(file);
            return bitmap;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (mSurfaceView != null) {
            viewWidth = mSurfaceView.getWidth();
            viewHeight = mSurfaceView.getHeight();
        }
    }

    //接收資料
    private void InitData() {
        Intent GetIntent = getIntent();
        Bundle GetBundle = GetIntent.getExtras();
        //用戶編號
        SendData[3] = GetBundle.getString("D4");
    }

    //儲存圖片
    protected void savepicture()
    {
        try{
            Calendar mCal = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
            String today = df.format(mCal.getTime());

            SimpleDateFormat sDateFormat2 = new SimpleDateFormat("yyyyMMddHHmmss");
            String date2 = sDateFormat2.format(new java.util.Date());

            String directory= "/data/data/com.bm_tablet_copy1";
            String path = directory + File.separator  + date2.substring(0,6);
            File dirFile = new File(path);

            try {
                if(!dirFile.exists())
                {
                    //如果資料夾不存在
                    dirFile.mkdir();//建立資料夾
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            // 保存圖片
            //System.out.println(dirFile.length());
            File file = new File(path + "/" + SendData[3].toString()  + "_" + date2 + ".jpg");
            FileOutputStream stream = new FileOutputStream(file);
            iv_show.setDrawingCacheEnabled(true);
            Bitmap bmp = iv_show.getDrawingCache();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            // Android設備Gallery應用只會在啟動的時候掃描系统文件夾
            // 這裏模擬一個媒體裝載的廣播，用於使保存的圖片可以在Gallery中查看
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                final Uri contentUri = Uri.fromFile(dirFile);
                scanIntent.setData(contentUri);
                sendBroadcast(scanIntent);
            } else {
                final Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory()));
                sendBroadcast(intent);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            iv_show.setDrawingCacheEnabled(true);
            Bitmap bmp2 = iv_show.getDrawingCache();
            bmp2.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] bytes = baos.toByteArray();
            imagebyte = Base64.encodeToString(bytes, Base64.DEFAULT);

            /*thenewfilepathUpload = ordersn + "_0" + count;
            Thread thread = new Thread(ThreadUpload_img);
            thread.savepicture();*/

            Toast.makeText(this, "圖片儲存成功", Toast.LENGTH_LONG).show();
            take_pic_agnain();
        }
        catch(Exception e)
        {
            toast= Toast.makeText(this,"圖片保存失敗", Toast.LENGTH_LONG);

            if(toast!=null)
                toast.show();
            e.printStackTrace();
        }
    }

    //重新拍照
    protected void take_pic_agnain()
    {
        iv_show.setVisibility(View.GONE);
        btn_start.setVisibility(View.INVISIBLE);
        btn_stop.setVisibility(View.INVISIBLE);
        mSurfaceView.setVisibility(View.VISIBLE);
        txt2save.setVisibility(View.INVISIBLE);
        zoomControlsX.setVisibility(View.VISIBLE);
        //initCamera();
    }

    //預覽圖片
    protected void viewimage()
    {
/*        File f = new File("/data/data/com.example.eric.serviceorder/" + appid + "_" + ordersn + "/");

        if(f.exists()){
            p=0;
            iv_show.setVisibility(View.GONE);
            btn_start.setVisibility(View.INVISIBLE);
            btn_stop.setVisibility(View.INVISIBLE);
            mSurfaceView.setVisibility(View.INVISIBLE);
            txt2save.setVisibility(View.INVISIBLE);
            zoomControlsX.setVisibility(View.INVISIBLE);
            btn_view.setVisibility(View.INVISIBLE);

            img_show = (ImageView) findViewById(R.id.iv_show_camera3_activity);
            img_show.setVisibility(View.VISIBLE);

            btn_up = (Button) findViewById(R.id.btn_up);
            btn_up.setVisibility(View.VISIBLE);
            //btn_up.setOnClickListener(click);

            btn_next = (Button) findViewById(R.id.btn_next);
            btn_next.setVisibility(View.VISIBLE);
            //btn_next.setOnClickListener(click);

            btn_recamera = (Button) findViewById(R.id.btn_recamera);
            btn_recamera.setVisibility(View.VISIBLE);
            btn_recamera.setOnClickListener(click);

            f1 = f.listFiles();
//        for(int i=0;i<f.listFiles().length;i++)
//        {
//            Log.e("File",f1[i].toString());
//            Log.e("File",f1[i].getName());
//        }
            img_show.setImageBitmap(getBitmap(f1[p].toString()));
            btn_next.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //Log.e("File", String.valueOf(p) + String.valueOf(f1.length));
                    if (p==f1.length - 1){
                        img_show.setImageBitmap(getBitmap(f1[f1.length - 1].toString()));
                    }else{
                        p++;
                        img_show.setImageBitmap(getBitmap(f1[p].toString()));
                    }
                }
            });
            btn_up.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if(p==0){
                        img_show.setImageBitmap(getBitmap(f1[0].toString()));
                    }else{
                        p--;
                        img_show.setImageBitmap(getBitmap(f1[p].toString()));
                    }
                }
            });
            //帶入SDCard內的圖片路徑(SDCard: DCIM資料夾/100MEDIA資料夾/001圖片)
            //img_show.setImageBitmap(getBitmap("/data/data/com.example.eric.serviceorder/" + appid + "_" + ordersn + "/" +appid + "_" + ordersn + "_02.jpg"));
        }else{
            Toast.makeText(getcamera.this, "無檔案", Toast.LENGTH_SHORT).show();
        }*/
    }

    //初始化控制項
    private void initView() {
        Intent intent = this.getIntent();
        //ordersn = intent.getStringExtra("ordersn");
        //appid = intent.getStringExtra("appid");

        btn_start = (Button)findViewById(R.id.btn_start);
        btn_stop = (Button)findViewById(R.id.btn_stop);
        txt2save =(TextView)findViewById(R.id.txt2save);

        iv_show = (ImageView) findViewById(R.id.iv_show_camera2_activity);
        //mSurfaceView
        mSurfaceView = (SurfaceView) findViewById(R.id.surface_view_camera2_activity);
        mSurfaceHolder = mSurfaceView.getHolder();
        // mSurfaceView 不需要自己的緩衝區
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        cameraLayout = (RelativeLayout)this.findViewById(R.id.content_camera);
        zoomControlsX = (ZoomControls) findViewById(R.id.zoomControlsX);

        // mSurfaceView添加回調
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) { //SurfaceView建立
                // 初始化Camera
                initCamera();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                //當SurfaceView尺寸變化時（包括裝置橫屏豎屏改變時時），需要重新設定相關引數
                if (mSurfaceHolder.getSurface() == null) {
                //檢查SurfaceView是否存在
                    return;
                }
                //改變設定前先關閉相機
                try {
                    mCamera.stopPreview();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //使用最佳比例配置重啟相機
                try {
                    mCamera.setPreviewDisplay(mSurfaceHolder);
                    final Camera.Parameters parameters = mCamera.getParameters();
                    final Camera.Size size = getBestPreviewSize(1, 1);
                    parameters.setPreviewSize(size.width, size.height);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                    }
                    mCamera.setParameters(parameters);
                    mCamera.startPreview();
                } catch (Exception e) {
                    Log.d("surfaceChanged", "Error starting camera preview:" + e.getMessage());
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) { //SurfaceView銷毀
                // 釋放Camera資源
                if (mCamera != null) {
                    mCamera.stopPreview();
                    mCamera.release();
                }
            }
        });
        //設置點擊監聽
        mSurfaceView.setOnClickListener(this);
    }

    //拉遠近
    private void enableZoom() {
        ciq = 0;
        zoomControlsX = new ZoomControls(this);
        zoomControlsX.setIsZoomInEnabled(true);
        zoomControlsX.setIsZoomOutEnabled(true);
        zoomControlsX.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //zoomCamera(false);
                Camera.Parameters parameters = mCamera.getParameters();
                int maxZoom = parameters.getMaxZoom();
                Log.e("maxZoom",maxZoom+"");
                Log.e("isZoomSupported",parameters.isZoomSupported()+"");
                Log.e("ciq",ciq+"");
                if (parameters.isZoomSupported()) {
                    if (ciq >=0 && ciq < maxZoom) {
                        ciq=ciq+2;
                        parameters.setZoom(ciq);
                        mCamera.setParameters(parameters);
                        Log.e("ciq",ciq+"");
                    } else {
                          Toast.makeText(getcamera.this, "已放置最大", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        zoomControlsX.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //zoomCamera(true);
                Camera.Parameters parameters = mCamera.getParameters();
                int maxZoom = parameters.getMaxZoom();
                Log.e("maxZoom",maxZoom+"");
                Log.e("isZoomSupported",parameters.isZoomSupported()+"");
                Log.e("ciq",ciq+"");
                if (parameters.isZoomSupported()) {
                    if (ciq >0 && ciq < maxZoom) {
                        ciq=ciq-2;
                        parameters.setZoom(ciq);
                        mCamera.setParameters(parameters);
                        Log.e("ciq",ciq+"");
                    } else {
                         Toast.makeText(getcamera.this, "已縮置最小", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
//        cameraLayout.addView(zoomControlsX);
    }

    //SurfaceHolder 回調介面方法
    private void initCamera() {
        mCamera = Camera.open();//預設開啟後置
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mCamera.setDisplayOrientation(90);//攝像頭進行旋轉270°
        }else{
            mCamera.setDisplayOrientation(90);//攝像頭進行旋轉270°
        }

        if (mCamera != null) {
            try {
                Camera.Parameters parameters = mCamera.getParameters();
                //設置預覽照片的大小
                parameters.setPreviewFpsRange(viewWidth, viewHeight);
                //設置相機預覽照片幀數
                parameters.setPreviewFpsRange(4, 10);
                //設置圖片格式
                parameters.setPictureFormat(ImageFormat.JPEG);
                //設置圖片的質量
                parameters.set("jpeg-quality", 90);
                //設置照片的大小
                parameters.setPictureSize(viewWidth, viewHeight);
                //通過SurfaceView顯示預覽
                mCamera.setPreviewDisplay(mSurfaceHolder);
                //開始預覽
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
