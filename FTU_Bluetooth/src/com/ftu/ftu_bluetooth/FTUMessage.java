package com.ftu.ftu_bluetooth;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.util.Log;

public class FTUMessage {
	final public static byte[] REQ_READ_FTUYXYC = {0x7A, 0x74, (byte) 0xff, 0x00, 0x02, 0x00, 0x03, 0x03, 0x07, 0x01};
	final public static byte[] REQ_READ_SOE_RE = {0x7A, 0x74 , (byte) 0xFF , 0x00 , 0x04 , 0x00 , 0x03 , 0x04 , (byte) 0xFF , (byte) 0xFF , 0x08 , 0x03};
	final public static byte[] REQ_READ_SOE_CLEAR = {
		0x7A, 0x74 , (byte) 0xFF , 0x00 , 0x04 , 0x00 , 0x03 , 0x04 , (byte) 0xFF , (byte) 0xFE , 0x07 , 0x03};
	final public static byte[] REQ_READ_DZ_1 = {//from 480, get 480 bytes
		0x7A, 0x74, (byte) 0xff, 0x00, 0x06, 0x00, 0x03, 0x08, (byte) 0xE0, 0x01, (byte) 0xE0, 0x01, (byte) 0xD2, 0x02};
	final public static byte[] REQ_READ_DZ_2 = {//from 00, get 480 bytes
		0x7A, 0x74, (byte) 0xff, 0x00, 0x06, 0x00, 0x03, 0x08, 0x00, 0x00, (byte) 0xE0, 0x01, (byte) 0xF1, 0x01};
	final public static byte[] REQ_READ_DZ_3 = {//from 480, get 120 bytes
		0x7A, 0x74, (byte) 0xff, 0x00, 0x06, 0x00, 0x03, 0x08, (byte) 0xE0, 0x01, 0x78, 0x00, 0x69, 0x02};
	final public static byte[] REQ_READ_DZ_4 = {//from 00, get 300 bytes
		0x7A, 0x74, (byte) 0xff, 0x00, 0x06, 0x00, 0x03, 0x08, 0x00, 0x00, (byte) 0x2C, 0x01, (byte) 0x3D, 0x01};
	final public static byte[] REQ_READ_DZ_5 = {//from 300, get 300 bytes
		0x7A, 0x74, (byte) 0xff, 0x00, 0x06, 0x00, 0x03, 0x08, (byte) 0x2C, 0x01, (byte) 0x2C, 0x01, (byte) 0x6A, 0x01};
	final public static byte[] REQ_YK_YUZHI = {0x7A, 0x74, (byte) 0xff, 0x00, 0x04, 0x00, 0x06, 0x01, 0x01, 0x00, 0x0B, 0x01};
	final public static byte[] REQ_READ_COMM = {0x7A, 0x74, (byte) 0xff, 0x00, 0x02, 0x00, 0x03, 0x0A, 0x0E, 0x01};
	final public static byte[] REQ_READ_FAULTINFO = {
		0x7A, 0x74 , (byte) 0xFF , 0x00 , 0x04 , 0x00 , 0x03 , 0x05 , (byte) 0xFF , (byte) 0xFF , 0x09 , 0x03};
	final public static byte[] REQ_READ_TIME = {0x7A, 0x74, (byte) 0xff, 0x00, 0x02, 0x00, 0x03, 0x03, 0x07, 0x01};
	final public static byte[] HEART_BEAT = {0x7A, 0x74, (byte) 0xff, 0x00, 0x02, 0x00, 0x01, 0x00, 0x02, 0x01};
//	final public static byte[] REQ_WRITE_DZ_1 = {//from 00, write 300 bytes
//		0x7A, 0x74, (byte) 0xff, 0x00, 0x32, 0x01, 0x04, 0x08, 0x00, 0x00, (byte) 0x2C, 0x01, 
//		0x7F, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x08, 0x00, 0x09, 0x00, 0x0a, 0x00, 0x01, 0x00, 0x02, 0x00, 
//		0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
//		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
//		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x04, 
//		0x00, 0x05, 0x00, 0x06, 0x00, 0x07, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x01, 0x00, 0x00, 0x0c, 0x00, 
//		(byte)0x0D, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
//		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
//		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
//		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
//		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
//		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
//		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
//		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
//		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
//		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
//		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
//		0x3d, 0x02};
	final public static byte[] REQ_WRITE_DZ_2 = {//from 300, write 300 bytes
		0x7A, 0x74, (byte) 0xff, 0x00, 0x32, 0x01, 0x04, 0x08, (byte) 0x2C, 0x01, (byte) 0x2C, 0x01, //head
		0x00, 00, 00, 00, 00, 00, 0x00, 00, 0x00, 00, 0x00, 00, 0x00, 00, 0x00, 00, 0x00, 00, 0x00, 
		00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 
		00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 
		00, 00, 00, 00, 00, 00, 00, 00, 00, 0x00, 00, 0x00, 00, 0x00, 00, 0x00, 00, 00, 00, 00, 00, 00, 00, 
		0x00, 0x00, 00, 00, 0x00, 00, 0x00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 
		00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 
		00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 
		00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 
		00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 
		00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 
		00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 
		00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 
		00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 
		00, 00, 00, 00, 00, 00,
		(byte) 0x98, 0x01};

	
	//-------- non-static members
	public byte[] bytes = new byte[550];
	public byte[] functionCode = new byte[]{bytes[6], bytes[7]};
	public int sizeActual;
	public int sizeExpected;
	public void clear(){
		sizeActual = 0;
		sizeExpected = 0;
//		bytes = null;		
	}
	
