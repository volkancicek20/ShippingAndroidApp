package com.socksapp.mobileproject.fragment;

import android.app.ProgressDialog;
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
import com.socksapp.mobileproject.databinding.FragmentProfileBinding;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Bu Fragment, Kişisel hesapta erişebileceğiniz fragment'ları gösterir.(örneğin EditProfileFragment vb.)
 */
public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private SharedPreferences nameShared,imageUrlShared;
    private SharedPreferences existsInstitutional;
    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * onCreate methodunda sharedPreferences ile veriler alınır
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nameShared = requireActivity().getSharedPreferences("Name",Context.MODE_PRIVATE);
        imageUrlShared = requireActivity().getSharedPreferences("ImageUrl",Context.MODE_PRIVATE);

        existsInstitutional = requireActivity().getSharedPreferences("ExistsInstitutional", Context.MODE_PRIVATE);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    /**
     * Bu methodda gidilecek olan fragmentların listenerları alınır
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(!existsInstitutional.getString("exists","").isEmpty()){
            binding.accountSettingsLinearLayout.setVisibility(View.GONE);
        }

        binding.accountSettingsLinearLayout.setOnClickListener(this::createInstitutionalAccount);

        setProfile(view);

        binding.myPostLinearLayout.setOnClickListener(this::goMyPostFragment);
        binding.offersLinearLayout.setOnClickListener(this::goMyOffersFragment);
        binding.editProfileLinearLayout.setOnClickListener(this::goEditProfileFragment);
    }


    /**
     * Kurumsal hesap açmadıysa kullanıcı bu method ile oluşturabilir
     */
    private void createInstitutionalAccount(View v) {
        ProgressDialog progressDialog = new ProgressDialog(v.getContext());
        progressDialog.setMessage("Kurumsal Hesap Oluşturuluyor");
        progressDialog.show();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                v.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        SharedPreferences.Editor editor = existsInstitutional.edit();
                        editor.putString("exists","exists");
                        editor.apply();
                        binding.accountSettingsLinearLayout.setVisibility(View.GONE);
                        ProfilePageFragment.tabLayout.setVisibility(View.VISIBLE);
                    }
                });
                timer.cancel();
            }
        }, 1000);

    }

    /**
     * UserOffersFragment'a gidiş
     */
    private void goMyOffersFragment(View v){
        Navigation.findNavController(v).navigate(R.id.action_profilePageFragment_to_userOffersFragment);
    }

    /**
     * MyPostFragment'a gidiş
     */
    private void goMyPostFragment(View v){
        Navigation.findNavController(v).navigate(R.id.action_profilePageFragment_to_myPostFragment);
    }

    /**
     * EditProfileFragment'a gidiş
     */
    private void goEditProfileFragment(View v){
        Navigation.findNavController(v).navigate(R.id.action_profilePageFragment_to_editProfileFragment);
    }

    /**
     * Resim ve kullanıcı adını yazar
     */
    private void setProfile(View view){

        String name = nameShared.getString("name","");
        String imageUrl = imageUrlShared.getString("imageUrl","");

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