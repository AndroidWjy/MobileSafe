package com.demo.mobilesafe.activity;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.mobilesafe.utils.MD5Utils;
import com.demo.mobilesafe.view.FousTextView;
import com.example.mobilesafe.R;

public class HomeActivity extends Activity {
	private GridView gvItem;
	private SharedPreferences sp;
	private String[] tvItems = { "手机防盗", "通讯卫士", "软件管理", "进程管理", "流量统计",
			"手机杀毒", "缓存清理", "高级工具", "设置中心" };
	private int[] ivItems = { R.drawable.home_safe,
			R.drawable.home_callmsgsafe, R.drawable.home_apps,
			R.drawable.home_taskmanager, R.drawable.home_netmanager,
			R.drawable.home_trojan, R.drawable.home_sysoptimize,
			R.drawable.home_tools, R.drawable.home_settings };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 拿到存储文件
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// 拿到跑马灯控件
		FousTextView ftv = (FousTextView) findViewById(R.id.ftv);
		//获取当前日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		String date = sdf.format(new java.util.Date());
		ftv.setText("今天是"+date+" 手机卫士正在保护您的手机！感谢支持 @wjy");

		gvItem = (GridView) findViewById(R.id.gv_item);
		// 为GridView设置适配器
		gvItem.setAdapter(new MyAdapter());

		gvItem.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					showPasswordDailog();
					break;
				case 7:
					startActivity(new Intent(HomeActivity.this,
							AToolsActivity.class));
					break;
				case 8:
					Intent intent = new Intent(HomeActivity.this,
							SettingActivity.class);
					startActivity(intent);
					break;

				default:
					break;
				}

			}

		});
	}
	
	/**
	 * 内部类实现
	 *
	 */
	class MyAdapter extends BaseAdapter { 
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return ivItems.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return ivItems[arg0];
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@SuppressLint("ViewHolder")
		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// 注意这里是View，通过View找到填充对象
			View view = View.inflate(HomeActivity.this,
					R.layout.activity_home_item, null);
			ImageView ivItem = (ImageView) view.findViewById(R.id.iv_item);
			TextView tvItem = (TextView) view.findViewById(R.id.tv_item);
			// 每个view获取对应照片和内容
			ivItem.setImageResource(ivItems[arg0]);
			tvItem.setText(tvItems[arg0]);
			return view;
		}

	}

	/**
	 * 选择所要弹出的框
	 */
	protected void showPasswordDailog() {
		String password = sp.getString("password", "");
		if (TextUtils.isEmpty(password)) {
			showPasswordSetDailog();
		} else {
			showPasswordInputDailog();
		}
	}

	/**
	 * 自定义设置密码的弹框界面
	 */
	private void showPasswordSetDailog() {
		// 创建Builder对象
		AlertDialog.Builder builder = new Builder(this);
		// 创建一个弹框对象，创建一个自定义的弹框
		final AlertDialog dialog = builder.create();
		// 填充一个View对象
		View view = View.inflate(this, R.layout.dailog_set_password, null);
		// 通过View对象找到对应的资源ID
		final EditText etPassword = (EditText) view
				.findViewById(R.id.et_password);
		final EditText etPasswordConfirm = (EditText) view
				.findViewById(R.id.et_password_confirm);

		Button btnOk = (Button) view.findViewById(R.id.btn_ok);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		// 考虑屏幕适配
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
		// 设置按钮的侦听
		btnOk.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String password = etPassword.getText().toString();
				String passwordConfirm = etPasswordConfirm.getText().toString();
				if (TextUtils.isEmpty(password)) {
					Toast.makeText(HomeActivity.this, "输入密码不能为空！", 0).show();
				} else {
					if (!password.equals(passwordConfirm)) {
						Toast.makeText(HomeActivity.this, "两次密码输入的不一致！", 0)
								.show();
					} else {
						sp.edit()
								.putString("password",
										MD5Utils.encoding(password)).commit();
						Toast.makeText(HomeActivity.this, "密码设置成功！", 0).show();
						dialog.dismiss();
						Intent intent = new Intent(HomeActivity.this,
								Setup1Activity.class);
						startActivity(intent);
					}
				}
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

	/**
	 * 输入密码的弹框界面
	 */
	private void showPasswordInputDailog() {
		// 创建Builder对象
		AlertDialog.Builder builder = new Builder(this);
		// 创建一个弹框对象，创建一个自定义的弹框
		final AlertDialog dialog = builder.create();
		// 填充一个View对象
		View view = View.inflate(this, R.layout.dailog_input_password, null);
		// 通过View对象找到对应的资源ID
		final EditText etPassword = (EditText) view
				.findViewById(R.id.et_password);

		Button btnOk = (Button) view.findViewById(R.id.btn_ok);
		Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		// 考虑屏幕适配
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
		// 设置按钮的侦听
		btnOk.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String password = etPassword.getText().toString();
				String passwordConfirm = sp.getString("password", "");
				password = MD5Utils.encoding(password);

				if (TextUtils.isEmpty(password) || passwordConfirm.isEmpty()) {
					Toast.makeText(HomeActivity.this, "输入密码不能为空！", 0).show();
				} else {
					if (!password.equals(passwordConfirm)) {
						Toast.makeText(HomeActivity.this, "密码输入错误！", 0).show();
					} else {
						Toast.makeText(HomeActivity.this, "登录成功！", 0).show();
						dialog.dismiss();
						Intent intent = new Intent(HomeActivity.this,
								LostFindActivity.class);
						startActivity(intent);
					}
				}
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}
}