	public class ReadFtuxyc extends FTUMessage{
		public byte[] bytes = new byte[508];
		boolean[] yaoXin = new boolean[48];
		String[] yaoCe = new String[24];
		
		ReadFtuxyc(){
			if(bytes != null){
				setYaoxin();
				setYaoCe();
			}
		}
		ReadFtuxyc(byte[] bytesArray){
			bytes = bytesArray;
			sizeExpected = bytesArray.length;
			setYaoxin();
			setYaoCe();
		}
		
		public void reSet(byte[] bytesArray){

//			bytes = bytesArray;
			sizeExpected = bytesArray.length;
//			bytes = new byte[bytesArray.length];
			for(int i = 0; i < bytes.length; i++){
				bytes[i] = bytesArray[i];
			}
			setYaoxin();
			setYaoCe();
		}
		
		public void setYaoxin(){
			byte[] aa = new byte[]{bytes[8], bytes[9], bytes[10], bytes[11], bytes[12], bytes[13]};
			yaoXin = Util.bytesToBooleanLittle(aa);
		}
		
		public void setYaoCe(){
			Integer[] ce = new Integer[24];
			int j = 0;
			for(int i = 14; i < 14 + 24*4; i+=4){
				ce[j++] = Util.byteArray2intLittle(bytes, i);
			}
			for(int i = 0; i < 24; i++){				
				switch(i){
				case 6:
				case 7:
				case 8:
				case 10:
					yaoCe[i] = Util.floatToDecimal(ce[i], 3).toString() + "A";
					break;
				case 14:
					yaoCe[i] = Util.floatToDecimal(ce[i], 1).toString() + "W";
					break;
				case 15:
					yaoCe[i] = Util.floatToDecimal(ce[i], 3).toString() + "Var";
					break;
				case 16:
					yaoCe[i] = Util.floatToDecimal(ce[i], 4).toString();
					break;
				case 17:
					yaoCe[i] = Util.floatToDecimal(ce[i], 3).toString() + "Hz";
					break;
				case 18:
					yaoCe[i] = Util.floatToDecimal(ce[i], 1).toString();
					break;
				case 19:
					yaoCe[i] = Util.floatToDecimal(ce[i], 3).toString() + "V";
					break;
				case 20:
				case 21:
					yaoCe[i] = Util.floatToDecimal(ce[i], 2).toString() + "Kwh";
					break;
				case 22:
				case 23:
					yaoCe[i] = Util.floatToDecimal(ce[i], 2).toString() + "Kvarh";
					break;
				default:
					yaoCe[i] = Util.floatToDecimal(ce[i], 2).toString() + "V";
					break;									
				}
			}
			
		}
		
	}
	
