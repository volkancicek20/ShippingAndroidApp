package com.socksapp.mobileproject.adapter;

import static com.socksapp.mobileproject.model.GetOffersModel.LAYOUT_ONE;
import static com.socksapp.mobileproject.model.GetOffersModel.LAYOUT_EMPTY;

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

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;
import com.socksapp.mobileproject.R;
import com.socksapp.mobileproject.databinding.RecyclerViewEmptyOfferBinding;
import com.socksapp.mobileproject.databinding.RecyclerViewEmptyPostBinding;
import com.socksapp.mobileproject.databinding.RecyclerViewOfferBinding;
import com.socksapp.mobileproject.databinding.RecyclerViewPostBinding;
import com.socksapp.mobileproject.model.GetOffersModel;
import com.socksapp.mobileproject.model.GetPostingModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GetOffersAdapter extends RecyclerView.Adapter{

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<GetOffersModel> arrayList;
    Context context;
    Fragment fragment;

    public GetOffersAdapter(ArrayList<GetOffersModel> arrayList,Context context,Fragment fragment){
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
                RecyclerViewOfferBinding recyclerViewOfferBinding = RecyclerViewOfferBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
                return new GetOffersHolder(recyclerViewOfferBinding);
            case LAYOUT_EMPTY:
                RecyclerViewEmptyOfferBinding recyclerViewEmptyOfferBinding = RecyclerViewEmptyOfferBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
                return new GetOffersEmptyHolder(recyclerViewEmptyOfferBinding);
            default:
                return null;

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        String imageUrl,userName,number,mail,price,startCity,startDistrict,endCity,endDistrict;
        Timestamp timestamp;
        switch (holder.getItemViewType()) {
            case LAYOUT_ONE:
                GetOffersHolder getOffersHolder = (GetOffersHolder) holder;

                imageUrl = arrayList.get(position).imageUrl;
                userName = arrayList.get(position).userName;
                number = arrayList.get(position).number;
                mail = arrayList.get(position).mail;
                price = arrayList.get(position).price;
                startCity = arrayList.get(position).startCity;
                startDistrict = arrayList.get(position).startDistrict;
                endCity = arrayList.get(position).endCity;
                endDistrict = arrayList.get(position).endDistrict;
                timestamp = arrayList.get(position).timestamp;

                getShow(imageUrl,userName,number,mail,price,startCity,startDistrict,endCity,endDistrict,timestamp,getOffersHolder);

                getOffersHolder.recyclerViewOfferBinding.offersRejectButton.setOnClickListener(v ->{

                });

                getOffersHolder.recyclerViewOfferBinding.offersApproveButton.setOnClickListener(v ->{

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

    public static class GetOffersHolder extends RecyclerView.ViewHolder{
        RecyclerViewOfferBinding recyclerViewOfferBinding;
        public GetOffersHolder(RecyclerViewOfferBinding recyclerViewOfferBinding) {
            super(recyclerViewOfferBinding.getRoot());
            this.recyclerViewOfferBinding = recyclerViewOfferBinding;
        }
    }

    public static class GetOffersEmptyHolder extends RecyclerView.ViewHolder{
        RecyclerViewEmptyOfferBinding recyclerViewEmptyOfferBinding;
        public GetOffersEmptyHolder(RecyclerViewEmptyOfferBinding recyclerViewEmptyOfferBinding) {
            super(recyclerViewEmptyOfferBinding.getRoot());
            this.recyclerViewEmptyOfferBinding = recyclerViewEmptyOfferBinding;
        }
    }


    private void getShow(String imageUrl, String userName, String number, String mail,String price,String startCity,String startDistrict,String endCity,String endDistrict, Timestamp timestamp, GetOffersHolder holder){

        if(imageUrl.isEmpty()){
            ImageView imageView;
            imageView = holder.recyclerViewOfferBinding.recyclerProfileImage;
            imageView.setImageResource(R.drawable.person_square);
        }else {
            Picasso.get().load(imageUrl).into(holder.recyclerViewOfferBinding.recyclerProfileImage);
        }

        String startPoint = startCity + "(" + startDistrict + ")";
        String endPoint = endCity + "(" + endDistrict + ")";
        String resultPoint = startPoint + "->" + endPoint;

        holder.recyclerViewOfferBinding.recyclerUserId.setText(userName);
        holder.recyclerViewOfferBinding.recyclerPrice.setText(price);
        holder.recyclerViewOfferBinding.text2.setText(resultPoint);

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

        holder.recyclerViewOfferBinding.timestampTime.setText(elapsedTime);

    }

}