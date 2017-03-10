package com.ftu.ftu_bluetooth;

import java.util.ArrayList;
import java.util.HashMap;

public class Constants {
	//These pages use listview to list menus, 1 ~ 20 reserved.
	final static public int MAIN_PAGE = 1;//1 is root page.
	final static public int TEST_PAGE = 4;//1 is root page.
	final static public int PAGE_1_1 = 2; //the first menu opened page.
//	final static public int PAGE_1_2 = 3; //the second menu opened page.
//	final static public int PAGE_1_4 = 5;
//	final static public int PAGE_1_5 = 6;
	final static public int PAGE_1_6 = 7;
	//These pages need specified layout.	
	final static public int PAGE_1_7 = 21;
	final static public int PAGE_1_1_1 = 22;
	final static public int PAGE_1_1_2 = 23;
	final static public int PAGE_1_1_3 = 24;
	final static public int PAGE_1_1_4 = 25;
//	final static public int PAGE_1_2_1 = 27;
//	final static public int PAGE_1_2_2 = 28;
	final static public int PAGE_1_3 = 29;
	final static public int PAGE_1_4_1 = 30;
	final static public int PAGE_1_4_2 = 31;
	final static public int PAGE_1_5_1 = 32;
	final static public int PAGE_1_5_2 = 33;
	final static public int PAGE_1_6_1 = 34;
	final static public int PAGE_1_6_2 = 35;
	final static public int PAGE_1_6_3 = 36;
	final static public int PAGE_1_2 = 37;
	final static public int PAGE_1_4 = 38;
	final static public int PAGE_1_5 = 39;

	final static public MenuInfo menu01 = new MenuInfo(R.drawable.ic_actual_value, R.string.main_menu_1,  R.drawable.click_next1, PAGE_1_1);
	final static public MenuInfo menu02 = new MenuInfo(R.drawable.ic_remote_control, R.string.main_menu_2,  R.drawable.click_next1, PAGE_1_2);
	final static public MenuInfo menu04 = new MenuInfo(R.drawable.ic_soe, R.string.main_menu_4,  R.drawable.click_next1, PAGE_1_4);
	final static public MenuInfo menu05 = new MenuInfo(R.drawable.ic_failure_info, R.string.main_menu_5,  R.drawable.click_next1, PAGE_1_5);
	final static public MenuInfo menu06 = new MenuInfo(R.drawable.ic_commu_info, R.string.main_menu_6,  R.drawable.click_next1, PAGE_1_6);
		
	final static int[] yaoxin_name = {R.string.yaoxin20, R.string.yaoxin21, R.string.yaoxin22, R.string.yaoxin23
		, R.string.yaoxin24, R.string.yaoxin25, R.string.yaoxin26, R.string.yaoxin27, R.string.yaoxin28 //26开始是电压越上限
		, R.string.yaoxin29, R.string.yaoxin30, R.string.yaoxin31, R.string.yaoxin32, R.string.yaoxin33
		, R.string.yaoxin34, R.string.yaoxin35, R.string.yaoxin36, R.string.yaoxin37};
	final static int[] failure_issue = {R.string.start, R.string.action, R.string.recover};
	
	final static public HashMap<Integer, MenuInfo> get_title_menu = 
    		new HashMap<Integer, MenuInfo>(){{
    	put(PAGE_1_1, menu01);
    	put(PAGE_1_2, menu02);
    	put(PAGE_1_4, menu04);
    	put(PAGE_1_5, menu05);
    	put(PAGE_1_6, menu06);
    }};
    
	final static public ArrayList<MenuInfo> main = new ArrayList<MenuInfo>(){
    	{
    	add(menu01);
    	add(menu02);
    	add(new MenuInfo(R.drawable.ic_para_setting, R.string.main_menu_3,  R.drawable.click_next1, PAGE_1_3));
    	add(menu04);
    	add(menu05);
    	add(menu06);
    	add(new MenuInfo(R.drawable.ic_authority, R.string.main_menu_7,  R.drawable.click_next1, PAGE_1_7));
    }
    };
    
    final static public ArrayList<MenuInfo> menu_1 = new ArrayList<MenuInfo>(){
    	{
    	add(new MenuInfo(R.drawable.ic_out_in_stat, R.string.menu_1_1,  R.drawable.click_next2, PAGE_1_1_1));
    	add(new MenuInfo(R.drawable.ic_protect_stat, R.string.menu_1_2,  R.drawable.click_next2, PAGE_1_1_2));
    	add(new MenuInfo(R.drawable.ic_realtime_power, R.string.menu_1_3,  R.drawable.click_next2, PAGE_1_1_3));
    	add(new MenuInfo(R.drawable.ic_others, R.string.menu_1_4,  R.drawable.click_next2, PAGE_1_1_4));
    }
    };
    
//    final static public ArrayList<MenuInfo> menu_2 = new ArrayList<MenuInfo>(){
//    	{
////    	add(new MenuInfo(R.drawable.ic_switch_off, R.string.menu_2_1,  R.drawable.click_next2, PAGE_1_2_1));
////    	add(new MenuInfo(R.drawable.ic_switch_on, R.string.menu_2_2,  R.drawable.click_next2, PAGE_1_2_2));
//    }
//    };
    
