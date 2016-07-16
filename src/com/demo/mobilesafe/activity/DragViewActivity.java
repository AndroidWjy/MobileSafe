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
 * ��������ؿ���ʾ��λ��
 * 
 * @author Administrator
 * 
 */
public class DragViewActivity extends Activity {

	private ImageView ivDrag;// ���϶��ؼ�

	private int startX;// ��ʼ��X����
	private int startY;// ��ʼ��Y����

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
		// �õ���Ļ���
		final int width = getWindowManager().getDefaultDisplay().getWidth();
		// �õ���Ļ�߶�
		final int height = getWindowManager().getDefaultDisplay().getHeight();

		int lastX = sp.getInt("lastX", 0);
		int lastY = sp.getInt("lastY", 0);
		// �õ����ֿؼ��Ĳ�����һ�����ֿռ�Ҫ����onMeasure����������onLayout�����õ�λ�ã���onDraw�����ƣ�
		RelativeLayout.LayoutParams params = (LayoutParams) ivDrag
				.getLayoutParams();
		params.leftMargin = lastX;
		params.topMargin = lastY;
		// ��������λ��
		ivDrag.setLayoutParams(params);
		// ÿ�ν����������ʾ����һ����һ�ε�����
		if (lastY < height / 2) {
			tvTop.setVisibility(View.INVISIBLE);
			tvBottom.setVisibility(View.VISIBLE);
		} else {
			tvTop.setVisibility(View.VISIBLE);
			tvBottom.setVisibility(View.INVISIBLE);
		}
		// ��ͼƬ����˫������
		ivDrag.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ���һ��������ʾ��������ĳ���
				System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
				// ϵͳ������ʱ��
				mHits[mHits.length - 1] = SystemClock.uptimeMillis();
				// ��������С��500ms
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

		// ����һ����������
		ivDrag.setOnTouchListener(new OnTouchListener() {
			// һ�������Ļ�˷�������
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				// ����ָ����
				case MotionEvent.ACTION_DOWN:
					// �õ��ؼ��տ�ʼ��λ��
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					// System.out.println("startX=" + startX + "startY" +
					// startY);
					break;
				// ����ָ�ƶ�
				case MotionEvent.ACTION_MOVE:
					// �õ��ƶ����λ��
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();
					// ����ƫ����
					int dx = endX - startX;
					int dy = endY - startY;
					// ���¼���λ��
					int l = ivDrag.getLeft() + dx;
					int t = ivDrag.getTop() + dy;
					int r = ivDrag.getRight() + dx;
					int b = ivDrag.getBottom() + dy;
					// ����������Ϲ�����ƶ�
					if (l < 0 || t < 0 || r > width || b > height - 20) {
						break;
					}
					// �ı���ʾ���λ��
					if (t < height / 2) {
						tvTop.setVisibility(View.INVISIBLE);
						tvBottom.setVisibility(View.VISIBLE);
					} else {
						tvTop.setVisibility(View.VISIBLE);
						tvBottom.setVisibility(View.INVISIBLE);
					}
					// ���²��ֿռ��λ��
					ivDrag.layout(l, t, r, b);
					// ���³�ʼ����ʼλ�ã��ٴλ�ȡ���ֿؼ���λ��
					startX = endX;
					startY = endY;
					break;
				// ����ָ����
				case MotionEvent.ACTION_UP:
					// ������λ�ü�¼��sp��
					Editor edit = sp.edit();
					edit.putInt("lastX", ivDrag.getLeft());
					edit.putInt("lastY", ivDrag.getTop());
					edit.commit();
					break;
				}
				return false;// true��ʾ�¼������´�����
			}
		});
	}
}
