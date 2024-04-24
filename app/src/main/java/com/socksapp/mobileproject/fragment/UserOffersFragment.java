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
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.google.firebase.firestore.WriteBatch;
import com.socksapp.mobileproject.R;
import com.socksapp.mobileproject.activity.MainActivity;
import com.socksapp.mobileproject.adapter.GetOffersAdapter;
import com.socksapp.mobileproject.databinding.FragmentUserOffersBinding;
import com.socksapp.mobileproject.model.GetOffersModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


/**
 * Bu Fragment, Kurumsal hesapların ilanlara teklif sunduğu zaman kullanıcının bu teklifleri gördüğü ve kabul veya reddeceği fragment'dır.
 */
public class UserOffersFragment extends Fragment {

    private FragmentUserOffersBinding binding;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String userMail;
    public ArrayList<GetOffersModel> getOffersModelArrayList;
    public GetOffersAdapter getOffersAdapter;
    private MainActivity mainActivity;
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

        userMail = user.getEmail();

        getOffersModelArrayList.clear();

        binding.content.nameFragment.setText("Gelen Teklifler");
        binding.content.buttonDrawerToggle.setOnClickListener(this::backProfilePage);

        binding.recyclerViewOffers.setLayoutManager(new LinearLayoutManager(view.getContext()));
        getOffersAdapter = new GetOffersAdapter(getOffersModelArrayList,view.getContext(),UserOffersFragment.this);
        binding.recyclerViewOffers.setAdapter(getOffersAdapter);

