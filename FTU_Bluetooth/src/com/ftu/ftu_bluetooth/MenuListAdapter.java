package com.ftu.ftu_bluetooth;

import android.content.Context;
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

public class MenuListAdapter extends ArrayAdapter<MenuInfo> {
    private LayoutInflater mInflater;

    protected FTU_Bluetooth mContext;
    private ArrayList<MenuInfo> mList;

    public MenuListAdapter(FTU_Bluetooth context, int resource,
            ArrayList<MenuInfo> objects) {
        super(context, resource, objects);
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//    	Log.d("MESSAGE", "position: " + position);
        View view = null;
        if (convertView != null) {
            view = convertView;
        } else {
            view = mInflater.inflate(R.layout.menu_browser_item, parent, false);
        }

        ImageView menuicon = (ImageView) view.findViewById(R.id.menu_image);
		Drawable d = mContext.getResources().getDrawable(mList.get(position).menuIcon);
		d.setColorFilter(null);//remove the color filter.
		menuicon.setImageDrawable(d);
//        menuicon.setImageResource(mList.get(position).menuIcon);
        TextView menuname = (TextView) view.findViewById(R.id.menu_name);
        menuname.setText(mContext.getString(mList.get(position).menuNameId));
        ImageView gotosub = (ImageView) view.findViewById(R.id.goto_submenu);
        gotosub.setImageResource(mList.get(position).gotoSubmenuIcon);
        view.findViewById(R.id.goto_submenu_area).setOnClickListener(
        		mContext.new MenuItemOnClickListener(mContext, mList.get(position),
        		mList, this));
        view.findViewById(R.id.goto_submenu_area).setOnTouchListener(
        		new View.OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						if(event.getAction() == MotionEvent.ACTION_DOWN){
					    	v.setBackgroundResource(android.R.drawable.list_selector_background);
					    }else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL){
//					     v.setBackgroundDrawable(mResources.getDrawable(R.drawable.path));
					    	v.setBackgroundColor(Color.TRANSPARENT);
					    }
						return false;
					}
				}
        		);
        return view;
    }
    
    public void refresh(ArrayList<MenuInfo> list){
    	mList = list;  
        notifyDataSetChanged();
    }
//    btn.setOnTouchListener(new Button.OnTouchListener() {
//		   @Override
//		   public boolean onTouch(View v, MotionEvent event) {
//		    // TODO Auto-generated method stub
//		    if(event.getAction() == MotionEvent.ACTION_DOWN){
//		    	v.setBackgroundResource(com.android.internal.R.drawable.list_selected_holo_light);
//		    }else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL){
////		     v.setBackgroundDrawable(mResources.getDrawable(R.drawable.path));
//		    	v.setBackgroundColor(Color.TRANSPARENT);
//		    }
//		    return false;
//		}
//	});	

}
