package com.demo.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mobilesafe.R;

/**
 * @author Administrator �Զ�����Ͽؼ�
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
	 * ��activity_settings.xml�н������� ���Զ���ؼ����Լ�������ʱ���˷�������
	 */
	public SettingClickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	/**
	 * ��ʼ���Զ���ؼ���ÿ�ι��췽�����ö�ȥ�Ҷ�Ӧ����ͼ�ļ�
	 */
	public void initView() {
		// root��ʾ��ͼ������ֱ��������ͼ����
		// ��view_setting_click.xml��������
		View view = View
				.inflate(getContext(), R.layout.view_setting_click, this);
		tvTitle = (TextView) view.findViewById(R.id.tv_title);
		tvDesc = (TextView) view.findViewById(R.id.tv_desc);
	}

	/**
	 * ��¶�������ⲿ�����
	 */
	public void setTitle(String title) {
		tvTitle.setText(title);
	}

	public void setDesc(String desc) {
		tvDesc.setText(desc);
	}
}
