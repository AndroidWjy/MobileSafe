package com.demo.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mobilesafe.R;

/**
 * �߼��趨
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
	 * ��ת�������ز�ѯҳ��
	 * @param v
	 */
	public void telephoneQuery(View v){
		startActivity(new Intent(this,AddressActivity.class));
	}
	
}
