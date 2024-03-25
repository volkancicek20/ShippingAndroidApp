package com.socksapp.mobileproject.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
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

    private SharedPreferences nameShared,numberShared,mailShared,imageUrlShared,institutionalNameShared,institutionalNumberShared,institutionalMailShared,institutionalImageUrlShared;;
    private SharedPreferences personalDone,institutionalDone;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        nameShared = requireActivity().getSharedPreferences("Name", Context.MODE_PRIVATE);
        numberShared = requireActivity().getSharedPreferences("Number", Context.MODE_PRIVATE);
        mailShared = requireActivity().getSharedPreferences("Mail", Context.MODE_PRIVATE);
        imageUrlShared = requireActivity().getSharedPreferences("ImageUrl", Context.MODE_PRIVATE);

        institutionalNameShared = requireActivity().getSharedPreferences("InstitutionalName", Context.MODE_PRIVATE);
        institutionalNumberShared = requireActivity().getSharedPreferences("InstitutionalNumber", Context.MODE_PRIVATE);
        institutionalMailShared = requireActivity().getSharedPreferences("InstitutionalMail", Context.MODE_PRIVATE);
        institutionalImageUrlShared = requireActivity().getSharedPreferences("InstitutionalImageUrl", Context.MODE_PRIVATE);

        personalDone = requireActivity().getSharedPreferences("PersonalDone", Context.MODE_PRIVATE);
        institutionalDone = requireActivity().getSharedPreferences("InstitutionalDone", Context.MODE_PRIVATE);

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
        binding.cardView1.setOnClickListener(this::goGetPostingJobFragment);
        binding.cardView2.setOnClickListener(this::goSearchingFragment);
        binding.cardView4.setOnClickListener(this::goOffersFragment);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                shutdown(view);
            }
        });

    }

    private void shutdown(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Uygulamadan çıkış yapılsın mı?");
        builder.setPositiveButton("Çık", (dialog, which) -> {
            System.exit(0);
        });
        builder.setNegativeButton("Hayır", (dialog, which) -> {

        });
        builder.show();
    }
    private void logout(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Hesaptan çıkış yapılsın mı?");
        builder.setPositiveButton("Çık", (dialog, which) -> {
            deleteData();
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

    private void goGetPostingJobFragment(View v){
        Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_getPostingJobFragment);
    }

    private void goSearchingFragment(View v){
        Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_searchingFragment);
    }

    private void goOffersFragment(View v){
        Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_offersFragment);
    }

    private void deleteData(){
        deleteSharedPreference(nameShared);
        deleteSharedPreference(mailShared);
        deleteSharedPreference(numberShared);
        deleteSharedPreference(imageUrlShared);

        deleteSharedPreference(institutionalNameShared);
        deleteSharedPreference(institutionalMailShared);
        deleteSharedPreference(institutionalNumberShared);
        deleteSharedPreference(institutionalImageUrlShared);

        deleteSharedPreference(personalDone);
        deleteSharedPreference(institutionalDone);
    }
    private void deleteSharedPreference(SharedPreferences sharedPreferences){
        SharedPreferences.Editor delete = sharedPreferences.edit();
        delete.clear();
        delete.apply();
    }
}