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
	private SettingItemView sivUpdate;// 自动更新
	private SettingItemView sivAddress;// 显示归属地
	private SettingClickView scvStyle;// 归属地样式
	private SettingClickView scvLocation;// 归属地位置

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
	 * 初始化软件自动更新
	 */
	public void initAutoUpdate() {
		// 自动更新设置
		sivUpdate = (SettingItemView) findViewById(R.id.siv_update);
		boolean auto_update = sp.getBoolean("auto_update", true);

		if (auto_update) {
			// sivUpdate.setDesc("自动更新已开启");
			// 调用自定义控件暴露出来的方法
			sivUpdate.setChecked(true);
		} else {
			// sivUpdate.setDesc("自动更新已关闭");
			sivUpdate.setChecked(false);
		}

		sivUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!sivUpdate.isChecked()) {
					sivUpdate.setChecked(true);
					sp.edit().putBoolean("auto_update", true).commit();
					// sivUpdate.setDesc("自动更新已开启");
				} else {
					sivUpdate.setChecked(false);
					sp.edit().putBoolean("auto_update", false).commit();
					// sivUpdate.setDesc("自动更新已关闭");
				}
			}
		});
	}

	/**
	 * 初始化归属地的服务
	 */
	public void initAddressService() {
		sivAddress = (SettingItemView) findViewById(R.id.siv_address);
		// 判断服务是否有启动
		boolean serviceRunning = ServiceStatusUtils.isServiceStarting(this,
				"com.demo.mobilesafe.service.AddressService");
		// 进行判断
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
	 * 初始化归属地显示框的样式
	 * 
	 */
	// 显示的描述字段
	final String[] items = new String[] { "半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿" };

	public void initAddressStyle() {
		scvStyle = (SettingClickView) findViewById(R.id.scv_address_style);

		scvStyle.setTitle("归属地提示框风格");
		int style = sp.getInt("address_style", 0);
		// 在描述栏显示对应的颜色
		scvStyle.setDesc(items[style]);

		scvStyle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showSingleChooseDailog();
			}
		});

	}

	/**
	 * 弹出一个单选框
	 */
	public void showSingleChooseDailog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("归属地提示框风格");
		// 从sp中拿到之前选择的内容
		int style = sp.getInt("address_style", 0);
		// 弹出单选框
		builder.setSingleChoiceItems(items, style,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// 将选中的标记位记录在sp中
						sp.edit().putInt("address_style", which).commit();
						// 将选中内容显示在描述栏里面
						scvStyle.setDesc(items[which]);
						dialog.dismiss();
					}
				});
		// 设置取消按钮
		builder.setNegativeButton("取消", null);

		builder.show();
	}

	/**
	 * 初始化归属地显示框的位置
	 */
	public void initAddressLocation() {
		scvLocation = (SettingClickView) findViewById(R.id.scv_address_location);
		scvLocation.setTitle("归属地提示框显示位置");
		scvLocation.setDesc("设置归属地提示框显示位置");
		
		scvLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//跳转置设置页面
				startActivity(new Intent(SettingActivity.this,
						DragViewActivity.class));
			}
		});
	}
}
