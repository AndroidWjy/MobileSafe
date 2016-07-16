package com.demo.mobilesafe.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.mobilesafe.R;

/**
 * @author Administrator
 *	获取保存在手机上的联系人
 */
public class ContactActivity extends Activity {
	private ListView lvContact;
	private ArrayList<HashMap<String, String>> contactList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);

		lvContact = (ListView) findViewById(R.id.lv_contact);
		contactList = readContact();
		// 设置联系人的适配器
		lvContact.setAdapter(new SimpleAdapter(this, contactList,
				R.layout.contact_list_item, new String[] { "name", "phone" },
				new int[] { R.id.tv_name, R.id.tv_phone }));
		//设置点击侦听
		lvContact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent data = new Intent();
				data.putExtra("phone", contactList.get(position).get("phone"));
				setResult(Activity.RESULT_OK, data);
				finish();
			}
		});
	}

	/**
	 * 读出联系人列表
	 * 
	 * @return
	 */
	public ArrayList<HashMap<String, String>> readContact() {
		// 创建一个集合对象
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		ContentResolver cr = getContentResolver();
		Cursor cursorId = cr.query(
				Uri.parse("content://com.android.contacts/raw_contacts"),
				new String[] { "contact_id" }, null, null, null);
		while (cursorId.moveToNext()) {
			// 拿到第一张表的ID
			String contactId = cursorId.getString(0);
			Cursor cursor = cr.query(
					Uri.parse("content://com.android.contacts/data"),
					new String[] { "data1", "mimetype" }, "raw_contact_id=?",
					new String[] { contactId }, null);
			if (cursor != null) {
				// 没查出一个ID就要创建一个map，防止map自动覆盖
				HashMap<String, String> map = new HashMap<String, String>();
				while (cursor.moveToNext()) {
					String data1 = cursor.getString(0);
					String mimetype = cursor.getString(1);
					if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
						map.put("phone", data1);
					} else if ("vnd.android.cursor.item/name".equals(mimetype)) {
						map.put("name", data1);
					}
				}
				cursor.close();
				list.add(map);
			}
		}
		cursorId.close();
		return list;
	}
}
