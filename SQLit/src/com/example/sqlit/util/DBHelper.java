package com.example.sqlit.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	public static final String Database = "MySqlite.db";

	public DBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, Database, null, 1);
	}

	// ���ݿ��һ�α�����ʱonCreate�ᱻ����
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table if not exists person (id integer primary key autoincrement,name varchar(10),age integer,sex varchar(2),address varchar(50))";
		db.execSQL(sql);
	}

	// ���DATABASE_VERSIONֵ����Ϊ2,ϵͳ�����������ݿ�汾��ͬ,�������onUpgrade
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
