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
 *	��ȡ�������ֻ��ϵ���ϵ��
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
		// ������ϵ�˵�������
		lvContact.setAdapter(new SimpleAdapter(this, contactList,
				R.layout.contact_list_item, new String[] { "name", "phone" },
				new int[] { R.id.tv_name, R.id.tv_phone }));
		//���õ������
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
	 * ������ϵ���б�
	 * 
	 * @return
	 */
	public ArrayList<HashMap<String, String>> readContact() {
		// ����һ�����϶���
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		ContentResolver cr = getContentResolver();
		Cursor cursorId = cr.query(
				Uri.parse("content://com.android.contacts/raw_contacts"),
				new String[] { "contact_id" }, null, null, null);
		while (cursorId.moveToNext()) {
			// �õ���һ�ű��ID
			String contactId = cursorId.getString(0);
			Cursor cursor = cr.query(
					Uri.parse("content://com.android.contacts/data"),
					new String[] { "data1", "mimetype" }, "raw_contact_id=?",
					new String[] { contactId }, null);
			if (cursor != null) {
				// û���һ��ID��Ҫ����һ��map����ֹmap�Զ�����
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
