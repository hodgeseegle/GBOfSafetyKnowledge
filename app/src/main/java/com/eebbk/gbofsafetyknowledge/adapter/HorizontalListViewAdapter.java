package com.eebbk.gbofsafetyknowledge.adapter;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.widget.BaseAdapter;

/**
 * decription ：HorizontalList的适配器
 * author ： zhua
 */
import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eebbk.gbofsafetyknowledge.R;
import com.eebbk.gbofsafetyknowledge.beans.Indicator;

public class HorizontalListViewAdapter extends BaseAdapter {
    private final int mCount;
    private final Context mContext;
    private final ArrayList<Indicator> mListView = new ArrayList<>();

    public HorizontalListViewAdapter(Context context, int num) {
        this.mContext = context;
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

        TextView view = (TextView) (mListView
                .get(position).getV().findViewById(R.id.TextView_img));
        int isSelected = mListView.get(position).getIsSelected();
        int isQuestioned = mListView.get(position).getIsQuestioned();
        if (isSelected == 1) {

            view.setBackgroundColor(getColor(mContext,R.color.red));
            mListView.get(position).getV().setSelected(true);
        } else {
            if (isQuestioned == 0) {

                view.setBackgroundColor(getColor(mContext,R.color.black));
                mListView.get(position).getV().setSelected(false);
            } else if (isQuestioned == 1) {

                view.setBackgroundColor(getColor(mContext,R.color.green));
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
        mListView.get(i).setIsQuestioned(1);

        notifyDataSetChanged();
    }

    public  int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }
}