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
 * 因为设置页面开发重复，所以加入了基类
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

		// 手势控制器
		mDectector = new GestureDetector(this, new SimpleOnGestureListener() {

			/**
			 * 调用onFling方法，e1表示起始位置，e2表示结束位置，x表示水平速率，y表示垂直速率
			 */
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {

				// 若滑动幅度过大
				if (Math.abs(e2.getRawY() - e1.getRawY()) > 100) {
					ToastUtils.showToast(BaseSetupActivity.this, "滑动幅度过大！");
					return true;
				}

				// 若滑动速度过缓
				if (Math.abs(velocityX) < 100) {
					ToastUtils.showToast(BaseSetupActivity.this, "滑动速度过缓！");
					return true;
				}

				// 若向右滑动大于200的距离就跳到上一页
				if (e2.getRawX() - e1.getRawX() > 200) {
					showPreviousPage();
					return true;
				}

				// 若向左滑动大于200就跳到下一页
				if (e1.getRawX() - e2.getRawX() > 200) {
					showNextPage();
					return true;
				}

				return super.onFling(e1, e2, velocityX, velocityY);
			}
		});
	}

	/**
	 * 跳到指定下一页的方法，需要子类实现
	 */
	public abstract void showNextPage();

	/**
	 * 跳到指定上一页的方法，需要子类实现
	 */
	public abstract void showPreviousPage();

	// 点击下一页
	public void next(View view) {
		showNextPage();
	}

	// 点击上一页
	public void previous(View view) {
		showPreviousPage();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mDectector.onTouchEvent(event);// 将触摸事件交给手势控制器处理
		return super.onTouchEvent(event);
	}

}
