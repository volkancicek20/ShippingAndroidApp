package com.socksapp.mobileproject.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.socksapp.mobileproject.R;
import com.socksapp.mobileproject.activity.MainActivity;
import com.socksapp.mobileproject.adapter.GetPostingAdapter;
import com.socksapp.mobileproject.adapter.SavedPostAdapter;
import com.socksapp.mobileproject.databinding.FragmentSavedPostBinding;
import com.socksapp.mobileproject.model.GetPostingModel;
import com.socksapp.mobileproject.model.RefItem;

import java.util.ArrayList;
import java.util.List;

public class SavedPostFragment extends Fragment {

    private FragmentSavedPostBinding binding;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private MainActivity mainActivity;
    private List<RefItem> arrayList;
    private String userMail;
    public SavedPostAdapter savedPostAdapter;
    public ArrayList<GetPostingModel> savedPostArrayList;

    public SavedPostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrayList = new ArrayList<>();
        savedPostArrayList = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSavedPostBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userMail = user.getEmail();

        binding.content.nameFragment.setText("Kaydedilen İlanlar");
        binding.content.buttonDrawerToggle.setOnClickListener(this::backProfilePage);

        binding.recyclerViewSavedPost.setLayoutManager(new LinearLayoutManager(view.getContext()));
        savedPostAdapter = new SavedPostAdapter(savedPostArrayList,view.getContext(),SavedPostFragment.this);
        binding.recyclerViewSavedPost.setAdapter(savedPostAdapter);

        getData();
    }

    private void backProfilePage(View view) {
        Navigation.findNavController(view).navigate(R.id.action_savedPostFragment_to_profilePageFragment);
    }

    private void getData(){
        arrayList = mainActivity.refDataAccess.getAllRefs();

        if(arrayList.isEmpty()){
            GetPostingModel post = new GetPostingModel();
            post.viewType = 2;
            savedPostArrayList.add(post);
            savedPostAdapter.notifyDataSetChanged();
        }else {
            for (RefItem item : arrayList) {
                String mail = item.getMail();
                String ref = item.getRef();

                firestore.collection("postMe").document(mail).collection(mail).document(ref).get().addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot.exists()){
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
                        String mail2 = documentSnapshot.getString("mail");
                        Timestamp timestamp = documentSnapshot.getTimestamp("timestamp");
                        DocumentReference reff = documentSnapshot.getReference();

                        GetPostingModel post = new GetPostingModel(1,imageUrl,name,startCity,startDistrict,endCity,endDistrict,loadType,loadAmount,date,time,number,mail2,timestamp,reff);
                        savedPostArrayList.add(post);
                        savedPostAdapter.notifyDataSetChanged();
                    }
                }).addOnFailureListener(e -> {
                    e.printStackTrace();
                });
            }
        }

    }

    public void removeSaved(View view, DocumentReference ref, int position){
        mainActivity.refDataAccess.deleteRef(ref.getId());
        savedPostArrayList.remove(position);
        savedPostAdapter.notifyItemRemoved(position);
        savedPostAdapter.notifyDataSetChanged();
        Toast.makeText(view.getContext(),"Kaydedilenlerden Silindi",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }
    }
}