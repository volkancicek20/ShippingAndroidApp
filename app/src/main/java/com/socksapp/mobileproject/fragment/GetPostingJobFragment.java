package com.socksapp.mobileproject.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.socksapp.mobileproject.R;
import com.socksapp.mobileproject.adapter.GetPostingAdapter;
import com.socksapp.mobileproject.databinding.FragmentGetPostingJobBinding;
import com.socksapp.mobileproject.model.GetPostingModel;

import java.util.ArrayList;

public class GetPostingJobFragment extends Fragment {

    private FragmentGetPostingJobBinding binding;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String[] cityNames;
    private ArrayAdapter<String> cityAdapter;
    private AutoCompleteTextView cityCompleteTextView;

    public ArrayList<GetPostingModel> getPostingModelArrayList;
    public GetPostingAdapter getPostingAdapter;

    public GetPostingJobFragment() {
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
        binding = FragmentGetPostingJobBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cityNames = getResources().getStringArray(R.array.city_names);
        cityAdapter = new ArrayAdapter<>(requireContext(), R.layout.list_item,cityNames);
        cityCompleteTextView = binding.getRoot().findViewById(R.id.city_complete_text);
        cityCompleteTextView.setAdapter(cityAdapter);

        binding.recyclerViewPost.setLayoutManager(new LinearLayoutManager(view.getContext()));
        getPostingAdapter = new GetPostingAdapter(getPostingModelArrayList,view.getContext(),GetPostingJobFragment.this);
        binding.recyclerViewPost.setAdapter(getPostingAdapter);

        getPost(view,"");

        binding.findPost.setOnClickListener(v -> {
            getPostingModelArrayList.clear();
            getPost(v,binding.cityCompleteText.getText().toString());
        });
    }


    private void getPost(View view,String city){
        CollectionReference collection;
        if(city.isEmpty()){
            collection = firestore.collection("postİstanbul");
        }else {
            collection = firestore.collection("post"+city);
        }

        collection.get().addOnSuccessListener(queryDocumentSnapshots -> {
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

                GetPostingModel post = new GetPostingModel(1,imageUrl,name,startCity,startDistrict,endCity,endDistrict,loadType,loadAmount,date,time,number,mail,timestamp);
                getPostingModelArrayList.add(post);
                getPostingAdapter.notifyDataSetChanged();

            }
        }).addOnFailureListener(e -> {
            Toast.makeText(view.getContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        });
    }
}