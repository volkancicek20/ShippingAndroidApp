package com.socksapp.mobileproject.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.socksapp.mobileproject.R;
import com.socksapp.mobileproject.databinding.FragmentEditInstitutionalBinding;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Bu Fragment, Eğer kullanıcı kurumsal hesap açmış ise kurumsal hesabın profilini düzenleyen ve firebase'e kaydeden fragment'dır.
 */
public class EditInstitutionalFragment extends Fragment {

    private FragmentEditInstitutionalBinding binding;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private SharedPreferences institutionalNameShared,institutionalNumberShared,institutionalMailShared,institutionalImageUrlShared;;
    private String userMail;
    public ActivityResultLauncher<Intent> activityResultLauncher;
    public ActivityResultLauncher<String> permissionLauncher;
    private Bitmap selectedBitmap;
    private Uri imageData;
    private boolean [] selectedCity;
    private Set<String> cityList;
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

    public EditInstitutionalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        institutionalNameShared = requireActivity().getSharedPreferences("InstitutionalName", Context.MODE_PRIVATE);
        institutionalNumberShared = requireActivity().getSharedPreferences("InstitutionalNumber", Context.MODE_PRIVATE);
        institutionalMailShared = requireActivity().getSharedPreferences("InstitutionalMail", Context.MODE_PRIVATE);
        institutionalImageUrlShared = requireActivity().getSharedPreferences("InstitutionalImageUrl", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditInstitutionalBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setPrefix();

        binding.content.nameFragment.setText("Kurumsal Profilim");
        binding.content.buttonDrawerToggle.setOnClickListener(this::backProfilePage);

        cityList = new HashSet<>();
        selectedCity = new boolean[cityArray.length];

        userMail = user.getEmail();
        imageData = null;

        getDataInstitutional();
        setProfile(view);
        registerLauncher(view);

        binding.profileImage.setOnClickListener(this::setImage);

        binding.saveProfile.setOnClickListener(this::saveProfile);

        binding.cityCardView.setOnClickListener(this::showCityDialog);

        binding.nameTextInputLayout.setEndIconOnClickListener(v -> {
            binding.nameEdittext.setEnabled(true);
            binding.nameEdittext.requestFocus();
            binding.nameTextInputLayout.setEndIconVisible(false);
        });

        binding.mailTextInputLayout.setEndIconOnClickListener(v -> {
            binding.mailEdittext.setEnabled(true);
            binding.mailEdittext.requestFocus();
            binding.mailTextInputLayout.setEndIconVisible(false);
        });

        binding.numberTextInputLayout.setEndIconOnClickListener(v -> {
            binding.numberEdittext.setEnabled(true);
            binding.numberEdittext.requestFocus();
            binding.numberTextInputLayout.setEndIconVisible(false);
        });

        binding.editCity.setOnClickListener(v ->{
            binding.cityCardView.setEnabled(true);
            binding.cityCardView.requestFocus();
            binding.selectCityText.setTextColor(Color.GRAY);
            binding.editCity.setVisibility(View.GONE);
            binding.iconDownCity.setVisibility(View.VISIBLE);
        });
    }

    private void backProfilePage(View view) {
        Bundle args = new Bundle();
        args.putString("type", "1");
        Navigation.findNavController(view).navigate(R.id.action_editInstitutionalFragment_to_profilePageFragment,args);
    }

    private void setPrefix(){
        AppCompatTextView prefixView = binding.numberTextInputLayout.findViewById(com.google.android.material.R.id.textinput_prefix_text);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        prefixView.setLayoutParams(layoutParams);
        prefixView.setGravity(Gravity.CENTER);
    }

    private void saveProfile(View view){
        String nameString = binding.nameEdittext.getText().toString();
        String numberString = binding.numberEdittext.getText().toString();
        String mailString = binding.mailEdittext.getText().toString();

        boolean nameCheck,numberCheck,mailCheck;

        nameCheck = !nameString.isEmpty();

        if(!mailString.isEmpty()){
            Pattern patternMail = Patterns.EMAIL_ADDRESS;
            if (!patternMail.matcher(mailString).matches()){
                binding.mailEdittext.setError("E-posta adresiniz doğrulanamadı");
                return;
            }else {
                mailCheck = true;
            }
        }else {
            mailCheck = false;
        }

        if(!numberString.isEmpty()){
            Pattern patternNumber = Pattern.compile("\\d{10}");
            String strippedPhoneNumber = numberString.replaceAll("\\s+", "");
            if(!patternNumber.matcher(strippedPhoneNumber).matches()){
                binding.numberEdittext.setError("Telefon numaranızı doğrulanamadı");
                return;
            }else {
                numberCheck = true;
            }
        }else {
            numberCheck = false;
        }

        uploadProfile(view,nameString,mailString,numberString,nameCheck,mailCheck,numberCheck);

    }

