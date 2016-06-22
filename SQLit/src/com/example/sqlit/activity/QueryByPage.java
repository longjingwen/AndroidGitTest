package com.example.sqlit.activity;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import com.example.sqlit.R;
import com.example.sqlit.util.Dao.DBManager;
import com.example.sqlit.util.Dao.entity.Person;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class QueryByPage extends Activity {
	private ListView listview;
	private DBManager dbmanager;
	private MyAdapter myadapter;
	private List<Person> list = null;
	// private Click click;
	private Button firstPage;
	private Button prePage;
	private Button nextPage;
	private Button lastPage;
	private TextView tipInfo;

	int pageSize = 10;
	int pageNow = 1;
	int pageCount = 0;
	int itemCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.querybypage);

		listview = (ListView) findViewById(R.id.myPersonList);
		firstPage = (Button) findViewById(R.id.firstPage);
		prePage = (Button) findViewById(R.id.prePage);
		nextPage = (Button) findViewById(R.id.nextPage);
		lastPage = (Button) findViewById(R.id.lastPage);
		tipInfo = (TextView) findViewById(R.id.tipInfo);

		dbmanager = new DBManager(this);
		// add();
		itemCount = dbmanager.getCount();
		pageCount = (itemCount + pageSize - 1) / pageSize;
		list = dbmanager.QueryByPage(pageNow, pageSize);
		myadapter = new MyAdapter();
		listview.setAdapter(myadapter);
		tipInfo.setText("第" + pageNow + "页/共" + pageCount + "页");
		onClickListener();
	}

	private void onClickListener() {
		// TODO Auto-generated method stub
		firstPage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (pageNow != 1) {
					pageNow = 1;
					list = dbmanager.QueryByPage(pageNow, pageSize);
					tipInfo.setText("第" + pageNow + "页/共" + pageCount + "页");
					myadapter.notifyDataSetChanged();
				}

			}
		});
		prePage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (pageNow == 1) {
					Toast.makeText(QueryByPage.this, "当前已是首页", Toast.LENGTH_SHORT).show();
				} else {
					pageNow -= 1;
					list = dbmanager.QueryByPage(pageNow, pageSize);
					tipInfo.setText("第" + pageNow + "页/共" + pageCount + "页");
					myadapter.notifyDataSetChanged();
				}

			}
		});
		nextPage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (pageNow == pageCount) {
					Toast.makeText(QueryByPage.this, "当前已是最后一页", Toast.LENGTH_SHORT).show();
				} else {
					pageNow += 1;
					list = dbmanager.QueryByPage(pageNow, pageSize);
					tipInfo.setText("第" + pageNow + "页/共" + pageCount + "页");
					myadapter.notifyDataSetChanged();
				}

			}
		});
		lastPage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (pageNow != pageCount) {
					pageNow = pageCount;
					list = dbmanager.QueryByPage(pageNow, pageSize);
					tipInfo.setText("第" + pageNow + "页/共" + pageCount + "页");
					myadapter.notifyDataSetChanged();
				}
			}
		});

	}

	// class Click implements View.OnClickListener {
	//
	// @Override
	// public void onClick(View v) {
	// // TODO Auto-generated method stub
	// switch (v.getId()) {
	// case R.id.firstPage:
	// Toast.makeText(QueryByPage.this, "你按下了首页按钮", Toast.LENGTH_SHORT).show();
	// break;
	// case R.id.prePage:
	// Toast.makeText(QueryByPage.this, "你按下了上一页按钮", Toast.LENGTH_SHORT).show();
	// break;
	// case R.id.nextPage:
	// Toast.makeText(QueryByPage.this, "你按下了下一页按钮", Toast.LENGTH_SHORT).show();
	// break;
	// case R.id.lastPage:
	// Toast.makeText(QueryByPage.this, "你按下了末页按钮", Toast.LENGTH_SHORT).show();
	// break;
	//
	// }
	//
	// }
	//
	// }

	public void add() {
		ArrayList<Person> persons = new ArrayList<Person>();

		for (int i = 0; i < 100; i++) {
			Person person = new Person("Ella", 22 + i, "girl", "sichuan");
			persons.add(person);
		}

		dbmanager.add(persons);
		Toast.makeText(this, "插入数据完成", Toast.LENGTH_SHORT).show();
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Person getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return getItem(position).getId();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MyHolder myHolder = null;
			if (convertView == null) {
				myHolder = new MyHolder();
				convertView = LayoutInflater.from(QueryByPage.this).inflate(R.layout.simple_cursor_adapter_item, null);
				myHolder.name = (TextView) convertView.findViewById(R.id.name);
				myHolder.age = (TextView) convertView.findViewById(R.id.age);
				myHolder.sex = (TextView) convertView.findViewById(R.id.sex);
				myHolder.address = (TextView) convertView.findViewById(R.id.address);
				convertView.setTag(myHolder);
			} else {
				myHolder = (MyHolder) convertView.getTag();
			}
			myHolder.name.setText(getItem(position).getName());
			myHolder.age.setText(getItem(position).getAge() + "");
			myHolder.sex.setText(getItem(position).getSex() + "");
			myHolder.address.setText(getItem(position).getAddress());
			return convertView;
		}

	}

	class MyHolder {
		TextView name;
		TextView age;
		TextView sex;
		TextView address;
	}
}
