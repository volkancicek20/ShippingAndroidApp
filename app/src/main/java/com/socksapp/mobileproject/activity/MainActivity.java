package com.socksapp.mobileproject.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.socksapp.mobileproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private SharedPreferences userName,number,mail,imageUrl,institutionalName,institutionalNumber,institutionalMail,institutionalImageUrl;;

    private String userMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        userName = getSharedPreferences("UserName", Context.MODE_PRIVATE);
        number = getSharedPreferences("Number", Context.MODE_PRIVATE);
        mail = getSharedPreferences("Mail", Context.MODE_PRIVATE);
        imageUrl = getSharedPreferences("ImageUrl", Context.MODE_PRIVATE);
        institutionalName = getSharedPreferences("institutionalName", Context.MODE_PRIVATE);
        institutionalNumber = getSharedPreferences("institutionalNumber", Context.MODE_PRIVATE);
        institutionalMail = getSharedPreferences("institutionalMail", Context.MODE_PRIVATE);
        institutionalImageUrl = getSharedPreferences("institutionalImageUrl", Context.MODE_PRIVATE);

        userMail = user.getEmail();
    }

}