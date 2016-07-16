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
 * �����ز�ѯ
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
		// Ϊ������趨һ���仯����
		etPhone.addTextChangedListener(new TextWatcher() {
			// �������仯ʱ���ã�s��ʾ�仯�Ĳ���
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String address = AddressDao.getAddress(s.toString());
				tvAddress.setText(address);
			}

			// �������仯֮ǰ����
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			// �������仯֮�����
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	/**
	 * �����ѯ��ť��ѯ������
	 * 
	 * @param v
	 */
	public void queryAddress(View v) {
		String phoneNumber = etPhone.getText().toString();
		if (!TextUtils.isEmpty(phoneNumber)) {
			String address = AddressDao.getAddress(phoneNumber);
			tvAddress.setText(address);
		} else {
			// ����һ������Ч����������=ƽ��+�岹����ѭ���ߴΣ�
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			etPhone.startAnimation(shake);
			vibrate();
			ToastUtils.showToast(this, "��ѯ�ĺ��벻��Ϊ�գ�");
		}
	}

	/**
	 * ��Ч��
	 */
	public void vibrate() {
		// �õ�ϵͳ����������
		Vibrator vt = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		// ��2s
		vt.vibrate(2000);
		// vt.vibrate(new long[]{1000,2000,1000,3000}, -1);
		//�����ʾ�𶯵�ʱ�䣬-1��ʾѭ��һ������ͣ1sȻ��ʼ�𶯣�����0��ʾ������ĵ�0λ��ʼһֱѭ����
		//vt.cancel();��ʾȡ��
	}
}
