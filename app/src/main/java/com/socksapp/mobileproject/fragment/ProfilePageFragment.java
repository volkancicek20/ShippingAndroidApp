package com.socksapp.mobileproject.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.socksapp.mobileproject.R;
import com.socksapp.mobileproject.adapter.ProfilePagerAdapter;
import com.socksapp.mobileproject.databinding.FragmentProfilePageBinding;

public class ProfilePageFragment extends Fragment {

    private FragmentProfilePageBinding binding;
    public static TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ProfilePagerAdapter adapter;
    private SharedPreferences existsInstitutional;

    private String type;
    public ProfilePageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        existsInstitutional = requireActivity().getSharedPreferences("ExistsInstitutional", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfilePageBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.content.nameFragment.setText("Profilim");
        binding.content.buttonDrawerToggle.setOnClickListener(this::goMainFragment);

        Bundle args = getArguments();
        if (args != null) {
            type = args.getString("type");
            if(type != null && type.equals("1")){
                type = "1";
            }else {
                type = "0";
            }
        }

        tabLayout = binding.tabLayout;
        viewPager2 = binding.viewPager;
        tabLayout.addTab(tabLayout.newTab().setText("Kişisel Hesap"));
        tabLayout.addTab(tabLayout.newTab().setText("Kurumsal Hesap"));

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        adapter = new ProfilePagerAdapter(fragmentManager, getLifecycle());

        viewPager2.setAdapter(adapter);

        if(existsInstitutional.getString("exists","").isEmpty()){
            tabLayout.setVisibility(View.GONE);
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(type.equals("1")){
                    viewPager2.setCurrentItem(Integer.parseInt(type));
                }else {
                    viewPager2.setCurrentItem(tab.getPosition());
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if(type.equals("1")){
                    tabLayout.selectTab(tabLayout.getTabAt(Integer.parseInt(type)));
                    type = "0";
                }else {
                    tabLayout.selectTab(tabLayout.getTabAt(position));
                }
            }
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                goMainFragment(view);
            }
        });

    }

    private void goMainFragment(View v){
        Navigation.findNavController(v).navigate(R.id.action_profilePageFragment_to_mainFragment);
    }

}