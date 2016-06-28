package com.eebbk.gbofsafetyknowledge.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.eebbk.gbofsafetyknowledge.R;

/**
 *description:首页 -- 安全知识大闯关
 *author:zhua
 *creator at:2016/6/13
 */
public class MainActivity extends Activity {

    private ImageView mImgPreschoolBtn;
    private ImageView mImgPrimaryschoolBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImgPreschoolBtn = (ImageView) findViewById(R.id.Button_preschool);
        mImgPrimaryschoolBtn = (ImageView) findViewById(R.id.Button_primaryschool);
    }

    @Override
    protected void onResume() {

        mImgPreschoolBtn.setImageResource(R.mipmap.preshchool_normal);
        mImgPrimaryschoolBtn.setImageResource(R.mipmap.primaryschool_normal);
        super.onResume();
    }

    /**
     *description:点击操作
     */
    public void onClick(View v){
        Intent intent = new Intent(MainActivity.this,AnswerActivity.class);
        switch (v.getId()){
            case R.id.Button_preschool://学前题目
                intent.putExtra("grade",0);//0--学前
                mImgPreschoolBtn.setImageResource(R.mipmap.preshchool_click);
                break;
            case R.id.Button_primaryschool://小学题
                intent.putExtra("grade",1);//1--小学
                mImgPrimaryschoolBtn.setImageResource(R.mipmap.primaryschool_click);
                break;
            default:
                break;
        }
        startActivity(intent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
