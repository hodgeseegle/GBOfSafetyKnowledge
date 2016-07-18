package com.eebbk.gbofsafetyknowledge.utils;

import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 工具类
 */
public class Util {
	/**
	 * 格式化时间
	 */
	public static String formatTime(long time) {
		DateFormat formatter = new SimpleDateFormat("mm:ss", Locale.CHINA);
		return formatter.format(new Date(time));
	}

	public static int dip2px(Context context, float dpValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
}
