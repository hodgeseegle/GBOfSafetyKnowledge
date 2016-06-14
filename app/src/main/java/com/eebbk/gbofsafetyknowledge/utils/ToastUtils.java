package com.eebbk.gbofsafetyknowledge.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.eebbk.gbofsafetyknowledge.R;

/** 
 * Toast工具类 
 * @author lling
 * 
 */ 
public class ToastUtils {  
    private static Handler mHandler = new Handler(Looper.getMainLooper());  
    private static Toast mToast;
    protected static LayoutInflater inflater;

    /** 
     * Toast发送消息，默认Toast.LENGTH_SHORT 
     * @param act 
     * @param msg 
     */ 
    public static void showMessage(final Context act, final String msg) {  
        showMessage(act, msg, Toast.LENGTH_SHORT);  
    }
    
    /** 
     * Toast发送消息，默认Toast.LENGTH_SHORT 
     * @param act 
     * @param msg 
     */ 
    public static void showMessageInCenter(final Context act, final String msg) {  
    	
    	Toast toast = Toast.makeText(act, msg, Toast.LENGTH_LONG);
		
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
    }  
       
    /** 
     * Toast发送消息，默认Toast.LENGTH_LONG 
     * @param act 
     * @param msg 
     */ 
    public static void showMessageLong(final Context act, final String msg) {  
        showMessage(act, msg, Toast.LENGTH_LONG);  
    }  
   
    /** 
     * Toast发送消息，默认Toast.LENGTH_SHORT 
     * @param act 
     * @param msg 
     */ 
    public static void showMessage(final Context act, final int msg) {  
        showMessage(act, msg, Toast.LENGTH_SHORT);  
    }  
       
    /** 
     * Toast发送消息，默认Toast.LENGTH_LONG 
     * @param act 
     * @param msg 
     */ 
    public static void showMessageLong(final Context act, final int msg) {  
        showMessage(act, msg, Toast.LENGTH_LONG);  
    }  
   
    /** 
     * Toast发送消息 
     * @param act 
     * @param msg 
     * @param len 
     */ 
    public static void showMessage(final Context act, final int msg,  
            final int len) {  
        new Thread(new Runnable() {  
            public void run() {  
            	mHandler.post(new Runnable() {  
                    @Override 
                    public void run() {  
                    	if (mToast != null) {
                    		mToast.setText(msg);
                    	} else {
                    		mToast = Toast.makeText(act, msg, len);
                    		if(inflater == null) {
                    			inflater = (LayoutInflater) act
                        	            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    		}
                    		mToast.setView(inflater.inflate(R.layout.rc_toast, null));
                    		mToast.setText(msg);
                    	}
                    	mToast.show();
                    }  
                });  
            }  
        }).start();  
    }  
       
    /** 
     * Toast发送消息 
     * @param act 
     * @param msg 
     * @param len 
     */ 
    public static void showMessage(final Context act, final String msg,  
            final int len) {  
        new Thread(new Runnable() {  
            public void run() {  
            	mHandler.post(new Runnable() {  
                    @Override 
                    public void run() {  
                    	if (mToast != null) {
                    		mToast.setText(msg);
                    	} else {
                    		mToast = Toast.makeText(act, msg, len);
                    		if(inflater == null) {
                    			inflater = (LayoutInflater) act
                        	            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    		}
                    		mToast.setView(inflater.inflate(R.layout.rc_toast, null));
                    		mToast.setText(msg);
                    	}
                    	mToast.show();
                    }  
                });  
            }  
        }).start();  
    }  
   
    /**
     * 系统弹出提示框
     * @param context 
     * @param message 提示信息
     * */
    public static void alert (final Context context, final String message) {
    	new Thread(new Runnable() {  
            public void run() {  
            	mHandler.post(new Runnable() {  
                    @Override 
                    public void run() {  
                    	// 构建一个Builder来显示网页中的alert对话框
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("提示");
                        builder.setMessage(message);
                        builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.setCancelable(false);
                        builder.create();
                        builder.show(); 
                    }  
                });  
            }  
        }).start();  
    }
}  
