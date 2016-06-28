package com.eebbk.gbofsafetyknowledge.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.eebbk.gbofsafetyknowledge.R;
import com.eebbk.gbofsafetyknowledge.beans.Player;
import com.eebbk.gbofsafetyknowledge.utils.Util;

/**
 * 视频播放器
 */
public class VideoPlayerActivity extends Activity {

    private ImageView mImgBtnPlay;
    private SeekBar mSeekBarScroll;
    private TextView mTxvPlayTime;
    private Player mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_video_player);

        init();
    }

    @Override
    protected void onResume() {
        if(!mPlayer.ismIsFirstPlay()){
            mPlayer.play();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {

        if(mPlayer.isPlaying()){
            mPlayer.pause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mPlayer.setmIsCanTime(false);
        mPlayer.stop();
        super.onDestroy();
    }

    /**
     * description:初始化
     */
    private void init() {
        ((RelativeLayout) findViewById(R.id.RelativeLayout_player))
                .setDrawingCacheEnabled(false);

        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mImgBtnPlay = (ImageView) findViewById(R.id.ImageView_play_pause);
        mSeekBarScroll = (SeekBar) findViewById(R.id.SeekBar_scroll);
        mTxvPlayTime = (TextView) findViewById(R.id.TextView_play_time);
        mImgBtnPlay.setOnClickListener(new ClickListener());
        mSeekBarScroll.setOnSeekBarChangeListener(barChangeListener);

        mPlayer = new Player(surfaceView, mSeekBarScroll, mTxvPlayTime);
        mPlayer.setmContext(this);
        mPlayer.setmErrorListener(mErrorListener);
        mPlayer.setmInfoListener(mInfoListener);
        mPlayer.setmBufferingUpdateListener(mBufferingUpdateListener);
        mPlayer.setmCompletionListener(mCompletionListener);
    }

    /**
     * 出错监听
     */
    private final MediaPlayer.OnErrorListener mErrorListener = new MediaPlayer.OnErrorListener() {

        @Override
        public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
            // TODO Auto-generated method stub

            Log.i("OnErrorListener","----------------------->OnErrorListener");
            return false;
        }
    };

    /**
     * 状态改变监听器
     */
    private final MediaPlayer.OnInfoListener mInfoListener = new MediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            switch (what) {
                case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                    Log.i("OnInfoListener","----------------------->MEDIA_INFO_BUFFERING_START");
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                    Log.i("OnInfoListener","----------------------->MEDIA_INFO_BUFFERING_END");
                    break;
            }
            return true;
        }
    };

    /**
     * 缓冲改变监听器
     */
    private MediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mediaPlayer,
                                      int bufferingProgress) {
            mSeekBarScroll.setSecondaryProgress(bufferingProgress);
        }
    };

    /**
     * 播放完毕监听
     */
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            Log.i("OnCompletionListener","----------------------->OnCompletionListener");
            finish();
        }
    };


    /**
     * 点击相应事件
     */
    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View arg0) {
            switch (arg0.getId()) {
                case R.id.ImageView_play_pause:// 播放、暂停
                    playAndPause();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 暂停和播放
     */
    private void playAndPause() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            mImgBtnPlay.setImageResource(R.drawable.play_btn_fullplay);
        } else {
            mPlayer.play();
            mImgBtnPlay.setImageResource(R.drawable.play_btn_fullpause);
        }
    }

    private SeekBar.OnSeekBarChangeListener barChangeListener = new SeekBar.OnSeekBarChangeListener() {
        int progress;
        int lastProgress;

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            lastProgress = seekBar.getProgress();
            mPlayer.setmIsCanTime(false);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mPlayer.seekToPos(progress);

            mPlayer.setmIsCanTime(true);
            mPlayer.startTimer();
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            int max = seekBar.getMax();
            this.progress = i * mPlayer.getDuration() / seekBar.getMax();

            String strTime = Util.formatTime(this.progress) + "/"
                    + Util.formatTime(mPlayer.getDuration());
            mTxvPlayTime.setText(strTime);
        }
    };
}
