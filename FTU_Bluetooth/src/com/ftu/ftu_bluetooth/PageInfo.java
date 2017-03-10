package com.ftu.ftu_bluetooth;

import java.util.ArrayList;

public class PageInfo {
	
	public int currentPageId;    
    public int previousPageId;
    public int layoutXml;
//    public ArrayList<MenuInfo> menuList = null;
//    public MenuInfo titleMenuInfo = null;
    public byte[] functionCode = new byte[2];
    public byte[] requestCode = null;
    
    PageInfo(){}
    PageInfo(int pageid, int previouid, int layout
    		, byte[] functioncode, byte[] request){
    	currentPageId = pageid;    
        previousPageId = previouid;
        layoutXml = layout;
//        menuList = menulist;
//        titleMenuInfo = menuinfo;
        functionCode = functioncode;
        requestCode = request;
    }
    
}
