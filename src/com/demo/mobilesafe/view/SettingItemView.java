package com.demo.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mobilesafe.R;

/**
 * @author Administrator �Զ���ؼ�
 */
public class SettingItemView extends RelativeLayout {

	private TextView tvTitle;
	private TextView tvDesc;
	private CheckBox cbStatus;
	private static String NAME_SPACE = "http://schemas.android.com/apk/res/com.example.mobilesafe";
	private String mTitle;
	private String mDescOff;
	private String mDescOn;

	public SettingItemView(Context context) {
		super(context);
		initView();
	}

	public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	
	/**
	 * ��activity_settings.xml�н�������
	 * ���Զ���ؼ����Լ�������ʱ���˷�������
	 */
	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mTitle = attrs.getAttributeValue(NAME_SPACE, "title");
		mDescOff = attrs.getAttributeValue(NAME_SPACE, "desc_off");
		mDescOn = attrs.getAttributeValue(NAME_SPACE, "desc_on");

		initView();
		// int attributeCount = attrs.getAttributeCount();
		// for (int i = 0; i < attributeCount; i++) {
		// String attributeName = attrs.getAttributeName(i);
		// String attributeValue = attrs.getAttributeValue(i);
		// System.out.println(attributeName+":"+attributeValue);
		// }
	}

	/**
	 * ��ʼ���Զ���ؼ���ÿ�ι��췽�����ö�ȥ�Ҷ�Ӧ����ͼ�ļ�
	 */
	public void initView() {
		// root��ʾ��ͼ������ֱ��������ͼ����
		//��view_setting_item.xml��������
		View view = View
				.inflate(getContext(), R.layout.view_setting_item, this);
		tvTitle = (TextView) view.findViewById(R.id.tv_title);
		tvDesc = (TextView) view.findViewById(R.id.tv_desc);
		cbStatus = (CheckBox) view.findViewById(R.id.cb_status);
		
		tvTitle.setText(mTitle);
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

	public void setChecked(boolean checked) {
		cbStatus.setChecked(checked);

		if (checked) {
			tvDesc.setText(mDescOn);
		} else {
			tvDesc.setText(mDescOff);
		}
	}

	public boolean isChecked() {
		return cbStatus.isChecked();
	}
}
