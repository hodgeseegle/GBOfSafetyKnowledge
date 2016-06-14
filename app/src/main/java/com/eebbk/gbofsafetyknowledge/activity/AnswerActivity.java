package com.eebbk.gbofsafetyknowledge.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import com.eebbk.gbofsafetyknowledge.R;
import com.eebbk.gbofsafetyknowledge.beans.QuestionVO;
import com.eebbk.gbofsafetyknowledge.fragments.QuestionFragment;
import com.eebbk.gbofsafetyknowledge.fragments.QuestionFragmentPagerAdapter;
import com.eebbk.gbofsafetyknowledge.utils.ToastUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * description:答题界面
 * author:zhua
 * creator at:2016/6/14
 */
public class AnswerActivity extends FragmentActivity implements QuestionFragment.Chose {
    private ViewPager mViewPager;
    private List<QuestionVO> mQuestionsVOs;
    private HashMap<Integer, String> mAnswers;
    private ArrayList<Fragment> mFragments;
    private QuestionFragmentPagerAdapter mQuestionFragmentPagerAdapter;

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
        mAnswers = new HashMap<Integer, String>();
        mFragments = new ArrayList<Fragment>();
        mQuestionsVOs = new ArrayList<QuestionVO>();
        mViewPager = (ViewPager) findViewById(R.id.ViewPager_Question);

        for (int i = 0; i < 10; i++) {
            QuestionVO questionVO = new QuestionVO();
            questionVO.setmTitle("1+1=?");
            questionVO.setmOptionA("normal8.png");
            questionVO.setmOptionB("normal8.png");
            questionVO.setmOptionC("normal8.png");
            questionVO.setmOptionD("normal8.png");
            questionVO.setmAnswer("A");
            questionVO.setmQuestionFormat(1);
//            questionVO.setmPicID("normal8.png");

            mQuestionsVOs.add(i, questionVO);
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
        mViewPager.setOffscreenPageLimit(10);
        mViewPager.setAdapter(mQuestionFragmentPagerAdapter);
    }

    /**
     * 用户答题选择
     */
    public void chose(String answer) {
        mAnswers.put(mViewPager.getCurrentItem(), answer);

        if (mViewPager.getCurrentItem() < mFragments.size() - 1) {
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1, true);
        }else{
            //当点击最后一个选项或者全都选上时，弹出对话框
            if (mAnswers.size() == mFragments.size() || mViewPager.getCurrentItem() == mFragments.size() - 1) {
                showDialog();
            }
        }
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
        finish();
    }
}
