package com.demo.mobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

/**
 * 判断服务是否开启，为了保证与后台服务同步，这里不用sp记录
 * 
 * @author Administrator
 * 
 */
public class ServiceStatusUtils {
	public static boolean isServiceStarting(Context ctx, String serviceName) {
		// 拿到系统的Activity管理者
		ActivityManager am = (ActivityManager) ctx
				.getSystemService(Context.ACTIVITY_SERVICE);
		// 拿到所有运行的服务
		List<RunningServiceInfo> runningServices = am.getRunningServices(100);
		for (RunningServiceInfo runningServiceInfo : runningServices) {
			String className = runningServiceInfo.service.getClassName();
			// 简称名字
			// String shortClassName = runningServiceInfo.service.getShortClassName();
			//System.out.println(className);
			if (className.equals(serviceName)) {
				return true;
			}
		}
		return false;
	}
}
