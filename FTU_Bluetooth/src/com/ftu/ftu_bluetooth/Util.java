package com.ftu.ftu_bluetooth;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Util {

	/**
	 * 
	 * Get pix value from dp value by considering density.
	 * 
	 * @param dp
	 * @return pix value
	 */
	final public static int Dp2Px(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	final public static String bytesToHexString(byte[] src, int size) {
		StringBuilder sb = new StringBuilder("");
		if (src == null || size <= 0) {
			return null;
		}
		for (int i = 0; i < size; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				sb.append(0);
			}
			sb.append(hv);
		}
		return sb.toString();
	}

	final public static String bytesToHexString(byte[] src) {
		StringBuilder sb = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				sb.append(0);
			}
			sb.append(hv);
		}
		return sb.toString();
	}

	// final public static String stringToHexString(String str){
	// String msg = "";
	// char[] chars = str.toCharArray();
	// for(int i = 0; i < chars.length; i++){
	// msg += Integer.toHexString((int)chars[i]); //"\\u" +
	// }
	// return msg;
	// }
	/**
	 * 将32位的int值放到4字节的byte[]里
	 * 
	 * @param num
	 * @return
	 */
	public static byte[] int2byteArray(int num) {
		byte[] result = new byte[4];
		result[0] = (byte) (num >>> 24);// 取最高8位放到0下标
		result[1] = (byte) (num >>> 16);// 取次高8为放到1下标
		result[2] = (byte) (num >>> 8); // 取次低8位放到2下标
		result[3] = (byte) (num); // 取最低8位放到3下标
		return result;
	}

	/**
	 * 转换short为byte
	 *
	 * @param b
	 * @param s
	 *            需要转换的short
	 * @param index
	 */
	public static void putShort(byte b[], short s, int index) {
		b[index + 1] = (byte) (s >> 8);
		b[index + 0] = (byte) (s >> 0);
	}

	/**
	 * 通过byte数组取到short
	 *
	 * @param b
	 * @param index
	 *            第几位开始取
	 * @return
	 */
	public static short getShortLittle(byte[] b, int index) {
		return (short) (((b[index + 1] << 8) | b[index + 0] & 0xff));
	}

	public static short getShortBig(byte[] b, int index) {
		return (short) (((b[index + 0] << 8) | b[index + 1] & 0xff));
	}

	/**
	 * 将4字节的byte数组转成一个int值
	 * 
	 * @param b
	 * @return
	 */
	public static int byteArray2intBig(byte[] b) {
		byte[] a = new byte[4];
		int i = a.length - 1, j = b.length - 1;
		for (; i >= 0; i--, j--) {// 从b的尾部(即int值的低位)开始copy数据
			if (j >= 0)
				a[i] = b[j];
			else
				a[i] = 0;// 如果b.length不足4,则将高位补0
		}
		int v0 = (a[0] & 0xff) << 24;// &0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
		int v1 = (a[1] & 0xff) << 16;
		int v2 = (a[2] & 0xff) << 8;
		int v3 = (a[3] & 0xff);
		return v0 + v1 + v2 + v3;
	}

	public static int byteArray2intLittle(byte[] b) {
		byte[] a = new byte[4];
		int i, j = b.length - 1;
		for (i = 0, j = 0; i < 4; i++, j++) {// 从b的尾部(即int值的低位)开始copy数据
			if (j >= b.length)
				a[i] = 0;// 如果b.length不足4,则将高位补0
			else
				a[i] = b[j];
		}
		int v0 = (a[0] & 0xff);
		int v1 = (a[1] & 0xff) << 8;
		int v2 = (a[2] & 0xff) << 16;
		int v3 = (a[3] & 0xff) << 24;// &0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位

		return v0 + v1 + v2 + v3;
	}

	final public static boolean[] bytesToBooleanLittle(byte[] src) {
		boolean[] boo = new boolean[src.length * 8];
		int k = 0;
		for (int i = 0; i < src.length; i++) {
			for (int j = 0; j < 8; j++) {
				boo[k++] = ((src[i] >> j) & 0x01) == 0x01;
			}
		}
		return boo;
	}

	final public static int byteArray2intLittle(byte[] b, int index) {
		byte[] a = new byte[4];
		int i;
		int j = index + 3;
		for (i = 0, j = index; i < 4; i++, j++) {// 从b的尾部(即int值的低位)开始copy数据
			if (j > index + 3)
				a[i] = 0;// 如果b.length不足4,则将高位补0
			else
				a[i] = b[j];
		}
		int v0 = (a[0] & 0xff);
		int v1 = (a[1] & 0xff) << 8;
		int v2 = (a[2] & 0xff) << 16;
		int v3 = (a[3] & 0xff) << 24;// &0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位

		return v0 + v1 + v2 + v3;
	}

	/**
	 * 
	 * @param in
	 * @param movleft
	 * @return
	 */
	final static public BigDecimal floatToDecimal(int in, int movleft) {
		BigDecimal bd = new BigDecimal(in);
		return bd.movePointLeft(movleft);
	}

	final static public boolean functionCodeMatch(byte[] functionCode,
			byte[] inEntireMessage) {
		return (functionCode[0] == inEntireMessage[6] && functionCode[1] == inEntireMessage[7]);
	}

	/**
	 * Calculate the check code and fill the last 2 bytes into the byte array.
	 * 
	 * @param array
	 *            The array to calculate
	 * @param length
	 *            The entire length of the array
	 */
	final static public void setCheckCode(byte[] array, int length) {
		int count = 0;
		for (int i = 2; i < length - 2; i++) {
			count += (short) (array[i] & 0xff);
		}
		shortToByteArray((short) count, array, length - 2);
	}

	/**
	 * 转换short为byte
	 *
	 * @param b
	 * @param s
	 *            需要转换的short
	 * @param index
	 *            : will fill byte[] from this position
	 */
	public static void shortToByteArray(short src, byte dest[], int index) {
		dest[index + 1] = (byte) (src >> 8);
		dest[index + 0] = (byte) (src >> 0);
	}

	/**
	 * Convert byte[2] to int
	 */
	public static int byteLength2intLitt(byte[] b, int index) {
		byte[] a = new byte[4];
		int i;
		int j = index + 2;
		for (i = 0, j = index; i < 2; i++, j++) {// 从b的尾部(即int值的低位)开始copy数据
			if (j >= index + 2)
				a[i] = 0;// 如果b.length不足4,则将高位补0
			else
				a[i] = b[j];
		}
		int v0 = (a[0] & 0xff);
		int v1 = (a[1] & 0xff) << 8;
		return v0 + v1;
	}

	final public static BigDecimal getText2Num(String s) {
		if (s.length() == 0)
			return new BigDecimal("0");
		BigDecimal b = new BigDecimal(s);
		b = b.setScale(2, BigDecimal.ROUND_HALF_UP);
		b = b.movePointRight(2);
		return b;
	}

	final public static BigDecimal getText2Num(String s, int offset) {
		if (s.length() == 0)
			return new BigDecimal("0");
		BigDecimal b = new BigDecimal(s);
		b = b.setScale(2, BigDecimal.ROUND_HALF_UP);
		b = b.movePointRight(offset);
		return b;
	}

	/**
	 * 将32位的int值放到4字节的byte[]里
	 * 
	 * @param num
	 * @return
	 */
	final public static byte[] int2byteArrayLittle(int num) {
		byte[] result = new byte[4];
		result[3] = (byte) (num >>> 24);// 取最高8位放到3下标
		result[2] = (byte) (num >>> 16);// 取次高8为放到2下标
		result[1] = (byte) (num >>> 8); // 取次低8位放到1下标
		result[0] = (byte) (num); // 取最低8位放到0下标
		return result;
	}

	// public static String formatDateString(Context context, long time) {
	// DateFormat dateFormat = android.text.format.DateFormat
	// .getDateFormat(context);
	// DateFormat timeFormat = android.text.format.DateFormat
	// .getTimeFormat(context);
	// Date date = new Date(time);
	// return dateFormat.format(date) + " " + timeFormat.format(date);
	// }
	public static String formatDateString(Context context, long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");// yyyy-MM-dd
																				// HH:mm:ss");
		Date date = new Date((long) time);
		// sdf.format(date);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));// TimeZone.getTimeZone("Asia/Shanghai"));
		return sdf.format(date);
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
}
