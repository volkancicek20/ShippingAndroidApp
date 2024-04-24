package com.socksapp.mobileproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.socksapp.mobileproject.databinding.ActivityMainBinding;
import com.socksapp.mobileproject.myclass.RefDataAccess;
import java.util.Map;

/**
 * Bu Activity, LoginFragment ve RegisterFragment fragment'ları hariç tüm fragment'ları kapsayan activity'dir.
 * Bu Activity, Navigation kullanarak fragment'ların geçişlerini sağlayan activity'dir.
 * Main olarak MainFragment gözükür.
 * Kullanıcının bilgileri firebase'den alarak SharedPreferences database'e kaydeder.
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private SharedPreferences nameShared,numberShared,mailShared,imageUrlShared,institutionalNameShared,institutionalNumberShared,institutionalMailShared,institutionalImageUrlShared;
    private SharedPreferences personalDone,institutionalDone;
    private SharedPreferences existsInstitutional;
    private String userMail;
    public RefDataAccess refDataAccess;

    /**
     * onCreate methodunda kullanıcının bilgilerini sharedPreferences ile saklama kodları bulunuyor.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        refDataAccess = new RefDataAccess(this);
        refDataAccess.open();

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        nameShared = getSharedPreferences("Name", Context.MODE_PRIVATE);
        numberShared = getSharedPreferences("Number", Context.MODE_PRIVATE);
        mailShared = getSharedPreferences("Mail", Context.MODE_PRIVATE);
        imageUrlShared = getSharedPreferences("ImageUrl", Context.MODE_PRIVATE);

        institutionalNameShared = getSharedPreferences("InstitutionalName", Context.MODE_PRIVATE);
        institutionalNumberShared = getSharedPreferences("InstitutionalNumber", Context.MODE_PRIVATE);
        institutionalMailShared = getSharedPreferences("InstitutionalMail", Context.MODE_PRIVATE);
        institutionalImageUrlShared = getSharedPreferences("InstitutionalImageUrl", Context.MODE_PRIVATE);

        personalDone = getSharedPreferences("PersonalDone", Context.MODE_PRIVATE);
        institutionalDone = getSharedPreferences("InstitutionalDone", Context.MODE_PRIVATE);

        existsInstitutional = getSharedPreferences("ExistsInstitutional", Context.MODE_PRIVATE);

        userMail = user.getEmail();

        getDataPersonal();
        getDataInstitutional();
    }

    private void getDataPersonal(){
        if(!personalDone.getString("done","").equals("done")){
            firestore.collection("users").document(userMail).get().addOnSuccessListener(documentSnapshot -> {
                if(documentSnapshot.exists()){
                    String name = documentSnapshot.getString("name");
                    String mail = documentSnapshot.getString("mail");
                    String number = documentSnapshot.getString("number");
                    String imageUrl = documentSnapshot.getString("imageUrl");

                    if(name != null && !name.isEmpty()){
                        SharedPreferences.Editor editor = nameShared.edit();
                        editor.putString("name",name);
                        editor.apply();
                    }
                    if(number != null && !number.isEmpty()){
                        SharedPreferences.Editor editor = mailShared.edit();
                        editor.putString("mail",mail);
                        editor.apply();
                    }
                    if(mail != null && !mail.isEmpty()){
                        SharedPreferences.Editor editor = numberShared.edit();
                        editor.putString("number",number);
                        editor.apply();
                    }
                    if(imageUrl != null && !imageUrl.isEmpty()){
                        SharedPreferences.Editor editor = imageUrlShared.edit();
                        editor.putString("imageUrl",imageUrl);
                        editor.apply();
                    }
                    if(name != null && !name.isEmpty() && number != null && !number.isEmpty() && mail != null && !mail.isEmpty() && imageUrl != null && !imageUrl.isEmpty()){
                        SharedPreferences.Editor editor = personalDone.edit();
                        editor.putString("done","done");
                        editor.apply();
                    }
                }
            });
        }
    }

    private void getDataInstitutional(){
        if(!institutionalDone.getString("done","").equals("done")){
            firestore.collection("usersInstitutional").document(userMail).get().addOnSuccessListener(documentSnapshot -> {
                if(documentSnapshot.exists()){

                    SharedPreferences.Editor exists = existsInstitutional.edit();
                    exists.putString("exists","exists");
                    exists.apply();

                    Map<String, Object> userData = documentSnapshot.getData();

                    if(userData != null){
                        String name = (String) userData.get("name");
                        String mail = (String) userData.get("mail");
                        String number = (String) userData.get("number");
                        String imageUrl = (String) userData.get("imageUrl");

                        if(name != null && !name.isEmpty()){
                            SharedPreferences.Editor editor = institutionalNameShared.edit();
                            editor.putString("name",name);
                            editor.apply();
                        }
                        if(number != null && !number.isEmpty()){
                            SharedPreferences.Editor editor = institutionalMailShared.edit();
                            editor.putString("mail",mail);
                            editor.apply();
                        }
                        if(mail != null && !mail.isEmpty()){
                            SharedPreferences.Editor editor = institutionalNumberShared.edit();
                            editor.putString("number",number);
                            editor.apply();
                        }
                        if(imageUrl != null && !imageUrl.isEmpty()){
                            SharedPreferences.Editor editor = institutionalImageUrlShared.edit();
                            editor.putString("imageUrl",imageUrl);
                            editor.apply();
                        }
                        if(name != null && !name.isEmpty() && number != null && !number.isEmpty() && mail != null && !mail.isEmpty() && imageUrl != null && !imageUrl.isEmpty()){
                            SharedPreferences.Editor editor = institutionalDone.edit();
                            editor.putString("done","done");
                            editor.apply();
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        refDataAccess.close();
    }
}