    final static public ArrayList<MenuInfo> menu_4 = new ArrayList<MenuInfo>(){
    	{
    	add(new MenuInfo(R.drawable.ic_soe1, R.string.menu_4_1,  R.drawable.click_next2, PAGE_1_4_1));
    	add(new MenuInfo(R.drawable.ic_soe2, R.string.menu_4_2,  R.drawable.click_next2, PAGE_1_4_2));
    }
    };
    
    final static public ArrayList<MenuInfo> menu_5 = new ArrayList<MenuInfo>(){
    	{
    	add(new MenuInfo(R.drawable.ic_failure_info_2, R.string.menu_5_1,  R.drawable.click_next2, PAGE_1_5_1));
    	add(new MenuInfo(R.drawable.ic_failure_info_2, R.string.menu_5_2,  R.drawable.click_next2, PAGE_1_5_2));
    }
    };
    
    final static public ArrayList<MenuInfo> menu_6 = new ArrayList<MenuInfo>(){
    	{
    	add(new MenuInfo(R.drawable.ic_net_para, R.string.menu_6_1,  R.drawable.click_next2, PAGE_1_6_1));
    	add(new MenuInfo(R.drawable.ic_serial_port, R.string.menu_6_2,  R.drawable.click_next2, PAGE_1_6_2));
       	add(new MenuInfo(R.drawable.ic_net_connection, R.string.menu_6_3,  R.drawable.click_next2, PAGE_1_6_3));
   }
    };
    
    //key: page id; value: page list name in this class;
    final static public HashMap<Integer, ArrayList<MenuInfo>> get_menu_list = 
    		new HashMap<Integer, ArrayList<MenuInfo>>(){{
    	put(MAIN_PAGE, main);
    	put(PAGE_1_1, menu_1);
//    	put(PAGE_1_2, menu_2);
    	put(PAGE_1_4, menu_4);
    	put(PAGE_1_5, menu_5);
    	put(PAGE_1_6, menu_6);
    }};
    