	public class ParaSetting extends FTUMessage{
		public byte[] bytes = new byte[508];
		public boolean[] funCtrl = new boolean[32];
		int head = 12; //The 600 bytes fixed value from 12.
		public ParaTableValue[] tableValue = new ParaTableValue[9];

		BigDecimal fault_holding;
		BigDecimal switching_off_time; //失电延时分闸
		BigDecimal get_power_time; //得电
		BigDecimal single_off_time; //单侧
		BigDecimal switch_time;
		BigDecimal once_time;
		BigDecimal twice_time;
		BigDecimal three_time;
		BigDecimal post_acce_time;
		BigDecimal harmonic_percent;
		BigDecimal evading_Inrush_multi;
		BigDecimal evading_Inrush_time;
		byte inverse_time_lag;
		
		ParaSetting(){
			if(bytes != null){
				setFunCtrl();
				setPanelValues();
				for(int i = 0; i < tableValue.length; i++){
					tableValue[i] = new ParaTableValue();
				}
				setTableValues();
			}
		}
		
		public void reSet(byte[] bytesArray, int from, int offset){

//			bytes = bytesArray;
//			sizeExpected = bytesArray.length;
//			bytes = new byte[bytesArray.length];
			for(int i = from; i < offset + from; i++){
				bytes[i] = bytesArray[i];
			}
			setFunCtrl();
			setPanelValues();
			setTableValues();
		}
		public void reSet(byte[] bytesArray){

//			bytes = bytesArray;
			sizeExpected = bytesArray.length;
//			bytes = new byte[bytesArray.length];
			for(int i = 0; i < bytes.length; i++){
				bytes[i] = bytesArray[i];
			}
			setFunCtrl();
			setPanelValues();
			setTableValues();
		}
		public void setFunCtrl(){
			byte[] aa = new byte[]{bytes[0 + head], bytes[1 + head], bytes[2 + head], bytes[3 + head]};
			funCtrl = Util.bytesToBooleanLittle(aa);
//			for(int i = 0; i < funCtrl.length; i++){
//				Log.d("MESSAGE", "funCtrl[i]: " + funCtrl[i]);
//			}
		}
		
		public void setPanelValues(){
		    fault_holding = Util.floatToDecimal(Util.byteLength2intLitt(bytes, 6 + head), 2);
		    switching_off_time = Util.floatToDecimal(Util.byteLength2intLitt(bytes, 14 + head), 2);//失电延时分闸
			get_power_time = Util.floatToDecimal(Util.byteLength2intLitt(bytes, 16 + head), 2);//得电延时合闸
			single_off_time = Util.floatToDecimal(Util.byteLength2intLitt(bytes, 18 + head), 2); //单侧得电

			switch_time = Util.floatToDecimal(Util.byteLength2intLitt(bytes, 72 + head), 0); //重合闸次数
//		    Log.d("MESSAGE", "switch_time: " + switch_time.intValue());
			once_time = Util.floatToDecimal(Util.byteLength2intLitt(bytes, 74 + head), 2);
			twice_time = Util.floatToDecimal(Util.byteLength2intLitt(bytes, 76 + head), 2);
			three_time = Util.floatToDecimal(Util.byteLength2intLitt(bytes, 78 + head), 2);
			post_acce_time = Util.floatToDecimal(Util.byteLength2intLitt(bytes, 80 + head), 2); //后加速

			harmonic_percent = Util.floatToDecimal(Util.byteLength2intLitt(bytes, 8 + head), 0); //谐波百分比
			evading_Inrush_multi = Util.floatToDecimal(Util.byteLength2intLitt(bytes, 10 + head), 2);
			evading_Inrush_time = Util.floatToDecimal(Util.byteLength2intLitt(bytes, 12 + head), 2);
		}
		
