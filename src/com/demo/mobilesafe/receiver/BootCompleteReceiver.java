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
		// �õ�sim�ͷ��������ı��
		boolean protect = sp.getBoolean("protect", false);
		if (protect) {
			String sim = sp.getString("sim", null);
			if (!TextUtils.isEmpty(sim)) {
				// �õ��绰������
				TelephonyManager manger = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
				// ��ȡ�����µ�sim��
				String simSerialNumber = manger.getSimSerialNumber() + "111";
				if (sim.equals(simSerialNumber)) {
					System.out.println("�ֻ���ȫ��");
				} else {
					System.out.println("�ֻ�SIM����������ͱ������ţ�");
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
