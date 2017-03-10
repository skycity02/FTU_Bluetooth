package com.ftu.ftu_bluetooth;
import java.lang.reflect.Field;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class TextInputDialog extends AlertDialog {
    private String mInputText;
    private String mTitle;
    private String mMsg;
    private Context mContext;
    private View mView;
    private EditText mFolderName;

    public TextInputDialog(Context context, String title, String msg, String text
    		,int theme) {
//        super(context, theme);
        super(context);
        mTitle = title;
        mMsg = msg;
        mInputText = text;
        mContext = context;
    }

    public String getInputText() {
        return mInputText;
    }

    protected void onCreate(Bundle savedInstanceState) {
        mView = getLayoutInflater().inflate(R.layout.textinput_dialog, null);

        setTitle(mTitle);
//        setMessage(mTitle);   
        setCancelable(false);

        mFolderName = (EditText) mView.findViewById(R.id.text);
        setView(mView);
        mFolderName.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);//.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        setButton(BUTTON_NEGATIVE, mContext.getString(android.R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            mInputText = mFolderName.getText().toString();
                            if(!((FTU_Bluetooth)mContext).checkPWD(mInputText)){
//                            	Log.d("MESSAGE", "false");
                            	Toast.makeText(mContext, mContext.getString(R.string.pwd_warn), Toast.LENGTH_SHORT).show();
                            	//用于不关闭对话框
                            	try { 
                            	Field field = dialog.getClass().getSuperclass().getSuperclass().getDeclaredField("mShowing"); 
                            	field.setAccessible(true); 
                            	field.set(dialog, false);
                            	} catch (Exception e) { 
                            	e.printStackTrace(); 
                            	}
                            }else{
//                            	Log.d("MESSAGE", "true");
                            	((FTU_Bluetooth)mContext).requestMessage();
                           		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                				if(imm != null){
                					imm.hideSoftInputFromWindow(mFolderName.getWindowToken(), 0);
                				}           		
        						//关闭对话框
                            	try {
                            	Field field = dialog.getClass().getSuperclass().getSuperclass().getDeclaredField("mShowing");
                            	field.setAccessible(true);
                            	field.set(dialog, true);
                            	} catch (Exception e) {
                            	e.printStackTrace();
                            	}
                            }
                        }
//                    }
                });
        setButton(BUTTON_POSITIVE, mContext.getString(android.R.string.cancel),
                new DialogInterface.OnClickListener() { 
	        public void onClick(DialogInterface dialog, int id) { 
	        	//关闭对话框
           	try {
           		InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
				if(imm != null){
					imm.hideSoftInputFromWindow(mFolderName.getWindowToken(), 0);
				}           		
           		Field field = dialog.getClass().getSuperclass().getSuperclass().getDeclaredField("mShowing");
           	field.setAccessible(true);
           	field.set(dialog, true);
           	} catch (Exception e) {
           	e.printStackTrace();
           	}
	     	   if(dialog!= null) dialog.cancel(); 
	        }
        });

        super.onCreate(savedInstanceState);
    }
}