		public void setTableValues(){
			int j = 0;
			for(int i = 0; i < tableValue.length*16; i+=16, j++){
				tableValue[j].bEnable = bytes[88 + i + head] == 0x01 ? true:false;
				tableValue[j].bTrip = bytes[89 + i + head];
				tableValue[j].set = Util.floatToDecimal(Util.byteLength2intLitt(bytes, 92 + i + head), 2);
				tableValue[j].time = Util.floatToDecimal(Util.byteLength2intLitt(bytes, 94 + i + head), 2);
				if(j == 8){
					inverse_time_lag = bytes[91 + i + head];
//					Log.d("MESSAGE", "inverse_time_lag: " + inverse_time_lag);
				}
			}
//			for(int i = 0; i < tableValue.length; i++){
//				Log.d("MESSAGE", "tableValue[i]: " + tableValue[i]);
//			}
		}
	}
	
	public class ParaTableValue{ //16字节
		public boolean bEnable;	//软压板 0=取消 1=投入
		public byte bTrip;	//出口类型 0=告警 1=出口
//		BYTE bRelayID;	//元件类型  <---不要去改
//		BYTE bRsv;	//反时限类型 <---不要去改
		public BigDecimal set;	//定值  0.01A/v
		public BigDecimal time;	//时限  0.01秒
		//后面8字节没用到
	}
	
	public class SOEMessage extends FTUMessage{
		
//		ArrayList<SOEInfo> soe_info = new ArrayList<SOEInfo>();
		final static int SOE_LENGTH = 300;//255;
		SOEInfo[] soe_info = new SOEInfo[SOE_LENGTH];
		ArrayList<SOEInfo> soe_list = new ArrayList<SOEInfo>();
		int id_from = 0;
		int msg_size = 0;
		int head = 10; //head is 10 bytes.
		BigDecimal time;
		short soe_num = 0;
		boolean	soe_issue =	false;
		SOEMessage(){
			if(bytes != null){
				reSet(bytes);
			}
		}
		
		public void getList(){
//			int j = 0;//id_from;
			int j = id_from;
			for(int i = head; i < head + msg_size*8; i+=8, j++){
				String second = Util.byteLength2intLitt(bytes, i + 4) + "";
//				Log.d("MESSAGE", "SOE j: " + j + " second:" + second);
				if(second.length() == 1){ //1位数
					second = "00" + second;
				}else if(second.length() == 2){ //2位数
					second = "0" + second;
				}else if(second.length() == 0){ //为0
					second = "000";
				}
//				Log.d("MESSAGE", "SOE j: " + j + " second:" + second);
				time = new BigDecimal(Util.byteArray2intLittle(bytes, i) + "" + 
						second);

				soe_num = (short)(Util.byteLength2intLitt(bytes, i + 6 ) & (short)0x7FFF);
				soe_issue =	((Util.byteLength2intLitt(bytes, i + 6) >> 15) & 0x01) == 0x01;
				if( j >= SOE_LENGTH){//id_from + msg_size 是j的最大值
//					Log.d("MESSAGE", "SOE j >= soe_info.length, j: " + j);
					soe_info[j - SOE_LENGTH] = new SOEInfo(soe_num, time, soe_issue);
				}else{
					soe_info[j] = new SOEInfo(soe_num, time, soe_issue);
				}
//				if(soe_info[j] == null || soe_info[j].soe_time == null || soe_info[j].soe_time.compareTo(time) < 0){//soe_info[j] == null || 
//					Log.d("MESSAGE", "SOE j: " + j + " time:" + time);
//					soe_info[j] = new SOEInfo(soe_num, time, soe_issue);
//				}
//				Log.d("MESSAGE", "SOE j: " + j + " time:" + time);
			}
		}
		
