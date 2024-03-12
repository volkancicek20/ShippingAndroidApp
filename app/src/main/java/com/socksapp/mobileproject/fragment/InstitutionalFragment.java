package com.socksapp.mobileproject.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.socksapp.mobileproject.R;
import com.socksapp.mobileproject.databinding.FragmentInstitutionalBinding;

import java.util.ArrayList;

public class InstitutionalFragment extends Fragment {

    private FragmentInstitutionalBinding binding;
    private boolean [] selectedCity;
    private ArrayList<String> cityList;
    private final String[] cityArray = {
            "İstanbul","Ankara","İzmir","Adana", "Adıyaman", "Afyonkarahisar", "Ağrı", "Amasya", "Antalya", "Artvin", "Aydın", "Balıkesir",
            "Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa", "Çanakkale", "Çankırı", "Çorum", "Denizli",
            "Diyarbakır", "Edirne", "Elazığ", "Erzincan", "Erzurum", "Eskişehir", "Gaziantep", "Giresun", "Gümüşhane", "Hakkâri",
            "Hatay", "Isparta", "Mersin", "Kars", "Kastamonu", "Kayseri", "Kırklareli", "Kırşehir",
            "Kocaeli", "Konya", "Kütahya", "Malatya", "Manisa", "Kahramanmaraş", "Mardin", "Muğla", "Muş", "Nevşehir",
            "Niğde", "Ordu", "Rize", "Sakarya", "Samsun", "Siirt", "Sinop", "Sivas", "Tekirdağ", "Tokat",
            "Trabzon", "Tunceli", "Şanlıurfa", "Uşak", "Van", "Yozgat", "Zonguldak", "Aksaray", "Bayburt", "Karaman",
            "Kırıkkale", "Batman", "Şırnak", "Bartın", "Ardahan", "Iğdır", "Yalova", "Karabük", "Kilis", "Osmaniye",
            "Düzce"
    };

    public InstitutionalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInstitutionalBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cityList = new ArrayList<>();
        selectedCity = new boolean[cityArray.length];
        binding.cityCardView.setOnClickListener(this::showCityDialog);
        binding.backPersonalProfile.setOnClickListener(this::goPersonalProfile);
    }

    private void showCityDialog(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        builder.setTitle("Şehir seçiniz");
        builder.setCancelable(false);

        builder.setMultiChoiceItems(cityArray, selectedCity, (dialog, which, isChecked) -> {
            System.out.println("which: "+which);
            String city = cityArray[which];
            System.out.println("city: "+city);
            System.out.println("++++++++++++++++++++++");
            if(isChecked){
                cityList.add(city);
            }else {
                cityList.remove(city);
            }
        }).setPositiveButton("TAMAM", (dialog, which) -> {
            StringBuilder stringBuilder = new StringBuilder();
            int i = 0;
            for(String city : cityList){
                stringBuilder.append(city);
                if(i != cityList.size() - 1){
                    stringBuilder.append(",");
                }
                binding.selectCityText.setText(stringBuilder.toString());
                i++;
            }
            if(cityList.size() == 0){
                binding.selectCityText.setText("");
            }
        }).setNegativeButton("ÇIK", (dialog, which) -> {
            dialog.dismiss();
        }).setNeutralButton("TEMİZLE", (dialog, which) -> {
            for(int i = 0; i < selectedCity.length; i++){
                selectedCity[i] = false;
                cityList.clear();
                binding.selectCityText.setText("");
            }
        });
        builder.show();
    }

    private void goPersonalProfile(View v){
        Navigation.findNavController(v).navigate(R.id.action_institutionalFragment_to_profileFragment);
    }
}