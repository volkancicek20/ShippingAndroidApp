package com.socksapp.mobileproject.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.socksapp.mobileproject.R;
import com.socksapp.mobileproject.databinding.FragmentInstitutionalOffersNotificationBinding;

public class InstitutionalOffersNotificationFragment extends Fragment {

    private FragmentInstitutionalOffersNotificationBinding binding;

    public InstitutionalOffersNotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInstitutionalOffersNotificationBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.content.nameFragment.setText("Gelen Teklif Bildirimleri");
        binding.content.buttonDrawerToggle.setOnClickListener(this::backProfilePage);
    }

    private void backProfilePage(View view) {
        Navigation.findNavController(view).navigate(R.id.action_institutionalOffersNotificationFragment_to_profilePageFragment);
    }


}