package com.demo.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences sp = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		// 拿到sim和防盗保护的标记
		boolean protect = sp.getBoolean("protect", false);
		if (protect) {
			String sim = sp.getString("sim", null);
			if (!TextUtils.isEmpty(sim)) {
				// 拿到电话管理者
				TelephonyManager manger = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
				// 获取到了新的sim卡
				String simSerialNumber = manger.getSimSerialNumber() + "111";
				if (sim.equals(simSerialNumber)) {
					System.out.println("手机安全！");
				} else {
					System.out.println("手机SIM卡变更，发送报警短信！");
					SmsManager sms = SmsManager.getDefault();
					String safePhone = sp.getString("safe_phone", null);
					if (!TextUtils.isEmpty(safePhone)) {
						sms.sendTextMessage(safePhone, null,
								"sim card changed!", null, null);
					}
				}
			}
		}
	}
}
