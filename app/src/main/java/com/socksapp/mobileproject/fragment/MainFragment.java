package com.socksapp.mobileproject.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.socksapp.mobileproject.R;
import com.socksapp.mobileproject.activity.LoginActivity;
import com.socksapp.mobileproject.activity.MainActivity;
import com.socksapp.mobileproject.databinding.FragmentMainBinding;

/**
 * Bu Fragment, AddFragment,ProfilePageFragment ve GetPostingJobFragment fragment'lara geçiş için gerekli kodları barındıran fragment'dır.
 * Ayrıca hesaptan çıkış kodu bulunmaktadır.
 */
public class MainFragment extends Fragment {

    private FragmentMainBinding binding;
    private FirebaseAuth auth;
    private FirebaseUser user;

    private SharedPreferences nameShared,numberShared,mailShared,imageUrlShared,institutionalNameShared,institutionalNumberShared,institutionalMailShared,institutionalImageUrlShared;;
    private SharedPreferences personalDone,institutionalDone,existsInstitutional;
    private MainActivity mainActivity;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * onCreate methodunda sharedPreferences ile veriler alınır
     */
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
        existsInstitutional = requireActivity().getSharedPreferences("ExistsInstitutional", Context.MODE_PRIVATE);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    /**
     * Fragment'lara gitmek için cardviewlar dinlenir
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.cardView5.setOnClickListener(this::logout);
        binding.cardView6.setOnClickListener(this::goProfileFragment);
        binding.cardView3.setOnClickListener(this::goAddFragment);
        binding.cardView1.setOnClickListener(this::goGetPostingJobFragment);

    }

    /**
     * Hesaptan çıkma kodu
     * Hesaptan çıkarken sharedPreferences deki verilerde silinir
     */
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

    /**
     * ProfileFragment'a gider
     */
    private void goProfileFragment(View v){
        Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_profilePageFragment);
    }

    /**
     * AddFragment'a gider
     */
    private void goAddFragment(View v){
        Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_addFragment);
    }

    /**
     * GetPostingJobFragment'a gider
     */
    private void goGetPostingJobFragment(View v){
        Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_getPostingJobFragment);
    }


    /**
     * Tüm veriler silinir
     */
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
        deleteSharedPreference(existsInstitutional);

        mainActivity.refDataAccess.deleteAllData();
    }

    private void deleteSharedPreference(SharedPreferences sharedPreferences){
        SharedPreferences.Editor delete = sharedPreferences.edit();
        delete.clear();
        delete.apply();
    }

    /**
     * Bu method MainActivity'i fragment'a bağlar bu sayede MainActivity de olan bilgileri kullanabilirim
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }
    }
}