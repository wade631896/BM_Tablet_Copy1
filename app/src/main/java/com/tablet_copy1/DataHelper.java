package com.tablet_copy1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataHelper extends SQLiteOpenHelper {

	@Override
	public synchronized void close() {
		// TODO Auto-generated method stub
		super.close();
	}

	public DataHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		// String sql =
		// "CREATE  TABLE RoadChecker (_id INTEGER PRIMARY KEY ,RoadNm VARCHAR,Dco VARCHAR,Rco VARCHAR)";
		// db.execSQL(sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}