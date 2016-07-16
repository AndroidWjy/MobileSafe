package com.demo.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mobilesafe.R;

/**
 * 高级设定
 * @author Administrator
 *
 */
public class AToolsActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atools);
	}
	
	/**
	 * 跳转至归属地查询页面
	 * @param v
	 */
	public void telephoneQuery(View v){
		startActivity(new Intent(this,AddressActivity.class));
	}
	
}