        getOffersData(view);
    }

    private void backProfilePage(View view) {
        Navigation.findNavController(view).navigate(R.id.action_userOffersFragment_to_profilePageFragment);
    }

    private void getOffersData(View view){
        CollectionReference collection = firestore.collection("offers").document(userMail).collection(userMail);

        collection.orderBy("timestamp", Query.Direction.DESCENDING).get().addOnSuccessListener(queryDocumentSnapshots -> {
            if(queryDocumentSnapshots.isEmpty()){
                GetOffersModel offers = new GetOffersModel();
                offers.viewType = 2;
                getOffersModelArrayList.add(offers);
                getOffersAdapter.notifyDataSetChanged();
            }
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                DocumentReference ref = documentSnapshot.getReference();
                String name = documentSnapshot.getString("institutionalName");
                String imageUrl = documentSnapshot.getString("institutionalImageUrl");
                String number = documentSnapshot.getString("institutionalNumber");
                String mail = documentSnapshot.getString("institutionalMail");
                String personalMail = documentSnapshot.getString("personalMail");
                String price = documentSnapshot.getString("price");
                String startCity = documentSnapshot.getString("startCity");
                String startDistrict = documentSnapshot.getString("startDistrict");
                String endCity = documentSnapshot.getString("endCity");
                String endDistrict = documentSnapshot.getString("endDistrict");
                String offersRef = documentSnapshot.getString("ref");
                String userId = documentSnapshot.getString("userId");
                Timestamp timestamp = documentSnapshot.getTimestamp("timestamp");

                GetOffersModel offers = new GetOffersModel(1,imageUrl,name,number,mail,personalMail,price,startCity,startDistrict,endCity,endDistrict,timestamp,ref,offersRef,userId);
                getOffersModelArrayList.add(offers);
                getOffersAdapter.notifyDataSetChanged();

            }
        }).addOnFailureListener(e -> {
            Toast.makeText(view.getContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        });
    }

    public void dialogShow(View view, String imageUrl,String name,String number,String mail,String personalMail,DocumentReference ref,int position,String offersRef,String startCity,String userId,String price){
        final Dialog dialog = new Dialog(view.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout_offers);

        LinearLayout info = dialog.findViewById(R.id.layoutInfo);
        LinearLayout approve = dialog.findViewById(R.id.layoutApprove);
        LinearLayout reject = dialog.findViewById(R.id.layoutReject);


        info.setOnClickListener(v -> {
            dialog.dismiss();
            Bundle args = new Bundle();
            args.putString("mail", personalMail);
            args.putString("name", name);

            Navigation.findNavController(view).navigate(R.id.action_userOffersFragment_to_infoInstitutionalFragment, args);
        });

        approve.setOnClickListener(v -> {
            approveOffers(v,offersRef,startCity,personalMail,userId,price,dialog,position,ref);
        });

        reject.setOnClickListener(v -> {
            rejectOffers(v,offersRef,startCity,personalMail,userId,price,dialog,position,ref);
        });


        if(dialog.getWindow() != null){
            dialog.show();
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setGravity(Gravity.BOTTOM);
        }
    }

    public void approveOffers(View view,String offersRef,String startCity,String personalMail,String userId,String price,Dialog dialogX,int position,DocumentReference ref){
        ProgressDialog progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("Teklif Kabul Ediliyor..");
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setMessage("Teklifi kabul etmek istiyor musunuz?");
        builder.setPositiveButton("KABUL ET", (dialog, which) ->{
            progressDialog.show();
            String result = "approve";
            String collect = "post"+startCity;
            firestore.collection(collect).document(offersRef).get().addOnSuccessListener(documentSnapshot -> {
                if(documentSnapshot.exists()){
                    String nameX = documentSnapshot.getString("name");
                    String imageUrlX = documentSnapshot.getString("imageUrl");
                    String startCityX = documentSnapshot.getString("startCity");
                    String startDistrictX = documentSnapshot.getString("startDistrict");
                    String endCityX = documentSnapshot.getString("endCity");
                    String endDistrictX = documentSnapshot.getString("endDistrict");
                    String loadTypeX = documentSnapshot.getString("loadType");
                    String loadAmountX = documentSnapshot.getString("loadAmount");
                    String dateX = documentSnapshot.getString("date");
                    String timeX = documentSnapshot.getString("time");
                    String numberX = documentSnapshot.getString("number");
                    String mailX = documentSnapshot.getString("mail");
                    String userIdX = documentSnapshot.getString("userId");
                    DocumentReference refX = documentSnapshot.getReference();

                    HashMap<String,Object> data = new HashMap<>();
                    data.put("name",nameX);
                    data.put("imageUrl",imageUrlX);
                    data.put("startCity",startCityX);
                    data.put("startDistrict",startDistrictX);
                    data.put("endCity",endCityX);
                    data.put("endDistrict",endDistrictX);
                    data.put("loadType",loadTypeX);
                    data.put("loadAmount",loadAmountX);
                    data.put("price",price);
                    data.put("date",dateX);
                    data.put("time",timeX);
                    data.put("number",numberX);
                    data.put("mail",mailX);
                    data.put("result",result);
                    data.put("timestamp",new Date());


                    WriteBatch batch = firestore.batch();

                    CollectionReference collectionReference = firestore.collection("notificationOffers").document(personalMail).collection(personalMail);
                    DocumentReference documentReference = collectionReference.document();
                    data.put("ref",documentReference.getId());
                    batch.set(documentReference, data);

                    CollectionReference collectionReference2 = firestore.collection("offers").document(userId).collection(userId);
                    String deleteRef = ref.getId();
                    DocumentReference documentReference2 = collectionReference2.document(deleteRef);

                    batch.delete(documentReference2);

                    batch.commit()
                        .addOnSuccessListener(unused -> {
                            if (position != RecyclerView.NO_POSITION) {
                                getOffersModelArrayList.remove(position);
                                getOffersAdapter.notifyItemRemoved(position);
                                getOffersAdapter.notifyDataSetChanged();
                            }
                            progressDialog.dismiss();
                            dialogX.dismiss();
                            dialog.dismiss();
                            Toast.makeText(view.getContext(), "Teklif onayınız karşı tarafa gönderilmiştir.", Toast.LENGTH_LONG).show();
                        })
                        .addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            dialogX.dismiss();
                            dialog.dismiss();
                            e.printStackTrace();
                        });

                }else {
                    progressDialog.dismiss();
                    dialogX.dismiss();
                    dialog.dismiss();
                }
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                dialogX.dismiss();
                dialog.dismiss();
            });

        });

        builder.setNegativeButton("Hayır", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    public void rejectOffers(View view,String offersRef,String startCity,String personalMail,String userId,String price,Dialog dialogX,int position,DocumentReference ref){
        ProgressDialog progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("Teklif Reddediliyor..");
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setMessage("Teklifi reddetmek istiyor musunuz?");
        builder.setPositiveButton("REDDET", (dialog, which) ->{
            progressDialog.show();
            String result = "reject";
            String collect = "post"+startCity;
            firestore.collection(collect).document(offersRef).get().addOnSuccessListener(documentSnapshot -> {
                if(documentSnapshot.exists()){
                    String nameX = documentSnapshot.getString("name");
                    String imageUrlX = documentSnapshot.getString("imageUrl");
                    String startCityX = documentSnapshot.getString("startCity");
                    String startDistrictX = documentSnapshot.getString("startDistrict");
                    String endCityX = documentSnapshot.getString("endCity");
                    String endDistrictX = documentSnapshot.getString("endDistrict");
                    String loadTypeX = documentSnapshot.getString("loadType");
                    String loadAmountX = documentSnapshot.getString("loadAmount");
                    String dateX = documentSnapshot.getString("date");
                    String timeX = documentSnapshot.getString("time");
                    String numberX = documentSnapshot.getString("number");
                    String mailX = documentSnapshot.getString("mail");
                    String userIdX = documentSnapshot.getString("userId");
                    DocumentReference refX = documentSnapshot.getReference();

                    HashMap<String,Object> data = new HashMap<>();
                    data.put("name",nameX);
                    data.put("imageUrl",imageUrlX);
                    data.put("startCity",startCityX);
                    data.put("startDistrict",startDistrictX);
                    data.put("endCity",endCityX);
                    data.put("endDistrict",endDistrictX);
                    data.put("loadType",loadTypeX);
                    data.put("loadAmount",loadAmountX);
                    data.put("price",price);
                    data.put("date",dateX);
                    data.put("time",timeX);
                    data.put("number",numberX);
                    data.put("mail",mailX);
                    data.put("result",result);
                    data.put("timestamp",new Date());

                    WriteBatch batch = firestore.batch();

                    CollectionReference collectionReference = firestore.collection("notificationOffers").document(personalMail).collection(personalMail);
                    DocumentReference documentReference = collectionReference.document();
                    data.put("ref",documentReference.getId());
                    batch.set(documentReference, data);

                    CollectionReference collectionReference2 = firestore.collection("offers").document(userId).collection(userId);
                    String deleteRef = ref.getId();
                    DocumentReference documentReference2 = collectionReference2.document(deleteRef);

                    batch.delete(documentReference2);

                    batch.commit()
                        .addOnSuccessListener(unused -> {
                            if (position != RecyclerView.NO_POSITION) {
                                getOffersModelArrayList.remove(position);
                                getOffersAdapter.notifyItemRemoved(position);
                                getOffersAdapter.notifyDataSetChanged();
                            }
                            progressDialog.dismiss();
                            dialogX.dismiss();
                            dialog.dismiss();
                            Toast.makeText(view.getContext(), "Teklif reddedildi", Toast.LENGTH_LONG).show();
                        })
                        .addOnFailureListener(e -> {
                            progressDialog.dismiss();
                            dialogX.dismiss();
                            dialog.dismiss();
                            e.printStackTrace();
                        });

                }else {
                    progressDialog.dismiss();
                    dialogX.dismiss();
                    dialog.dismiss();
                }
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                dialogX.dismiss();
                dialog.dismiss();
            });

        });

        builder.setNegativeButton("Hayır", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }
    }
}