package com.eebbk.gbofsafetyknowledge.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import java.io.IOException;
import java.io.InputStream;

/**
 * decription ：bitmap工具类
 * @author ： zhua
 */
public class BitmapUtils {

    private final Context mContext;
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
            InputStream assetFile;

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
}
