package com.demo.mobilesafe.activity;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.mobilesafe.utils.MD5Utils;
import com.demo.mobilesafe.view.FousTextView;
import com.example.mobilesafe.R;

public class HomeActivity extends Activity {
	private GridView gvItem;
	private SharedPreferences sp;
	private String[] tvItems = { "�ֻ�����", "ͨѶ��ʿ", "�������", "���̹���", "����ͳ��",
			"�ֻ�ɱ��", "��������", "�߼�����", "��������" };
	private int[] ivItems = { R.drawable.home_safe,
			R.drawable.home_callmsgsafe, R.drawable.home_apps,
			R.drawable.home_taskmanager, R.drawable.home_netmanager,
			R.drawable.home_trojan, R.drawable.home_sysoptimize,
			R.drawable.home_tools, R.drawable.home_settings };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// �õ��洢�ļ�
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// �õ�����ƿؼ�
		FousTextView ftv = (FousTextView) findViewById(R.id.ftv);
		//��ȡ��ǰ����
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��MM��dd��");
		String date = sdf.format(new java.util.Date());
		ftv.setText("������"+date+" �ֻ���ʿ���ڱ��������ֻ�����л֧�� @wjy");

		gvItem = (GridView) findViewById(R.id.gv_item);
		// ΪGridView����������
		gvItem.setAdapter(new MyAdapter());

		gvItem.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					showPasswordDailog();
					break;
				case 7:
					startActivity(new Intent(HomeActivity.this,
							AToolsActivity.class));
					break;
				case 8:
					Intent intent = new Intent(HomeActivity.this,
							SettingActivity.class);
					startActivity(intent);
					break;

				default:
					break;
				}

			}

		});
	}
	
	/**
	 * �ڲ���ʵ��
	 *
	 */
	class MyAdapter extends BaseAdapter { 
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return ivItems.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return ivItems[arg0];
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@SuppressLint("ViewHolder")
		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// ע��������View��ͨ��View�ҵ�������
			View view = View.inflate(HomeActivity.this,
					R.layout.activity_home_item, null);
			ImageView ivItem = (ImageView) view.findViewById(R.id.iv_item);
			TextView tvItem = (TextView) view.findViewById(R.id.tv_item);
			// ÿ��view��ȡ��Ӧ��Ƭ������
			ivItem.setImageResource(ivItems[arg0]);
			tvItem.setText(tvItems[arg0]);
			return view;
		}

	}

	/**
	 * ѡ����Ҫ�����Ŀ�
	 */
	protected void showPasswordDailog() {
		String password = sp.getString("password", "");
		if (TextUtils.isEmpty(password)) {
			showPasswordSetDailog();
		} else {
			showPasswordInputDailog();
		}
	}

	/**
	 * �Զ�����������ĵ������
	 */
	private void showPasswordSetDailog() {
		// ����Builder����
		AlertDialog.Builder builder = new Builder(this);
		// ����һ��������󣬴���һ���Զ���ĵ���
		final AlertDialog dialog = builder.create();
		// ���һ��View����
		View view = View.inflate(this, R.layout.dailog_set_password, null);
		// ͨ��View�����ҵ���Ӧ����ԴID
		final EditText etPassword = (EditText) view
				.findViewById(R.id.et_password);
		final EditText etPasswordConfirm = (EditText) view
				.findViewById(R.id.et_password_confirm);

		Button btnOk = (Button) view.findViewById(R.id.btn_ok);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		// ������Ļ����
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
		// ���ð�ť������
		btnOk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String password = etPassword.getText().toString();
				String passwordConfirm = etPasswordConfirm.getText().toString();
				if (TextUtils.isEmpty(password)) {
					Toast.makeText(HomeActivity.this, "�������벻��Ϊ�գ�", 0).show();
				} else {
					if (!password.equals(passwordConfirm)) {
						Toast.makeText(HomeActivity.this, "������������Ĳ�һ�£�", 0)
								.show();
					} else {
						sp.edit()
								.putString("password",
										MD5Utils.encoding(password)).commit();
						Toast.makeText(HomeActivity.this, "�������óɹ���", 0).show();
						dialog.dismiss();
						Intent intent = new Intent(HomeActivity.this,
								Setup1Activity.class);
						startActivity(intent);
					}
				}
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	/**
	 * ��������ĵ������
	 */
	private void showPasswordInputDailog() {
		// ����Builder����
		AlertDialog.Builder builder = new Builder(this);
		// ����һ��������󣬴���һ���Զ���ĵ���
		final AlertDialog dialog = builder.create();
		// ���һ��View����
		View view = View.inflate(this, R.layout.dailog_input_password, null);
		// ͨ��View�����ҵ���Ӧ����ԴID
		final EditText etPassword = (EditText) view
				.findViewById(R.id.et_password);

		Button btnOk = (Button) view.findViewById(R.id.btn_ok);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		// ������Ļ����
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
		// ���ð�ť������
		btnOk.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String password = etPassword.getText().toString();
				String passwordConfirm = sp.getString("password", "");
				password = MD5Utils.encoding(password);

				if (TextUtils.isEmpty(password) || passwordConfirm.isEmpty()) {
					Toast.makeText(HomeActivity.this, "�������벻��Ϊ�գ�", 0).show();
				} else {
					if (!password.equals(passwordConfirm)) {
						Toast.makeText(HomeActivity.this, "�����������", 0).show();
					} else {
						Toast.makeText(HomeActivity.this, "��¼�ɹ���", 0).show();
						dialog.dismiss();
						Intent intent = new Intent(HomeActivity.this,
								LostFindActivity.class);
						startActivity(intent);
					}
				}
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}
}
