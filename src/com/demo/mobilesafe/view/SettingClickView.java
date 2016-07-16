package com.demo.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mobilesafe.R;

/**
 * @author Administrator 自定义组合控件
 */
public class SettingClickView extends RelativeLayout {

	private TextView tvTitle;
	private TextView tvDesc;

	public SettingClickView(Context context) {
		super(context);
		initView();
	}

	public SettingClickView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	/**
	 * 从activity_settings.xml中解析出来 当自定义控件有自己的属性时，此方法调用
	 */
	public SettingClickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	/**
	 * 初始化自定义控件，每次构造方法调用都去找对应的视图文件
	 */
	public void initView() {
		// root表示视图容器，直接塞给视图容器
		// 从view_setting_click.xml解析出来
		View view = View
				.inflate(getContext(), R.layout.view_setting_click, this);
		tvTitle = (TextView) view.findViewById(R.id.tv_title);
		tvDesc = (TextView) view.findViewById(R.id.tv_desc);
	}

	/**
	 * 暴露方法供外部类调用
	 */
	public void setTitle(String title) {
		tvTitle.setText(title);
	}

	public void setDesc(String desc) {
		tvDesc.setText(desc);
	}
}
