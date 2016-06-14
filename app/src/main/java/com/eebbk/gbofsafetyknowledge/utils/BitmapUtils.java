package com.eebbk.gbofsafetyknowledge.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * decription ：bitmap工具类
 *
 * @author ： zhua
 */
public class BitmapUtils {

    private Context mContext;
    private static BitmapUtils mBitmapUtils = null;

    private BitmapUtils(Context context) {
        mContext = context;
    }

    public static BitmapUtils getInstance(Context context) {//接收全局的context

        if (mBitmapUtils == null) {
            mBitmapUtils = new BitmapUtils(context);
        }

        return mBitmapUtils;
    }

    //从Assert中获取bitmap
    public Bitmap getDrawableBitmap(String name) {
        Bitmap bm = null;
        AssetManager assetManager = mContext.getAssets();
        if (assetManager != null) {
            InputStream assetFile = null;

            try {
                assetFile = assetManager.open(name);
                bm = BitmapFactory.decodeStream(assetFile);
                assetFile.close();
            } catch (IOException var5) {
                var5.printStackTrace();
            }
        }

        return bm;
    }

    //从Assert中获取Drawable
    public Drawable getDrawable(String name) {
        BitmapDrawable da = null;
        AssetManager assetManager = mContext.getAssets();
        if (assetManager != null) {
            InputStream assetFile = null;
            try {
                assetFile = assetManager.open(name);
                Bitmap e = BitmapFactory.decodeStream(assetFile);
                da = new BitmapDrawable(e);
                assetFile.close();
            } catch (IOException var5) {
                var5.printStackTrace();
            }
        }

        return da;
    }
}
