package com.demo.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.demo.mobilesafe.service.AddressService;
import com.demo.mobilesafe.utils.ServiceStatusUtils;
import com.demo.mobilesafe.view.SettingClickView;
import com.demo.mobilesafe.view.SettingItemView;
import com.example.mobilesafe.R;

public class SettingActivity extends Activity {
	private SharedPreferences sp;
	private SettingItemView sivUpdate;// �Զ�����
	private SettingItemView sivAddress;// ��ʾ������
	private SettingClickView scvStyle;// ��������ʽ
	private SettingClickView scvLocation;// ������λ��

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		sp = getSharedPreferences("config", MODE_PRIVATE);

		initAutoUpdate();
		initAddressService();
		initAddressStyle();
		initAddressLocation();
	}

	/**
	 * ��ʼ������Զ�����
	 */
	public void initAutoUpdate() {
		// �Զ���������
		sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
		boolean auto_update = sp.getBoolean("auto_update", true);

		if (auto_update) {
			// sivUpdate.setDesc("�Զ������ѿ���");
			// �����Զ���ؼ���¶�����ķ���
			sivUpdate.setChecked(true);
		} else {
			// sivUpdate.setDesc("�Զ������ѹر�");
			sivUpdate.setChecked(false);
		}

		sivUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!sivUpdate.isChecked()) {
					sivUpdate.setChecked(true);
					sp.edit().putBoolean("auto_update", true).commit();
					// sivUpdate.setDesc("�Զ������ѿ���");
				} else {
					sivUpdate.setChecked(false);
					sp.edit().putBoolean("auto_update", false).commit();
					// sivUpdate.setDesc("�Զ������ѹر�");
				}
			}
		});
	}

	/**
	 * ��ʼ�������صķ���
	 */
	public void initAddressService() {
		sivAddress = (SettingItemView) findViewById(R.id.siv_address);
		// �жϷ����Ƿ�������
		boolean serviceRunning = ServiceStatusUtils.isServiceStarting(this,
				"com.demo.mobilesafe.service.AddressService");
		// �����ж�
		if (serviceRunning) {
			sivAddress.setChecked(true);
		} else {
			sivAddress.setChecked(false);
		}
		sivAddress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!sivAddress.isChecked()) {
					sivAddress.setChecked(true);
					startService(new Intent(SettingActivity.this,
							AddressService.class));
				} else {
					sivAddress.setChecked(false);
					stopService(new Intent(SettingActivity.this,
							AddressService.class));
				}
			}
		});
	}

	/**
	 * ��ʼ����������ʾ�����ʽ
	 * 
	 */
	// ��ʾ�������ֶ�
	final String[] items = new String[] { "��͸��", "������", "��ʿ��", "������", "ƻ����" };

	public void initAddressStyle() {
		scvStyle = (SettingClickView) findViewById(R.id.scv_address_style);

		scvStyle.setTitle("��������ʾ����");
		int style = sp.getInt("address_style", 0);
		// ����������ʾ��Ӧ����ɫ
		scvStyle.setDesc(items[style]);

		scvStyle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showSingleChooseDailog();
			}
		});

	}

	/**
	 * ����һ����ѡ��
	 */
	public void showSingleChooseDailog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("��������ʾ����");
		// ��sp���õ�֮ǰѡ�������
		int style = sp.getInt("address_style", 0);
		// ������ѡ��
		builder.setSingleChoiceItems(items, style,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// ��ѡ�еı��λ��¼��sp��
						sp.edit().putInt("address_style", which).commit();
						// ��ѡ��������ʾ������������
						scvStyle.setDesc(items[which]);
						dialog.dismiss();
					}
				});
		// ����ȡ����ť
		builder.setNegativeButton("ȡ��", null);

		builder.show();
	}

	/**
	 * ��ʼ����������ʾ���λ��
	 */
	public void initAddressLocation() {
		scvLocation = (SettingClickView) findViewById(R.id.scv_address_location);
		scvLocation.setTitle("��������ʾ����ʾλ��");
		scvLocation.setDesc("���ù�������ʾ����ʾλ��");
		
		scvLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//��ת������ҳ��
				startActivity(new Intent(SettingActivity.this,
						DragViewActivity.class));
			}
		});
	}
}
