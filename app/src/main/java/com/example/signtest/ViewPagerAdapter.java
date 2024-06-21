package com.example.signtest;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
public class ViewPagerAdapter extends FragmentPagerAdapter {

    Context context;
    int TotalTabs;

    public ViewPagerAdapter(Context c,@NonNull FragmentManager fm,int totalTabs) {
        super(fm);
        context=c;
        this.TotalTabs=totalTabs;

    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                SignInFragment signInFragment=new SignInFragment();
                return  signInFragment;
            case 1:
                SignUpFragment signUpFragment=new SignUpFragment();
                return signUpFragment;

            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return TotalTabs;
    }
}