		public void reSet(byte[] bytesArray){
			bytes = bytesArray;
			
			id_from = Util.getShortLittle(bytes, 8); //SOE id start from. 
			msg_size = (Util.getShortLittle(bytes, 4) - 4)/8; //how many SOE in this msg.
			
			time = new BigDecimal(Util.byteArray2intLittle(bytes, head) + "" + 
					Util.byteLength2intLitt(bytes, head + 4));//信息里第一条即最老的soe
//			Log.d("MESSAGE", "id_from : " + id_from);
//			Log.d("MESSAGE", "msg_size: " + msg_size);
			if(id_from + msg_size == 0) return;
//			if(msg_size < 60){
////				FTU_Bluetooth.soeStatus = FTU_Bluetooth.SOE_FINISH;
////				Log.d("MESSAGE", "soeStatus = SOE_FINISH");
//			}else if(msg_size == 60){
//				FTU_Bluetooth.soeStatus = FTU_Bluetooth.SOE_WAIT_END;
//			}//id_from + msg_size <= soe_info.length
//			Log.d("MESSAGE", "id_from + msg_size: " + (id_from + msg_size));
//			if( soe_info[id_from + msg_size - 1] != null){ //新来的soe全部已经收录
//				if(soe_info[id_from].soe_time == time){//如果序号跟时间都跟存在表里的一样，不需要更新。
//					//do nothing
//				}else{//如果序号跟时间跟表里的不一样，需要更新表里的数据，适用于时间被改变的情况
//					getList();			
//				}
//				
//			}else{//把报文里的信息更新到表里去，适用于有新soe的情况。
				getList();
//			}
			soe_list.clear();
			int k = 0;
			for(int i = SOE_LENGTH - 1 ; i >= 0 && k < SOE_LENGTH; i--){
				if(soe_info[i] != null){
					soe_list.add(soe_info[i]);
					k++;
				}
				
			}
			Collections.sort(soe_list, new SOEComparator());
			if(soe_list.size() > 60){
				for(int j = soe_list.size() - 1; j >= 60; j--){
					soe_list.remove(j);
				}
			}
			
		}
	}
	private class SOEComparator implements Comparator<SOEInfo> {

		@Override
		public int compare(SOEInfo lhs, SOEInfo rhs) {
			// TODO Auto-generated method stub
			return rhs.soe_time.compareTo(lhs.soe_time);
		}
		
	}
	//------------------ Failure Info 故障信息 ----------------------------------------
    public class FailureMessage extends FTUMessage{
		final static int FAILURE_LENGTH = 100;
		FailureInfo[] failure_info = new FailureInfo[FAILURE_LENGTH];
		ArrayList<FailureInfo> failure_list = new ArrayList<FailureInfo>();
		int id_from = 0;
		int msg_size = 0; //the max is 30
		int head = 10; //head is 10 bytes.
		
		byte failure_num = 0;
		byte failure_issue = 0;//先看b14是否为1？如果b14为0，则看b15, b15:1=动作 0=恢复 b14=1 启动
		String value = "";
		BigDecimal time;
		int action;
		boolean b14;
		boolean b15;
		int val_int;
		FailureMessage(){
			if(bytes != null){
				reSet(bytes);
			}
		}
		
		public void getList(){//
			int j = id_from;
			for(int i = head; i < head + msg_size*16; i+=16, j++){//16 bytes per failure info
				failure_num = (byte)(Util.byteLength2intLitt(bytes, i));
				action = Util.byteLength2intLitt(bytes, i + 2);
				b14 = ((action >> 14) & 0x01) == 0x01;
				if(b14){
					failure_issue = 0x01; //启动。
				}else{
					b15 = ((action >> 15) & 0x01) == 0x01;
					if(b15){
						failure_issue = 0x02; //动作。
					}else{
						failure_issue = 0x03; //恢复。
					}
				}
				val_int = Util.byteArray2intLittle(bytes, i + 4);
				switch(failure_num){
				case 0x01:
				case 0x02:
				case 0x07:
				case 0x08:
					value = Util.floatToDecimal(val_int, 2).toString() + "V";
					break;
				case 0x0a: //重合闸次数
					value = Util.floatToDecimal(val_int, 0).toString();
					break;
				default:   //剩下5种都是电流
					value = Util.floatToDecimal(val_int, 3).toString() + "A";
					break;
				}
					
				String second = Util.byteLength2intLitt(bytes, i + 12) + "";
//				Log.d("MESSAGE", "failure j: " + j + " second:" + second);
				if(second.length() == 1){ //1位数
					second = "00" + second;
				}else if(second.length() == 2){ //2位数
					second = "0" + second;
				}else if(second.length() == 0){ //为0
					second = "000";
				}
//				Log.d("MESSAGE", "failure j: " + j + " second:" + second);
				
				time = new BigDecimal(Util.byteArray2intLittle(bytes, i + 8) + "" + 
							second);
				if( j >= FAILURE_LENGTH){//id_from + msg_size 是j的最大值
					failure_info[j - FAILURE_LENGTH] = new FailureInfo(failure_num, failure_issue, value, time);
				}else{
				    failure_info[j] = new FailureInfo(failure_num, failure_issue, value, time);
				}
			}
		}
		
