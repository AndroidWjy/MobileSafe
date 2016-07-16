package com.demo.mobilesafe.service;

import com.demo.mobilesafe.receiver.AdminReceiver;
import com.demo.mobilesafe.utils.ToastUtils;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;

public class WipeDataService extends Service {

	private DevicePolicyManager mDPM;
	private ComponentName admin;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		mDPM = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);

		admin = new ComponentName(this, AdminReceiver.class);
		if (mDPM.isAdminActive(admin)) {
			// 0��ʾ�������ڴ濨�е�����
			mDPM.wipeData(0);
			stopSelf();
		} else {
			ToastUtils.showToast(this, "�����豸�����齨δ������");
			stopSelf();
		}
	}
	public void onDestroy() {
		super.onDestroy();
		System.out.println("�������ݷ����ѹر�!");
	}
}
