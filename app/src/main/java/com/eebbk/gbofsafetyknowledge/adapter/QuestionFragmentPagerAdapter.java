package com.eebbk.gbofsafetyknowledge.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * decription ：
 * author ： zhua
 */
public class QuestionFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private final FragmentManager fm;

    public QuestionFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (fragments.size() <= position) {
            position = position % fragments.size();
        }
        return super.instantiateItem(container, position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    public void setFragments(ArrayList<Fragment> fragments) {
        if (this.fragments != null) {
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment f : this.fragments) {
                ft.remove(f);
            }
            ft.commit();
            fm.executePendingTransactions();
        }
        this.fragments = fragments;
        for (Fragment f : this.fragments) {
            Log.e("Fragment", f.toString());
        }
        notifyDataSetChanged();
    }
}
