package com.demo.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class LocationService extends Service {

	private SharedPreferences sp;
	private LocationManager location;
	private listener listener;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		sp = getSharedPreferences("config", MODE_PRIVATE);
		location = (LocationManager) getSystemService(LOCATION_SERVICE);
		// �õ����ñ�׼�Ķ���
		Criteria criteria = new Criteria();
		// ���þ�ȷ��
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// �����Ѷ�λ
		criteria.setCostAllowed(true);
		String provider = location.getBestProvider(criteria, true);
		listener = new listener();
		location.requestLocationUpdates(provider, 0, 0, listener);
	}

	public class listener implements LocationListener {
		// ��λ�øı�ʱ�˷�������
		@Override
		public void onLocationChanged(Location location) {
			System.out.println("��ȡ���µ�λ�ã�");
			
			// γ��
			double latitude = location.getLatitude();
			// ����
			double longitude = location.getLongitude();
			sp.edit().putString("location",
					"latitude: " + latitude + " longitude: " + longitude).commit();
			
			stopSelf();

		}

		// ��״̬�����ı�ʱ���˷������ã������ڱ�Ϊ����
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		// ����λ�������
		@Override
		public void onProviderEnabled(String provider) {
		}

		// ����λ���񲻿���
		@Override
		public void onProviderDisabled(String provider) {
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		location.removeUpdates(listener);
		System.out.println("��λ������ֹͣ��");
	}
}
