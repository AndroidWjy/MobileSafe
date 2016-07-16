package com.demo.mobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

/**
 * �жϷ����Ƿ�����Ϊ�˱�֤���̨����ͬ�������ﲻ��sp��¼
 * 
 * @author Administrator
 * 
 */
public class ServiceStatusUtils {
	public static boolean isServiceStarting(Context ctx, String serviceName) {
		// �õ�ϵͳ��Activity������
		ActivityManager am = (ActivityManager) ctx
				.getSystemService(Context.ACTIVITY_SERVICE);
		// �õ��������еķ���
		List<RunningServiceInfo> runningServices = am.getRunningServices(100);
		for (RunningServiceInfo runningServiceInfo : runningServices) {
			String className = runningServiceInfo.service.getClassName();
			// �������
			// String shortClassName = runningServiceInfo.service.getShortClassName();
			//System.out.println(className);
			if (className.equals(serviceName)) {
				return true;
			}
		}
		return false;
	}
}
