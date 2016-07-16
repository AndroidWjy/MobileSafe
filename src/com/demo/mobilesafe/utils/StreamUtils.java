package com.demo.mobilesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtils {
	
	/**
	 * 从流中读取数据并转换为字符串
	 * @throws IOException 
	 */
	public static String readFromStream(InputStream is) throws IOException{
		byte[] bt = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while((len=is.read(bt))!=-1){
			bos.write(bt,0,len);
		}
		String result = bos.toString();
		return result;
	}
}
