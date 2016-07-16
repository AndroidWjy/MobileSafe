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
 * @author W JY�� Splash����ҳ�Ŀ��� 2015��12��27��
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

	// �ӷ����������󵽵����ݣ���m��ֲ���������
	private String mVersionName;// �°汾�İ汾����
	private int mVersionCode;// �°汾�İ汾��
	private String mDescription;// �°汾������
	private String mDownloadUrl;// �°汾�����ص�ַ

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CODE_UPDATE_DIALOG:
				showUpdateDialog();
				break;
			case CODE_URL_ERROR:
				Toast.makeText(SplashActivity.this, "URL����", 0).show();
				enterHome();
				break;
			case CODE_IO_ERROR:
				Toast.makeText(SplashActivity.this, "��������ʧ��", 0).show();
				enterHome();
				break;
			case CODE_JSON_ERROR:
				Toast.makeText(SplashActivity.this, "JSO��������", 0).show();
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
		// �������ݿ���Դ
		copyDB("address.db");
		tvVersion = (TextView) findViewById(R.id.tv_version);
		tvVersion.setText("�汾�ţ�" + getVersionName());
		tvProgress = (TextView) findViewById(R.id.tv_progress);
		SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
		if (sp.getBoolean("auto_update", true)) {
			getUpdateInfo();
		} else {
			// ������Ϊ�����£���ʼ������ҳ��
			handler.sendEmptyMessageDelayed(4, 2000);
		}
	}

	/**
	 * ��ȡAPP�İ汾������
	 */
	public String getVersionName() {
		PackageManager packageManager = getPackageManager();
		try {
			// �õ�������Ϣ
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			String versionName = packageInfo.versionName;
			return versionName;
		} catch (NameNotFoundException e) {
			// �Ҳ���ָ���İ���
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * ��ȡAPP�İ汾��
	 */
	public int getVersionCode() {
		PackageManager packageManager = getPackageManager();
		try {
			// �õ�������Ϣ
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			int versionCode = packageInfo.versionCode;
			return versionCode;
		} catch (NameNotFoundException e) {
			// �Ҳ���ָ���İ���
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * ��ȡ�������ϸ��µ���Ϣ
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
						// System.out.println("����������" + result);
						// ����Json�е�����
						JSONObject jsb = new JSONObject(result);
						mVersionName = jsb.getString("versionName");
						mVersionCode = jsb.getInt("versionCode");
						mDescription = jsb.getString("description");
						mDownloadUrl = jsb.getString("downloadUrl");
						// �����°汾
						if (mVersionCode > getVersionCode()) {
							// ��handler������Ϣ
							msg.what = CODE_UPDATE_DIALOG;
						} else {// ��������ֱ�ӽ��뵽������
							msg.what = CODE_ENTER_HOME;
							// handler.sendMessageDelayed(msg, 2000);
						}
					}
				} catch (MalformedURLException e) {
					// URL����
					msg.what = CODE_URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					// ��д���������쳣
					msg.what = CODE_IO_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					// Json��������
					msg.what = CODE_JSON_ERROR;
					e.printStackTrace();
				} finally {
					// �����̵߳ȴ��ķ�ʽ������ҳ��ʾ
					long endTime = System.currentTimeMillis();
					long useTime = startTime - endTime;
					if (useTime < 2000) {
						try {
							Thread.sleep(2000 - useTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					// ������Ϣ
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
	 * ���������Ի��򣬱��������߳�������
	 */
	public void showUpdateDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("���°汾��" + mVersionName);
		builder.setMessage(mDescription);
		builder.setPositiveButton("��������", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				download();
			}
		});
		builder.setNegativeButton("�Ժ���˵", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				enterHome();
			}
		});
		// ���û����·��ؼ�����ֱ����ת����ҳ��
		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				enterHome();
			}
		});
		builder.show();
	}

	/**
	 * ���뵽������
	 */
	public void enterHome() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		finish();
	}

	public void download() {
		// ��SD����ȷ����ʱ���ſ�ʼ�������
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// �õ�SD��Ŀ¼
			String target = Environment.getExternalStorageDirectory()
					+ "/update.apk";
			HttpUtils http = new HttpUtils();
			// ֱ�ӿ�ʼ����
			http.download(mDownloadUrl, target, new RequestCallBack<File>() {
				@Override
				public void onLoading(long total, long current,
						boolean isUploading) {
					super.onLoading(total, current, isUploading);
					tvProgress.setVisibility(View.VISIBLE);
					tvProgress.setText("���ؽ���Ϊ��" + current * 100 / total + "%");
				}

				@Override
				public void onSuccess(ResponseInfo<File> arg0) {
					tvProgress.setVisibility(View.GONE);
					Toast.makeText(SplashActivity.this, "�ļ����سɹ���", 0).show();

					Intent intent = new Intent();
					// android.intent.action.VIEW
					intent.setAction(Intent.ACTION_VIEW);
					// ���ú�Ҫ��ת��activity
					intent.setDataAndType(Uri.fromFile(arg0.result),
							"application/vnd.android.package-archive");
					// Ϊ�˷�ֹ�ͻ��ڰ�װ���ʱȡ��
					startActivityForResult(intent, 0);
				}

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					Toast.makeText(SplashActivity.this, "�ļ�����ʧ�ܣ�", 0).show();
				}
			});
		} else {
			Toast.makeText(SplashActivity.this, "δ��⵽SD����", 0).show();
		}
	}

	// activity�Ĳ����ص���ֻҪ��ǰactivity�����ٴ˷����ͻ����
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {
			enterHome();
		}
	}

	/**
	 * �������ݿ���Դ
	 * 
	 * @param db
	 */
	public void copyDB(String dbName) {
		FileOutputStream fos = null;
		InputStream is = null;
		// �����ļ�Ŀ¼
		File file = new File(getFilesDir(), dbName);
		if (file.exists()) {
			System.out.println(dbName+"�Ѿ����ڣ�");
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
