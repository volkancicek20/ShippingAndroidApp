package com.socksapp.mobileproject.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.socksapp.mobileproject.R;
import com.socksapp.mobileproject.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


//        binding.backMain.setOnClickListener(this::goMainFragment);
        binding.institutionalFragmentText.setOnClickListener(this::goInstitutionalFragment);
    }


    private void goMainFragment(View v){
        Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_mainFragment);
    }

    private void goInstitutionalFragment(View v){
        Navigation.findNavController(v).navigate(R.id.action_profileFragment_to_institutionalFragment);
    }
}