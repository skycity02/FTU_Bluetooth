package com.ftu.ftu_bluetooth;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SOEInfo {

//    public short soe_id;    
    public short soe_num;
//    public boolean hasSubmenu;
    public BigDecimal soe_time;
    public boolean soe_issue; //
    
    SOEInfo(){}
    /**
     * 
     * @param name_num b14-b0: 遥信序号
     * @param time
     * @param issue b15: 0=由合至分 1=由分至合
     */
    SOEInfo(short name_no, BigDecimal time, boolean issue){
//    	soe_id = id;    
    	soe_num = name_no;
//        hasSubmenu = submenu;
        soe_time = time;
        soe_issue = issue;
    }
}
