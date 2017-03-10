package com.ftu.ftu_bluetooth;

import com.ftu.ftu_bluetooth.FTUMessage.ReadFtuxyc;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShowPage {
	Context mContext;
	View mView;
	ShowPage(Context ctx, View v){
		mContext = ctx;
		mView = v;
	}
//	final public static void showReportPage(int pageID){
//		switch(pageID){
//			case Constants.PAGE_1_1_1:
////				showYaoxin_PAGE_1_1_1();
//				break;
//		}
//				
//		
//	}
//	void showYaoxin_PAGE_1_1_1(ReadFtuxyc readftu){
//		int[] ids = new int[]{R.id.kairu01, R.id.kairu02, R.id.kairu03, R.id.kairu04, R.id.kairu05,
//				R.id.kairu06, R.id.kairu07, R.id.kairu08, R.id.kairu09, R.id.kairu10,
//				R.id.kairu11, R.id.kairu12, R.id.kairu13, R.id.kairu14, R.id.kairu15,
//				R.id.kairu16, R.id.kairu17, R.id.kairu18, R.id.kairu19, R.id.kairu20};
//		for (int i = 0; i < 20; i++){
//		TextView view = (TextView) mView.findViewById(ids[i]);
//		view.setText("D"+ (i + 1) + "：" + readftu.yaoXin[i]);
//		}
//	}
}
