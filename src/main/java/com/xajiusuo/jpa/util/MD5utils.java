package com.xajiusuo.jpa.util;

import java.security.MessageDigest;

/**
 * md5加密算法，对登录人密码进行加密
 * @author hadoop
 *
 */
public class MD5utils {

	private final static String[] hexArray = { "0", "1", "2", "3", "4", "5","Z","-","%","*","V","N","o","p","+","/",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f","k","l","m","n","A","B","H","T","w","Y","S" }; // 存储十六进制值的数组
	private final static String[] hexArray2 = { "0", "1", "2", "3", "4", "5", "7", "8", "9", "a", "b", "c", "d", "e", "f","g","h","i","j","k","m","n","o","p","q","r","s","t","u","v","w","x","y","z"}; // 存储十六进制值的数组

	private static String byteArrayToHex(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHex(b[i]));
		}
		return resultSb.toString();
	}
	
	private static String byteArrayToHex2(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHex2(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHex(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexArray[d1] + hexArray[d2];
	}
	
	private static String byteToHex2(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexArray2[d1] + hexArray2[d2];
	}
	
	public static String getMd5(String originString){
		if (originString != null) {
			try {
				// 创建具有MD5算法的信息摘要
				MessageDigest md = MessageDigest.getInstance("MD5");
				// 使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
				byte[] results = md.digest(originString.getBytes());
				// 将得到的字节数组变成字符串返回
				String resultString = byteArrayToHex(results);
				return resultString.toUpperCase();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}
	
	public static String getMd5Plus(String originString){
		if (originString != null) {
			try {
				// 创建具有MD5算法的信息摘要
				MessageDigest md = MessageDigest.getInstance("MD5");
				// 使用指定的字节数组对摘要进行最后更新，然后完成摘要计算
				byte[] results = md.digest(originString.getBytes());
				// 将得到的字节数组变成字符串返回
				String resultString = byteArrayToHex2(results);
				return resultString;
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}
	
	
	public static void main(String[] args) {
		System.out.println(MD5utils.getMd5Plus("真实姓名"));
	}
	
}
