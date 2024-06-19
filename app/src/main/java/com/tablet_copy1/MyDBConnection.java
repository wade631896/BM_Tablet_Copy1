package com.tablet_copy1;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.content.Context;

public class MyDBConnection extends SQLiteOpenHelper {
	/*
	 * private static final String Table1="Road"; private static final String
	 * Table2="Result";
	 * 
	 * private static final String DropTableSQL1="Drop Table if exists "+Table1;
	 * private static final String DropTableSQL2="Drop Table if exists "+Table2;
	 * 
	 * private static final String CreateTableSQL1=
	 * "Create Table if not exists "+Table1+" "+ "(Cno nvarchar(3),"+
	 * "RoadNo nvarchar(3),"+ "RoadNm nvarchar(20),"+ "Rco nvarchar(5),"+
	 * "Dco float)"; private static final String CreateTableSQL2=
	 * "Create Table if not exists "+Table2+" "+ "(Roadno nvarchar(3),"+
	 * "Custno nvarchar(9),"+ "CustLoc nvarchar(60),"+ "CustLoc1 nvarchar(60),"+
	 * "Fullnm nvarchar(60),"+ "Tel1 nvarchar(60),"+ "Tel2 nvarchar(60),"+
	 * "Lamploc nvarchar(20),"+ "PlantNm nvarchar(5),"+ "Watnum nvarchar(15),"+
	 * "LatPantnum nvarchar(24),"+ "Pantnum nvarchar(8),"+
	 * "Remark nvarchar(200),"+ "CopyMk nvarchar(1),"+ "Seq nvarchar(4))";
	 */

	public MyDBConnection(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// db.execSQL(CreateTableSQL1);
		// db.execSQL(CreateTableSQL2);
		// String sql =
		// "CREATE  TABLE RoadChecker (_id INTEGER PRIMARY KEY ,RoadNm VARCHAR,Dco VARCHAR,Rco VARCHAR)";
		// db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// db.execSQL(DropTableSQL1);
		// db.execSQL(DropTableSQL2);
		// onCreate(db);
		//db.execSQL("DROP TABLE IF EXISTS RoadChecker");
		//onCreate(db);
	}
}