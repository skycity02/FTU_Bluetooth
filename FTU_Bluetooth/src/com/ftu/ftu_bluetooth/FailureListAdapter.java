package com.ftu.ftu_bluetooth;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.ftu.ftu_bluetooth.FTU_Bluetooth.MenuItemOnClickListener;
import java.util.ArrayList;
import java.util.List;

public class FailureListAdapter extends ArrayAdapter<FailureInfo> {
	private LayoutInflater mInflater;

	protected Context mContext;
	private ArrayList<FailureInfo> mList;

	public FailureListAdapter(Context context, int resource,
			ArrayList<FailureInfo> objects) {
		super(context, resource, objects);
		mInflater = LayoutInflater.from(context);
		mContext = context;
		mList = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Log.d("MESSAGE", "failureListAdapter position: " + position);
		View view = null;
		if (convertView != null) {
			view = convertView;
		} else {
			view = mInflater.inflate(R.layout.failure_list_item, parent, false);
		}

		TextView id = (TextView) view.findViewById(R.id.failure_id);
		id.setText("" + (position + 1));

		TextView name = (TextView) view.findViewById(R.id.failure_name);
		String failure_name = mContext.getString(Constants.yaoxin_name[mList
				.get(position).failure_num + 5]);
		name.setText(failure_name);

		TextView issue = (TextView) view.findViewById(R.id.failure_issue);
		byte issue_id = (byte) (mList.get(position).failure_issue - 1);
		issue.setText(mContext.getString(Constants.failure_issue[mList
				.get(position).failure_issue - 1]));
		// issue.setTextColor(mList.get(position).failure_issue?
		// android.graphics.Color.RED: android.graphics.Color.BLACK);
		if (issue_id == 0x00) {
			issue.setTextColor(android.graphics.Color.YELLOW); // start
		} else if (issue_id == 0x01) {
			issue.setTextColor(android.graphics.Color.RED); // action
		} else if (issue_id == 0x02) {
			issue.setTextColor(android.graphics.Color.BLACK); // recover
		}

		TextView value = (TextView) view.findViewById(R.id.failure_value);
		value.setText(mList.get(position).failure_value);

		TextView time = (TextView) view.findViewById(R.id.failure_time);
		time.setText(Util.formatDateString(mContext,
				mList.get(position).failure_time.longValue()));
		// Log.d("MESSAGE", "Failure position: " + position + "time: " +
		// mList.get(position).failure_time.longValue()
		// + "time: " + Util.formatDateString(mContext,
		// mList.get(position).failure_time.longValue()));

		return view;
	}

	// public void refresh(ArrayList<MenuInfo> list){
	// mList = list;
	// notifyDataSetChanged();
	// }

}
