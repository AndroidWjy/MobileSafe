package com.demo.mobilesafe.service;

import com.demo.mobilesafe.receiver.AdminReceiver;
import com.demo.mobilesafe.utils.ToastUtils;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;

/**
 * 锁定屏幕的服务
 * @author Administrator
 *
 */
public class LockScreenService extends Service {

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
			mDPM.resetPassword("062020", 0);
			mDPM.lockNow();
			stopSelf();
		} else {
			ToastUtils.showToast(this, "超级设备管理组建未开启！");
			stopSelf();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("锁屏服务关闭！");
	}
}
