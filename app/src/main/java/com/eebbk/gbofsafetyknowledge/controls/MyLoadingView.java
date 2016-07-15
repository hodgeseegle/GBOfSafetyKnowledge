package com.eebbk.gbofsafetyknowledge.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.eebbk.gbofsafetyknowledge.R;

/**
 * 统一loading框，无任何功能
 */
public class MyLoadingView extends RelativeLayout {
	
	public MyLoadingView(Context context) {
		super(context);
		init(context);
	}

	public MyLoadingView(Context context, AttributeSet attrs){
		super(context, attrs);
		init(context);
	}

	public MyLoadingView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		init(context);
	}

	/**
	 * init view and layout
	 */
	private void init(Context ctx){
		LayoutInflater.from(ctx).inflate(R.layout.comment_dlg_wait, this) ;
	}
}
