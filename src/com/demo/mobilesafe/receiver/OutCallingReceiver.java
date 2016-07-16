package com.demo.mobilesafe.receiver;

import com.demo.mobilesafe.dao.AddressDao;
import com.demo.mobilesafe.utils.ToastUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 设定一个动态的打电话出去的广播接受者，为了保证与服务同步所以创建动态的
 * 权限：android.permission.PROCESS_OUTGOING_CALLS
 * @author Administrator
 *
 */
public class OutCallingReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		//拿到电话号码
		String number = getResultData();
		String address = AddressDao.getAddress(number);
		ToastUtils.showToast(context, address);
	}

}
