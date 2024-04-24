package com.socksapp.mobileproject.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.socksapp.mobileproject.R;
import com.socksapp.mobileproject.databinding.FragmentInstitutionalBinding;

/**
 * Bu Fragment, Kurumsal hesabınız var ise kurumsal hesapta erişebileceğiniz fragment'ları gösterir.(örneğin EditInstitutionalFragment vb.)
 */
public class InstitutionalFragment extends Fragment {

    private FragmentInstitutionalBinding binding;
    private SharedPreferences institutionalNameShared,institutionalImageUrlShared;;
    public InstitutionalFragment() {
        // Required empty public constructor
    }

    /**
     * onCreate methodunda sharedPreferences ile veriler alınır
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        institutionalNameShared = requireActivity().getSharedPreferences("InstitutionalName", Context.MODE_PRIVATE);
        institutionalImageUrlShared = requireActivity().getSharedPreferences("InstitutionalImageUrl", Context.MODE_PRIVATE);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInstitutionalBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    /**
     * Bu methodda gidilecek olan fragmentların listenerları alınır
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setProfile(view);

        binding.editProfileLinearLayout.setOnClickListener(this::goEditInstitutionalFragment);
        binding.bookmarkLinearLayout.setOnClickListener(this::goSavedPostFragment);
        binding.notificationLinearLayout.setOnClickListener(this::goInstitutionalOffersNotificationFragment);

    }

    /**
     * EditInstitutionalFragment'a gitme kodu
     */
    private void goEditInstitutionalFragment(View v){
        Navigation.findNavController(v).navigate(R.id.action_profilePageFragment_to_editInstitutionalFragment);
    }

    /**
     * SavedPostFragment'a gitme kodu
     */
    private void goSavedPostFragment(View v){
        Navigation.findNavController(v).navigate(R.id.action_profilePageFragment_to_savedPostFragment);
    }

    /**
     * InstitutionalOffersNotificationFragment'a gitme kodu
     */
    private void goInstitutionalOffersNotificationFragment(View v){
        Navigation.findNavController(v).navigate(R.id.action_profilePageFragment_to_institutionalOffersNotificationFragment);
    }


    /**
     * Resim ve Kullanıcı adı alınır ve yazılır
     */
    private void setProfile(View view){

        String name = institutionalNameShared.getString("name","");
        String imageUrl = institutionalImageUrlShared.getString("imageUrl","");

        if(!name.isEmpty()){
            binding.profileName.setText(name);
        }
        if(!imageUrl.isEmpty()){
            Glide.with(view.getContext())
                .load(imageUrl)
                .apply(new RequestOptions()
                .error(R.drawable.person_active_96)
                .centerCrop())
                .into(binding.profileImage);
        }else {
            binding.profileImage.setImageResource(R.drawable.person_active_96);
        }

    }

}