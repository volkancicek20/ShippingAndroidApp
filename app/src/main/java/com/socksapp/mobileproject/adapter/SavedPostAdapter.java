package com.socksapp.mobileproject.adapter;

import static com.socksapp.mobileproject.model.GetPostingModel.LAYOUT_EMPTY;
import static com.socksapp.mobileproject.model.GetPostingModel.LAYOUT_ONE;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.socksapp.mobileproject.R;
import com.socksapp.mobileproject.databinding.RecyclerViewEmptyPostBinding;
import com.socksapp.mobileproject.databinding.RecyclerViewEmptySavedPostBinding;
import com.socksapp.mobileproject.databinding.RecyclerViewPostBinding;
import com.socksapp.mobileproject.databinding.RecyclerViewSavedPostBinding;
import com.socksapp.mobileproject.fragment.GetPostingJobFragment;
import com.socksapp.mobileproject.fragment.MyPostFragment;
import com.socksapp.mobileproject.fragment.SavedPostFragment;
import com.socksapp.mobileproject.model.GetPostingModel;

import java.util.ArrayList;

/**
 * Bu Adapter, Kurumsal hesapların kaydettiği ilanların gösterimini sağlayan recyclerview'ın adapteridir.
 */
public class SavedPostAdapter extends RecyclerView.Adapter {

    public ArrayList<GetPostingModel> arrayList;
    public Context context;
    public SavedPostFragment fragment;

    public SavedPostAdapter (ArrayList<GetPostingModel> arrayList,Context context,SavedPostFragment fragment){
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
            if (arrayList.get(position).getViewType() == 2) {
                return LAYOUT_EMPTY;
            }
            return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType){
            case LAYOUT_ONE:
                RecyclerViewSavedPostBinding recyclerViewSavedPostBinding = RecyclerViewSavedPostBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
                return new SavedPostHolder(recyclerViewSavedPostBinding);
            case LAYOUT_EMPTY:
                RecyclerViewEmptySavedPostBinding recyclerViewEmptySavedPostBinding = RecyclerViewEmptySavedPostBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
                return new SavedEmptyPostHolder(recyclerViewEmptySavedPostBinding);
            default:
                return null;

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        String imageUrl,userName,startCity,startDistrict,endCity,endDistrict,loadType,loadAmount,date,time,number,mail,userId,permission;
        Timestamp timestamp;
        DocumentReference ref;
        switch (holder.getItemViewType()) {
            case LAYOUT_ONE:

                SavedPostHolder savedPostHolder = (SavedPostHolder) holder;

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
                userId = arrayList.get(position).userId;
                timestamp = arrayList.get(position).timestamp;
                ref = arrayList.get(position).ref;
                permission = arrayList.get(position).permission;

                getShow(imageUrl,userName,startCity,startDistrict,endCity,endDistrict,loadType,loadAmount,date,time,number,mail,timestamp,savedPostHolder,permission);

                savedPostHolder.recyclerViewSavedPostBinding.verticalMenu.setOnClickListener(v ->{
                    fragment.removeSaved(v,ref,savedPostHolder.getAdapterPosition());
                });

                break;
            case LAYOUT_EMPTY:
                System.out.println();
                break;
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class SavedPostHolder extends RecyclerView.ViewHolder{
        RecyclerViewSavedPostBinding recyclerViewSavedPostBinding;
        public SavedPostHolder(RecyclerViewSavedPostBinding recyclerViewSavedPostBinding) {
            super(recyclerViewSavedPostBinding.getRoot());
            this.recyclerViewSavedPostBinding = recyclerViewSavedPostBinding;
        }
    }

    public static class SavedEmptyPostHolder extends RecyclerView.ViewHolder{
        RecyclerViewEmptySavedPostBinding recyclerViewEmptySavedPostBinding;
        public SavedEmptyPostHolder(RecyclerViewEmptySavedPostBinding recyclerViewEmptySavedPostBinding) {
            super(recyclerViewEmptySavedPostBinding.getRoot());
            this.recyclerViewEmptySavedPostBinding = recyclerViewEmptySavedPostBinding;
        }
    }

    /**
     * Recyclerview da gösterilecek veriler alınır ve gösterilir
     */
    private void getShow(String imageUrl, String userName, String startCity, String startDistrict, String endCity, String endDistrict, String loadType, String loadAmount, String date, String time, String number, String mail, Timestamp timestamp, SavedPostHolder holder,String permission){
        if(imageUrl.isEmpty()){
            ImageView imageView;
            imageView = holder.recyclerViewSavedPostBinding.recyclerProfileImage;
            imageView.setImageResource(R.drawable.icon_person_white);
        }else {
            Glide.with(context)
                .load(imageUrl)
                .apply(new RequestOptions()
                .error(R.drawable.person_active_96)
                .centerCrop())
                .into(holder.recyclerViewSavedPostBinding.recyclerProfileImage);
        }

        String startPoint = startCity + "/" + startDistrict;
        String endPoint = endCity + "/" + endDistrict;
        String point = startPoint + " -> " + endPoint;
        String loadInfo = loadAmount + ",  " + loadType;
        String plannedDate = time + ",  " + date;
        String phoneNumber = "+90 " + number;


        holder.recyclerViewSavedPostBinding.recyclerUserId.setText(userName);
        holder.recyclerViewSavedPostBinding.recyclerPoint.setText(point);
        holder.recyclerViewSavedPostBinding.recyclerLoadInfo.setText(loadInfo);
        holder.recyclerViewSavedPostBinding.recyclerPlannedDate.setText(plannedDate);

        if(permission.equals("1")){
            holder.recyclerViewSavedPostBinding.recyclerNumber.setText(phoneNumber);
            holder.recyclerViewSavedPostBinding.recyclerMail.setText(mail);
        }else {
            holder.recyclerViewSavedPostBinding.recyclerNumber.setVisibility(View.GONE);
            holder.recyclerViewSavedPostBinding.recyclerMail.setVisibility(View.GONE);
            holder.recyclerViewSavedPostBinding.numberIcon.setVisibility(View.GONE);
            holder.recyclerViewSavedPostBinding.mailIcon.setVisibility(View.GONE);
        }


        long secondsElapsed = (Timestamp.now().getSeconds() - timestamp.getSeconds());
        String elapsedTime;

        if(secondsElapsed < 0){
            elapsedTime = "şimdi";
        } else if (secondsElapsed >= 31536000) {
            elapsedTime = "• " + (secondsElapsed / 31536000) + " yıl önce";
        } else if (secondsElapsed >= 2592000) {
            elapsedTime = "• " + (secondsElapsed / 2592000) + " ay önce";
        } else if (secondsElapsed >= 86400) {
            elapsedTime = "• " + (secondsElapsed / 86400) + " gün önce";
        } else if (secondsElapsed >= 3600) {
            elapsedTime = "• " + (secondsElapsed / 3600) + " saat önce";
        } else if (secondsElapsed >= 60) {
            elapsedTime = "• " + (secondsElapsed / 60) + " dakika önce";
        } else {
            elapsedTime = "• " + secondsElapsed + " saniye önce";
        }

        holder.recyclerViewSavedPostBinding.timestampTime.setText(elapsedTime);

    }
}
