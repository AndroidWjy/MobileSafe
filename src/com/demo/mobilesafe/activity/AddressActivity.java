package com.demo.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.demo.mobilesafe.dao.AddressDao;
import com.demo.mobilesafe.utils.ToastUtils;
import com.example.mobilesafe.R;

/**
 * 归属地查询
 * 
 * @author Administrator
 * 
 */
public class AddressActivity extends Activity {
	private EditText etPhone;
	private TextView tvAddress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		etPhone = (EditText) findViewById(R.id.et_telephone);
		tvAddress = (TextView) findViewById(R.id.tv_address);
		// 为输入框设定一个变化监听
		etPhone.addTextChangedListener(new TextWatcher() {
			// 当输入框变化时调用，s表示变化的参数
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String address = AddressDao.getAddress(s.toString());
				tvAddress.setText(address);
			}

			// 当输入框变化之前调用
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			// 当输入框变化之后调用
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	/**
	 * 点击查询按钮查询归属地
	 * 
	 * @param v
	 */
	public void queryAddress(View v) {
		String phoneNumber = etPhone.getText().toString();
		if (!TextUtils.isEmpty(phoneNumber)) {
			String address = AddressDao.getAddress(phoneNumber);
			tvAddress.setText(address);
		} else {
			// 创建一个动画效果，抖动器=平移+插补器（循环七次）
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			etPhone.startAnimation(shake);
			vibrate();
			ToastUtils.showToast(this, "查询的号码不能为空！");
		}
	}

	/**
	 * 震动效果
	 */
	public void vibrate() {
		// 拿到系统的振动器服务
		Vibrator vt = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		// 震动2s
		vt.vibrate(2000);
		// vt.vibrate(new long[]{1000,2000,1000,3000}, -1);
		//数组表示震动的时间，-1表示循环一次且先停1s然后开始震动；若是0表示从数组的第0位开始一直循环；
		//vt.cancel();表示取消
	}
}
