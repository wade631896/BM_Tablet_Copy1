package com.tablet_copy1;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Message;

import com.bm_tablet_copy1.R;

public class first extends Activity {

	private ImageView mImgRollingDice;
	public String MusicVoice = "2";// 音效開關 2=開 1=關
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			mImgRollingDice.setImageResource(R.drawable.logo9);

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first);
		setupViewComponent();
		img();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				final Intent mainIntent = new Intent(first.this,
						Tablet_Copy.class);
				startActivity(mainIntent);
				finish();//當跳到另一 Activity 時，讓 MainActivity 結束。
				// 這樣可以避免使用者按 back 後，又回到該 activity。
			}
		}, 500);
	}

	private void setupViewComponent() {
		mImgRollingDice = (ImageView) findViewById(R.id.imageView1);
	}

	private void img() {

		Resources res = getResources();
		final AnimationDrawable animDraw = (AnimationDrawable) res
				.getDrawable(R.drawable.anim_drawable);
		mImgRollingDice.setImageDrawable(animDraw);
		animDraw.start();

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(1250);//開始動畫延遲1.25秒塞圖片
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				animDraw.stop();
				handler.sendMessage(handler.obtainMessage());
			}
		}).start();
	}

}
