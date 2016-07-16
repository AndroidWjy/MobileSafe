package com.demo.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.demo.mobilesafe.utils.ToastUtils;
import com.example.mobilesafe.R;

/**
 * 向导页绑定安全号码
 * 
 * @author Administrator
 * 
 */
public class Setup3Activity extends BaseSetupActivity {

	private EditText etPhone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);

		etPhone = (EditText) findViewById(R.id.et_phone);

		String phone = mPref.getString("safe_phone", "");
		etPhone.setText(phone);
	}

	@Override
	public void showNextPage() {
		String phone = etPhone.getText().toString().trim();// 将空格去掉

		if (TextUtils.isEmpty(phone)) {

			ToastUtils.showToast(this, "安全号码不能为空！");
			return;
		}

		mPref.edit().putString("safe_phone", phone).commit();// 将安全号码保存在sp中

		startActivity(new Intent(this, Setup4Activity.class));
		finish();

		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
	}

	@Override
	public void showPreviousPage() {
		startActivity(new Intent(this, Setup2Activity.class));
		finish();

		overridePendingTransition(R.anim.tran_pre_in, R.anim.tran_pre_out);
	}

	/**
	 * 选择联系人的方法
	 * 
	 * @param view
	 */
	public void selectContact(View view) {
		Intent intent = new Intent(this, ContactActivity.class);
		//带有参数回调的启动方式
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// System.out.println("resultCode:" + resultCode);
		// System.out.println("requestCode:" + requestCode);

		if (resultCode == Activity.RESULT_OK) {
			String phone = data.getStringExtra("phone");
			// 替代空格和-的内容
			phone = phone.replaceAll("-", "").replaceAll(" ", "");

			etPhone.setText(phone);
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
}
