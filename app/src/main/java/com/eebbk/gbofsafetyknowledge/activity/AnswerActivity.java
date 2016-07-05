package com.eebbk.gbofsafetyknowledge.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eebbk.gbofsafetyknowledge.R;
import com.eebbk.gbofsafetyknowledge.adapter.HorizontalListViewAdapter;
import com.eebbk.gbofsafetyknowledge.adapter.QuestionFragmentPagerAdapter;
import com.eebbk.gbofsafetyknowledge.beans.QuestionVO;
import com.eebbk.gbofsafetyknowledge.controls.HorizontalListView;
import com.eebbk.gbofsafetyknowledge.controls.MyLoadingView;
import com.eebbk.gbofsafetyknowledge.dao.QuestionDAO;
import com.eebbk.gbofsafetyknowledge.fragments.QuestionFragment;
import com.eebbk.gbofsafetyknowledge.utils.ToastUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * description:答题界面
 * author:zhua
 */
public class AnswerActivity extends FragmentActivity implements QuestionFragment.Chose {

    //题目数
    private static final int QUESTION_NUM = 6;
    //筛选题目结束
    public static final int SELECT_OVER = 0;
    //筛选题目结束
    private static final int SLEEP = 1;
    private ViewPager mViewPager;
    private QuestionDAO mQuestionDAO;
    //试题内容列表
    private List<QuestionVO> mQuestionsVOs;
    //用户选择的答案
    private SparseArray<String> mAnswers;
    //显示题目Fragment
    private ArrayList<Fragment> mFragments;
    //试题指示器适配器
    private HorizontalListViewAdapter mHorizontalListViewAdapter;
    // 记录当前显示的页码
    private int mCurPageNum = 1;
    //指示器布局
    private LinearLayout mLayoutIndicator;
    //答题结果One
    private TextView mTxtResultOne;
    //答题结果Two
    private TextView mTxtResulTwo;
    //建议内容
    private TextView mTxtProposal_content;
    //建议布局
    private RelativeLayout mlayoutProposal;
    //二维码布局
    private RelativeLayout mlayoutqrCode;
    //播放声音mediaplayer
    private MediaPlayer mPlayer;
    //获得该应用的AssetManager
    private AssetManager mAssetManager;
    //自定义消息处理
    private MyHandler mMyHandler;
    //缓冲提示
    private MyLoadingView mMyLoadingView;
    //播放任务
    private PlaysyncTask mPlaysyncTask;
    //总布局
    private RelativeLayout mlayoutAnswer;
    //标题背景
    private LinearLayout mlayoutTitleBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        int grade = getIntent().getIntExtra("grade", -1);

        mAnswers = new SparseArray<>();
        mFragments = new ArrayList<>();
        mQuestionsVOs = new ArrayList<>();
        mViewPager = (ViewPager) findViewById(R.id.ViewPager_Question);
        mViewPager.addOnPageChangeListener(new PageChange());
        HorizontalListView horizontalListView = (HorizontalListView) findViewById(R.id.HorizontalListView_listview);
        mHorizontalListViewAdapter = new HorizontalListViewAdapter(AnswerActivity.this, QUESTION_NUM);
        horizontalListView.setAdapter(mHorizontalListViewAdapter);
        mLayoutIndicator = (LinearLayout) findViewById(R.id.LinearLayout_indicator);
        mTxtResultOne = (TextView) findViewById(R.id.TextView_resultOne);
        mTxtResulTwo = (TextView) findViewById(R.id.TextView_resultTwo);
        Typeface fontFace = Typeface.createFromAsset(getAssets(),
                "fonts/FZSEJW.TTF");
        mTxtResultOne.setTypeface(fontFace);
        mTxtResulTwo.setTypeface(fontFace);
        mTxtProposal_content = (TextView) findViewById(R.id.TextView_proposal_content);
        mlayoutProposal = (RelativeLayout) findViewById(R.id.RelativeLayout_proposal);
        mlayoutqrCode = (RelativeLayout) findViewById(R.id.RelativeLayout_qrCode);
        mMyLoadingView = (MyLoadingView) findViewById(R.id.MyLoadingView_LoadingView);
        mlayoutAnswer = (RelativeLayout) findViewById(R.id.layoutanswer_bg);
        mlayoutTitleBg = (LinearLayout) findViewById(R.id.layouttitle_bg);
        ImageView imgVideoPlay = (ImageView) findViewById(R.id.ImageView_video);
        imgVideoPlay.setOnClickListener(new ClickListener());
        mPlayer = new MediaPlayer();
        mAssetManager = getAssets();
        mMyHandler = new MyHandler(this);

        horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                mViewPager.setCurrentItem(i);
                mHorizontalListViewAdapter.setSelectIndex(i);
                mHorizontalListViewAdapter.notifyDataSetChanged();
            }
        });

        mQuestionDAO = new QuestionDAO(getApplicationContext());

        showProgress();
        new ReadDAOsyncTask().execute(grade);
    }

    /**
     * 初始化数据
     */
    private void initData() {

        for (int i = 0; i < mQuestionsVOs.size(); i++) {
            QuestionFragment questionFragment = new QuestionFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("QuestionVO", mQuestionsVOs.get(i));
            questionFragment.setArguments(bundle);
            mFragments.add(questionFragment);
        }

        QuestionFragmentPagerAdapter questionFragmentPagerAdapter = new QuestionFragmentPagerAdapter(getSupportFragmentManager());
        questionFragmentPagerAdapter.setFragments(mFragments);
        mViewPager.setAdapter(questionFragmentPagerAdapter);

        mPlaysyncTask = new PlaysyncTask();
        mPlaysyncTask.execute(mQuestionsVOs.get(mCurPageNum - 1).getmVoiceID());
    }

    /**
     * 用户答题选择
     */
    public void chose(String answer) {
        mAnswers.put(mViewPager.getCurrentItem(), answer);

        mHorizontalListViewAdapter.setFlag(mViewPager.getCurrentItem());

        if (mViewPager.getCurrentItem() < mFragments.size() - 1 && mAnswers.size() != mFragments.size()) {
            mMyHandler.sendEmptyMessageDelayed(SLEEP, 1000);
        } else {
            //当点击最后一个选项或者全都选上时，弹出对话框
            if (mAnswers.size() == mFragments.size() || mViewPager.getCurrentItem() == mFragments.size() - 1) {
                if (mAnswers.size() == mFragments.size()) {
                    mHorizontalListViewAdapter.setSelectIndex(-1);
                    mHorizontalListViewAdapter.notifyDataSetChanged();
                }
                showDlg(2);
            }
        }

        mHorizontalListViewAdapter.notifyDataSetChanged();
    }

    /**
     * 弹出对话框
     */
    private void showDlg(int flag) {//1  退出  2  提交
        mPlayer.stop();

        AlertDialog.Builder builder = new AlertDialog.Builder(AnswerActivity.this);
        if (flag == 1) {
            builder.setMessage("确认要退出吗？");
            builder.setTitle("提示");
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else if (flag == 2) {
            builder.setMessage("确认提交吗？");
            builder.setTitle("提示");
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setPositiveButton("提交", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    commit();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        builder.create().show();
    }

    /**
     * 提交
     */
    private void commit() {

        //检验是否还有题目未做
        if (mAnswers == null || mAnswers.size() < mQuestionsVOs.size()) {
            ToastUtils.showMessage(this, "还有题目未做完，请做完再提交");
            return;
        }
        //计算检验错误
        ArrayList<String> corrects = new ArrayList<>();
        for (int i = 0; i < mQuestionsVOs.size(); i++) {
            QuestionVO questionVO = mQuestionsVOs.get(i);
            if (questionVO.getmAnswer().trim().equalsIgnoreCase(mAnswers.get(i))) {
                corrects.add(String.valueOf(i));
            }
        }

        showProposal(corrects.size());
    }

    /**
     * 页面滑动监听
     */
    private class PageChange implements ViewPager.OnPageChangeListener {

        boolean mIsForL = true;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            mCurPageNum = position + 1;
            if (mPlaysyncTask != null) {
                mPlaysyncTask.cancel(true);
                mPlaysyncTask = null;
            }

            mPlaysyncTask = new PlaysyncTask();
            mPlaysyncTask.execute(mQuestionsVOs.get(mCurPageNum - 1).getmVoiceID());

            mHorizontalListViewAdapter.setSelectIndex(position);
            mMyHandler.removeMessages(SLEEP);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == 1) {
                mIsForL = true;
            }
            if (state == 2) {
                mIsForL = false;
            }
            if (mCurPageNum == 1) {
                if (state == 0 && mIsForL) {
                    ToastUtils.showMessage(getApplicationContext(), "已经是第一页喽");
                }
            } else if (mCurPageNum == mFragments.size()) {
                if (state == 0 && mIsForL) {
                    ToastUtils.showMessage(getApplicationContext(), "已经是最后一页喽");
                }
            }
        }
    }

    /**
     * 显示建议
     */
    private void showProposal(int corrctNum) {
        mViewPager.setVisibility(View.GONE);
        mLayoutIndicator.setVisibility(View.GONE);

        mTxtResultOne.setVisibility(View.VISIBLE);
        mTxtResulTwo.setVisibility(View.VISIBLE);
        mlayoutProposal.setVisibility(View.VISIBLE);
        mlayoutTitleBg.setVisibility(View.VISIBLE);
        mlayoutqrCode.setVisibility(View.VISIBLE);
        mlayoutAnswer.setBackgroundResource(R.mipmap.bg_proposal);

        switch (corrctNum) {
            case 0:
            case 1:
            case 2:
                mTxtResultOne.setText(getString(R.string.resultOne_oneortwo, corrctNum));
                mTxtResulTwo.setText(getString(R.string.resultTwo_oneortwo));
                mTxtProposal_content.setText(getString(R.string.proposal_oneortwo));
                break;
            case 3:
            case 4:
                mTxtResultOne.setText(getString(R.string.resultOne_threeorfour, corrctNum));
                mTxtResulTwo.setText(getString(R.string.resultTwo_threeorfour));
                mTxtProposal_content.setText(getString(R.string.proposal_threeorfour));
                break;
            case 5:
                mTxtResultOne.setText(getString(R.string.resultOne_five));
                mTxtResulTwo.setText(getString(R.string.resultTwo_five));
                mTxtProposal_content.setText(getString(R.string.proposal_five));
                break;
            case 6:
                mTxtResultOne.setText(getString(R.string.resultOne_six));
                mTxtResulTwo.setText(getString(R.string.resultTwo_six));
                mTxtProposal_content.setText(getString(R.string.proposal_six));
                break;
        }
    }

    @Override
    protected void onDestroy() {

        if (mPlayer != null && mPlayer.isPlaying()) {//停止声音，并销毁
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {//暂停声音

        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
        }
        super.onPause();
    }

    /**
     * 消息处理
     */
    public static class MyHandler extends Handler {

        final WeakReference<AnswerActivity> mActivity;

        @SuppressWarnings("unchecked")
        MyHandler(AnswerActivity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        @SuppressWarnings("unchecked")
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            AnswerActivity activity = mActivity.get();
            switch (msg.what) {
                case SELECT_OVER:
                    activity.hideProgress();
                    activity.mQuestionsVOs = (List<QuestionVO>) msg.obj;
                    if (!activity.mQuestionsVOs.isEmpty()) {
                        activity.initData();
                    } else {
                        ToastUtils.showMessageLong(activity, "暂时没有题目");
                        activity.finish();
                    }
                    break;
                case SLEEP:
                    activity.mViewPager.setCurrentItem(activity.mViewPager.getCurrentItem() + 1, true);
                    break;


                default:
                    break;
            }
        }
    }

    /**
     * 读取数据库线程
     */
    private class ReadDAOsyncTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... integers) {
            mQuestionDAO.selectQuestion(integers[0], mMyHandler);
            return null;
        }
    }

    /**
     * 显示缓冲
     */
    private void showProgress() {

        mViewPager.setVisibility(View.GONE);
        mLayoutIndicator.setVisibility(View.GONE);
        mTxtResultOne.setVisibility(View.GONE);
        mTxtResulTwo.setVisibility(View.GONE);
        mlayoutProposal.setVisibility(View.GONE);
        mlayoutTitleBg.setVisibility(View.GONE);
        mlayoutqrCode.setVisibility(View.GONE);

        mMyLoadingView.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏缓冲
     */
    private void hideProgress() {

        mMyLoadingView.setVisibility(View.GONE);
        mViewPager.setVisibility(View.VISIBLE);
        mLayoutIndicator.setVisibility(View.VISIBLE);
    }

    private class PlaysyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... params) {
            AssetFileDescriptor fileDescriptor;
            mPlayer.reset();
            try {
                fileDescriptor = mAssetManager.openFd(params[0] + ".ogg");
                mPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                        fileDescriptor.getStartOffset(),
                        fileDescriptor.getLength());
                mPlayer.prepareAsync();
                mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        if (!isCancelled()) {
                            mPlayer.start();
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showDlg(1);
        }
        return super.onKeyDown(keyCode, event);
    }

    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.ImageView_video) {
                Intent intent = new Intent(AnswerActivity.this, VideoPlayerActivity.class);
                startActivity(intent);
            }
        }
    }
}
