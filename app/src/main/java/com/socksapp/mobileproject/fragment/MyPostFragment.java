package com.socksapp.mobileproject.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.socksapp.mobileproject.R;
import com.socksapp.mobileproject.adapter.GetPostingAdapter;
import com.socksapp.mobileproject.databinding.FragmentMyPostBinding;
import com.socksapp.mobileproject.model.GetPostingModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyPostFragment extends Fragment {

    private FragmentMyPostBinding binding;
    public static FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private static String userMail;

    public static ArrayList<GetPostingModel> getPostingModelArrayList;
    public static GetPostingAdapter getPostingAdapter;

    public MyPostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        getPostingModelArrayList = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyPostBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userMail = user.getEmail();

        binding.content.nameFragment.setText("İlanlarım");
        binding.content.buttonDrawerToggle.setOnClickListener(this::backProfilePage);

        binding.recyclerViewMyPost.setLayoutManager(new LinearLayoutManager(view.getContext()));
        getPostingAdapter = new GetPostingAdapter(getPostingModelArrayList,view.getContext(),MyPostFragment.this);
        binding.recyclerViewMyPost.setAdapter(getPostingAdapter);

        getPost(view);

    }

    private void backProfilePage(View view) {
        Navigation.findNavController(view).navigate(R.id.action_myPostFragment_to_profilePageFragment);
    }

    private void getPost(View view){

        CollectionReference collection = firestore.collection("postMe").document(userMail).collection(userMail);

        collection.orderBy("timestamp", Query.Direction.DESCENDING).get().addOnSuccessListener(queryDocumentSnapshots -> {
            if(queryDocumentSnapshots.isEmpty()){
                GetPostingModel post = new GetPostingModel();
                post.viewType = 3;
                getPostingModelArrayList.add(post);
                getPostingAdapter.notifyDataSetChanged();
            }
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                String name = documentSnapshot.getString("name");
                String imageUrl = documentSnapshot.getString("imageUrl");
                String startCity = documentSnapshot.getString("startCity");
                String startDistrict = documentSnapshot.getString("startDistrict");
                String endCity = documentSnapshot.getString("endCity");
                String endDistrict = documentSnapshot.getString("endDistrict");
                String loadType = documentSnapshot.getString("loadType");
                String loadAmount = documentSnapshot.getString("loadAmount");
                String date = documentSnapshot.getString("date");
                String time = documentSnapshot.getString("time");
                String number = documentSnapshot.getString("number");
                String mail = documentSnapshot.getString("mail");
                Timestamp timestamp = documentSnapshot.getTimestamp("timestamp");
                DocumentReference ref = documentSnapshot.getReference();

                GetPostingModel post = new GetPostingModel(1,imageUrl,name,startCity,startDistrict,endCity,endDistrict,loadType,loadAmount,date,time,number,mail,timestamp,userMail,ref);
                getPostingModelArrayList.add(post);
                getPostingAdapter.notifyDataSetChanged();

            }
        }).addOnFailureListener(e -> {
            Toast.makeText(view.getContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        });
    }

    public static void deleteOffers(View view, DocumentReference ref, String myMail, int position,String city){
        ProgressDialog progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("İlan Kaldırılıyor..");
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setMessage("İlanı kaldırmak istiyor musunuz?");
        builder.setPositiveButton("KALDIR", (dialog, which) ->{
            progressDialog.show();
            WriteBatch batch = firestore.batch();

            DocumentReference docRef1 = firestore.collection("post" + city).document(ref.getId());
            batch.delete(docRef1);

            DocumentReference docRef2 = firestore.collection("postMe").document(myMail).collection(myMail).document(ref.getId());
            batch.delete(docRef2);

            batch.commit()
                .addOnSuccessListener(aVoid -> {
                    if (position != RecyclerView.NO_POSITION) {
                        getPostingModelArrayList.remove(position);
                        getPostingAdapter.notifyItemRemoved(position);
                        getPostingAdapter.notifyDataSetChanged();
                    }
                    progressDialog.dismiss();
                    dialog.dismiss();
                    Toast.makeText(view.getContext(), "İlanınız Kaldırıldı", Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    dialog.dismiss();
                    Toast.makeText(view.getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });
        });

        builder.setNegativeButton("Hayır", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    public static void dialogShow(View view, DocumentReference ref, String myMail, int position,String city){
        final Dialog dialog = new Dialog(view.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout);

        LinearLayout delete = dialog.findViewById(R.id.layoutDelete);

        LinearLayout save = dialog.findViewById(R.id.layoutSave);
        LinearLayout offers = dialog.findViewById(R.id.layoutOffer);
        LinearLayout report = dialog.findViewById(R.id.layoutReport);
        View line = dialog.findViewById(R.id.line);

        save.setVisibility(View.GONE);
        offers.setVisibility(View.GONE);
        report.setVisibility(View.GONE);
        line.setVisibility(View.GONE);




        delete.setOnClickListener(v -> {
            dialog.dismiss();
            deleteOffers(view,ref,myMail,position,city);
        });

        if(dialog.getWindow() != null){
            dialog.show();
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setGravity(Gravity.BOTTOM);
        }
        else {
            Toast.makeText(view.getContext(),"Dialog null",Toast.LENGTH_SHORT).show();
        }
    }


}