package com.socksapp.mobileproject.adapter;

import static com.socksapp.mobileproject.model.GetNotificationOffersModel.LAYOUT_ONE;
import static com.socksapp.mobileproject.model.GetNotificationOffersModel.LAYOUT_EMPTY;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.socksapp.mobileproject.databinding.RecyclerViewEmptyNotificationOffersBinding;
import com.socksapp.mobileproject.databinding.RecyclerViewNotificationOffersBinding;
import com.socksapp.mobileproject.fragment.InstitutionalOffersNotificationFragment;
import com.socksapp.mobileproject.fragment.UserOffersFragment;
import com.socksapp.mobileproject.model.GetNotificationOffersModel;
import com.socksapp.mobileproject.model.GetOffersModel;

import java.util.ArrayList;

public class GetNotificationOffers extends RecyclerView.Adapter {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<GetNotificationOffersModel> arrayList;
    Context context;
    InstitutionalOffersNotificationFragment fragment;

    public GetNotificationOffers(ArrayList<GetNotificationOffersModel> arrayList, Context context, InstitutionalOffersNotificationFragment fragment) {
        this.arrayList = arrayList;
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    public int getItemViewType(int position) {
        if(arrayList.isEmpty()){
            return LAYOUT_EMPTY;
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
            case 1:
                RecyclerViewNotificationOffersBinding recyclerViewNotificationOffersBinding = RecyclerViewNotificationOffersBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
                return new GetNotificationHolder(recyclerViewNotificationOffersBinding);
            case 2:
                RecyclerViewEmptyNotificationOffersBinding recyclerViewEmptyNotificationOffersBinding = RecyclerViewEmptyNotificationOffersBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
                return new GetEmptyNotificationHolder(recyclerViewEmptyNotificationOffersBinding);
            default:
                return null;

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        String imageUrl,userName,number,mail,personalMail,price,result,startCity,startDistrict,endCity,endDistrict;
        DocumentReference ref;
        switch (holder.getItemViewType()) {
            case LAYOUT_ONE:
                GetOffersAdapter.GetOffersHolder getOffersHolder = (GetOffersAdapter.GetOffersHolder) holder;

                ref = arrayList.get(position).ref;
                imageUrl = arrayList.get(position).imageUrl;
                userName = arrayList.get(position).userName;
                number = arrayList.get(position).number;
                mail = arrayList.get(position).mail;
                price = arrayList.get(position).price;
                result = arrayList.get(position).result;
                startCity = arrayList.get(position).startCity;
                startDistrict = arrayList.get(position).startDistrict;
                endCity = arrayList.get(position).endCity;
                endDistrict = arrayList.get(position).endDistrict;
                personalMail = arrayList.get(position).personalMail;

                //getShow(imageUrl,userName,number,mail,price,startCity,startDistrict,endCity,endDistrict,timestamp,getOffersHolder);

                getOffersHolder.recyclerViewOfferBinding.verticalMenu.setOnClickListener(v -> {
                    //fragment.dialogShow(v,imageUrl,userName,number,mail,personalMail,ref,getOffersHolder.getAdapterPosition());
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

    public static class GetNotificationHolder extends RecyclerView.ViewHolder{

        RecyclerViewNotificationOffersBinding recyclerViewNotificationOffersBinding;
        public GetNotificationHolder(RecyclerViewNotificationOffersBinding recyclerViewNotificationOffersBinding) {
            super(recyclerViewNotificationOffersBinding.getRoot());
            this.recyclerViewNotificationOffersBinding = recyclerViewNotificationOffersBinding;
        }
    }

    public static class GetEmptyNotificationHolder extends RecyclerView.ViewHolder{

        RecyclerViewEmptyNotificationOffersBinding recyclerViewEmptyNotificationOffersBinding;
        public GetEmptyNotificationHolder(RecyclerViewEmptyNotificationOffersBinding recyclerViewEmptyNotificationOffersBinding) {
            super(recyclerViewEmptyNotificationOffersBinding.getRoot());
            this.recyclerViewEmptyNotificationOffersBinding = recyclerViewEmptyNotificationOffersBinding;
        }
    }
}
