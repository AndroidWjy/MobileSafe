package com.demo.mobilesafe.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.demo.mobilesafe.dao.AddressDao;
import com.example.mobilesafe.R;

/**
 * ���������ȥ���������ʾ�ķ��� ����һ���÷��������ȥ��һ���ù㲥������
 * 
 * @author Administrator
 * 
 */
public class AddressService extends Service {

	private TelephonyManager tm;
	private myListener listener;
	// ȥ��Ĺ㲥������
	private OutCallingReceiver receiver;
	// window������
	private WindowManager wm;
	private View view;
	private SharedPreferences sp;
	// ����
	private int startX;
	private int startY;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		sp = getSharedPreferences("config", MODE_PRIVATE);
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new myListener();
		// ��������ķ���������������һ��ҵ���߼���һ��������������
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		
		receiver = new OutCallingReceiver();
		
		IntentFilter filter = new IntentFilter();
		// ��Ӵ�绰��Action�����µ�����
		filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		// ע��㲥������
		registerReceiver(receiver, filter);
	}

	// �ڲ���
	class myListener extends PhoneStateListener {
		// ���绰��״̬�����仯ʱ���ã�������
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			// �绰���𣬼����磬incomingNumber��ʾ�������
			case TelephonyManager.CALL_STATE_RINGING:
				System.out.println("�绰����");
				String address = AddressDao.getAddress(incomingNumber);
				showToast(address);
				break;
			// �绰����״̬
			case TelephonyManager.CALL_STATE_IDLE:
				if (wm != null && view != null) {
					// ����ǰ�绰������״̬��ȥ��view����
					wm.removeView(view);
					view = null;
				}
				break;
			default:
				break;
			}
		}
	}

	/**
	 * �趨һ����̬�Ĵ�绰��ȥ�Ĺ㲥�����ߣ�Ϊ�˱�֤�����ͬ�����Դ�����̬��
	 * Ȩ�ޣ�android.permission.PROCESS_OUTGOING_CALLS
	 * 
	 * @author Administrator
	 * 
	 */
	class OutCallingReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// �õ��绰����
			String number = getResultData();
			String address = AddressDao.getAddress(number);
			showToast(address);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// ����ֹͣ��Ӧ����������ҲҲҪ����
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		// ֹͣ�㲥������
		unregisterReceiver(receiver);
	}

	/**
	 * �Զ���window��ʾ��������app��Ҳ������ʾ
	 */
	public void showToast(String address) {
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		int lastX = sp.getInt("lastX", 0);
		int lastY = sp.getInt("lastY", 0);
		// ������Ҫ���ֿؼ��Ĳ�������view������
		final WindowManager.LayoutParams params = new WindowManager.LayoutParams();

		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_PHONE;
		params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		// �趨��Ļ�ľ���λ��Ϊ����
		params.gravity = Gravity.LEFT + Gravity.TOP;
		params.x = lastX;
		params.y = lastY;

		view = View.inflate(this, R.layout.toast_phone_address, null);
		// ����Ҫ�仯��������ɫ�����鱣������
		int[] items = new int[] { R.drawable.call_locate_white,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green };
		int style = sp.getInt("address_style", 0);
		// ����sp�б����������ɫ��Ϊ�����ļ��趨ָ��������
		view.setBackgroundResource(items[style]);

		// ���ú���ʾ������
		TextView tvNumber = (TextView) view.findViewById(R.id.tv_number);
		tvNumber.setText(address);
		// ���趨�õĲ�����view������ӵ�window��ʾ����
		wm.addView(view, params);

		// ���ô�������
		view.setOnTouchListener(new OnTouchListener() {
			@SuppressWarnings("deprecation")
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();

					int dx = endX - startX;
					int dy = endY - startY;
					// ����ƫ���������²��ֿؼ�λ��
					params.x += dx;
					params.y += dy;
					// System.out.println("x= "+params.x+" y= "+params.y);
					// ����params������λ��
					if (params.x < 0) {
						params.x = 0;
					}
					if (params.y < 0) {
						params.y = 0;
					}
					//��Ȼview�����ܵ���Ļ����ʵ��window������ʾ�����ǣ���������һֱ�ڸ���
					if (params.x > wm.getDefaultDisplay().getWidth()
							- view.getWidth()) {
						params.x = wm.getDefaultDisplay().getWidth()
								- view.getWidth();
					}
					if (params.y > wm.getDefaultDisplay().getHeight()
							- view.getHeight()) {
						params.y = wm.getDefaultDisplay().getHeight()
								- view.getHeight();
					}

					wm.updateViewLayout(view, params);
					// ���³�ʼ��λ��
					startX = endX;
					startY = endY;
					break;
				case MotionEvent.ACTION_UP:
					// ������λ�ü�¼��sp��
					Editor edit = sp.edit();
					//��window���𣬲�����get���������������������
					edit.putInt("lastX", params.x);
					edit.putInt("lastY", params.y);
					edit.commit();
					break;
				}
				return true;
			}
		});
	}
}
