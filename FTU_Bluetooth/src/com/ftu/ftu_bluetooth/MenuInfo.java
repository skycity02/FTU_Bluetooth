package com.ftu.ftu_bluetooth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuInfo {

    public int menuIcon;    
    public int menuNameId;
//    public boolean hasSubmenu;
    public int gotoSubmenuIcon;
    public int nextPageId;
    
    MenuInfo(){}
    MenuInfo(int icon, int name, int gotoSubmenu, int nextpage){
    	menuIcon = icon;    
        menuNameId = name;
//        hasSubmenu = submenu;
        gotoSubmenuIcon = gotoSubmenu;
        nextPageId = nextpage;
    }
}
