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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.socksapp.mobileproject.activity.MainActivity;
import com.socksapp.mobileproject.adapter.GetPostingAdapter;
import com.socksapp.mobileproject.databinding.FragmentGetPostingJobBinding;
import com.socksapp.mobileproject.model.GetPostingModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GetPostingJobFragment extends Fragment {

    private FragmentGetPostingJobBinding binding;
    private static FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String[] cityNames;
    public static String userMail;
    private ArrayAdapter<String> cityAdapter;
    private AutoCompleteTextView cityCompleteTextView;

    public ArrayList<GetPostingModel> getPostingModelArrayList;
    public GetPostingAdapter getPostingAdapter;
    private static MainActivity mainActivity;

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

        userMail = user.getEmail();

        binding.content.buttonDrawerToggle.setOnClickListener(this::goMainFragment);

        cityNames = getResources().getStringArray(R.array.city_names);
        cityAdapter = new ArrayAdapter<>(requireContext(), R.layout.list_item,cityNames);
        cityCompleteTextView = binding.getRoot().findViewById(R.id.city_complete_text);
        cityCompleteTextView.setAdapter(cityAdapter);

        binding.recyclerViewPost.setLayoutManager(new LinearLayoutManager(view.getContext()));
        getPostingAdapter = new GetPostingAdapter(getPostingModelArrayList,view.getContext(),GetPostingJobFragment.this);
        binding.recyclerViewPost.setAdapter(getPostingAdapter);

        getPost(view,"");

        binding.content.findPost.setOnClickListener(v -> {
            getPostingModelArrayList.clear();
            getPost(v,binding.content.cityCompleteText.getText().toString());
        });
    }

    private void goMainFragment(View v){
        Navigation.findNavController(v).navigate(R.id.action_getPostingJobFragment_to_mainFragment);
    }


    private void getPost(View view,String city){
        CollectionReference collection;
        if(city.isEmpty()){
            collection = firestore.collection("postİstanbul");
        }else {
            collection = firestore.collection("post"+city);
        }

        collection.orderBy("timestamp", Query.Direction.DESCENDING).get().addOnSuccessListener(queryDocumentSnapshots -> {
            if(queryDocumentSnapshots.isEmpty()){
                GetPostingModel post = new GetPostingModel();
                post.viewType = 2;
                getPostingModelArrayList.add(post);
                getPostingAdapter.notifyDataSetChanged();
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
                    Timestamp timestamp = documentSnapshot.getTimestamp("timestamp");
                    DocumentReference ref = documentSnapshot.getReference();

                    GetPostingModel post = new GetPostingModel(1,imageUrl,name,startCity,startDistrict,endCity,endDistrict,loadType,loadAmount,date,time,number,mail,timestamp,userMail,ref);
                    getPostingModelArrayList.add(post);
                    getPostingAdapter.notifyDataSetChanged();
                }
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(view.getContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        });
    }

    public static void dialogShow(View view, String myMail, String startCity, String startDistrict, String endCity, String endDistrict,DocumentReference ref){
        final Dialog dialog = new Dialog(view.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout);

        LinearLayout offers = dialog.findViewById(R.id.layoutOffer);
        LinearLayout save = dialog.findViewById(R.id.layoutSave);
        LinearLayout delete = dialog.findViewById(R.id.layoutDelete);
        delete.setVisibility(View.GONE);


        save.setOnClickListener(v -> {
            dialog.dismiss();
            mainActivity.refDataAccess.insertRef(ref.getId(),myMail);
            Toast.makeText(view.getContext(), "Kaydedildi", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        offers.setOnClickListener(v -> {
            dialog.dismiss();
            getOffers(view,myMail,startCity,startDistrict,endCity,endDistrict);
        });


        if(dialog.getWindow() != null){
            dialog.show();
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setGravity(Gravity.BOTTOM);
        }else {
            Toast.makeText(view.getContext(),"Dialog null",Toast.LENGTH_SHORT).show();
        }
    }

    public static void getOffers(View v, String offersMail,String startCity,String startDistrict,String endCity,String endDistrict){
        View view = LayoutInflater.from(v.getContext()).inflate(R.layout.offers_layout, null);

        EditText editText = view.findViewById(R.id.price_edittext);
        Button okButton = view.findViewById(R.id.getOffersButton);

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        okButton.setOnClickListener(vv -> {
            String price = editText.getText().toString();
            getInstitutionalData(v,userMail,offersMail,price,startCity,startDistrict,endCity,endDistrict,alertDialog);
        });
    }

    public static void getInstitutionalData(View view,String userMail,String offersMail,String price,String startCity,String startDistrict,String endCity,String endDistrict,AlertDialog alertDialog){
        ProgressDialog progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("Teklifiniz ekleniyor..");
        progressDialog.show();
        firestore.collection("usersInstitutional").document(userMail).get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()){
                Map<String, Object> userData = documentSnapshot.getData();

                if(userData != null) {
                    String name = (String) userData.get("name");
                    String mail = (String) userData.get("mail");
                    String number = (String) userData.get("number");
                    String imageUrl = (String) userData.get("imageUrl");

                    ArrayList<String> citiesData = (ArrayList<String>) userData.get("cities");

                    if(name != null && mail != null && number != null && imageUrl != null && citiesData != null){

                        if(!name.isEmpty() && !mail.isEmpty() && !number.isEmpty() && !citiesData.isEmpty()){

                            WriteBatch batch = firestore.batch();

                            Map<String, Object> data = new HashMap<>();
                            data.put("price",price);
                            data.put("personalMail",userMail);
                            data.put("institutionalMail",mail);
                            data.put("institutionalName",name);
                            data.put("institutionalNumber",number);
                            data.put("institutionalImageUrl",imageUrl);
                            data.put("startCity",startCity);
                            data.put("startDistrict",startDistrict);
                            data.put("endCity",endCity);
                            data.put("endDistrict",endDistrict);
                            data.put("timestamp",new Date());

                            CollectionReference collectionReference = firestore.collection("offers").document(offersMail).collection(offersMail);
                            DocumentReference offerDocRef = collectionReference.document();
                            batch.set(offerDocRef, data, SetOptions.merge());

                            Map<String, Object> citiesMap = new HashMap<>();
                            citiesMap.put("cities", new ArrayList<>(citiesData));

                            batch.set(offerDocRef, citiesMap, SetOptions.merge());

                            batch.commit().addOnSuccessListener(unused -> {
                                Toast.makeText(view.getContext(),"Teklifiniz eklendi",Toast.LENGTH_LONG).show();
                                alertDialog.dismiss();
                                progressDialog.dismiss();

                            }).addOnFailureListener(e -> {

                                alertDialog.dismiss();
                                progressDialog.dismiss();
                            });
                        }else {
                            Toast.makeText(view.getContext(),"Kurumsal proilinizde eksik bilgiler içermektedir",Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(view.getContext(),"Kurumsal Profil oluşturulmamış ya da eksik bilgiler içermektedir",Toast.LENGTH_LONG).show();
                        alertDialog.dismiss();
                        progressDialog.dismiss();
                    }
                }

            }else {
                // kurumsal hesabı bulunmuyor
                alertDialog.dismiss();
                progressDialog.dismiss();
            }
        }).addOnFailureListener(e -> {
            alertDialog.dismiss();
            progressDialog.dismiss();
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