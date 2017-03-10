package com.ftu.ftu_bluetooth;

import java.math.BigDecimal;

public class FailureInfo {

    public byte failure_num;
    public byte failure_issue;
    public String failure_value;
    public BigDecimal failure_time;
    
    FailureInfo(){}
    
    FailureInfo(byte name_no, byte issue, String value, BigDecimal time){
    	failure_num = name_no;
        failure_issue = issue;
        failure_value = value;
        failure_time = time;
    }
}
