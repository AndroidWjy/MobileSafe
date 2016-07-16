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
		// 拿到配置标准的对象
		Criteria criteria = new Criteria();
		// 设置精确度
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// 允许付费定位
		criteria.setCostAllowed(true);
		String provider = location.getBestProvider(criteria, true);
		listener = new listener();
		location.requestLocationUpdates(provider, 0, 0, listener);
	}

	public class listener implements LocationListener {
		// 当位置改变时此方法调用
		@Override
		public void onLocationChanged(Location location) {
			System.out.println("获取到新的位置！");
			
			// 纬度
			double latitude = location.getLatitude();
			// 经度
			double longitude = location.getLongitude();
			sp.edit().putString("location",
					"latitude: " + latitude + " longitude: " + longitude).commit();
			
			stopSelf();

		}

		// 当状态发生改变时，此方法调用，如室内变为室外
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		// 当定位服务可用
		@Override
		public void onProviderEnabled(String provider) {
		}

		// 当定位服务不可用
		@Override
		public void onProviderDisabled(String provider) {
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		location.removeUpdates(listener);
		System.out.println("定位服务已停止！");
	}
}
