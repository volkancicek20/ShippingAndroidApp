package com.socksapp.mobileproject.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import com.socksapp.mobileproject.databinding.FragmentEditProfileBinding;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Bu Fragment, Kullanıcı profilini düzenleyen ve firebase'e kaydeden fragment'dır.
 */
public class EditProfileFragment extends Fragment {

    private FragmentEditProfileBinding binding;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private SharedPreferences nameShared,numberShared,mailShared,imageUrlShared;
    private String userMail;
    public ActivityResultLauncher<Intent> activityResultLauncher;
    public ActivityResultLauncher<String> permissionLauncher;
    private Bitmap selectedBitmap;
    private Uri imageData;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * onCreate methodunda sharedPreferences ile veriler alınır
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        nameShared = requireActivity().getSharedPreferences("Name", Context.MODE_PRIVATE);
        numberShared = requireActivity().getSharedPreferences("Number",Context.MODE_PRIVATE);
        mailShared = requireActivity().getSharedPreferences("Mail",Context.MODE_PRIVATE);
        imageUrlShared = requireActivity().getSharedPreferences("ImageUrl",Context.MODE_PRIVATE);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditProfileBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    /**
     * onViewCreated methodunda edittextlerin listener'ları ve veri çekme ve kaydetme methodları yer alıyor
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setPrefix();

        binding.content.nameFragment.setText("Kişisel Profilim");
        binding.content.buttonDrawerToggle.setOnClickListener(this::backProfilePage);

        userMail = user.getEmail();

        imageData = null;

        setProfile(view);
        registerLauncher(view);

        binding.profileImage.setOnClickListener(this::setImage);

        binding.saveProfile.setOnClickListener(this::saveProfile);

        binding.nameTextInputLayout.setEndIconOnClickListener(v ->{
            binding.nameEdittext.setEnabled(true);
            binding.nameEdittext.requestFocus();
            binding.nameTextInputLayout.setEndIconVisible(false);
        });

        binding.mailTextInputLayout.setEndIconOnClickListener(v ->{
            binding.mailEdittext.setEnabled(true);
            binding.mailEdittext.requestFocus();
            binding.mailTextInputLayout.setEndIconVisible(false);
        });

        binding.numberTextInputLayout.setEndIconOnClickListener(v -> {
            binding.numberEdittext.setEnabled(true);
            binding.numberEdittext.requestFocus();
            binding.numberTextInputLayout.setEndIconVisible(false);
        });


    }

    /**
     * ProfilePageFragment fragment'a gider
     */
    private void backProfilePage(View view) {
        Navigation.findNavController(view).navigate(R.id.action_editProfileFragment_to_profilePageFragment);
    }

    /**
     * setPrefix methodu numara girdiğimiz zaman sabit sayı olan "+90" ifadesini dizayn ediyor
     */
    private void setPrefix(){
        AppCompatTextView prefixView = binding.numberTextInputLayout.findViewById(com.google.android.material.R.id.textinput_prefix_text);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        prefixView.setLayoutParams(layoutParams);
        prefixView.setGravity(Gravity.CENTER);
    }

    /**
     * Profili düzenlediğimiz zaman bu method gerekli kontroller yapıldıktan sonra firebase'e kaydediyor.
     */
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

