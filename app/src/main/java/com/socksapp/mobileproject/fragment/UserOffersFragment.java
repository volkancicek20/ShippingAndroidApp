package com.socksapp.mobileproject.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.socksapp.mobileproject.R;
import com.socksapp.mobileproject.adapter.GetOffersAdapter;
import com.socksapp.mobileproject.adapter.OffersPagerAdapter;
import com.socksapp.mobileproject.databinding.FragmentUserOffersBinding;
import com.socksapp.mobileproject.model.GetOffersModel;

import java.util.ArrayList;

public class UserOffersFragment extends Fragment {

    private FragmentUserOffersBinding binding;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String userMail;
    public ArrayList<GetOffersModel> getOffersModelArrayList;
    public GetOffersAdapter getOffersAdapter;
    public UserOffersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        getOffersModelArrayList = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserOffersBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerViewOffers.setLayoutManager(new LinearLayoutManager(view.getContext()));
        getOffersAdapter = new GetOffersAdapter(getOffersModelArrayList,view.getContext(),UserOffersFragment.this);
        binding.recyclerViewOffers.setAdapter(getOffersAdapter);

        userMail = user.getEmail();

        getOffersData(view);
    }

    private void getOffersData(View view){
        CollectionReference collection = firestore.collection("offers").document(userMail).collection(userMail);

        collection.orderBy("timestamp", Query.Direction.DESCENDING).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                DocumentReference ref = documentSnapshot.getReference();
                String name = documentSnapshot.getString("institutionalName");
                String imageUrl = documentSnapshot.getString("institutionalImageUrl");
                String number = documentSnapshot.getString("institutionalNumber");
                String mail = documentSnapshot.getString("institutionalMail");
                String price = documentSnapshot.getString("price");
                String startCity = documentSnapshot.getString("startCity");
                String startDistrict = documentSnapshot.getString("startDistrict");
                String endCity = documentSnapshot.getString("endCity");
                String endDistrict = documentSnapshot.getString("endDistrict");
                Timestamp timestamp = documentSnapshot.getTimestamp("timestamp");

                GetOffersModel offers = new GetOffersModel(1,imageUrl,name,number,mail,price,startCity,startDistrict,endCity,endDistrict,timestamp,ref);
                getOffersModelArrayList.add(offers);
                getOffersAdapter.notifyDataSetChanged();

            }
        }).addOnFailureListener(e -> {
            Toast.makeText(view.getContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        });
    }

    public void rejectOffers(View view,DocumentReference ref,String mail,int position){
        ProgressDialog progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("Teklif Reddediliyor..");
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setMessage("Teklifi reddetmek istiyor musunuz?");
        builder.setPositiveButton("REDDET", (dialog, which) ->{
            progressDialog.show();
            CollectionReference collectionReference = firestore.collection("offers").document(mail).collection(mail);
            String deleteRef = ref.getId();
            DocumentReference documentReference = collectionReference.document(deleteRef);

            documentReference.delete().addOnSuccessListener(unused -> {
                if (position != RecyclerView.NO_POSITION) {
                    getOffersModelArrayList.remove(position);
                    getOffersAdapter.notifyItemRemoved(position);
                    getOffersAdapter.notifyDataSetChanged();
                }
                progressDialog.dismiss();
                dialog.dismiss();
                Toast.makeText(view.getContext(),"Teklif reddedildi",Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                dialog.dismiss();
                Toast.makeText(view.getContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            });
        });

        builder.setNegativeButton("Hayır", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();

        dialog.show();
    }
}