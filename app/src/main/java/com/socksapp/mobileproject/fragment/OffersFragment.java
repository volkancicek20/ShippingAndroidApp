package com.socksapp.mobileproject.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.socksapp.mobileproject.adapter.GetOffersAdapter;
import com.socksapp.mobileproject.databinding.FragmentOffersBinding;
import com.socksapp.mobileproject.model.GetOffersModel;
import com.socksapp.mobileproject.model.GetPostingModel;

import java.util.ArrayList;

public class OffersFragment extends Fragment {

    private FragmentOffersBinding binding;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String userMail;
    public ArrayList<GetOffersModel> getOffersModelArrayList;
    public GetOffersAdapter getOffersAdapter;

    public OffersFragment() {
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
        binding = FragmentOffersBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerViewOffers.setLayoutManager(new LinearLayoutManager(view.getContext()));
        getOffersAdapter = new GetOffersAdapter(getOffersModelArrayList,view.getContext(),OffersFragment.this);
        binding.recyclerViewOffers.setAdapter(getOffersAdapter);

        userMail = user.getEmail();

        getOffersData(view);
    }

    private void getOffersData(View view){
        CollectionReference collection = firestore.collection("offers").document(userMail).collection(userMail);

        collection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

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

                GetOffersModel offers = new GetOffersModel(1,imageUrl,name,number,mail,price,startCity,startDistrict,endCity,endDistrict,timestamp);
                getOffersModelArrayList.add(offers);
                getOffersAdapter.notifyDataSetChanged();

            }
        }).addOnFailureListener(e -> {
            Toast.makeText(view.getContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        });
    }
}