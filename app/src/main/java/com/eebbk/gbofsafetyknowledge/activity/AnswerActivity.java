package com.eebbk.gbofsafetyknowledge.activity;

import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eebbk.gbofsafetyknowledge.R;
import com.eebbk.gbofsafetyknowledge.adapter.HorizontalListViewAdapter;
import com.eebbk.gbofsafetyknowledge.beans.QuestionVO;
import com.eebbk.gbofsafetyknowledge.controls.HorizontalListView;
import com.eebbk.gbofsafetyknowledge.dao.QuestionDAO;
import com.eebbk.gbofsafetyknowledge.fragments.QuestionFragment;
import com.eebbk.gbofsafetyknowledge.adapter.QuestionFragmentPagerAdapter;
import com.eebbk.gbofsafetyknowledge.utils.ToastUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * description:答题界面
 * author:zhua
 */
public class AnswerActivity extends FragmentActivity implements QuestionFragment.Chose {
    //题目数
    private static final int QUESTION_NUM = 10;
    private ViewPager mViewPager;
    private QuestionFragmentPagerAdapter mQuestionFragmentPagerAdapter;
    private QuestionDAO mQuestionDAO;
    //试题内容列表
    private List<QuestionVO> mQuestionsVOs;
    //用户选择的答案
    private HashMap<Integer, String> mAnswers;
    //显示题目Fragment
    private ArrayList<Fragment> mFragments;
    //试题指示器适配器
    private HorizontalListView mHorizontalListView;
    //试题指示器适配器
    private HorizontalListViewAdapter mHorizontalListViewAdapter;
    // 记录当前显示的页码
    private int mCurPageNum = 1;
    //指示器布局
    private LinearLayout mLayoutIndicator;
    //答题结果
    private TextView mTxtResult;
    //建议
    private TextView mTxtProposal;
    //建议布局
    private RelativeLayout mlayoutProposal;
    //二维码布局
    private RelativeLayout mlayoutqrCode;
    //二维码图片
    private ImageView mImgqrCode;
    //播放声音mediaplayer
    private MediaPlayer mPlayer;
    //获得该应用的AssetManager
    AssetManager mAssetManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        initView();
    }

    /**
     * 初始化
     */
    public void initView() {
        int grade = getIntent().getIntExtra("grade", -1);

        mAnswers = new HashMap<Integer, String>();
        mFragments = new ArrayList<Fragment>();
        mQuestionsVOs = new ArrayList<QuestionVO>();
        mViewPager = (ViewPager) findViewById(R.id.ViewPager_Question);
        mViewPager.addOnPageChangeListener(new PageChange());
        mHorizontalListView = (HorizontalListView) findViewById(R.id.HorizontalListView_listview);
        mHorizontalListViewAdapter = new HorizontalListViewAdapter(AnswerActivity.this, QUESTION_NUM);
        mHorizontalListView.setAdapter(mHorizontalListViewAdapter);
        mLayoutIndicator = (LinearLayout) findViewById(R.id.LinearLayout_indicator);
        mTxtResult = (TextView) findViewById(R.id.TextView_result);
        mTxtProposal = (TextView) findViewById(R.id.TextView_proposal);
        mlayoutProposal = (RelativeLayout) findViewById(R.id.RelativeLayout_proposal);
        mlayoutqrCode = (RelativeLayout) findViewById(R.id.RelativeLayout_qrCode);
        mImgqrCode = (ImageView) findViewById(R.id.ImageView_qrCode);
        mPlayer = new MediaPlayer();
        mAssetManager = getAssets();


        mHorizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                mViewPager.setCurrentItem(i);
                mHorizontalListViewAdapter.setSelectIndex(i);
                mHorizontalListViewAdapter.notifyDataSetChanged();
            }
        });

        mQuestionDAO = new QuestionDAO(getApplicationContext());
        mQuestionDAO.copyDatabase();

        if (mQuestionDAO != null) {
            mQuestionsVOs = mQuestionDAO.getQuestions(grade);
        }

        if (mQuestionsVOs.isEmpty()) {
            ToastUtils.showMessageLong(AnswerActivity.this, "暂时没有题目");
            finish();
        }

        for (int i = 0; i < mQuestionsVOs.size(); i++) {
            QuestionFragment questionFragment = new QuestionFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("QuestionVO", mQuestionsVOs.get(i));
            questionFragment.setArguments(bundle);
            mFragments.add(questionFragment);
        }

        mQuestionFragmentPagerAdapter = new QuestionFragmentPagerAdapter(getSupportFragmentManager());
        mQuestionFragmentPagerAdapter.setFragments(mFragments);
        mViewPager.setAdapter(mQuestionFragmentPagerAdapter);

        playSound(mCurPageNum);
    }

    /**
     * 用户答题选择
     */
    public void chose(String answer) {
        mAnswers.put(mViewPager.getCurrentItem(), answer);

        mHorizontalListViewAdapter.setFlag(mViewPager.getCurrentItem());

        if (mViewPager.getCurrentItem() < mFragments.size() - 1 && mAnswers.size() != mFragments.size()) {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
        } else {
            //当点击最后一个选项或者全都选上时，弹出对话框
            if (mAnswers.size() == mFragments.size() || mViewPager.getCurrentItem() == mFragments.size() - 1) {
                if (mAnswers.size() == mFragments.size()) {
                    mHorizontalListViewAdapter.setSelectIndex(-1);
                    mHorizontalListViewAdapter.notifyDataSetChanged();
                }
                showDialog();
            }
        }

        mHorizontalListViewAdapter.notifyDataSetChanged();
    }

    /**
     * 弹出对话框
     */
    protected void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AnswerActivity.this);
        builder.setMessage("确认提交吗？");
        builder.setTitle("提示");
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
        builder.create().show();
    }

    /**
     * 提交
     */
    public void commit() {

        mPlayer.stop();

        //检验是否还有题目未做
        if (mAnswers == null || mAnswers.size() < mQuestionsVOs.size()) {
            ToastUtils.showMessage(this, "还有题目未做完，请做完再提交");
            return;
        }
        //计算检验错误
        ArrayList<String> corrects = new ArrayList<String>();
        ArrayList<String> worngs = new ArrayList<String>();
        for (int i = 0; i < mQuestionsVOs.size(); i++) {
            QuestionVO questionVO = mQuestionsVOs.get(i);
            if (questionVO.getmAnswer().trim().equalsIgnoreCase(mAnswers.get(i))) {
                corrects.add(String.valueOf(i));
            } else {
                worngs.add(String.valueOf(i));
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
            playSound(mCurPageNum);

            mHorizontalListViewAdapter.setSelectIndex(position);
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
    public void showProposal(int corrctNum) {
        mViewPager.setVisibility(View.GONE);
        mLayoutIndicator.setVisibility(View.GONE);

        mTxtResult.setVisibility(View.VISIBLE);
        mlayoutProposal.setVisibility(View.VISIBLE);
        mlayoutqrCode.setVisibility(View.VISIBLE);
        mTxtResult.setText("闯关完成！答对了" + corrctNum + "道题");
    }

    /**
     * 播放声音
     */
    public void playSound(int curPageNum) {
        AssetFileDescriptor fileDescriptor = null;
        mPlayer.reset();
        try {
            fileDescriptor = mAssetManager.openFd("kanong" + curPageNum + ".mp3");
            mPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                    fileDescriptor.getStartOffset(),
                    fileDescriptor.getLength());
            mPlayer.prepareAsync();
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mPlayer.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {

        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {

        if(mPlayer != null && mPlayer.isPlaying()){
            mPlayer.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*if(mPlayer != null){
            mPlayer.start();
        }*/
    }
}