    private void uploadProfile(View view, String uploadName,String uploadMail,String uploadNumber,boolean nameCheck,boolean mailCheck,boolean numberCheck) {
        ProgressDialog progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("Kaydediliyor..");
        progressDialog.show();
        DocumentReference usersRef = firestore.collection("usersInstitutional").document(userMail);
        WriteBatch batch = firestore.batch();
        Map<String, Object> updates = new HashMap<>();
        if (imageData != null) {
            if(nameCheck && mailCheck && numberCheck){
                updates.put("name", uploadName);
                updates.put("mail", uploadMail);
                updates.put("number", uploadNumber);
                batch.set(usersRef, updates, SetOptions.merge());
            }else if (nameCheck && mailCheck){
                updates.put("name", uploadName);
                updates.put("mail", uploadMail);
                batch.set(usersRef, updates,SetOptions.merge());
            } else if (nameCheck && numberCheck) {
                updates.put("name", uploadName);
                updates.put("number", uploadNumber);
                batch.set(usersRef, updates,SetOptions.merge());
            }else if (mailCheck && numberCheck){
                updates.put("mail", uploadMail);
                updates.put("number", uploadNumber);
                batch.set(usersRef, updates,SetOptions.merge());
            }else {
                if(nameCheck){
                    updates.put("name", uploadName);
                    batch.set(usersRef, updates,SetOptions.merge());
                }
                if(mailCheck){
                    updates.put("mail", uploadMail);
                    batch.set(usersRef, updates,SetOptions.merge());
                }
                if (numberCheck){
                    updates.put("number", uploadNumber);
                    batch.set(usersRef, updates,SetOptions.merge());
                }
            }

            Map<String, Object> citiesMap = new HashMap<>();
            citiesMap.put("cities", new ArrayList<>(cityList));

            DocumentReference userDocRef = firestore.collection("usersInstitutional").document(userMail);
            batch.set(userDocRef, citiesMap, SetOptions.merge());

            storageReference.child("usersInstitutionalProfilePhoto").child(userMail).putFile(imageData)
                .addOnSuccessListener(taskSnapshot -> {
                    Task<Uri> downloadUrlTask = taskSnapshot.getStorage().getDownloadUrl();
                    downloadUrlTask.addOnCompleteListener(task -> {
                        String imageUrl = task.getResult().toString();

                        batch.update(usersRef, "imageUrl", imageUrl);

                        batch.commit()
                            .addOnSuccessListener(aVoid -> {
                                progressDialog.dismiss();
                                updateProfile(!citiesMap.isEmpty(),nameCheck,mailCheck,numberCheck,uploadName,uploadMail,uploadNumber,imageUrl);
                                showToastShort("Profiliniz kaydedildi");
                            })
                            .addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                showErrorMessage(view.getContext(), e.getLocalizedMessage());
                            });
                    });
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    showErrorMessage(view.getContext(), e.getLocalizedMessage());
                });

        } else {
            if(nameCheck && mailCheck && numberCheck){
                updates.put("name", uploadName);
                updates.put("mail", uploadMail);
                updates.put("number", uploadNumber);
                batch.set(usersRef, updates,SetOptions.merge());
            }else if (nameCheck && mailCheck){
                updates.put("name", uploadName);
                updates.put("mail", uploadMail);
                batch.set(usersRef, updates,SetOptions.merge());
            } else if (nameCheck && numberCheck) {
                updates.put("name", uploadName);
                updates.put("number", uploadNumber);
                batch.set(usersRef, updates,SetOptions.merge());
            }else if (mailCheck && numberCheck){
                updates.put("mail", uploadMail);
                updates.put("number", uploadNumber);
                batch.set(usersRef, updates,SetOptions.merge());
            }else {
                if(nameCheck){
                    updates.put("name", uploadName);
                    batch.set(usersRef, updates,SetOptions.merge());
                }
                if(mailCheck){
                    updates.put("mail", uploadMail);
                    batch.set(usersRef, updates,SetOptions.merge());
                }
                if (numberCheck){
                    updates.put("number", uploadNumber);
                    batch.set(usersRef, updates,SetOptions.merge());
                }
            }

            Map<String, Object> citiesMap = new HashMap<>();
            citiesMap.put("cities", new ArrayList<>(cityList));

            DocumentReference userDocRef = firestore.collection("usersInstitutional").document(userMail);
            batch.set(userDocRef, citiesMap, SetOptions.merge());

            batch.commit()
                .addOnSuccessListener(aVoid -> {
                    progressDialog.dismiss();
                    updateProfile(!citiesMap.isEmpty(),nameCheck,mailCheck,numberCheck,uploadName,uploadMail,uploadNumber);
                    showToastShort("Profiliniz kaydedildi");
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    showErrorMessage(view.getContext(), e.getLocalizedMessage());
                });

        }
    }

    private void updateProfile(boolean cityCheck,boolean nameCheck,boolean mailCheck,boolean numberCheck,String uploadName,String uploadMail,String uploadNumber,String uploadImageUrl){
        if(nameCheck){
            SharedPreferences.Editor editor = institutionalNameShared.edit();
            editor.putString("name",uploadName);
            editor.apply();

            binding.nameEdittext.setEnabled(false);
            binding.nameEdittext.setText("");
            binding.nameEdittext.setHint(uploadName);
            binding.nameTextInputLayout.setEndIconVisible(true);
        }
        if(mailCheck){
            SharedPreferences.Editor editor = institutionalMailShared.edit();
            editor.putString("mail",uploadMail);
            editor.apply();

            binding.mailEdittext.setEnabled(false);
            binding.mailEdittext.setText("");
            binding.mailEdittext.setHint(uploadMail);
            binding.mailTextInputLayout.setEndIconVisible(true);
        }
        if(numberCheck){
            SharedPreferences.Editor editor = institutionalNumberShared.edit();
            editor.putString("number",uploadNumber);
            editor.apply();

            binding.numberEdittext.setEnabled(false);
            binding.numberEdittext.setText("");
            binding.numberEdittext.setHint(uploadNumber);
            binding.numberTextInputLayout.setEndIconVisible(true);
        }
        if(cityCheck){
            binding.cityCardView.setEnabled(false);
            binding.editCity.setVisibility(View.VISIBLE);
            binding.iconDownCity.setVisibility(View.GONE);
        }

        SharedPreferences.Editor editor = institutionalImageUrlShared.edit();
        editor.putString("imageUrl",uploadImageUrl);
        editor.apply();

        imageData = null;
    }

    private void updateProfile(boolean cityCheck,boolean nameCheck,boolean mailCheck,boolean numberCheck,String uploadName,String uploadMail,String uploadNumber){
        if(nameCheck){
            SharedPreferences.Editor editor = institutionalNameShared.edit();
            editor.putString("name",uploadName);
            editor.apply();

            binding.nameEdittext.setEnabled(false);
            binding.nameEdittext.setText("");
            binding.nameEdittext.setHint(uploadName);
            binding.nameTextInputLayout.setEndIconVisible(true);
        }
        if(mailCheck){
            SharedPreferences.Editor editor = institutionalMailShared.edit();
            editor.putString("mail",uploadMail);
            editor.apply();

            binding.mailEdittext.setEnabled(false);
            binding.mailEdittext.setText("");
            binding.mailEdittext.setHint(uploadMail);
            binding.mailTextInputLayout.setEndIconVisible(true);
        }
        if(numberCheck){
            SharedPreferences.Editor editor = institutionalNumberShared.edit();
            editor.putString("number",uploadNumber);
            editor.apply();

            binding.numberEdittext.setEnabled(false);
            binding.numberEdittext.setText("");
            binding.numberEdittext.setHint(uploadNumber);
            binding.numberTextInputLayout.setEndIconVisible(true);
        }
        if(cityCheck){
            binding.cityCardView.setEnabled(false);
            binding.editCity.setVisibility(View.VISIBLE);
            binding.iconDownCity.setVisibility(View.GONE);
        }
    }

    private void showCityDialog(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        builder.setTitle("Şehir seçiniz");
        builder.setCancelable(false);

        builder.setMultiChoiceItems(cityArray, selectedCity, (dialog, which, isChecked) -> {
            String city = cityArray[which];
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
                i++;
            }
            binding.selectCityText.setText(stringBuilder.toString());
            binding.selectCityText.setTextColor(Color.GRAY);
            if(cityList.isEmpty()){
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

    private void setImage(View view) {
        String[] permissions;
        String rationaleMessage;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[]{Manifest.permission.READ_MEDIA_IMAGES};
        } else {
            permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        }

        rationaleMessage = "Galeriye gitmek için izin gerekli";

        if (ContextCompat.checkSelfPermission(view.getContext(), permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permissions[0])) {
                Snackbar.make(view, rationaleMessage, Snackbar.LENGTH_INDEFINITE).setAction("İzin ver", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        permissionLauncher.launch(permissions[0]);
                    }
                }).show();
            } else {
                permissionLauncher.launch(permissions[0]);
            }
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intent);
        }
    }

    private void registerLauncher(View view){
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK){
                    Intent intentForResult = result.getData();
                    if(intentForResult != null){
                        imageData = intentForResult.getData();
                        try {
                            ImageDecoder.Source source = ImageDecoder.createSource(view.getContext().getContentResolver(),imageData);
                            selectedBitmap = ImageDecoder.decodeBitmap(source);
                            binding.profileImage.setImageBitmap(selectedBitmap);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result){
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intent);
                }else{
                    showToastShort("İzinleri aktif etmeniz gerekiyor");
                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                }
            }
        });
    }

    private void setProfile(View view){

        String name = institutionalNameShared.getString("name","");
        String mail = institutionalMailShared.getString("mail","");
        String number = institutionalNumberShared.getString("number","");
        String imageUrl = institutionalImageUrlShared.getString("imageUrl","");

        if(!name.isEmpty()){
            binding.nameEdittext.setHint(name);
            binding.nameEdittext.setEnabled(false);
        }else {
            binding.nameTextInputLayout.setEndIconVisible(false);
        }
        if(!number.isEmpty()){
            binding.numberEdittext.setHint(number);
            binding.numberEdittext.setEnabled(false);
        }else {
            binding.numberTextInputLayout.setEndIconVisible(false);
        }
        if(!mail.isEmpty()){
            binding.mailEdittext.setHint(mail);
            binding.mailEdittext.setEnabled(false);
        }else {
            binding.mailTextInputLayout.setEndIconVisible(false);
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

    @SuppressWarnings("unchecked")
    private void getDataInstitutional(){
        firestore.collection("usersInstitutional").document(userMail).get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()){

                Map<String, Object> userData = documentSnapshot.getData();

                if(userData != null){

                    ArrayList<String> citiesData = (ArrayList<String>) userData.get("cities");

                    if(citiesData != null && !citiesData.isEmpty()){
                        cityList.addAll(citiesData);
                        StringBuilder stringBuilder = new StringBuilder();
                        int i = 0;
                        for(String city : cityList){
                            stringBuilder.append(city);
                            if(i != cityList.size() - 1){
                                stringBuilder.append(",");
                            }
                            i++;
                        }
                        binding.selectCityText.setText(stringBuilder.toString());
                        binding.selectCityText.setTextColor(Color.GRAY);
                        binding.cityCardView.setEnabled(false);
                        markSelectedCities();
                    }else {
                        binding.cityCardView.setEnabled(false);
                    }
                }
            }
        });
    }

    private void markSelectedCities() {
        if (selectedCity == null || cityList == null || cityList.isEmpty()) {
            return;
        }

        for (String city : cityList) {
            int index = findCityIndex(city);
            if (index != -1) {
                selectedCity[index] = true;
            }
        }
    }

    private int findCityIndex(String cityName) {
        for (int i = 0; i < cityArray.length; i++) {
            if (cityArray[i].equals(cityName)) {
                return i;
            }
        }
        return -1;
    }


    public void showToastShort(String message){
        Toast.makeText(requireActivity().getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }


    public void showErrorMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}