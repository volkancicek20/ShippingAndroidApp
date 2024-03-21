package com.socksapp.mobileproject.adapter;

import static com.socksapp.mobileproject.model.GetPostingModel.LAYOUT_EMPTY;
import static com.socksapp.mobileproject.model.GetPostingModel.LAYOUT_ONE;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.socksapp.mobileproject.R;
import com.socksapp.mobileproject.databinding.RecyclerViewEmptyPostBinding;
import com.socksapp.mobileproject.databinding.RecyclerViewPostBinding;
import com.socksapp.mobileproject.model.GetPostingModel;

import com.google.firebase.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GetPostingAdapter extends RecyclerView.Adapter {


    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<GetPostingModel> arrayList;
    Context context;
    Fragment fragment;

    public GetPostingAdapter(ArrayList<GetPostingModel> arrayList,Context context,Fragment fragment){
        this.arrayList = arrayList;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public int getItemViewType(int position) {
        if(arrayList.isEmpty()){
            return 2;
        }else {
            if (arrayList.get(position).getViewType() == 1) {
                return LAYOUT_ONE;
            }
            return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case LAYOUT_ONE:
                RecyclerViewPostBinding recyclerViewPostBinding = RecyclerViewPostBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
                return new GetPostingHolder(recyclerViewPostBinding);
            case LAYOUT_EMPTY:
                RecyclerViewEmptyPostBinding recyclerViewEmptyPostBinding = RecyclerViewEmptyPostBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
                return new GetPostingEmptyHolder(recyclerViewEmptyPostBinding);
            default:
                return null;

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        String imageUrl,userName,startCity,startDistrict,endCity,endDistrict,loadType,loadAmount,date,time,number,mail;
        Timestamp timestamp;
        switch (holder.getItemViewType()) {
            case LAYOUT_ONE:
                GetPostingHolder getPostingHolder = (GetPostingHolder) holder;

                imageUrl = arrayList.get(position).imageUrl;
                userName = arrayList.get(position).userName;
                startCity = arrayList.get(position).startCity;
                startDistrict = arrayList.get(position).startDistrict;
                endCity = arrayList.get(position).endCity;
                endDistrict = arrayList.get(position).endDistrict;
                loadType = arrayList.get(position).loadType;
                loadAmount = arrayList.get(position).loadAmount;
                date = arrayList.get(position).date;
                time = arrayList.get(position).time;
                number = arrayList.get(position).number;
                mail = arrayList.get(position).mail;
                timestamp = arrayList.get(position).timestamp;

                getShow(imageUrl,userName,startCity,startDistrict,endCity,endDistrict,loadType,loadAmount,date,time,number,mail,timestamp,getPostingHolder);

                getPostingHolder.recyclerViewPostBinding.offersButton.setOnClickListener(v ->{
                    getOffers(mail,startCity,startDistrict,endCity,endDistrict);
                });

                break;
            case LAYOUT_EMPTY:
                System.out.println("BB");
                break;
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class GetPostingHolder extends RecyclerView.ViewHolder{
        RecyclerViewPostBinding recyclerViewPostBinding;
        public GetPostingHolder(RecyclerViewPostBinding recyclerViewPostBinding) {
            super(recyclerViewPostBinding.getRoot());
            this.recyclerViewPostBinding = recyclerViewPostBinding;
        }
    }

    public static class GetPostingEmptyHolder extends RecyclerView.ViewHolder{
        RecyclerViewEmptyPostBinding recyclerViewEmptyPostBinding;
        public GetPostingEmptyHolder(RecyclerViewEmptyPostBinding recyclerViewEmptyPostBinding) {
            super(recyclerViewEmptyPostBinding.getRoot());
            this.recyclerViewEmptyPostBinding = recyclerViewEmptyPostBinding;
        }
    }


    private void getShow(String imageUrl,String userName,String startCity,String startDistrict,String endCity,String endDistrict,String loadType,String loadAmount,String date,String time,String number,String mail,Timestamp timestamp,GetPostingHolder holder){

        if(imageUrl.isEmpty()){
            ImageView imageView;
            imageView = holder.recyclerViewPostBinding.recyclerProfileImage;
            imageView.setImageResource(R.drawable.icon_person);
        }

        String startPoint = startCity + "/" + startDistrict;
        String endPoint = endCity + "/" + endDistrict;
        String loadInfo = loadAmount + ",  " + loadType;
        String plannedDate = time + ",  " + date;
        String phoneNumber = "+90 " + number;


        holder.recyclerViewPostBinding.recyclerUserId.setText(userName);
        holder.recyclerViewPostBinding.recyclerStartingPoint.setText(startPoint);
        holder.recyclerViewPostBinding.recyclerEndingPoint.setText(endPoint);
        holder.recyclerViewPostBinding.recyclerLoadInfo.setText(loadInfo);
        holder.recyclerViewPostBinding.recyclerPlannedDate.setText(plannedDate);
        holder.recyclerViewPostBinding.recyclerNumber.setText(phoneNumber);
        holder.recyclerViewPostBinding.recyclerMail.setText(mail);

        long secondsElapsed = (Timestamp.now().getSeconds() - timestamp.getSeconds());
        String elapsedTime;

        if(secondsElapsed < 0){
            elapsedTime = "şimdi";
        } else if (secondsElapsed >= 31536000) {
            elapsedTime = "" + (secondsElapsed / 31536000) + " yıl önce";
        } else if (secondsElapsed >= 2592000) {
            elapsedTime = "" + (secondsElapsed / 2592000) + " ay önce";
        } else if (secondsElapsed >= 86400) {
            elapsedTime = "" + (secondsElapsed / 86400) + " gün önce";
        } else if (secondsElapsed >= 3600) {
            elapsedTime = "" + (secondsElapsed / 3600) + " saat önce";
        } else if (secondsElapsed >= 60) {
            elapsedTime = "" + (secondsElapsed / 60) + " dakika önce";
        } else {
            elapsedTime = "" + secondsElapsed + " saniye önce";
        }

        holder.recyclerViewPostBinding.timestampTime.setText(elapsedTime);

    }


    private void getOffers(String offersMail,String startCity,String startDistrict,String endCity,String endDistrict){
        View view = LayoutInflater.from(context).inflate(R.layout.offers_layout, null);

        EditText editText = view.findViewById(R.id.price_edittext);
        Button okButton = view.findViewById(R.id.getOffersButton);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        okButton.setOnClickListener(v -> {
            String price = editText.getText().toString();
            getInstitutionalData(user.getEmail(),offersMail,price,startCity,startDistrict,endCity,endDistrict,alertDialog);
        });
    }

    private void getInstitutionalData(String userMail,String offersMail,String price,String startCity,String startDistrict,String endCity,String endDistrict,AlertDialog alertDialog){
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Teklifiniz ekleniyor..");
        progressDialog.show();
        firebaseFirestore.collection("usersInstitutional").document(userMail).get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()){
                Map<String, Object> userData = documentSnapshot.getData();

                if(userData != null) {
                    String name = (String) userData.get("name");
                    String mail = (String) userData.get("mail");
                    String number = (String) userData.get("number");
                    String imageUrl = (String) userData.get("imageUrl");

                    ArrayList<String> citiesData = (ArrayList<String>) userData.get("cities");

                    if(name != null && !name.isEmpty() && mail != null && !mail.isEmpty() && number != null && !number.isEmpty() && imageUrl != null && citiesData != null && !citiesData.isEmpty()){

                        WriteBatch batch = firebaseFirestore.batch();

                        Map<String, Object> data = new HashMap<>();
                        data.put("price",price);
                        data.put("personalMail",offersMail);
                        data.put("institutionalMail",mail);
                        data.put("institutionalName",name);
                        data.put("institutionalNumber",number);
                        if(imageUrl.isEmpty()){
                            data.put("institutionalImageUrl","");
                        }else {
                            data.put("institutionalImageUrl",imageUrl);
                        }
                        data.put("startCity",startCity);
                        data.put("startDistrict",startDistrict);
                        data.put("endCity",endCity);
                        data.put("endDistrict",endDistrict);
                        data.put("timestamp",new Date());

                        CollectionReference collectionReference = firebaseFirestore.collection("offers").document(offersMail).collection(offersMail);
                        DocumentReference offerDocRef = collectionReference.document();
                        batch.set(offerDocRef, data, SetOptions.merge());

                        Map<String, Object> citiesMap = new HashMap<>();
                        citiesMap.put("cities", new ArrayList<>(citiesData));

                        batch.set(offerDocRef, citiesMap, SetOptions.merge());

                        batch.commit().addOnSuccessListener(unused -> {
                            Toast.makeText(context,"Teklifiniz eklendi",Toast.LENGTH_LONG).show();
                            alertDialog.dismiss();
                            progressDialog.dismiss();

                        }).addOnFailureListener(e -> {

                            alertDialog.dismiss();
                            progressDialog.dismiss();
                        });

                    }else {
                        // kurumsal profili tam tamamlamamış
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

}
