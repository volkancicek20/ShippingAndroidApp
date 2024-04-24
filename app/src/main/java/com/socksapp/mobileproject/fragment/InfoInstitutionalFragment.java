package com.socksapp.mobileproject.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.FirebaseFirestore;
import com.socksapp.mobileproject.R;
import com.socksapp.mobileproject.databinding.FragmentInfoInstitutionalBinding;
import java.util.ArrayList;

/**
 * Bu Fragment, Kurumsal hesap size teklif gönderdiyse o hesabın bilgilerine bakmanızı sağlayan fragment'dır.
 */
public class InfoInstitutionalFragment extends Fragment {

    private FragmentInfoInstitutionalBinding binding;
    private FirebaseFirestore firestore;

    public InfoInstitutionalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInfoInstitutionalBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    /**
     * Kurumsal profilin bilgilerini göstermek için mail ve name bilgilerini karşı fragment'dan alınır
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.content.buttonDrawerToggle.setOnClickListener(this::goOffersFragment);

        Bundle args = getArguments();
        if (args != null) {
            String mail = args.getString("mail");
            String name = args.getString("name");
            binding.content.nameFragment.setText(name);
            getDataCity(view,mail);
        }else {

        }

        binding.activeLayout.setOnClickListener(v -> {
            visibleChip();
        });
    }

    /**
     * UserOffersFragment'a geri dönme methodu
     */
    private void goOffersFragment(View view) {
        Navigation.findNavController(view).navigate(R.id.action_infoInstitutionalFragment_to_userOffersFragment);
    }

    /**
     * Kurumsal profilin aktif olduğu illerin gösterildiği layout'u gizler ya da gözüktürür
     */
    private void visibleChip(){
        if(binding.chipGroupCities.getVisibility() == View.GONE){
            binding.chipGroupCities.setVisibility(View.VISIBLE);
        }else {
            binding.chipGroupCities.setVisibility(View.GONE);
        }
    }

    /**
     * kurumsal profilin bilgilerini firebase'den alır
     */
    @SuppressWarnings("unchecked")
    private void getDataCity(View view,String institutionalMail){
        firestore.collection("usersInstitutional").document(institutionalMail).get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()){

                ArrayList<String> cities = (ArrayList<String>) documentSnapshot.get("cities");

                String name = documentSnapshot.getString("name");
                String imageUrl = documentSnapshot.getString("imageUrl");
                String number = documentSnapshot.getString("number");
                String mail = documentSnapshot.getString("mail");

                if(cities != null && name != null && imageUrl != null && number != null && mail != null){
                    ChipGroup chipGroupCities = binding.chipGroupCities;

                    for (String city : cities) {
                        Chip chip = new Chip(view.getContext());
                        chip.setText(city);
                        chip.setCheckable(false);
                        chipGroupCities.addView(chip);
                    }

                    Glide.with(view.getContext())
                        .load(imageUrl)
                        .apply(new RequestOptions()
                        .error(R.drawable.person_active_96)
                        .centerCrop())
                        .into(binding.profileImage);

                    binding.profileName.setText(name);
                    binding.profileMail.setText(mail);
                    binding.profilePhone.setText(number);

                }else {
                    Toast.makeText(view.getContext(),"Bu profil hatalı yüklenmiştir.",Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(view.getContext(),"Bu profil silinmiştir.",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {

        });
    }

}