    /**
     * Bu method yukarıdaki methodun devamı
     */
    private void uploadProfile(View view, String uploadName,String uploadMail,String uploadNumber,boolean nameCheck,boolean mailCheck,boolean numberCheck) {
        ProgressDialog progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("Kaydediliyor..");
        progressDialog.show();
        DocumentReference usersRef = firestore.collection("users").document(userMail);
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

            storageReference.child("userProfilePhoto").child(userMail).putFile(imageData)
                .addOnSuccessListener(taskSnapshot -> {
                    Task<Uri> downloadUrlTask = taskSnapshot.getStorage().getDownloadUrl();
                    downloadUrlTask.addOnCompleteListener(task -> {
                        String imageUrl = task.getResult().toString();

                        batch.update(usersRef, "imageUrl", imageUrl);

                        batch.commit()
                            .addOnSuccessListener(aVoid -> {
                                progressDialog.dismiss();
                                updateProfile(nameCheck,mailCheck,numberCheck,uploadName,uploadMail,uploadNumber,imageUrl);
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

            batch.commit()
                .addOnSuccessListener(aVoid -> {
                    progressDialog.dismiss();
                    updateProfile(nameCheck,mailCheck,numberCheck,uploadName,uploadMail,uploadNumber);
                    showToastShort("Profiliniz kaydedildi");
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    showErrorMessage(view.getContext(), e.getLocalizedMessage());
                });

        }
    }

    /**
     * Bu method firebase'e yükledikten sonra fragment'ı düzenler. Yeni bilgileri yazdırır ve sharedPreferences'i yeniler.
     * Kullanıcı profil resmi seçdiyse bu method çalışır
     */
    private void updateProfile(boolean nameCheck,boolean mailCheck,boolean numberCheck,String uploadName,String uploadMail,String uploadNumber,String uploadImageUrl){
        if(nameCheck){
            SharedPreferences.Editor editor = nameShared.edit();
            editor.putString("name",uploadName);
            editor.apply();

            binding.nameEdittext.setEnabled(false);
            binding.nameEdittext.setText("");
            binding.nameEdittext.setHint(uploadName);
            binding.nameTextInputLayout.setEndIconVisible(true);
        }
        if(mailCheck){
            SharedPreferences.Editor editor = mailShared.edit();
            editor.putString("mail",uploadMail);
            editor.apply();

            binding.mailEdittext.setEnabled(false);
            binding.mailEdittext.setText("");
            binding.mailEdittext.setHint(uploadMail);
            binding.mailTextInputLayout.setEndIconVisible(true);
        }
        if(numberCheck){
            SharedPreferences.Editor editor = numberShared.edit();
            editor.putString("number",uploadNumber);
            editor.apply();

            binding.numberEdittext.setEnabled(false);
            binding.numberEdittext.setText("");
            binding.numberEdittext.setHint(uploadNumber);
            binding.numberTextInputLayout.setEndIconVisible(true);
        }

        SharedPreferences.Editor editor = imageUrlShared.edit();
        editor.putString("imageUrl",uploadImageUrl);
        editor.apply();

        imageData = null;
    }

    /**
     * Bu method firebase'e yükledikten sonra fragment'ı düzenler. Yeni bilgileri yazdırır ve sharedPreferences'i yeniler.
     * Kullanıcı profil resmi seçmediyse bu method çalışır
     */
    private void updateProfile(boolean nameCheck,boolean mailCheck,boolean numberCheck,String uploadName,String uploadMail,String uploadNumber){
        if(nameCheck){
            SharedPreferences.Editor editor = nameShared.edit();
            editor.putString("name",uploadName);
            editor.apply();

            binding.nameEdittext.setEnabled(false);
            binding.nameEdittext.setText("");
            binding.nameEdittext.setHint(uploadName);
            binding.nameTextInputLayout.setEndIconVisible(true);
        }
        if(mailCheck){
            SharedPreferences.Editor editor = mailShared.edit();
            editor.putString("mail",uploadMail);
            editor.apply();

            binding.mailEdittext.setEnabled(false);
            binding.mailEdittext.setText("");
            binding.mailEdittext.setHint(uploadMail);
            binding.mailTextInputLayout.setEndIconVisible(true);
        }
        if(numberCheck){
            SharedPreferences.Editor editor = numberShared.edit();
            editor.putString("number",uploadNumber);
            editor.apply();

            binding.numberEdittext.setEnabled(false);
            binding.numberEdittext.setText("");
            binding.numberEdittext.setHint(uploadNumber);
            binding.numberTextInputLayout.setEndIconVisible(true);
        }
    }

    /**
     * Profil resmini seçmemize yarayan method budur.
     * Gerekli izinler sorulur
     */
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

    /**
     * Bu method galeriye gider ve resmi seçtiğimiz zaman imageDataya aktarır daha sonra fragment'da belirlediğimiz imageView'a ekler
     * Eğer izin verilmemiş ise izinlere yönlendirir
     */
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

    /**
     * Fragment açılınca sharedPreferences den alınan veriler fragment da belirlediğimiz yerlere aktarılır.
     */
    private void setProfile(View view){

        String name = nameShared.getString("name","");
        String mail = mailShared.getString("mail","");
        String number = numberShared.getString("number","");
        String imageUrl = imageUrlShared.getString("imageUrl","");

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

    public void showToastShort(String message){
        Toast.makeText(requireActivity().getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
    private void showErrorMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}