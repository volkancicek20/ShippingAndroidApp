package com.socksapp.mobileproject.adapter;

import static com.socksapp.mobileproject.model.GetOffersModel.LAYOUT_ONE;
import static com.socksapp.mobileproject.model.GetOffersModel.LAYOUT_EMPTY;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.socksapp.mobileproject.R;
import com.socksapp.mobileproject.databinding.RecyclerViewEmptyOfferBinding;
import com.socksapp.mobileproject.databinding.RecyclerViewOfferBinding;
import com.socksapp.mobileproject.fragment.UserOffersFragment;
import com.socksapp.mobileproject.model.GetOffersModel;

import java.util.ArrayList;

public class GetOffersAdapter extends RecyclerView.Adapter{

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore firebaseFirestore;
    ArrayList<GetOffersModel> arrayList;
    Context context;
    UserOffersFragment fragment;

    public GetOffersAdapter(ArrayList<GetOffersModel> arrayList,Context context,UserOffersFragment fragment){
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
                return 2;
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
        String imageUrl,userName,number,mail,personalMail,price,startCity,startDistrict,endCity,endDistrict,offersRef,userId;
        DocumentReference ref;
        Timestamp timestamp;
        switch (holder.getItemViewType()) {
            case LAYOUT_ONE:
                GetOffersHolder getOffersHolder = (GetOffersHolder) holder;

                ref = arrayList.get(position).ref;
                offersRef = arrayList.get(position).offersRef;
                userId = arrayList.get(position).userId;
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
                personalMail = arrayList.get(position).personalMail;

                getShow(imageUrl,userName,number,mail,price,startCity,startDistrict,endCity,endDistrict,timestamp,getOffersHolder);

                getOffersHolder.recyclerViewOfferBinding.verticalMenu.setOnClickListener(v -> {
                    fragment.dialogShow(v,imageUrl,userName,number,mail,personalMail,ref,getOffersHolder.getAdapterPosition(),offersRef,startCity,userId,price);
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
            imageView.setImageResource(R.drawable.person_active_96);
        }else {
            Glide.with(context)
                .load(imageUrl)
                .apply(new RequestOptions()
                .error(R.drawable.person_active_96)
                .centerCrop())
                .into(holder.recyclerViewOfferBinding.recyclerProfileImage);
//            Picasso.get().load(imageUrl).into(holder.recyclerViewOfferBinding.recyclerProfileImage);
        }


        String startPoint = startCity + "(" + startDistrict + ")";
        String endPoint = endCity + "(" + endDistrict + ")";
        String point = startPoint + " -> " + endPoint;

        holder.recyclerViewOfferBinding.recyclerUserId.setText(userName);
        holder.recyclerViewOfferBinding.recyclerPrice.setText(price+" TL");
        holder.recyclerViewOfferBinding.recyclerPoint.setText(point);

        long secondsElapsed = (Timestamp.now().getSeconds() - timestamp.getSeconds());
        String elapsedTime;

        if(secondsElapsed < 0){
            elapsedTime = "şimdi •";
        } else if (secondsElapsed >= 31536000) {
            elapsedTime = "" + (secondsElapsed / 31536000) + " yıl önce •";
        } else if (secondsElapsed >= 2592000) {
            elapsedTime = "" + (secondsElapsed / 2592000) + " ay önce •";
        } else if (secondsElapsed >= 86400) {
            elapsedTime = "" + (secondsElapsed / 86400) + " gün önce •";
        } else if (secondsElapsed >= 3600) {
            elapsedTime = "" + (secondsElapsed / 3600) + " saat önce •";
        } else if (secondsElapsed >= 60) {
            elapsedTime = "" + (secondsElapsed / 60) + " dakika önce •";
        } else {
            elapsedTime = "" + secondsElapsed + " saniye önce •";
        }

        holder.recyclerViewOfferBinding.timestampTime.setText(elapsedTime);

    }

//    public void rejectOffers(View view,DocumentReference ref,String mail){
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
//        builder.setMessage("Teklifi reddetmek istiyor musunuz?");
//        builder.setPositiveButton("REDDET", (dialog, which) ->{
//            CollectionReference collectionReference = firebaseFirestore.collection("offers").document(mail).collection(mail);
//            String deleteRef = ref.getId();
//            DocumentReference documentReference = collectionReference.document(deleteRef);
//
//            documentReference.delete().addOnSuccessListener(unused -> {
//                Toast.makeText(view.getContext(),"Teklif reddedildi",Toast.LENGTH_SHORT).show();
//            }).addOnFailureListener(e -> {
//                Toast.makeText(view.getContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
//            });
//            dialog.dismiss();
//        });
//
//        builder.setNegativeButton("Hayır", (dialog, which) -> {
//            dialog.dismiss();
//        });
//
//        AlertDialog dialog = builder.create();
//
//        dialog.show();
//    }

}
