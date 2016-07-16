package com.demo.mobilesafe.activity;

import com.demo.mobilesafe.utils.ToastUtils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;

/**
 * ��Ϊ����ҳ�濪���ظ������Լ����˻���
 * 
 * @author Administrator
 * 
 */
public abstract class BaseSetupActivity extends Activity {

	private GestureDetector mDectector;
	public SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mPref = getSharedPreferences("config", MODE_PRIVATE);

		// ���ƿ�����
		mDectector = new GestureDetector(this, new SimpleOnGestureListener() {

			/**
			 * ����onFling������e1��ʾ��ʼλ�ã�e2��ʾ����λ�ã�x��ʾˮƽ���ʣ�y��ʾ��ֱ����
			 */
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {

				// ���������ȹ���
				if (Math.abs(e2.getRawY() - e1.getRawY()) > 100) {
					ToastUtils.showToast(BaseSetupActivity.this, "�������ȹ���");
					return true;
				}

				// �������ٶȹ���
				if (Math.abs(velocityX) < 100) {
					ToastUtils.showToast(BaseSetupActivity.this, "�����ٶȹ�����");
					return true;
				}

				// �����һ�������200�ľ����������һҳ
				if (e2.getRawX() - e1.getRawX() > 200) {
					showPreviousPage();
					return true;
				}

				// �����󻬶�����200��������һҳ
				if (e1.getRawX() - e2.getRawX() > 200) {
					showNextPage();
					return true;
				}

				return super.onFling(e1, e2, velocityX, velocityY);
			}
		});
	}

	/**
	 * ����ָ����һҳ�ķ�������Ҫ����ʵ��
	 */
	public abstract void showNextPage();

	/**
	 * ����ָ����һҳ�ķ�������Ҫ����ʵ��
	 */
	public abstract void showPreviousPage();

	// �����һҳ
	public void next(View view) {
		showNextPage();
	}

	// �����һҳ
	public void previous(View view) {
		showPreviousPage();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mDectector.onTouchEvent(event);// �������¼��������ƿ���������
		return super.onTouchEvent(event);
	}

}
