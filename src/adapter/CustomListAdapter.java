package adapter;

import java.util.List;

import com.example.pinglocation.R;

import model.User;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<User> userAccounts;
	
	public CustomListAdapter(Activity activity, List<User> users){
		this.activity = activity;
		this.userAccounts = users;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return userAccounts.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return userAccounts.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if(inflater == null)
			inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(convertView == null)
			convertView = inflater.inflate(R.layout.user_list_row, null);
			
		TextView name = (TextView) convertView.findViewById(R.id.row_name);
		TextView phone = (TextView) convertView.findViewById(R.id.row_phone);
		TextView location = (TextView) convertView.findViewById(R.id.row_location);
		
		User user = userAccounts.get(position);
		
		name.setText(user.getName());
		phone.setText(user.getPhone());
		location.setText(user.getLongitude() + "," + user.getLatitude());
		
		
		return convertView;
	}

}
