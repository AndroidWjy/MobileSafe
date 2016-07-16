package com.demo.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobilesafe.R;

/**
 * 手机防盗页面
 * 
 * @author
 * @version 创建时间：2016-1-1 下午12:12:12
 */
public class LostFindActivity extends Activity {
	private TextView tvSafePhone;
	private ImageView ivLock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lost_find);

		tvSafePhone = (TextView) findViewById(R.id.tv_safe_phone);
		ivLock = (ImageView) findViewById(R.id.iv_lock);
		SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
		// 将安全号码显示在防盗页面
		String safePhone = sp.getString("safe_phone", "");
		tvSafePhone.setText(safePhone);
		// 根据防盗功能开启状态决定显示的图片
		boolean protect = sp.getBoolean("protect", false);
		if (protect) {
			ivLock.setImageResource(R.drawable.lock);
		} else {
			ivLock.setImageResource(R.drawable.unlock);
		}
	}

	/**
	 * 点击跳转到向导页面
	 * 
	 * @param v
	 */
	public void reEntry(View v) {
		startActivity(new Intent(this, Setup1Activity.class));
		finish();
	}
}
