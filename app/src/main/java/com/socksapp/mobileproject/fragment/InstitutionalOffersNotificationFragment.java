package com.socksapp.mobileproject.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.socksapp.mobileproject.R;
import com.socksapp.mobileproject.activity.MainActivity;
import com.socksapp.mobileproject.adapter.GetNotificationOffersAdapter;
import com.socksapp.mobileproject.databinding.FragmentInstitutionalOffersNotificationBinding;
import com.socksapp.mobileproject.model.GetNotificationOffersModel;
import java.util.ArrayList;

public class InstitutionalOffersNotificationFragment extends Fragment {

    private FragmentInstitutionalOffersNotificationBinding binding;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseUser user;
    public String userMail;
    public ArrayList<GetNotificationOffersModel> getNotificationOffersModelArrayList;
    public GetNotificationOffersAdapter getNotificationOffersAdapter;
    private MainActivity mainActivity;

    public InstitutionalOffersNotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        getNotificationOffersModelArrayList = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInstitutionalOffersNotificationBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.content.nameFragment.setText("Gelen Teklif Bildirimleri");
        binding.content.buttonDrawerToggle.setOnClickListener(this::backProfilePage);

        userMail = user.getEmail();

        binding.recyclerViewOffersNotification.setLayoutManager(new LinearLayoutManager(view.getContext()));
        getNotificationOffersAdapter = new GetNotificationOffersAdapter(getNotificationOffersModelArrayList,view.getContext(),InstitutionalOffersNotificationFragment.this);
        binding.recyclerViewOffersNotification.setAdapter(getNotificationOffersAdapter);

        getData();
    }

    private void backProfilePage(View view) {
        Bundle args = new Bundle();
        args.putString("type", "1");
        Navigation.findNavController(view).navigate(R.id.action_institutionalOffersNotificationFragment_to_profilePageFragment,args);
    }

    private void getData(){
        CollectionReference collection;
        collection = firestore.collection("notificationOffers").document(userMail).collection(userMail);

        collection.orderBy("timestamp", Query.Direction.DESCENDING).get().addOnSuccessListener(queryDocumentSnapshots -> {
            if(queryDocumentSnapshots.isEmpty()){
                GetNotificationOffersModel post = new GetNotificationOffersModel();
                post.viewType = 2;
                getNotificationOffersModelArrayList.add(post);
                getNotificationOffersAdapter.notifyDataSetChanged();
            }else {
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
                    String price = documentSnapshot.getString("price");
                    String result = documentSnapshot.getString("result");
                    String refId = documentSnapshot.getString("ref");
                    Timestamp timestamp = documentSnapshot.getTimestamp("timestamp");

                    GetNotificationOffersModel post = new GetNotificationOffersModel(1,imageUrl,name,number,mail,price,result,startCity,startDistrict,endCity,endDistrict,loadType,loadAmount,date,time,refId,timestamp);
                    getNotificationOffersModelArrayList.add(post);
                    getNotificationOffersAdapter.notifyDataSetChanged();
                }
            }
        }).addOnFailureListener(e -> {
            e.printStackTrace();
        });
    }

    public void deleteNotification(View view,String refId,int position){
        System.out.println("ref: "+refId);
        firestore.collection("notificationOffers").document(userMail).collection(userMail).document(refId).delete().addOnSuccessListener(unused -> {
            if (position != RecyclerView.NO_POSITION) {
                getNotificationOffersModelArrayList.remove(position);
                getNotificationOffersAdapter.notifyItemRemoved(position);
                getNotificationOffersAdapter.notifyDataSetChanged();
            }
            Toast.makeText(view.getContext(),"Bildirim Silindi",Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            e.printStackTrace();
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }
    }
}