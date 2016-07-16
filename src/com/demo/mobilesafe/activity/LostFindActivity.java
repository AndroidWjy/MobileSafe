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
 * �ֻ�����ҳ��
 * 
 * @author
 * @version ����ʱ�䣺2016-1-1 ����12:12:12
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
		// ����ȫ������ʾ�ڷ���ҳ��
		String safePhone = sp.getString("safe_phone", "");
		tvSafePhone.setText(safePhone);
		// ���ݷ������ܿ���״̬������ʾ��ͼƬ
		boolean protect = sp.getBoolean("protect", false);
		if (protect) {
			ivLock.setImageResource(R.drawable.lock);
		} else {
			ivLock.setImageResource(R.drawable.unlock);
		}
	}

	/**
	 * �����ת����ҳ��
	 * 
	 * @param v
	 */
	public void reEntry(View v) {
		startActivity(new Intent(this, Setup1Activity.class));
		finish();
	}
}
