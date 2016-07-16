package com.demo.mobilesafe.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.demo.mobilesafe.utils.StreamUtils;
import com.example.mobilesafe.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author W JY。 Splash闪存页的开发 2015年12月27日
 */
public class SplashActivity extends Activity {

	protected static final int CODE_UPDATE_DIALOG = 0;

	protected static final int CODE_URL_ERROR = 1;

	protected static final int CODE_IO_ERROR = 2;

	protected static final int CODE_JSON_ERROR = 3;

	protected static final int CODE_ENTER_HOME = 4;

	private static String path = "http://192.168.1.169:8080/update.json";

	private TextView tvVersion;
	private TextView tvProgress;

	// 从服务器端请求到的数据，加m与局部变量区分
	private String mVersionName;// 新版本的版本名字
	private int mVersionCode;// 新版本的版本号
	private String mDescription;// 新版本的描述
	private String mDownloadUrl;// 新版本的下载地址

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CODE_UPDATE_DIALOG:
				showUpdateDialog();
				break;
			case CODE_URL_ERROR:
				Toast.makeText(SplashActivity.this, "URL错误", 0).show();
				enterHome();
				break;
			case CODE_IO_ERROR:
				Toast.makeText(SplashActivity.this, "网络连接失败", 0).show();
				enterHome();
				break;
			case CODE_JSON_ERROR:
				Toast.makeText(SplashActivity.this, "JSO解析错误", 0).show();
				enterHome();
				break;
			case CODE_ENTER_HOME:
				enterHome();
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		// 加载数据库资源
		copyDB("address.db");
		tvVersion = (TextView) findViewById(R.id.tv_version);
		tvVersion.setText("版本号：" + getVersionName());
		tvProgress = (TextView) findViewById(R.id.tv_progress);
		SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
		if (sp.getBoolean("auto_update", true)) {
			getUpdateInfo();
		} else {
			// 若设置为不更新，则开始进入主页面
			handler.sendEmptyMessageDelayed(4, 2000);
		}
	}

	/**
	 * 获取APP的版本的名字
	 */
	public String getVersionName() {
		PackageManager packageManager = getPackageManager();
		try {
			// 拿到包的信息
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			String versionName = packageInfo.versionName;
			return versionName;
		} catch (NameNotFoundException e) {
			// 找不到指定的包名
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取APP的版本号
	 */
	public int getVersionCode() {
		PackageManager packageManager = getPackageManager();
		try {
			// 拿到包的信息
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			int versionCode = packageInfo.versionCode;
			return versionCode;
		} catch (NameNotFoundException e) {
			// 找不到指定的包名
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 获取服务器上更新的信息
	 */
	public void getUpdateInfo() {
		Thread t = new Thread() {
			HttpURLConnection conn = null;
			Message msg = handler.obtainMessage();
			long startTime = System.currentTimeMillis();

			@Override
			public void run() {
				try {
					URL url = new URL(path);
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setReadTimeout(5000);
					conn.setConnectTimeout(5000);
					conn.connect();
					if (conn.getResponseCode() == 200) {
						InputStream is = conn.getInputStream();
						String result = StreamUtils.readFromStream(is);
						// System.out.println("这是请求结果" + result);
						// 解析Json中的数据
						JSONObject jsb = new JSONObject(result);
						mVersionName = jsb.getString("versionName");
						mVersionCode = jsb.getInt("versionCode");
						mDescription = jsb.getString("description");
						mDownloadUrl = jsb.getString("downloadUrl");
						// 若是新版本
						if (mVersionCode > getVersionCode()) {
							// 向handler发送消息
							msg.what = CODE_UPDATE_DIALOG;
						} else {// 若不是则直接进入到主界面
							msg.what = CODE_ENTER_HOME;
							// handler.sendMessageDelayed(msg, 2000);
						}
					}
				} catch (MalformedURLException e) {
					// URL错误
					msg.what = CODE_URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					// 读写错误，网络异常
					msg.what = CODE_IO_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					// Json解析错误
					msg.what = CODE_JSON_ERROR;
					e.printStackTrace();
				} finally {
					// 利用线程等待的方式让闪屏页显示
					long endTime = System.currentTimeMillis();
					long useTime = startTime - endTime;
					if (useTime < 2000) {
						try {
							Thread.sleep(2000 - useTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					// 发送消息
					handler.sendMessage(msg);
					if (conn != null) {
						conn.disconnect();
					}
				}
			}
		};
		t.start();
	}

	/**
	 * 弹出升级对话框，必须在主线程中运行
	 */
	public void showUpdateDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("更新版本：" + mVersionName);
		builder.setMessage(mDescription);
		builder.setPositiveButton("立即更新", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				download();
			}
		});
		builder.setNegativeButton("以后再说", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				enterHome();
			}
		});
		// 若用户按下返回键，则直接跳转到主页面
		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				enterHome();
			}
		});
		builder.show();
	}

	/**
	 * 进入到主界面
	 */
	public void enterHome() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		finish();
	}

	public void download() {
		// 当SD卡正确挂载时，才开始下载软件
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// 拿到SD卡目录
			String target = Environment.getExternalStorageDirectory()
					+ "/update.apk";
			HttpUtils http = new HttpUtils();
			// 直接开始下载
			http.download(mDownloadUrl, target, new RequestCallBack<File>() {
				@Override
				public void onLoading(long total, long current,
						boolean isUploading) {
					super.onLoading(total, current, isUploading);
					tvProgress.setVisibility(View.VISIBLE);
					tvProgress.setText("下载进度为：" + current * 100 / total + "%");
				}

				@Override
				public void onSuccess(ResponseInfo<File> arg0) {
					tvProgress.setVisibility(View.GONE);
					Toast.makeText(SplashActivity.this, "文件下载成功！", 0).show();

					Intent intent = new Intent();
					// android.intent.action.VIEW
					intent.setAction(Intent.ACTION_VIEW);
					// 设置好要跳转的activity
					intent.setDataAndType(Uri.fromFile(arg0.result),
							"application/vnd.android.package-archive");
					// 为了防止客户在安装软件时取消
					startActivityForResult(intent, 0);
				}

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					Toast.makeText(SplashActivity.this, "文件下载失败！", 0).show();
				}
			});
		} else {
			Toast.makeText(SplashActivity.this, "未检测到SD卡！", 0).show();
		}
	}

	// activity的参数回调，只要当前activity被销毁此方法就会调用
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {
			enterHome();
		}
	}

	/**
	 * 加载数据库资源
	 * 
	 * @param db
	 */
	public void copyDB(String dbName) {
		FileOutputStream fos = null;
		InputStream is = null;
		// 创建文件目录
		File file = new File(getFilesDir(), dbName);
		if (file.exists()) {
			System.out.println(dbName+"已经存在！");
			return;
		}
		try {
			fos = new FileOutputStream(file);
			is = getAssets().open(dbName);
			int len = -1;
			byte[] bt = new byte[1024];
			while ((len = is.read(bt)) != -1) {
				fos.write(bt, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
