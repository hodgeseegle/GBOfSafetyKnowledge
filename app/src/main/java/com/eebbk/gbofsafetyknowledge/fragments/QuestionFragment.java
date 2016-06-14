package com.eebbk.gbofsafetyknowledge.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.eebbk.gbofsafetyknowledge.R;
import com.eebbk.gbofsafetyknowledge.beans.QuestionVO;
import com.eebbk.gbofsafetyknowledge.utils.BitmapUtils;

/**
 * decription ：答题 fragment
 * author ： zhua
 * Created at 2016/6/14.
 */
public class QuestionFragment extends Fragment {

    private RadioGroup mRadioGroup = null;
    private BitmapUtils mBitmapUtils = null;
    private Chose mChose;

    public QuestionFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mChose = (Chose) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = null;
        Bundle data = getArguments();//获得从activity中传递过来的值

        QuestionVO questionVO = (QuestionVO) data.getSerializable("QuestionVO");
        mBitmapUtils = BitmapUtils.getInstance(getActivity().getApplicationContext());
        if (questionVO.getmQuestionFormat() == 2) {
            view = inflater.inflate(R.layout.layout_question_one, container, false);
            initViewOne(view, questionVO);
        } else if (questionVO.getmQuestionFormat() == 1) {
            view = inflater.inflate(R.layout.layout_question_two, container, false);
            initViewTwo(view, questionVO);
        }

        return view;
    }

    //初始化组件
    public void initViewOne(View view, QuestionVO questionVO) {

        RadioButton radioButtonA = (RadioButton) view.findViewById(R.id.RadioButton_A);
        RadioButton radioButtonB = (RadioButton) view.findViewById(R.id.RadioButton_B);
        RadioButton radioButtonC = (RadioButton) view.findViewById(R.id.RadioButton_C);
        RadioButton radioButtonD = (RadioButton) view.findViewById(R.id.RadioButton_D);
        TextView title = (TextView) view.findViewById(R.id.TextView_title);
        ImageView img = (ImageView) view.findViewById(R.id.ImageView_pic);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.RadioGroup_radioBtn);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.RadioButton_A) {
                    mChose.chose("A");
                } else if (i == R.id.RadioButton_B) {
                    mChose.chose("B");
                } else if (i == R.id.RadioButton_C) {
                    mChose.chose("C");
                } else if (i == R.id.RadioButton_D) {
                    mChose.chose("D");
                }
            }
        });

        title.setText(questionVO.getmTitle());
        radioButtonA.setText("A." + questionVO.getmOptionA());
        radioButtonB.setText("B." + questionVO.getmOptionB());
        radioButtonC.setText("C." + questionVO.getmOptionC());
        radioButtonD.setText("D." + questionVO.getmOptionD());

        if (questionVO.getmPicID() != null) {
            Bitmap bmp = mBitmapUtils.getDrawableBitmap(questionVO.getmPicID());
            if (bmp != null) {
                img.setImageBitmap(bmp);
            }
        }
    }

    //初始化组件
    public void initViewTwo(View view, QuestionVO questionVO) {

        TextView title = (TextView) view.findViewById(R.id.TextView_title);
        ImageView picA = (ImageView) view.findViewById(R.id.ImageView_picA);
        ImageView picB = (ImageView) view.findViewById(R.id.ImageView_picB);
        ImageView picC = (ImageView) view.findViewById(R.id.ImageView_picC);
        ImageView picD = (ImageView) view.findViewById(R.id.ImageView_picD);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.RadioGroup_radioBtn);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.RadioButton_A) {
                    mChose.chose("A");
                } else if (i == R.id.RadioButton_B) {
                    mChose.chose("B");
                } else if (i == R.id.RadioButton_C) {
                    mChose.chose("C");
                } else if (i == R.id.RadioButton_D) {
                    mChose.chose("D");
                }
            }
        });

        title.setText(questionVO.getmTitle());
        Bitmap bmpA = mBitmapUtils.getDrawableBitmap(questionVO.getmOptionA());
        Bitmap bmpB = mBitmapUtils.getDrawableBitmap(questionVO.getmOptionB());
        Bitmap bmpC = mBitmapUtils.getDrawableBitmap(questionVO.getmOptionC());
        Bitmap bmpD = mBitmapUtils.getDrawableBitmap(questionVO.getmOptionD());
        if (bmpA != null) {
            picA.setImageBitmap(bmpA);
        }
        if (bmpB != null) {
            picB.setImageBitmap(bmpB);
        }
        if (bmpC != null) {
            picC.setImageBitmap(bmpC);
        }
        if (bmpD != null) {
            picD.setImageBitmap(bmpD);
        }
    }

    //选择答案接口
    public interface Chose {
        public void chose(String answer);
    }
}
