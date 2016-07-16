package com.demo.mobilesafe.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * ִ�����ݿ�Ĳ���
 * 
 * @author Administrator
 * 
 */
public class AddressDao {
	private static final String path = "data/data/com.example.mobilesafe/files/address.db";

	/**
	 * ͨ�����뵽���ݿ��в�ѯ������
	 * 
	 * @param number
	 * @return
	 */
	public static String getAddress(String number) {
		String address = "δ֪����";
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		// ����������ʽ��ʶ��绰����
		if (number.matches("^1[3-8]\\d{9}$")) {
			// ��ȡ�ַ�����ǰ7λ��Ϊ�жϣ�sub����λ��Ϊend-1
			Cursor cursor = db
					.rawQuery(
							"select location from data2 where id = (select outkey from data1 where id = ?)",
							new String[] { number.substring(0, 7) });
			while (cursor.moveToNext()) {
				address = cursor.getString(0);
			}

			cursor.close();
		} else if (number.matches("^\\d+$")) { // ƥ�����֣�\\��ʾƥ��\,\d��ʾ[0-9],+��ʾ���ƥ��ǰ����ַ�
			int len = number.length();
			switch (len) {
			case 3:
				address = "�����绰";
				break;
			case 4:
				address = "ģ�����绰";
				break;
			case 5:
				address = "�ͷ��绰";
				break;
			case 8:
				address = "���ص绰";
				break;
			default:
				if (len > 10 && number.startsWith("0")) {//�п����ǳ�;�绰
					// �Ȳ�������4λ�ĺ���
					Cursor cursor = db.rawQuery(
							"select location from data2 where area=?",
							new String[] { number.substring(1, 4) });
					if (cursor.moveToNext()) {
						address = cursor.getString(0);
					} else {
						cursor.close();
						// ������Ϊ3λ�ĺ���
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
