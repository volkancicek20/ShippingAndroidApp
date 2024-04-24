package com.socksapp.mobileproject.adapter;

import static com.socksapp.mobileproject.model.GetNotificationOffersModel.LAYOUT_ONE;
import static com.socksapp.mobileproject.model.GetNotificationOffersModel.LAYOUT_EMPTY;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.socksapp.mobileproject.R;
import com.socksapp.mobileproject.databinding.RecyclerViewEmptyNotificationOffersBinding;
import com.socksapp.mobileproject.databinding.RecyclerViewNotificationOffersBinding;
import com.socksapp.mobileproject.fragment.InstitutionalOffersNotificationFragment;
import com.socksapp.mobileproject.model.GetNotificationOffersModel;
import java.util.ArrayList;

/**
 * Bu Adapter, Kurumsal hesapların tekliflerinin geri bildirimini gösterimini yapan recyclerview'ın adapteridir.
 */
public class GetNotificationOffersAdapter extends RecyclerView.Adapter {
    ArrayList<GetNotificationOffersModel> arrayList;
    Context context;
    InstitutionalOffersNotificationFragment fragment;

    public GetNotificationOffersAdapter(ArrayList<GetNotificationOffersModel> arrayList, Context context, InstitutionalOffersNotificationFragment fragment) {
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
            case LAYOUT_ONE:
                RecyclerViewNotificationOffersBinding recyclerViewNotificationOffersBinding = RecyclerViewNotificationOffersBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
                return new GetNotificationHolder(recyclerViewNotificationOffersBinding);
            case LAYOUT_EMPTY:
                RecyclerViewEmptyNotificationOffersBinding recyclerViewEmptyNotificationOffersBinding = RecyclerViewEmptyNotificationOffersBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
                return new GetEmptyNotificationHolder(recyclerViewEmptyNotificationOffersBinding);
            default:
                return null;

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String imageUrl,userName,number,mail,price,result,startCity,startDistrict,endCity,endDistrict,loadType,loadAmount,date,time,refId;
        switch (holder.getItemViewType()) {
            case LAYOUT_ONE:
                GetNotificationHolder getNotificationHolder = (GetNotificationHolder) holder;

                refId = arrayList.get(position).refId;
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
                loadType = arrayList.get(position).loadType;
                loadAmount = arrayList.get(position).loadAmount;
                date = arrayList.get(position).date;
                time = arrayList.get(position).time;

                getShow(imageUrl,userName,number,mail,price,result,startCity,startDistrict,endCity,endDistrict,loadType,loadAmount,date,time,getNotificationHolder);

                getNotificationHolder.recyclerViewNotificationOffersBinding.verticalMenu.setOnClickListener(v -> {
                    fragment.deleteNotification(v,refId,getNotificationHolder.getAdapterPosition());
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

    private void getShow(String imageUrl, String userName,String number, String mail,String price,String result, String startCity, String startDistrict, String endCity, String endDistrict, String loadType, String loadAmount, String date, String time, GetNotificationHolder holder){

        if(imageUrl.isEmpty()){
            ImageView imageView;
            imageView = holder.recyclerViewNotificationOffersBinding.recyclerProfileImage;
            imageView.setImageResource(R.drawable.icon_person_white);
        }else {
            Glide.with(context)
                .load(imageUrl)
                .apply(new RequestOptions()
                .error(R.drawable.person_active_96)
                .centerCrop())
                .into(holder.recyclerViewNotificationOffersBinding.recyclerProfileImage);
        }


        String startPoint = startCity + "/" + startDistrict;
        String endPoint = endCity + "/" + endDistrict;
        String point = startPoint + " -> " + endPoint;
        String loadInfo = loadAmount + ",  " + loadType;
        String plannedDate = time + ",  " + date;
        String phoneNumber = "+90 " + number;


        holder.recyclerViewNotificationOffersBinding.recyclerUserId.setText(userName);
        holder.recyclerViewNotificationOffersBinding.recyclerPoint.setText(point);
        holder.recyclerViewNotificationOffersBinding.recyclerLoadInfo.setText(loadInfo);
        holder.recyclerViewNotificationOffersBinding.recyclerPlannedDate.setText(plannedDate);
        holder.recyclerViewNotificationOffersBinding.recyclerNumber.setText(phoneNumber);
        holder.recyclerViewNotificationOffersBinding.recyclerMail.setText(mail);


        holder.recyclerViewNotificationOffersBinding.recyclerPrice.setText(price+" TL");

        if(result.equals("approve")){
            holder.recyclerViewNotificationOffersBinding.resultText.setText("TEKLİFİNİZ KABUL EDİLDİ");
        }

        if(result.equals("reject")){
            holder.recyclerViewNotificationOffersBinding.resultText.setText("TEKLİFİNİZ REDDEDİLDİ");
        }

    }
}
