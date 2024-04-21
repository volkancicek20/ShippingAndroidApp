package com.socksapp.mobileproject.adapter;

import static com.socksapp.mobileproject.model.GetInstitutionalModel.LAYOUT_EMPTY;
import static com.socksapp.mobileproject.model.GetInstitutionalModel.LAYOUT_ONE;

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
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.socksapp.mobileproject.R;
import com.socksapp.mobileproject.databinding.RecyclerViewEmptyFindBinding;
import com.socksapp.mobileproject.databinding.RecyclerViewEmptyOfferBinding;
import com.socksapp.mobileproject.databinding.RecyclerViewFindBinding;
import com.socksapp.mobileproject.databinding.RecyclerViewOfferBinding;
import com.socksapp.mobileproject.fragment.InstitutionalFragment;
import com.socksapp.mobileproject.fragment.SearchingFragment;
import com.socksapp.mobileproject.model.GetInstitutionalModel;
import com.socksapp.mobileproject.model.GetOffersModel;

import java.util.ArrayList;

public class GetInstitutionalAdapter extends RecyclerView.Adapter{

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<GetInstitutionalModel> arrayList;
    Context context;
    SearchingFragment fragment;

    public GetInstitutionalAdapter(ArrayList<GetInstitutionalModel> arrayList,Context context,SearchingFragment fragment){
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
                RecyclerViewFindBinding recyclerViewFindBinding = RecyclerViewFindBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
                return new GetInstitutionalHolder(recyclerViewFindBinding);
            case LAYOUT_EMPTY:
                RecyclerViewEmptyFindBinding recyclerViewEmptyFindBinding = RecyclerViewEmptyFindBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
                return new GetInstitutionalEmptyHolder(recyclerViewEmptyFindBinding);
            default:
                return null;

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        String imageUrl,name,number,mail;
        ArrayList<String> cities;
        switch (holder.getItemViewType()) {
            case LAYOUT_ONE:
                GetInstitutionalHolder getInstitutionalHolder = (GetInstitutionalHolder) holder;

                imageUrl = arrayList.get(position).imageUrl;
                name = arrayList.get(position).name;
                number = arrayList.get(position).number;
                mail = arrayList.get(position).mail;
                cities = arrayList.get(position).cities;

                getShow(imageUrl,name,number,mail,cities,getInstitutionalHolder);

                getInstitutionalHolder.recyclerViewFindBinding.cardView.setOnClickListener(v ->{
                    if(getInstitutionalHolder.recyclerViewFindBinding.chipGroupCities.getVisibility() == View.GONE){
                        getInstitutionalHolder.recyclerViewFindBinding.chipGroupCities.setVisibility(View.VISIBLE);
                    }else {
                        getInstitutionalHolder.recyclerViewFindBinding.chipGroupCities.setVisibility(View.GONE);
                    }
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

    public static class GetInstitutionalHolder extends RecyclerView.ViewHolder{
        RecyclerViewFindBinding recyclerViewFindBinding;
        public GetInstitutionalHolder(RecyclerViewFindBinding recyclerViewFindBinding) {
            super(recyclerViewFindBinding.getRoot());
            this.recyclerViewFindBinding = recyclerViewFindBinding;
        }
    }

    public static class GetInstitutionalEmptyHolder extends RecyclerView.ViewHolder{
        RecyclerViewEmptyFindBinding recyclerViewEmptyFindBinding;
        public GetInstitutionalEmptyHolder(RecyclerViewEmptyFindBinding recyclerViewEmptyFindBinding) {
            super(recyclerViewEmptyFindBinding.getRoot());
            this.recyclerViewEmptyFindBinding = recyclerViewEmptyFindBinding;
        }
    }

    private void getShow(String imageUrl, String name, String number, String mail,ArrayList<String> cities, GetInstitutionalHolder holder){

        if(imageUrl.isEmpty()){
            ImageView imageView;
            imageView = holder.recyclerViewFindBinding.recyclerProfileImage;
            imageView.setImageResource(R.drawable.person_active_96);
        }else {
            Glide.with(context)
                .load(imageUrl)
                .apply(new RequestOptions()
                .error(R.drawable.person_active_96)
                .centerCrop())
                .into(holder.recyclerViewFindBinding.recyclerProfileImage);
//            Picasso.get().load(imageUrl).into(holder.recyclerViewFindBinding.recyclerProfileImage);
        }

        holder.recyclerViewFindBinding.recyclerName.setText(name);
        holder.recyclerViewFindBinding.recyclerNumber.setText(number);
        holder.recyclerViewFindBinding.recyclerMail.setText(mail);

        ChipGroup chipGroupCities = holder.recyclerViewFindBinding.chipGroupCities;

        for (String city : cities) {
            Chip chip = new Chip(context);
            chip.setText(city);
            chip.setCheckable(false);
            chipGroupCities.addView(chip);
        }

    }

}
