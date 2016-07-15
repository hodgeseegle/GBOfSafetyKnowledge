package com.eebbk.gbofsafetyknowledge.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.eebbk.gbofsafetyknowledge.R;
import com.eebbk.gbofsafetyknowledge.beans.Indicator;

import java.util.ArrayList;

/**
 * decription ：HorizontalList的适配器
 * author ： zhua
 */

public class HorizontalListViewAdapter extends BaseAdapter {
    private final int mCount;
    private final ArrayList<Indicator> mListView = new ArrayList<>();

    public HorizontalListViewAdapter(Context context, int num) {
        this.mCount = num;
        for (int i = 0; i < num; i++) {

            Indicator indicator = new Indicator();
            indicator.setV(View.inflate(context, R.layout.horizontal_list_item, null));
            if (i == 0) {
                indicator.setIsSelected(1);
            }
            mListView.add(indicator);
        }
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {// 在这里没有直接使用convertView和holder的形式，因为convertView会复用造成小按钮上index不精确

        ImageView view = (ImageView) (mListView
                .get(position).getV().findViewById(R.id.ImageView_img));
        int isSelected = mListView.get(position).getIsSelected();
        int isQuestioned = mListView.get(position).getIsQuestioned();
        if (isSelected == 1) {

            view.setImageResource(R.mipmap.indicator_selected);
            mListView.get(position).getV().setSelected(true);
        } else {
            if (isQuestioned == 0) {
                view.setImageResource(R.mipmap.indicator_normal);
                mListView.get(position).getV().setSelected(false);
            } else if (isQuestioned == 1) {
                view.setImageResource(R.mipmap.indicator_answered);
                mListView.get(position).getV().setSelected(false);
            }
        }
        return mListView.get(position).getV();
    }

    public void setSelectIndex(int i) {

        for (int j = 0; j < mListView.size(); j++) {
            mListView.get(j).setIsSelected(0);
        }
        if (i != -1) {
            mListView.get(i).setIsSelected(1);
        }
        notifyDataSetChanged();
    }

    public void setFlag(int i) {
        mListView.get(i).setIsQuestioned();

        notifyDataSetChanged();
    }
}