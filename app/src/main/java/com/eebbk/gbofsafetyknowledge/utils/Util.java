package com.eebbk.gbofsafetyknowledge.utils;

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
}
