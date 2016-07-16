package com.demo.mobilesafe.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 执行数据库的操作
 * 
 * @author Administrator
 * 
 */
public class AddressDao {
	private static final String path = "data/data/com.example.mobilesafe/files/address.db";

	/**
	 * 通过号码到数据库中查询归属地
	 * 
	 * @param number
	 * @return
	 */
	public static String getAddress(String number) {
		String address = "未知号码";
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		// 利用正则表达式来识别电话号码
		if (number.matches("^1[3-8]\\d{9}$")) {
			// 截取字符串，前7位作为判断，sub结束位置为end-1
			Cursor cursor = db
					.rawQuery(
							"select location from data2 where id = (select outkey from data1 where id = ?)",
							new String[] { number.substring(0, 7) });
			while (cursor.moveToNext()) {
				address = cursor.getString(0);
			}

			cursor.close();
		} else if (number.matches("^\\d+$")) { // 匹配数字，\\表示匹配\,\d表示[0-9],+表示多次匹配前面的字符
			int len = number.length();
			switch (len) {
			case 3:
				address = "报警电话";
				break;
			case 4:
				address = "模拟器电话";
				break;
			case 5:
				address = "客服电话";
				break;
			case 8:
				address = "本地电话";
				break;
			default:
				if (len > 10 && number.startsWith("0")) {//有可能是长途电话
					// 先查区号是4位的号码
					Cursor cursor = db.rawQuery(
							"select location from data2 where area=?",
							new String[] { number.substring(1, 4) });
					if (cursor.moveToNext()) {
						address = cursor.getString(0);
					} else {
						cursor.close();
						// 查区号为3位的号码
						cursor = db.rawQuery(
								"select location from data2 where area=?",
								new String[] { number.substring(1, 3) });

						if (cursor.moveToNext()) {
							address = cursor.getString(0);
						}

						cursor.close();
					}
				}
				break;
			}
		}
		db.close();
		return address;
	}
}
