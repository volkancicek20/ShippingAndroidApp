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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.socksapp.mobileproject.adapter.GetInstitutionalAdapter;
import com.socksapp.mobileproject.adapter.GetPostingAdapter;
import com.socksapp.mobileproject.databinding.FragmentSearchingBinding;
import com.socksapp.mobileproject.model.GetInstitutionalModel;
import com.socksapp.mobileproject.model.GetPostingModel;

import java.util.ArrayList;
import java.util.Map;

public class SearchingFragment extends Fragment {

    private FragmentSearchingBinding binding;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseUser user;
    public ArrayList<GetInstitutionalModel> getInstitutionalModelArrayList;
    public GetInstitutionalAdapter getInstitutionalAdapter;

    public SearchingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        getInstitutionalModelArrayList = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchingBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerViewFind.setLayoutManager(new LinearLayoutManager(view.getContext()));
        getInstitutionalAdapter = new GetInstitutionalAdapter(getInstitutionalModelArrayList,view.getContext(),SearchingFragment.this);
        binding.recyclerViewFind.setAdapter(getInstitutionalAdapter);

        getData();
    }

    @SuppressWarnings("unchecked")
    private void getData(){

        CollectionReference collection;
        collection = firestore.collection("usersInstitutional");

        collection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                Map<String, Object> userData = documentSnapshot.getData();

                ArrayList<String> cities = (ArrayList<String>) userData.get("cities");

                String name = (String) userData.get("name");
                String imageUrl = (String) userData.get("imageUrl");
                String number = (String) userData.get("number");
                String mail = (String) userData.get("mail");

                if(cities != null && name != null && imageUrl != null && number != null && mail != null){
                    GetInstitutionalModel find = new GetInstitutionalModel(1,imageUrl,name,number,mail,cities);
                    getInstitutionalModelArrayList.add(find);
                    getInstitutionalAdapter.notifyDataSetChanged();
                }

            }
        }).addOnFailureListener(e -> {
            e.printStackTrace();
        });
    }
}