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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.socksapp.mobileproject.R;
import com.socksapp.mobileproject.databinding.RecyclerViewEmptyMyPostBinding;
import com.socksapp.mobileproject.databinding.RecyclerViewEmptyPostBinding;
import com.socksapp.mobileproject.databinding.RecyclerViewPostBinding;
import com.socksapp.mobileproject.fragment.GetPostingJobFragment;
import com.socksapp.mobileproject.fragment.MyPostFragment;
import com.socksapp.mobileproject.model.GetPostingModel;
import com.google.firebase.Timestamp;
import java.util.ArrayList;

public class GetPostingAdapter extends RecyclerView.Adapter {

    public FirebaseAuth auth;
    public FirebaseUser user;
    public ArrayList<GetPostingModel> arrayList;
    public Context context;
    public Fragment fragment;

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
            if (arrayList.get(position).getViewType() == 2) {
                return LAYOUT_EMPTY;
            }
            if (arrayList.get(position).getViewType() == 3) {
                return 3;
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
            case 3:
                RecyclerViewEmptyMyPostBinding recyclerViewEmptyMyPostBinding = RecyclerViewEmptyMyPostBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
                return new GetMyPostingEmptyHolder(recyclerViewEmptyMyPostBinding);
            default:
                return null;

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        String imageUrl,userName,startCity,startDistrict,endCity,endDistrict,loadType,loadAmount,date,time,number,mail,userId,permission;
        Timestamp timestamp;
        DocumentReference ref;
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
                userId = arrayList.get(position).userId;
                timestamp = arrayList.get(position).timestamp;
                ref = arrayList.get(position).ref;
                permission = arrayList.get(position).permission;


                getShow(imageUrl,userName,startCity,startDistrict,endCity,endDistrict,loadType,loadAmount,date,time,number,mail,timestamp,getPostingHolder,permission);


                if (fragment instanceof GetPostingJobFragment){
                    if(GetPostingJobFragment.existsInstitutional.getString("exists","").isEmpty()){
                        getPostingHolder.recyclerViewPostBinding.verticalMenu.setVisibility(View.GONE);
                    }
                }

                getPostingHolder.recyclerViewPostBinding.verticalMenu.setOnClickListener(v ->{
                    if(fragment instanceof MyPostFragment){
                        MyPostFragment.dialogShow(v,ref,user.getEmail(),getPostingHolder.getAdapterPosition(),startCity);
                    }else if (fragment instanceof GetPostingJobFragment){
                        GetPostingJobFragment.dialogShow(v,userId,startCity,startDistrict,endCity,endDistrict,ref);
                    }
                });

                break;
            case LAYOUT_EMPTY:
                System.out.println();
                break;
            case 3:
                System.out.println();
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

    public static class GetMyPostingEmptyHolder extends RecyclerView.ViewHolder{
        RecyclerViewEmptyMyPostBinding recyclerViewEmptyMyPostBinding;
        public GetMyPostingEmptyHolder(RecyclerViewEmptyMyPostBinding recyclerViewEmptyMyPostBinding) {
            super(recyclerViewEmptyMyPostBinding.getRoot());
            this.recyclerViewEmptyMyPostBinding = recyclerViewEmptyMyPostBinding;
        }
    }


    private void getShow(String imageUrl,String userName,String startCity,String startDistrict,String endCity,String endDistrict,String loadType,String loadAmount,String date,String time,String number,String mail,Timestamp timestamp,GetPostingHolder holder,String permission){

        if(imageUrl.isEmpty()){
            ImageView imageView;
            imageView = holder.recyclerViewPostBinding.recyclerProfileImage;
            imageView.setImageResource(R.drawable.icon_person_white);
        }else {
            Glide.with(context)
                .load(imageUrl)
                .apply(new RequestOptions()
                .error(R.drawable.person_active_96)
                .centerCrop())
                .into(holder.recyclerViewPostBinding.recyclerProfileImage);
        }


        String startPoint = startCity + "/" + startDistrict;
        String endPoint = endCity + "/" + endDistrict;
        String point = startPoint + " -> " + endPoint;
        String loadInfo = loadAmount + ",  " + loadType;
        String plannedDate = time + ",  " + date;
        String phoneNumber = "+90 " + number;


        holder.recyclerViewPostBinding.recyclerUserId.setText(userName);
        holder.recyclerViewPostBinding.recyclerPoint.setText(point);
        holder.recyclerViewPostBinding.recyclerLoadInfo.setText(loadInfo);
        holder.recyclerViewPostBinding.recyclerPlannedDate.setText(plannedDate);

        if(permission.equals("0")){
            holder.recyclerViewPostBinding.recyclerNumber.setVisibility(View.GONE);
            holder.recyclerViewPostBinding.recyclerMail.setVisibility(View.GONE);
            holder.recyclerViewPostBinding.numberIcon.setVisibility(View.GONE);
            holder.recyclerViewPostBinding.mailIcon.setVisibility(View.GONE);
        }else {
            holder.recyclerViewPostBinding.recyclerNumber.setText(phoneNumber);
            holder.recyclerViewPostBinding.recyclerMail.setText(mail);
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

        holder.recyclerViewPostBinding.timestampTime.setText(elapsedTime);

    }
}
