package com.socksapp.mobileproject.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.socksapp.mobileproject.R;
import com.socksapp.mobileproject.activity.LoginActivity;
import com.socksapp.mobileproject.databinding.FragmentMainBinding;

public class MainFragment extends Fragment {

    private FragmentMainBinding binding;
    private FirebaseAuth auth;
    private FirebaseUser user;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.cardView5.setOnClickListener(this::logout);
        binding.cardView6.setOnClickListener(this::goProfileFragment);
        binding.cardView3.setOnClickListener(this::goAddFragment);
    }

    private void logout(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Uygulamadan çıkış yapılsın mı?");
        builder.setPositiveButton("Çık", (dialog, which) -> {
            auth.signOut();
            Intent intent = new Intent(v.getContext(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });
        builder.setNegativeButton("Hayır", (dialog, which) -> {

        });
        builder.show();
    }

    private void goProfileFragment(View v){
        Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_profileFragment);
    }

    private void goAddFragment(View v){
        Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_addFragment);
    }
}