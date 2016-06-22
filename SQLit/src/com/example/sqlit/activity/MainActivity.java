package com.example.sqlit.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.sqlit.R;
import com.example.sqlit.util.Dao.DBManager;
import com.example.sqlit.util.Dao.entity.Person;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	private DBManager mgr;
	private ListView listView;
	private SimpleCursorAdapter sua;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listView = (ListView) findViewById(R.id.listView);
		// 初始化DBManager
		mgr = new DBManager(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 应用的最后一个Activity关闭时应释放DB
		mgr.closeDB();
	}

	// Cursor的使用
	public void QuaryByCursor(View view) {
		Cursor cursor = mgr.queryTheCursor();
		String[] from={"name","age","sex","address"};
		int[] to={R.id.name,R.id.age,R.id.sex,R.id.address};
		sua=new SimpleCursorAdapter(this,R.layout.simple_cursor_adapter_item,cursor, from, to,0);
		listView.setAdapter(sua);
	}

	public void add(View view) {
		ArrayList<Person> persons = new ArrayList<Person>();

		Person person1 = new Person("Ella", 22, "girl", "sichuan");
		Person person2 = new Person("Jenny", 22, "girl", "beijing");
		Person person3 = new Person("Jessica", 23, "boy", "shanghai");
		Person person4 = new Person("Kelly", 23, "boy", "zhejiang");
		Person person5 = new Person("Jane", 25, "girl", "shenzheng");

		persons.add(person1);
		persons.add(person2);
		persons.add(person3);
		persons.add(person4);
		persons.add(person5);

		mgr.add(persons);
		Toast.makeText(this, "插入数据完成", 1500).show();
	}

	public void update(View view) {
		Person person = new Person();
		person.setName("Jane");
		person.setAge(30);
		mgr.updateAge(person);
		Toast.makeText(this, "更新数据完成", 1500).show();
	}

	public void delete(View view) {
		Person person = new Person();
		person.setAge(30);
		mgr.deleteOldPerson(person);
		Toast.makeText(this, "删除数据", 1500).show();
	}

	public void query(View view) {
		List<Person> persons = mgr.query();
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (Person person : persons) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name", person.getName());
			map.put("address", person.getAge() + " years old, " + person.getAddress());
			list.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, list, android.R.layout.simple_list_item_2,
				new String[] { "name", "address" }, new int[] { android.R.id.text1, android.R.id.text2 });
		listView.setAdapter(adapter);
		Toast.makeText(this, "查询数据完成", 1500).show();
	}

	public void queryTheCursor(View view) {
		Cursor c = mgr.queryTheCursor();
		startManagingCursor(c); // 托付给activity根据自己的生命周期去管理Cursor的生命周期
		CursorWrapper cursorWrapper = new CursorWrapper(c) {
			@Override
			public String getString(int columnIndex) {
				// 将简介前加上年龄
				if (getColumnName(columnIndex).equals("address")) {
					int age = getInt(getColumnIndex("age"));
					return age + " years old, " + super.getString(columnIndex);
				}
				return super.getString(columnIndex);
			}
		};
		// 确保查询结果中有"_id"列
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursorWrapper,
				new String[] { "name", "address" }, new int[] { android.R.id.text1, android.R.id.text2 });
		ListView listView = (ListView) findViewById(R.id.listView);
		listView.setAdapter(adapter);
	}
}
