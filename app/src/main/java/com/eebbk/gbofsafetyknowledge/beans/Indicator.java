package com.eebbk.gbofsafetyknowledge.beans;

import android.view.View;

/**
 * decription ：指示器类
 * author ： zhua
 */
public class Indicator {

    private int isSelected;// 0  未选中  1选中

    private int isQuestioned;//0 未答  1答过

    private View v;

    public Indicator(){
        isSelected = 0;
        isQuestioned = 0;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    public int getIsQuestioned() {
        return isQuestioned;
    }

    public void setIsQuestioned(int isQuestioned) {
        this.isQuestioned = isQuestioned;
    }

    public View getV() {
        return v;
    }

    public void setV(View v) {
        this.v = v;
    }
}
