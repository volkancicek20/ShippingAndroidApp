package com.socksapp.mobileproject.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.socksapp.mobileproject.R;
import com.socksapp.mobileproject.databinding.FragmentRegisterBinding;

/**
 * Bu Fragment, Kullanıcıların e-posta adresleriyle kayıt yapacağı fragment'dır.
 */
public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore firestore;

    public RegisterFragment() {
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
        binding = FragmentRegisterBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    /**
     * mail ve şifre edittextleri kontrol eder
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.loginPage.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_registerFragment_to_loginFragment);
        });
        binding.signupButton.setOnClickListener(v -> {
            Editable mail = binding.signupEmail.getText();
            Editable password = binding.signupPassword.getText();
            Editable passwordCheck = binding.signupConfirm.getText();
            if(mail != null && password != null && passwordCheck != null){
                String mailText = mail.toString().trim();
                String passwordText = password.toString().trim();
                String passwordConfirmText = passwordCheck.toString().trim();
                if(!mailText.isEmpty() && !passwordText.isEmpty() && !passwordConfirmText.isEmpty()){
                    if(passwordText.equals(passwordConfirmText)){
                        sign(v,mailText,passwordText);
                    }else {
                        binding.signupConfirm.setError("Şifreler eşleşmiyor");
                    }
                }else {
                    if(mailText.isEmpty()){
                        binding.signupEmail.setError("E-posta adresinizi giriniz");
                    }
                    if(passwordText.isEmpty()){
                        binding.signupPassword.setError("Şifrenizi giriniz");
                    }
                    if(passwordConfirmText.isEmpty()){
                        binding.signupConfirm.setError("Şifrenizi tekrar giriniz");
                    }
                }
            }
        });
    }

    /**
     * Bu method ile firebase'e kayıt olur ve veriler yazılır
     */
    private void sign(View view,String mail,String password){
        ProgressDialog progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("Kayıt olunuyor..");
        progressDialog.setCancelable(false);
        progressDialog.setInverseBackgroundForced(false);
        progressDialog.show();
        auth.createUserWithEmailAndPassword(mail, password)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    sendEmailVerification(view);
                } else {
                    progressDialog.dismiss();
                    Exception exception = task.getException();
                    if(exception instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(view.getContext(), "Bu e-posta adresiyle zaten kayıtlı bir kullanıcı var.", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(view.getContext(), "Kayıt olurken bir hata oluştu. Hata:["+exception+"]", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                    Toast.makeText(view.getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            });
    }

    /**
     * mail dogrulama linki gönderilir
     */
    private void sendEmailVerification(View view) {
        auth.getCurrentUser().sendEmailVerification()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(view.getContext(), "Doğrulama e-postası gönderildi. Lütfen e-postanızı kontrol edin.", Toast.LENGTH_LONG).show();
                } else {
                    Exception exception = task.getException();
                    Toast.makeText(view.getContext(), "Doğrulama e-postası gönderilirken bir hata oluştu.", Toast.LENGTH_SHORT).show();
                }
            });
    }
}