  //key: current page id; value: previous page id;
    final static public HashMap<Integer, Integer> get_previous_page = 
    		new HashMap<Integer, Integer>(){{
    	put(PAGE_1_1, MAIN_PAGE);
    	put(PAGE_1_2, MAIN_PAGE);
    	put(PAGE_1_3, MAIN_PAGE);
    	put(PAGE_1_4, MAIN_PAGE);
    	put(PAGE_1_5, MAIN_PAGE);
    	put(PAGE_1_6, MAIN_PAGE);
    	put(PAGE_1_7, MAIN_PAGE);
    	put(TEST_PAGE, MAIN_PAGE);
    	put(PAGE_1_1_1, PAGE_1_1);
    	put(PAGE_1_1_2, PAGE_1_1);
    	put(PAGE_1_1_3, PAGE_1_1);
    	put(PAGE_1_1_4, PAGE_1_1);
    	put(PAGE_1_4_1, PAGE_1_4);
    	put(PAGE_1_4_2, PAGE_1_4);
    	put(PAGE_1_6_1, PAGE_1_6);
    	put(PAGE_1_6_2, PAGE_1_6);
    	put(PAGE_1_6_3, PAGE_1_6);
    }};
    
//    final static public HashMap<Integer, Integer> get_page_layoutfile = 
//    		new HashMap<Integer, Integer>(){{
//    	put(PAGE_1_3, R.layout.p_1_7_password);
//    	put(PAGE_1_7, R.layout.p_1_7_password);
//    	put(PAGE_1_1_1, R.layout.p_1_1_1_kairuliang);
//    }};
    
//    PageInfo(int pageid, int previouid, int layout, 
//    		byte[] functioncode, byte[] request)
    final static public PageInfo p_1_3= new PageInfo(PAGE_1_3, MAIN_PAGE, R.layout.p_1_3_para_setting,
    		new byte[]{0x03, 0x08}, FTUMessage.REQ_READ_DZ_4);
    final static public PageInfo p_1_7= new PageInfo(PAGE_1_7, MAIN_PAGE, R.layout.p_1_7_password,
    		null, null);
    final static public PageInfo p_1_1_1 = new PageInfo(PAGE_1_1_1, PAGE_1_1, R.layout.p_1_1_1_kairuliang,
    		new byte[]{0x03, 0x03}, FTUMessage.REQ_READ_FTUYXYC);
    final static public PageInfo p_1_1_2 = new PageInfo(PAGE_1_1_2, PAGE_1_1, R.layout.p_1_1_2_protect_action,
    		new byte[]{0x03, 0x03}, FTUMessage.REQ_READ_FTUYXYC);
    final static public PageInfo p_1_1_3 = new PageInfo(PAGE_1_1_3, PAGE_1_1, R.layout.p_1_1_3_realtime_value,
    		new byte[]{0x03, 0x03}, FTUMessage.REQ_READ_FTUYXYC);
    final static public PageInfo p_1_1_4 = new PageInfo(PAGE_1_1_4, PAGE_1_1, R.layout.p_1_1_4_others,
    		new byte[]{0x03, 0x03}, FTUMessage.REQ_READ_FTUYXYC);
//    final static public PageInfo p_1_2_1= new PageInfo(PAGE_1_2_1, PAGE_1_2, R.layout.p_1_2_1_remote_off,
//    		new byte[]{0x06, 0x01}, FTUMessage.REQ_READ_FTUYXYC);
//    final static public PageInfo p_1_2_2= new PageInfo(PAGE_1_2_2, PAGE_1_2, R.layout.p_1_2_1_remote_off,
//    		new byte[]{0x06, 0x01}, FTUMessage.REQ_READ_FTUYXYC);
    final static public PageInfo p_1_2 = new PageInfo(PAGE_1_2, PAGE_1_2, R.layout.p_1_2_1_remote_off,
    		new byte[]{0x06, 0x01}, FTUMessage.REQ_READ_FTUYXYC);
//    final static public PageInfo p_1_4_1= new PageInfo(PAGE_1_4_1, PAGE_1_4, R.layout.p_1_4_1_soe_1,
//    		new byte[]{0x03, 0x04}, FTUMessage.REQ_READ_SOE_RE);
//    final static public PageInfo p_1_4_2= new PageInfo(PAGE_1_4_2, PAGE_1_4, R.layout.p_1_2_1_remote_off,
//    		new byte[]{0x03, 0x04}, FTUMessage.REQ_READ_SOE_RE);
    final static public PageInfo p_1_4 = new PageInfo(PAGE_1_4, MAIN_PAGE, R.layout.p_1_4_1_soe_1,
    		new byte[]{0x03, 0x04}, FTUMessage.REQ_READ_SOE_RE);
    final static public PageInfo p_1_5 = new PageInfo(PAGE_1_5, MAIN_PAGE, R.layout.p_1_4_1_soe_1,
    		new byte[]{0x03, 0x05}, FTUMessage.REQ_READ_FAULTINFO);
    final static public PageInfo p_1_6_1 = new PageInfo(PAGE_1_6_1, PAGE_1_6, R.layout.p_1_6_1_netcard,
    		new byte[]{0x03, 0x0a}, FTUMessage.REQ_READ_COMM);
    final static public PageInfo p_1_6_2 = new PageInfo(PAGE_1_6_2, PAGE_1_6, R.layout.p_1_6_2_serial,
    		new byte[]{0x03, 0x0a}, FTUMessage.REQ_READ_COMM);
    final static public PageInfo p_1_6_3 = new PageInfo(PAGE_1_6_3, PAGE_1_6, R.layout.p_1_6_3_nw_connect,
    		new byte[]{0x03, 0x0a}, FTUMessage.REQ_READ_COMM);
   
    final static public HashMap<Integer, PageInfo> get_page_info = 
    		new HashMap<Integer, PageInfo>(){{
    	put(PAGE_1_3, p_1_3);
    	put(PAGE_1_7, p_1_7);
    	put(PAGE_1_1_1, p_1_1_1);
    	put(PAGE_1_1_2, p_1_1_2);
    	put(PAGE_1_1_3, p_1_1_3);
    	put(PAGE_1_1_4, p_1_1_4);
//    	put(PAGE_1_2_1, p_1_2_1);
//    	put(PAGE_1_2_2, p_1_2_2);
    	put(PAGE_1_2, p_1_2);
//    	put(PAGE_1_4_1, p_1_4_1);
//    	put(PAGE_1_4_2, p_1_4_2);
    	put(PAGE_1_4, p_1_4);
    	put(PAGE_1_5, p_1_5);
    	put(PAGE_1_6_1, p_1_6_1);
    	put(PAGE_1_6_2, p_1_6_2);
    	put(PAGE_1_6_3, p_1_6_3);
   }};
}
