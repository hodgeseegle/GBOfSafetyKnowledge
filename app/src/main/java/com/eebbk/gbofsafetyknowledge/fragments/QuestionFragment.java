package com.eebbk.gbofsafetyknowledge.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.eebbk.gbofsafetyknowledge.R;
import com.eebbk.gbofsafetyknowledge.beans.QuestionVO;
import com.eebbk.gbofsafetyknowledge.controls.MarqueeTextView;
import com.eebbk.gbofsafetyknowledge.controls.MyRadioGroup;
import com.eebbk.gbofsafetyknowledge.utils.BitmapUtils;

/**
 * decription ：答题 fragment
 * author ： zhua
 */
public class QuestionFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "QuestionFragment";
    private MyRadioGroup mRadioGroup = null;
    private BitmapUtils mBitmapUtils = null;
    private Chose mChose;
    private RadioButton mRadioButton_A;
    private RadioButton mRadioButton_B;
    private RadioButton mRadioButton_C;
    private RadioButton mRadioButton_D;
    private LinearLayout mLayout_A = null;
    private LinearLayout mLayout_B = null;
    private LinearLayout mLayout_C = null;
    private LinearLayout mLayout_D = null;
    private Typeface mFontFace;

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
        if (questionVO == null) {
            return null;
        }
        mBitmapUtils = BitmapUtils.getInstance(getActivity().getApplicationContext());
        mFontFace = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/FZSEJW.TTF");
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
    private void initViewOne(View view, QuestionVO questionVO) {
        MarqueeTextView optionA = (MarqueeTextView) view.findViewById(R.id.TextView_optionA);
        optionA.setOnClickListener(this);
        optionA.setOnTouchListener(new TouchListener());
        MarqueeTextView optionB = (MarqueeTextView) view.findViewById(R.id.TextView_optionB);
        optionB.setOnClickListener(this);
        optionB.setOnTouchListener(new TouchListener());
        MarqueeTextView optionC = (MarqueeTextView) view.findViewById(R.id.TextView_optionC);
        optionC.setOnClickListener(this);
        optionC.setOnTouchListener(new TouchListener());
        MarqueeTextView optionD = (MarqueeTextView) view.findViewById(R.id.TextView_optionD);
        optionD.setOnClickListener(this);
        optionD.setOnTouchListener(new TouchListener());
        TextView title = (TextView) view.findViewById(R.id.TextView_title);
        title.setTypeface(mFontFace);
        ImageView img = (ImageView) view.findViewById(R.id.ImageView_pic);

        mRadioButton_A = (RadioButton) view.findViewById(R.id.RadioButton_A);
        mRadioButton_A.setOnTouchListener(new TouchListener());
        mRadioButton_B = (RadioButton) view.findViewById(R.id.RadioButton_B);
        mRadioButton_B.setOnTouchListener(new TouchListener());
        mRadioButton_C = (RadioButton) view.findViewById(R.id.RadioButton_C);
        mRadioButton_C.setOnTouchListener(new TouchListener());
        mRadioButton_D = (RadioButton) view.findViewById(R.id.RadioButton_D);
        mRadioButton_D.setOnTouchListener(new TouchListener());
        mRadioGroup = (MyRadioGroup) view.findViewById(R.id.RadioGroup_radioBtn);

        mLayout_A = (LinearLayout) view.findViewById(R.id.LinearLayout_A);
        mLayout_B = (LinearLayout) view.findViewById(R.id.LinearLayout_B);
        mLayout_C = (LinearLayout) view.findViewById(R.id.LinearLayout_C);
        mLayout_D = (LinearLayout) view.findViewById(R.id.LinearLayout_D);

        mRadioGroup.setOnCheckedChangeListener(new MyRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(MyRadioGroup radioGroup, int i) {
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
        optionA.setText(questionVO.getmOptionA());
        optionB.setText(questionVO.getmOptionB());
        optionC.setText(questionVO.getmOptionC());
        optionD.setText(questionVO.getmOptionD());

        if (questionVO.getmPicID() != null) {
            Bitmap bmp = mBitmapUtils.getDrawableBitmap(questionVO.getmPicID());
            if (bmp != null) {
                img.setImageBitmap(bmp);
            }
        }
    }

    //初始化组件
    private void initViewTwo(View view, QuestionVO questionVO) {

        TextView title = (TextView) view.findViewById(R.id.TextView_title);
        title.setTypeface(mFontFace);
        ImageView picA = (ImageView) view.findViewById(R.id.ImageView_picA);
        picA.setOnClickListener(this);
        ImageView picB = (ImageView) view.findViewById(R.id.ImageView_picB);
        picB.setOnClickListener(this);
        ImageView picC = (ImageView) view.findViewById(R.id.ImageView_picC);
        picC.setOnClickListener(this);
        ImageView picD = (ImageView) view.findViewById(R.id.ImageView_picD);
        picD.setOnClickListener(this);

        mRadioButton_A = (RadioButton) view.findViewById(R.id.RadioButton_A);
        mRadioButton_B = (RadioButton) view.findViewById(R.id.RadioButton_B);
        mRadioButton_C = (RadioButton) view.findViewById(R.id.RadioButton_C);
        mRadioButton_D = (RadioButton) view.findViewById(R.id.RadioButton_D);
        mRadioGroup = (MyRadioGroup) view.findViewById(R.id.RadioGroup_radioBtn);

        mRadioGroup.setOnCheckedChangeListener(new MyRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(MyRadioGroup radioGroup, int i) {
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
        Bitmap bmpA = mBitmapUtils.getDrawableBitmap(questionVO.getmOptionA().toLowerCase() + ".png");
        Bitmap bmpB = mBitmapUtils.getDrawableBitmap(questionVO.getmOptionB().toLowerCase() + ".png");
        Bitmap bmpC = mBitmapUtils.getDrawableBitmap(questionVO.getmOptionC().toLowerCase() + ".png");
        Bitmap bmpD = mBitmapUtils.getDrawableBitmap(questionVO.getmOptionD().toLowerCase() + ".png");
        Log.i(TAG, "initViewTwo: " + questionVO.getmOptionD().toLowerCase() + ".png");

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ImageView_picA:
            case R.id.TextView_optionA:
                mRadioButton_A.setChecked(true);
                break;
            case R.id.ImageView_picB:
            case R.id.TextView_optionB:
                mRadioButton_B.setChecked(true);
                break;
            case R.id.ImageView_picC:
            case R.id.TextView_optionC:
                mRadioButton_C.setChecked(true);
                break;
            case R.id.TextView_optionD:
            case R.id.ImageView_picD:
                mRadioButton_D.setChecked(true);
                break;
            default:
                break;
        }
    }

    public class TouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                   /* if(mLayout_A != null && view.getParent() == mLayout_A){

                    }*/
                    switch (view.getId()) {
                        case R.id.RadioButton_A:
                        case R.id.TextView_optionA:
                            if (mLayout_A != null) {
                                mLayout_A.setBackgroundColor(getResources().getColor(R.color.title_touch_color));
                            }
                            break;
                        case R.id.RadioButton_B:
                        case R.id.TextView_optionB:
                            if (mLayout_B != null) {
                                mLayout_B.setBackgroundColor(getResources().getColor(R.color.title_touch_color));
                            }
                            break;
                        case R.id.RadioButton_C:
                        case R.id.TextView_optionC:
                            if (mLayout_C != null) {
                                mLayout_C.setBackgroundColor(getResources().getColor(R.color.title_touch_color));
                            }
                            break;
                        case R.id.RadioButton_D:
                        case R.id.TextView_optionD:
                            if (mLayout_D != null) {
                                mLayout_D.setBackgroundColor(getResources().getColor(R.color.title_touch_color));
                            }
                            break;
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_OUTSIDE:
                case MotionEvent.ACTION_UP:
                    switch (view.getId()) {
                        case R.id.RadioButton_A:
                        case R.id.TextView_optionA:
                            if (mLayout_A != null) {
                                mLayout_A.setBackgroundColor(getResources().getColor(R.color.transparent));
                            }
                            break;
                        case R.id.RadioButton_B:
                        case R.id.TextView_optionB:
                            if (mLayout_B != null) {
                                mLayout_B.setBackgroundColor(getResources().getColor(R.color.transparent));
                            }
                            break;
                        case R.id.RadioButton_C:
                        case R.id.TextView_optionC:
                            if (mLayout_C != null) {
                                mLayout_C.setBackgroundColor(getResources().getColor(R.color.transparent));
                            }
                            break;
                        case R.id.RadioButton_D:
                        case R.id.TextView_optionD:
                            if (mLayout_D != null) {
                                mLayout_D.setBackgroundColor(getResources().getColor(R.color.transparent));
                            }
                            break;
                    }
                    break;
            }
            return false;
        }
    }

    //选择答案接口
    public interface Chose {
        void chose(String answer);
    }
}
