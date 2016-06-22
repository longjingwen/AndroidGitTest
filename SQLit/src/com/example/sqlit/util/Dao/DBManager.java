package com.example.sqlit.util.Dao;

import java.util.ArrayList;
import java.util.List;

import com.example.sqlit.util.DBHelper;
import com.example.sqlit.util.Dao.entity.Person;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
	private DBHelper dbhelper;
	private SQLiteDatabase db;

	public DBManager(Context context) {
		dbhelper = new DBHelper(context, null, null, 0);
		// 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0,
		// mFactory);
		// 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
		db = dbhelper.getWritableDatabase();
	}

	/**
	 * add persons
	 * 
	 * @param persons
	 */
	public void add(List<Person> persons) {
		db.beginTransaction(); // 开始事务
		try {
			for (Person person : persons) {
				db.execSQL("INSERT INTO person VALUES(null, ?, ?, ?,?)",
						new Object[] { person.getName(), person.getAge(), person.getSex(), person.getAddress() });
			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}

	/**
	 * update person's age
	 * 
	 * @param person
	 */
	public void updateAge(Person person) {
		ContentValues cv = new ContentValues();
		cv.put("age", person.getAge());
		db.update("person", cv, "name = ?", new String[] { person.getName() });
	}

	/**
	 * delete old person
	 * 
	 * @param person
	 */
	public void deleteOldPerson(Person person) {
		db.delete("person", "age >= ?", new String[] { String.valueOf(person.getAge()) });
	}

	/**
	 * query all persons, return list
	 * 
	 * @return List<Person>
	 */
	public List<Person> query() {
		ArrayList<Person> persons = new ArrayList<Person>();
		Cursor c = queryTheCursor();
		while (c.moveToNext()) {
			Person person = new Person();
			person.setId(c.getInt(c.getColumnIndex("_id")));
			person.setName(c.getString(c.getColumnIndex("name")));
			person.setAge(c.getInt(c.getColumnIndex("age")));
			person.setSex(c.getString(c.getColumnIndex("sex")));
			person.setAddress(c.getString(c.getColumnIndex("address")));
			persons.add(person);
		}
		c.close();
		return persons;
	}

	/**
	 * query all persons, return cursor
	 * 
	 * @return Cursor
	 */
	public Cursor queryTheCursor() {
		Cursor c = db.rawQuery("SELECT id as _id,name,age,sex,address FROM person", null);
		return c;
	}

	/**
	 * close database
	 */
	public void closeDB() {
		db.close();
	}

	public List<Person> QueryByPage(int NowPage, int PageSize) {
		// TODO Auto-generated method stub
		ArrayList<Person> list = new ArrayList<Person>();
		Cursor cursor = db.rawQuery("SELECT * FROM person limit ?,?",
				new String[] { (NowPage - 1) * PageSize + "", PageSize + "" });
		if (cursor != null) {
			while (cursor.moveToNext()) {
//				Person person = new Person();
//				person.setId(cursor.getInt(cursor.getColumnIndex("id")));
//				person.setAge(cursor.getInt(cursor.getColumnIndex("age")));
//				person.setName(cursor.getString(cursor.getColumnIndex("name")));
//				person.setSex(cursor.getString(cursor.getColumnIndex("sex")));
//				person.setAddress(cursor.getString(cursor.getColumnIndex("address")));
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				String sex = cursor.getString(cursor.getColumnIndex("sex"));
				int age = cursor.getInt(cursor.getColumnIndex("age"));
				String address = cursor.getString(cursor.getColumnIndex("address"));
				Person person2=new Person(id,name,age,sex,address);
				list.add(person2);

			}
		}
		cursor.close();// 关闭游标对象
		return list;
	}
	public int getCount() {
		Cursor c = db.rawQuery("SELECT * FROM person", null);
		return c.getCount();
	}
}
