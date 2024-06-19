package com.tablet_copy1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.bm_tablet_copy1.R;

public class Page3Adapter extends BaseAdapter {
	String Data1=null; //表位置
	String Data2=null; //表／番號6
	String Data3=null; //供氣壓力
	String Data4=null; //壓力係數
	String Data5=null; //燈表別
	private LayoutInflater adapterLayoutInflater;
	public Page3Adapter(Context c, String sendData1, String sendData2, String sendData3, String sendData4, String sendData5){
		Data1=sendData1;
		Data2=sendData2;
		Data3=sendData3;
		Data4=sendData4;
		Data5=sendData5;
		
		adapterLayoutInflater = LayoutInflater.from(c);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		TagView tag;
		if(view == null){
			view = adapterLayoutInflater.inflate(R.layout.listviewpage3, null); //連結listviewpage3.xml
			tag = new TagView(
					(TextView)view.findViewById(R.id.ListTextView1),
					(TextView)view.findViewById(R.id.ListTextView2));    //listviewpage3的TextView連結
			view.setTag(tag);  //顯示view
		}
		else{
			tag = (TagView)view.getTag();
		}
		//tag.image.setBackgroundResource(R.drawable.icon);
		//tag.button.setText("button"+position);
		switch (position) {    //依照每一列給值
		case 0:
			tag.text.setText("表位置：");
			tag.text1.setText(Data1);
			break;
		case 1:
			tag.text.setText("表/番 號：");
			tag.text1.setText(Data2);
			break;
		case 2:
			tag.text.setText("供氣壓力：");
			tag.text1.setText(Data3);
			tag.text1.setTextColor(android.graphics.Color.RED);
			tag.text.setTextSize(35); 
			tag.text1.setTextSize(35); 
			break;
		case 3:
			tag.text.setText("壓力係數：");
			tag.text1.setText(Data4);
			break;
		case 4:
			tag.text.setText("燈表別：");
			tag.text1.setText(Data5);
			break;
		}
		
		return view;
	}
	public class TagView{
		TextView text;
		TextView text1;
		public TagView(TextView text,TextView text1){  //建立TextView物件
			this.text = text;
			this.text1 = text1;
			
		}
	}
}
