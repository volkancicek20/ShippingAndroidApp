package com.socksapp.mobileproject.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.socksapp.mobileproject.R;
import com.socksapp.mobileproject.activity.MainActivity;
import com.socksapp.mobileproject.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore firestore;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.forgotpassword.setOnClickListener(v ->{
            if(!binding.loginEmail.getText().toString().trim().isEmpty()){
                resetPassword(v,binding.loginEmail.getText().toString().trim());
            }else {
                binding.loginEmail.setError("Mail adresinizi giriniz");
                Toast.makeText(v.getContext(),"Şifre değişikliği için e-posta adresinizi giriniz.",Toast.LENGTH_SHORT).show();
            }
        });
        binding.confirmMail.setOnClickListener(v ->{
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(view.getContext(), "Doğrulama e-postası gönderildi. Lütfen e-postanızı kontrol edin.", Toast.LENGTH_SHORT).show();
                            binding.confirmMail.setVisibility(View.GONE);
                        } else {
                            Exception exception = task.getException();
                            Toast.makeText(view.getContext(), "Doğrulama e-postası gönderilirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
        binding.register.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment);
        });
        binding.loginButton.setOnClickListener(v -> {
            Editable mail = binding.loginEmail.getText();
            Editable password = binding.loginPassword.getText();
            if(mail != null && password != null){
                String mailText = mail.toString().trim();
                String passwordText = password.toString().trim();
                if(!mailText.isEmpty() && !passwordText.isEmpty()){
                    login(v,mailText,passwordText);
                }else {
                    if(mailText.isEmpty()){
                        binding.loginEmail.setError("E-posta adresinizi giriniz");
                    }
                    if(passwordText.isEmpty()){
                        binding.loginPassword.setError("Şifrenizi giriniz");
                    }
                }
            }
        });
    }

    private void login(View view,String mail,String password){
        ProgressDialog progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("Giriş yapılıyor..");
        progressDialog.setCancelable(false);
        progressDialog.setInverseBackgroundForced(false);
        progressDialog.show();
        auth.signInWithEmailAndPassword(mail, password)
            .addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    progressDialog.dismiss();
                    userVerified(view);
                } else {
                    progressDialog.dismiss();
                    Exception exception = task.getException();
                    if (exception instanceof FirebaseAuthInvalidUserException) {
                        Toast.makeText(view.getContext(), "E-posta adresi kayıtlı değil.", Toast.LENGTH_SHORT).show();
                    } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(view.getContext(), "Geçersiz e-posta veya şifre.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(view.getContext(), "Giriş yaparken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(view.getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            });
    }

    private void resetPassword(View view,String mail) {
        auth.sendPasswordResetEmail(mail)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(view.getContext(), "Şifre sıfırlama bağlantısı e-posta adresinize gönderildi. Lütfen e-postanızı kontrol edin.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(view.getContext(), "Şifre sıfırlama bağlantısı gönderilirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void userVerified(View view){
        if(auth.getCurrentUser().isEmailVerified()){
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
        }else {
            Toast.makeText(view.getContext(), "E-posta adresinizi doğrulamadınız.", Toast.LENGTH_SHORT).show();
            binding.confirmMail.setVisibility(View.VISIBLE);
        }
    }
}