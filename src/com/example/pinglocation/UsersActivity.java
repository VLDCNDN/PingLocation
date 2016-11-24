package com.example.pinglocation;

import helper.SQLiteHandler;
import adapter.CustomListAdapter;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;

public class UsersActivity extends Activity {

	CustomListAdapter dataAdapter;
	ListView listView;
	SQLiteHandler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_users);

		db = new SQLiteHandler(this);
		listView = (ListView) findViewById(R.id.list);

		populateListView();
		listClicked();
	}

	public void populateListView() {
		Cursor cursor = db.getAllUser();

		String[] from = new String[] { SQLiteHandler.KEY_NAME,
				SQLiteHandler.KEY_PHONE };
		int[] to = new int[] { R.id.row_name, R.id.row_phone };

		dataAdapter = new CustomListAdapter(this, R.layout.user_list_row,
				cursor, from, to);

		listView.setAdapter(dataAdapter);

	}

	public void listClicked() {
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent i = new Intent(UsersActivity.this, UserInfoActivity.class);
				Cursor cursor = db.getSelectedAccountInfo(id);
				Bundle bundle = new Bundle();
				
				if(cursor.moveToFirst()){
					String name = cursor.getString(cursor.getColumnIndex(SQLiteHandler.KEY_NAME));
					String phone = cursor.getString(cursor.getColumnIndex(SQLiteHandler.KEY_PHONE));
					String email = cursor.getString(cursor.getColumnIndex(SQLiteHandler.KEY_EMAIL));
					String uid = cursor.getString(cursor.getColumnIndex(SQLiteHandler.KEY_UID));
					
					bundle.putString(SQLiteHandler.KEY_NAME, name);
					bundle.putString(SQLiteHandler.KEY_PHONE, phone);
					bundle.putString(SQLiteHandler.KEY_EMAIL, email);
					bundle.putString(SQLiteHandler.KEY_UID, uid);
					i.putExtras(bundle);
				}
				
				cursor.close();
				startActivity(i);
				
			}
		});
	}

}
