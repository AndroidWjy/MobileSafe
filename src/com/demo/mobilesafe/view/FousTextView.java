package com.demo.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;

/** 
 * �Զ���ؼ�����ȡ�����TextView
 * @author WJY
 * @version ����ʱ�䣺2015-12-27 ����5:24:34 
 */
public class FousTextView extends TextView {
	//��������new��ʱ����ô˷���
	public FousTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	//�����Լ�����ʽ��˷�������
	public FousTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	//�����Լ���������˷�������
	public FousTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		//����true���Զ���Ŀؼ��ܹ��Լ���ý���
		return true;
	}
}