		public void reSet(byte[] bytesArray){
			bytes = bytesArray;
			
			id_from = Util.getShortLittle(bytes, 8); //Failure id start from. 
			msg_size = (Util.getShortLittle(bytes, 4) - 4)/16; //how many Failure in Msg, Max 30. 
			
			time = new BigDecimal(Util.byteArray2intLittle(bytes, head + 8) + "" + 
					Util.byteLength2intLitt(bytes, head + 12));//信息里第一条即最老的soe
//			Log.d("MESSAGE", "failure id_from : " + id_from);
//			Log.d("MESSAGE", "failure msg_size: " + msg_size);
			if(id_from + msg_size == 0) return;
//			if(msg_size < 30){
////				FTU_Bluetooth.failureStatus = FTU_Bluetooth.FAILURE_FINISH;
////				Log.d("MESSAGE", "soeStatus = SOE_FINISH");
//			}else if(msg_size == 30){
//				FTU_Bluetooth.failureStatus = FTU_Bluetooth.FAILURE_WAIT_END;
//			}
//			Log.d("MESSAGE", "id_from + msg_size: " + (id_from + msg_size));
//			if(failure_info[id_from + msg_size - 1] != null){ //新来的soe全部已经收录
//				if(failure_info[id_from].failure_time == time){//如果序号跟时间都跟存在表里的一样，不需要更新。
//					//do nothing
//				}else{//如果序号跟时间跟表里的不一样，需要更新表里的数据，适用于时间被改变的情况
//					getList();			
//				}
//				
//			}else{//把报文里的信息更新到表里去，适用于有新soe的情况。
//				getList();
//			}
			getList();
			failure_list.clear();
			int k = 0;
			for(int i = FAILURE_LENGTH - 1 ; i >= 0 && k < FAILURE_LENGTH; i--){
				if(failure_info[i] != null){
					failure_list.add(failure_info[i]);
					k++;
				}				
			}

			Collections.sort(failure_list, new FailureComparator());
		}
	}
    
	private class FailureComparator implements Comparator<FailureInfo> {

		@Override
		public int compare(FailureInfo lhs, FailureInfo rhs) {
			// TODO Auto-generated method stub
			return rhs.failure_time.compareTo(lhs.failure_time);
		}
		
	}
	
	//------------ Communication parameter setting. 通讯参数设置 --------------------
	public class CommSetting extends FTUMessage{//max 290 bytes
		public byte[] bytes = new byte[290];
		public byte[] bytesForSave = new byte[290];
		int head = 48;
		//---------network card ----------
		short[] nw1_ip = new short[4];
		short[] nw1_mask = new short[4];
		short[] nw1_gate = new short[4];
		short[] nw2_ip = new short[4];
		short[] nw2_mask = new short[4];
		short[] nw2_gate = new short[4];
		//-------serial port-----------
		short[] serial_baud = new short[5];
		short[] serial_checkbit = new short[5];
		short[] serial_protocol = new short[5];
		int[] serial_addr = new int[5];
		//-------- network connection -----------
		short[] connect1_ip = new short[4];
		short[] connect2_ip = new short[4];
		short[] connect3_ip = new short[4];
		int[] connect_port = new int[3];
		short[] connect_mode1 = new short[3];
		short[] connect_mode2 = new short[3];
		short[] connect_protocol = new short[3];
		int[] connect_addr = new int[3];		
		
