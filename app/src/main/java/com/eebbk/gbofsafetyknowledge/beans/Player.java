package com.eebbk.gbofsafetyknowledge.beans;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.eebbk.gbofsafetyknowledge.R;
import com.eebbk.gbofsafetyknowledge.utils.Util;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * 视频Player
 */
public class Player implements SurfaceHolder.Callback {

    //间隔
    private static final int INTERVAL_TIME = 1000;
    //Handler  中计时标志
    private static final int TIME_FLAG = 1;

    private MediaPlayer mMediaPlayer;
    private final SurfaceHolder mSurfaceHolder;
    private final TextView mTxvPlayTime;
    private OnInfoListener mInfoListener;
    private OnBufferingUpdateListener mBufferingUpdateListener;
    private OnCompletionListener mCompletionListener;
    private OnErrorListener mErrorListener;
    private final SeekBar mSeekBar;
    // 播放的总时长
    private int mDuration;
    //判断是否mediaplayer执行了OnPrepared
    private boolean mIsPrepared = false;
    //实时的记录当前播放的位置
    private int mPosition = 0;
    //是否可以执行计时操作
    private boolean mIsCanTime = true;
    //是否是第一次进行播放
    private boolean mIsFirstPlay = true;
    private SoftReference<Context> mContext;

    public void setmContext(Context mContext) {
        this.mContext = new SoftReference<>(mContext);
        mMediaPlayer = MediaPlayer.create(this.mContext.get(), R.raw.video);
    }

    public boolean ismIsFirstPlay() {
        return mIsFirstPlay;
    }

    public void setmIsCanTime(boolean mIsCanTime) {
        this.mIsCanTime = mIsCanTime;
    }

    public void setmErrorListener(OnErrorListener mErrorListener) {
        this.mErrorListener = mErrorListener;
    }

    public void setmBufferingUpdateListener(OnBufferingUpdateListener mBufferingUpdateListener) {
        this.mBufferingUpdateListener = mBufferingUpdateListener;
    }

    public void setmCompletionListener(OnCompletionListener mCompletionListener) {
        this.mCompletionListener = mCompletionListener;
    }

    public void setmInfoListener(OnInfoListener mInfoListener) {
        this.mInfoListener = mInfoListener;
    }

    public int getDuration() {
        return mDuration;
    }

    public Player(SurfaceView surfaceView,SeekBar seekBar, TextView playTime) {
        this.mSeekBar = seekBar;
        this.mTxvPlayTime = playTime;
        mSurfaceHolder = surfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setKeepScreenOn(true);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
            mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
    }

    public void play() {
        mMediaPlayer.start();
    }

    public void pause() {
        mMediaPlayer.pause();
    }

    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void play(final int position) {
        this.mPosition = position;

        mMediaPlayer.setDisplay(mSurfaceHolder);
        if (mIsFirstPlay) {
            try {
                mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
                mMediaPlayer.setOnInfoListener(mInfoListener);
                mMediaPlayer.setOnCompletionListener(mCompletionListener);
                mMediaPlayer.setOnErrorListener(mErrorListener);
                mDuration = mMediaPlayer.getDuration();
                mMediaPlayer.start();
                mIsFirstPlay = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        Log.e("surfaceChanged", "surfaceChanged");
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        mSeekBar.setProgress(mPosition);
        play(mPosition);
        mPosition = 0;
        startTimer();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        if (mMediaPlayer != null) {
            mPosition = mMediaPlayer.getCurrentPosition();
        }
    }

    /**
     * 消息处理
     */
    private final Handler mHandler = new TimeHandler(this);


    private static class TimeHandler extends Handler {

        final WeakReference<Player> mPlayer;
        @SuppressWarnings("unchecked")
        TimeHandler(Player player) {
            mPlayer = new WeakReference(player);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Player player = mPlayer.get();
            switch (msg.what) {
                case TIME_FLAG:
                    if (player.mMediaPlayer == null) {
                        break;
                    }

                    player.mPosition = player.mMediaPlayer.getCurrentPosition();

                    int pos = 0;

                    if (player.mDuration != 0) {
                        pos = player.mSeekBar.getMax() * player.mPosition / player.mDuration;
                    }

                    player.mSeekBar.setProgress(pos);

                    player.mTxvPlayTime.setText(player.mContext.get().getResources().getString(R.string.time_separator,Util.formatTime(player.mPosition),Util.formatTime(player.mDuration)));
                    break;
                default:
                    break;
            }

            if (player.mIsCanTime) {
                sendEmptyMessageDelayed(TIME_FLAG, INTERVAL_TIME);
            }
        }
    }

    /**
     * 通过定时器和Handler来更新进度条
     */
    public void startTimer() {
        mHandler.sendEmptyMessageDelayed(TIME_FLAG, INTERVAL_TIME);
    }

    /**
     * 跳转
     */
    public void seekToPos(int pos) {
        mMediaPlayer.seekTo(pos);
    }

    /**
     * 是否进行播放
     */
    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }
}