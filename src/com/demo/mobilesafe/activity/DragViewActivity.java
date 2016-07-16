package com.demo.mobilesafe.activity;

import com.example.mobilesafe.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

/**
 * 定义归属地框显示的位置
 * 
 * @author Administrator
 * 
 */
public class DragViewActivity extends Activity {

	private ImageView ivDrag;// 可拖动控件

	private int startX;// 起始的X坐标
	private int startY;// 起始的Y坐标

	private SharedPreferences sp;

	private TextView tvTop;

	private TextView tvBottom;

	private long[] mHits = new long[2];

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drag_view);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		ivDrag = (ImageView) findViewById(R.id.iv_drag);
		tvTop = (TextView) findViewById(R.id.tv_top);
		tvBottom = (TextView) findViewById(R.id.tv_bottom);
		// 拿到屏幕宽度
		final int width = getWindowManager().getDefaultDisplay().getWidth();
		// 拿到屏幕高度
		final int height = getWindowManager().getDefaultDisplay().getHeight();

		int lastX = sp.getInt("lastX", 0);
		int lastY = sp.getInt("lastY", 0);
		// 拿到布局控件的参数，一个布局空间要经过onMeasure（测量），onLayout（放置的位置），onDraw（绘制）
		RelativeLayout.LayoutParams params = (LayoutParams) ivDrag
				.getLayoutParams();
		params.leftMargin = lastX;
		params.topMargin = lastY;
		// 重新设置位置
		ivDrag.setLayoutParams(params);
		// 每次进入归属地显示都读一下上一次的坐标
		if (lastY < height / 2) {
			tvTop.setVisibility(View.INVISIBLE);
			tvBottom.setVisibility(View.VISIBLE);
		} else {
			tvTop.setVisibility(View.VISIBLE);
			tvBottom.setVisibility(View.INVISIBLE);
		}
		// 对图片进行双击置中
		ivDrag.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 最后一个参数表示复制数组的长度
				System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
				// 系统开机的时间
				mHits[mHits.length - 1] = SystemClock.uptimeMillis();
				// 若点击间隔小于500ms
				if (mHits[0] >= mHits[mHits.length - 1] - 500) {
					int l = width / 2 - ivDrag.getWidth() / 2;
					// int t = height/2 - ivDrag.getHeight()/2;
					ivDrag.layout(l, ivDrag.getTop(), l + ivDrag.getWidth(),
							ivDrag.getBottom());

					Editor edit = sp.edit();
					edit.putInt("lastX", ivDrag.getLeft());
					edit.putInt("lastY", ivDrag.getTop());
					edit.commit();
				}
			}
		});

		// 创建一个触摸侦听
		ivDrag.setOnTouchListener(new OnTouchListener() {
			// 一旦点击屏幕此方法调用
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				// 当手指放下
				case MotionEvent.ACTION_DOWN:
					// 拿到控件刚开始的位置
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					// System.out.println("startX=" + startX + "startY" +
					// startY);
					break;
				// 当手指移动
				case MotionEvent.ACTION_MOVE:
					// 拿到移动后的位置
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();
					// 计算偏移量
					int dx = endX - startX;
					int dy = endY - startY;
					// 重新计算位置
					int l = ivDrag.getLeft() + dx;
					int t = ivDrag.getTop() + dy;
					int r = ivDrag.getRight() + dx;
					int b = ivDrag.getBottom() + dy;
					// 处理掉不符合规则的移动
					if (l < 0 || t < 0 || r > width || b > height - 20) {
						break;
					}
					// 改变提示框的位置
					if (t < height / 2) {
						tvTop.setVisibility(View.INVISIBLE);
						tvBottom.setVisibility(View.VISIBLE);
					} else {
						tvTop.setVisibility(View.VISIBLE);
						tvBottom.setVisibility(View.INVISIBLE);
					}
					// 更新布局空间的位置
					ivDrag.layout(l, t, r, b);
					// 重新初始化开始位置，再次获取布局控件的位置
					startX = endX;
					startY = endY;
					break;
				// 当手指拿起
				case MotionEvent.ACTION_UP:
					// 将最后的位置记录在sp中
					Editor edit = sp.edit();
					edit.putInt("lastX", ivDrag.getLeft());
					edit.putInt("lastY", ivDrag.getTop());
					edit.commit();
					break;
				}
				return false;// true表示事件不向下传递了
			}
		});
	}
}
