package adapter;

import helper.SQLiteHandler;

import java.util.List;

import com.example.pinglocation.R;

import model.User;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class CustomListAdapter extends SimpleCursorAdapter {
	private Context context;
	private LayoutInflater inflater;
	private Cursor c;
	private int layout;
	
	private class ViewHolder{
		TextView textName;
		TextView textPhone;
		
		ViewHolder(View v){
			textName = (TextView) v.findViewById(R.id.row_name);
			textPhone = (TextView) v.findViewById(R.id.row_phone);
		}
	}
	
	public CustomListAdapter(Context context, int layout, Cursor c, String[] from, int[] to){
		super(context,layout,c,from,to);
		this.context = context;
		this.layout = layout;
		this.c = c;
		this.inflater = LayoutInflater.from(context);
	}
	
	public View newView(Context context, Cursor cursor, ViewGroup parent){
		View view = inflater.inflate(layout, parent,false);
		view.setTag(new ViewHolder(view));
		
		return view;
	}
	
	public void bindView(View view, Context context, Cursor cursor){
	//	super.bindView(view, context, cursor);
		
		String name = cursor.getString(cursor.getColumnIndex(SQLiteHandler.KEY_NAME));
		String phone = cursor.getString(cursor.getColumnIndex(SQLiteHandler.KEY_PHONE));
		
		ViewHolder holder = (ViewHolder) view.getTag();
		
		holder.textName.setText(name);
		holder.textPhone.setText(phone);
		
	}
	
	
}
