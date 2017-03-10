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

public class SOEListAdapter extends ArrayAdapter<SOEInfo> {
    private LayoutInflater mInflater;

    protected Context mContext;
    private ArrayList<SOEInfo> mList;

    public SOEListAdapter(Context context, int resource,
            ArrayList<SOEInfo> objects) {
        super(context, resource, objects);
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//    	Log.d("MESSAGE", "SOEListAdapter position: " + position);
        View view = null;
        if (convertView != null) {
            view = convertView;
        } else {
            view = mInflater.inflate(R.layout.soe_list_item, parent, false);
        }

        TextView id = (TextView) view.findViewById(R.id.soe_id);
        id.setText("" + (position + 1));
        TextView name = (TextView) view.findViewById(R.id.soe_name);
        String soe_name = "";
        if(mList.get(position).soe_num < 20){
        	soe_name = mContext.getString(R.string.switch_in) + mList.get(position).soe_num;
        }else{
        	soe_name = mContext.getString(Constants.yaoxin_name[mList.get(position).soe_num - 20]);
        }
        name.setText(soe_name);
        TextView time = (TextView) view.findViewById(R.id.soe_time);
        time.setText(Util.formatDateString(mContext, mList.get(position).soe_time.longValue()));
//        Log.d("MESSAGE", "SOE position: " + position + "time: " + mList.get(position).soe_time.longValue()
//        		+ "time: " + Util.formatDateString(mContext, mList.get(position).soe_time.longValue()));

        TextView issue = (TextView) view.findViewById(R.id.soe_issue);
        issue.setText(mList.get(position).soe_issue? "由分至合":"由合至分");
        issue.setTextColor(mList.get(position).soe_issue? android.graphics.Color.RED: android.graphics.Color.BLACK);
        
        return view;
    }
    
//    public void refresh(ArrayList<MenuInfo> list){
//    	mList = list;  
//        notifyDataSetChanged();
//    }


}
