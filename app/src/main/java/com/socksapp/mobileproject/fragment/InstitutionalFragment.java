package com.socksapp.mobileproject.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.socksapp.mobileproject.R;
import com.socksapp.mobileproject.databinding.FragmentInstitutionalBinding;

import java.util.ArrayList;

public class InstitutionalFragment extends Fragment {

    private FragmentInstitutionalBinding binding;
    private boolean [] selectedCity;
    private final ArrayList<Integer> cityList = new ArrayList<>();
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
        selectedCity = new boolean[cityArray.length];
        binding.cityCardView.setOnClickListener(this::showCityDialog);
    }

    private void showCityDialog(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        builder.setTitle("Şehir seçiniz");
        builder.setCancelable(false);

        builder.setMultiChoiceItems(cityArray, selectedCity, (dialog, which, isChecked) -> {
            if(isChecked){
                cityList.add(which);
            }else {
                cityList.remove(which);
            }
        }).setPositiveButton("TAMAM", (dialog, which) -> {
            StringBuilder stringBuilder = new StringBuilder();
            for(int i = 0; i < cityList.size(); i++){
                stringBuilder.append(cityArray[cityList.get(i)]);
                if(i != cityList.size() - 1){
                    stringBuilder.append(",");
                }
                binding.selectCityText.setText(stringBuilder.toString());
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
}