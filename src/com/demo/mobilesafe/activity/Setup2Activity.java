package com.demo.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.demo.mobilesafe.utils.ToastUtils;
import com.demo.mobilesafe.view.SettingItemView;
import com.example.mobilesafe.R;

/**
 * 向导页绑定SIM卡
 * @author Administrator
 *
 */
public class Setup2Activity extends BaseSetupActivity {

	private SettingItemView sivSim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);

		sivSim = (SettingItemView) findViewById(R.id.siv_sim);

		String sim = mPref.getString("sim", null);
		if (!TextUtils.isEmpty(sim)) {
			sivSim.setChecked(true);
		} else {
			sivSim.setChecked(false);
		}

		sivSim.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (sivSim.isChecked()) {
					sivSim.setChecked(false);
					mPref.edit().remove("sim").commit();
				} else {
					sivSim.setChecked(true);
					//拿到电话管理者
					TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
					String simSerialNumber = tm.getSimSerialNumber();
					System.out.println("sim卡的序列号为：" + simSerialNumber);

					mPref.edit().putString("sim", simSerialNumber).commit();
				}
			}
		});
	}

	@Override
	public void showNextPage() {
		String sim = mPref.getString("sim", null);
		if (TextUtils.isEmpty(sim)) {
			ToastUtils.showToast(this, "sim卡没有绑定！");
			return;
		}

		startActivity(new Intent(this, Setup3Activity.class));
		finish();

		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
	}

	@Override
	public void showPreviousPage() {
		startActivity(new Intent(this, Setup1Activity.class));
		finish();

		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}
}
