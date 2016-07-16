package com.demo.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.mobilesafe.R;


/**
 * 向导页功能简介
 * 
 */
public class Setup1Activity extends BaseSetupActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
	}

	@Override
	public void showNextPage() {
		startActivity(new Intent(this, Setup2Activity.class));
		finish();
		//动画效果
		overridePendingTransition(R.anim.tran_next_in, R.anim.tran_next_out);
	}

	@Override
	public void showPreviousPage() {

	}
}
