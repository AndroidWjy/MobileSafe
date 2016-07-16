package com.demo.mobilesafe.receiver;

import com.demo.mobilesafe.dao.AddressDao;
import com.demo.mobilesafe.utils.ToastUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * �趨һ����̬�Ĵ�绰��ȥ�Ĺ㲥�����ߣ�Ϊ�˱�֤�����ͬ�����Դ�����̬��
 * Ȩ�ޣ�android.permission.PROCESS_OUTGOING_CALLS
 * @author Administrator
 *
 */
public class OutCallingReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		//�õ��绰����
		String number = getResultData();
		String address = AddressDao.getAddress(number);
		ToastUtils.showToast(context, address);
	}

}
