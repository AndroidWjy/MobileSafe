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
 * 监听来电和去电归属地显示的服务 来电一般用服务监听，去电一般用广播接受者
 * 
 * @author Administrator
 * 
 */
public class AddressService extends Service {

	private TelephonyManager tm;
	private myListener listener;
	// 去电的广播接受者
	private OutCallingReceiver receiver;
	// window弹框处理
	private WindowManager wm;
	private View view;
	private SharedPreferences sp;
	// 坐标
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
		// 监听来电的方法，两个参数，一个业务逻辑，一个是侦听的内容
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		
		receiver = new OutCallingReceiver();
		
		IntentFilter filter = new IntentFilter();
		// 添加打电话的Action，有新的来电
		filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		// 注册广播接受者
		registerReceiver(receiver, filter);
	}

	// 内部类
	class myListener extends PhoneStateListener {
		// 当电话的状态发生变化时调用，如来电
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			// 电话响起，即来电，incomingNumber表示来电号码
			case TelephonyManager.CALL_STATE_RINGING:
				System.out.println("电话响起！");
				String address = AddressDao.getAddress(incomingNumber);
				showToast(address);
				break;
			// 电话空闲状态
			case TelephonyManager.CALL_STATE_IDLE:
				if (wm != null && view != null) {
					// 若当前电话是闲置状态则去掉view对象
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
	 * 设定一个动态的打电话出去的广播接受者，为了保证与服务同步所以创建动态的
	 * 权限：android.permission.PROCESS_OUTGOING_CALLS
	 * 
	 * @author Administrator
	 * 
	 */
	class OutCallingReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 拿到电话号码
			String number = getResultData();
			String address = AddressDao.getAddress(number);
			showToast(address);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 服务停止相应的侦听服务也也要销毁
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		// 停止广播接受者
		unregisterReceiver(receiver);
	}

	/**
	 * 自定义window显示框，在其他app中也可以显示
	 */
	public void showToast(String address) {
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		int lastX = sp.getInt("lastX", 0);
		int lastY = sp.getInt("lastY", 0);
		// 定义需要布局控件的参数，即view的属性
		final WindowManager.LayoutParams params = new WindowManager.LayoutParams();

		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_PHONE;
		params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		// 设定屏幕的绝对位置为左上
		params.gravity = Gravity.LEFT + Gravity.TOP;
		params.x = lastX;
		params.y = lastY;

		view = View.inflate(this, R.layout.toast_phone_address, null);
		// 将所要变化的主题颜色用数组保存起来
		int[] items = new int[] { R.drawable.call_locate_white,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green };
		int style = sp.getInt("address_style", 0);
		// 根据sp中保存的主题颜色，为布局文件设定指定的主题
		view.setBackgroundResource(items[style]);

		// 设置好显示的内容
		TextView tvNumber = (TextView) view.findViewById(R.id.tv_number);
		tvNumber.setText(address);
		// 将设定好的参数与view对象添加到window显示框中
		wm.addView(view, params);

		// 设置触摸侦听
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
					// 计算偏移量并更新布局控件位置
					params.x += dx;
					params.y += dy;
					// System.out.println("x= "+params.x+" y= "+params.y);
					// 调整params的坐标位置
					if (params.x < 0) {
						params.x = 0;
					}
					if (params.y < 0) {
						params.y = 0;
					}
					//不然view坐标跑到屏幕，事实上window不会显示被覆盖，但是坐标一直在更新
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
					// 重新初始化位置
					startX = endX;
					startY = endY;
					break;
				case MotionEvent.ACTION_UP:
					// 将最后的位置记录在sp中
					Editor edit = sp.edit();
					//在window级别，不能用get方法，采用下面这个方法
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
