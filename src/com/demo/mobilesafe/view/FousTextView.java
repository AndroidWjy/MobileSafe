package com.demo.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;

/** 
 * 自定义控件，获取焦点的TextView
 * @author WJY
 * @version 创建时间：2015-12-27 下午5:24:34 
 */
public class FousTextView extends TextView {
	//创建对象，new的时候调用此方法
	public FousTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	//若有自己的样式则此方法调用
	public FousTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	//若有自己的属性则此方法调用
	public FousTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		//返回true让自定义的控件能够自己获得焦点
		return true;
	}
}
