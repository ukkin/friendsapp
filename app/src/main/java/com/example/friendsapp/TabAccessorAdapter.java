package com.example.friendsapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAccessorAdapter extends FragmentPagerAdapter {
    int mNumOfTabs;
    TabAccessorAdapter(FragmentManager fm,int NoofTabs) {
        super(fm);
        this.mNumOfTabs = NoofTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                FriendFragment friend= new FriendFragment();
                return friend;
            case 1:
                GroupsFragment groups=new GroupsFragment();
                return groups;
            case 2:
                ContactsFragment contacts=new ContactsFragment();
                return contacts;
             default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return "Friends";
            case 1:
                return "Groups";
            case 2:
                return "Contacts";
                default:
                    return null;
        }
    }
}


