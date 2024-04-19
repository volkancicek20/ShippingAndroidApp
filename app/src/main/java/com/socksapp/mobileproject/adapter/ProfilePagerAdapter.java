package com.socksapp.mobileproject.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.socksapp.mobileproject.fragment.InstitutionalFragment;
import com.socksapp.mobileproject.fragment.ProfileFragment;

public class ProfilePagerAdapter extends FragmentStateAdapter {
    public ProfilePagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1){
            return new InstitutionalFragment();
        }
        return new ProfileFragment();
    }
    @Override
    public int getItemCount() {
        return 2;
    }
}