package com.demo.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
	/**
	 * 进行MD5编码
	 * @param password
	 * @return
	 */
	public static String encoding(String password) {
		try {
			// 拿到MD5算法
			MessageDigest md = MessageDigest.getInstance("md5");
			// 开始计算
			byte[] digest = md.digest(password.getBytes());
			StringBuffer sb = new StringBuffer();
			// 遍历这个数组
			for (byte b : digest) {
				int i = b & 0xff;// 获取字节的低八位有效值
				String hexString = Integer.toHexString(i);// 将整数转为16进制

				if (hexString.length() < 2) {
					hexString = "0" + hexString;// 如果是1位的话,补0
				}

				sb.append(hexString);
			}
			return sb.toString();

		} catch (NoSuchAlgorithmException e) {
			// 找不到此种算法
			e.printStackTrace();
		}
		return "";
	}
}
