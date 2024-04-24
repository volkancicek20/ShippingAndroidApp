package com.socksapp.mobileproject.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.socksapp.mobileproject.R;

/**
 * Bu Activity, LoginFragment ve RegisterFragment fragment'ları kapsar.
 * Ayrıca kullanıcı daha önceden giriş yapmış ise direkt MainActivity activity'i çağırır.
 */
public class LoginActivity extends AppCompatActivity {

    private FirebaseUser user;
    private FirebaseAuth auth;

    /**
     * onCreate methodunda kullanıcı giriş yapmışsa eğer direkt MainActivity'e yönlendiren komutlar bulunmaktadır.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user != null){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}