		CommSetting(){
			if(bytes != null){
				reSet(bytes);
			}
		};
		
		public void reSet(byte[] bytesArray){
//			bytes = bytesArray;
			for(int i = 0; i < bytes.length; i++){
				bytes[i] = bytesArray[i];
			}
			setNetworkCard();
			setNetworkConnt();
			setSerial();
		}
		
		public void copyForSave(){
			for(int i = 0; i < bytes.length; i++){
				bytesForSave[i] = bytes[i];
			}
		}
		
		void setNetworkCard(){
			int j, i;
			for(i = head + 3, j = 0; i >= head; i--, j++){
				nw1_ip[j] = (short) (bytes[i] & 0xff);
			}
			
			for(i = head + 7, j = 0; i >= head + 4; i--, j++){
				nw1_mask[j] = (short) (bytes[i] & 0xff);
			}
			
			for(i = head + 11, j = 0; i >= head + 8; i--, j++){
				nw1_gate[j] = (short) (bytes[i] & 0xff);
			}
			
			for(i = head + 20 + 3, j = 0; i >= head + 20; i--, j++){
				nw2_ip[j] = (short) (bytes[i] & 0xff);
			}
			
			for(i = head + 20 + 7, j = 0; i >= head + 4 + 20; i--, j++){
				nw2_mask[j] = (short) (bytes[i] & 0xff);
			}
			
			for(i = head + 20 + 11, j = 0; i >= head + 8 + 20; i--, j++){
				nw2_gate[j] = (short) (bytes[i] & 0xff);
			}
		}
		
		//串口参数
        void setSerial(){
        	int i, j;
        	for(i = 88 + 6, j = 0; j < 5; i +=20, j++){
        		serial_baud[j] = (short) Util.byteLength2intLitt(bytes, i);
        	}
        	for(i = 88 + 10, j = 0; j < 5; i +=20, j++){
        		serial_checkbit[j] = (short) Util.byteLength2intLitt(bytes, i);
        	}
        	for(i = 88 + 4, j = 0; j < 5; i +=20, j++){
        		serial_protocol[j] = (short) Util.byteLength2intLitt(bytes, i);//规约
        	}
        	for(i = 88 + 16, j = 0; j < 5; i +=20, j++){ //bug fix, set origin 18 to correct 16.
        	    serial_addr[j] = Util.byteLength2intLitt(bytes, i);//规约地址
        	}
		}
        
        //网络连接
        void setNetworkConnt(){
        	int j, i;
			for(i = 208 + 7, j = 0; i >= 208 + 4; i--, j++){
				connect1_ip[j] = (short) (bytes[i] & 0xff);
			}
			
			for(i = 228 + 7, j = 0; i >= 228 + 4; i--, j++){
				connect2_ip[j] = (short) (bytes[i] & 0xff);
			}
			
			for(i = 248 + 7, j = 0; i >= 248 + 4; i--, j++){
				connect3_ip[j] = (short) (bytes[i] & 0xff);
			}
			
			for(i = 208 + 10, j = 0; j < 3; i +=20, j++){
				connect_port[j] = Util.byteLength2intLitt(bytes, i);
        	}
			int mode;
			for(i = 208 + 12, j = 0; j < 3; i +=20, j++){
				mode = (short) Util.byteLength2intLitt(bytes, i);
				if(mode == 1){
					connect_mode1[j] = 0;
					connect_mode2[j] = 0;
				}else if(mode == 2){
					connect_mode1[j] = 0;
					connect_mode2[j] = 1;
				}else if(mode == 3){
					connect_mode1[j] = 1;
					connect_mode2[j] = 0;
				}else if(mode == 4){
					connect_mode1[j] = 1;
					connect_mode2[j] = 1;
				}
        	}
			for(i = 208 + 8, j = 0; j < 3; i +=20, j++){
				connect_protocol[j] = (short) Util.byteLength2intLitt(bytes, i);
        	}
			for(i = 208 + 14, j = 0; j < 3; i +=20, j++){
				connect_addr[j] =  Util.byteLength2intLitt(bytes, i);
        	}
		}
		
	}
	
}
