package com.demo.mobilesafe.receiver;

import com.demo.mobilesafe.service.LocationService;
import com.demo.mobilesafe.service.LockScreenService;
import com.demo.mobilesafe.service.WipeDataService;
import com.example.mobilesafe.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {

	private MediaPlayer mp;

	@Override
	public void onReceive(Context context, Intent intent) {
		// ��ȡ���ŵķ�ʽ
		Bundle bundle = intent.getExtras();
		Object[] objects = (Object[]) bundle.get("pdus");
		// ������Ŷ���
		for (Object object : objects) {
			// ͨ����ȡ�������ݹ���һ������
			SmsMessage sms = SmsMessage.createFromPdu((byte[]) object);
			// �õ����ź���
			String address = sms.getOriginatingAddress();
			// �õ���������
			String body = sms.getMessageBody();

			System.out.println(address + ":" + body);

			if ("#*alarm*#".equals(body)) {
				if (mp == null) {
					mp = MediaPlayer.create(context, R.raw.ylzs);
					mp.setVolume(1f, 1f);
					mp.setLooping(true);
					mp.start();
				}
				abortBroadcast();
			} else if ("#*location*#".equals(body)) {
				context.startService(new Intent(context, LocationService.class));
				SharedPreferences sp = context.getSharedPreferences("config",
						Context.MODE_PRIVATE);
				String location = sp.getString("location", "get location....");
				System.out.println("location:" + location);
				SmsManager smss = SmsManager.getDefault();
				smss.sendTextMessage(address, null, location, null, null);
				abortBroadcast();
			} else if ("#*lockscreen*#".equals(body)) {
				context.startService(new Intent(context,
						LockScreenService.class));
				SmsManager smss = SmsManager.getDefault();
				smss.sendTextMessage(address, null,
						"The screen is locked,you password is '062020'.", null,
						null);
				abortBroadcast();
			} else if ("#*wipedata*#".equals(body)) {
				context.startService(new Intent(context, WipeDataService.class));
				abortBroadcast();
			}
		}
	}

}
