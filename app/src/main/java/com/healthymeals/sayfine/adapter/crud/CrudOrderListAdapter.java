package com.healthymeals.sayfine.adapter.crud;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.healthymeals.sayfine.GlideApp;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.model.Order;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

public class CrudOrderListAdapter extends RecyclerView.Adapter<CrudOrderListAdapter.MyViewHolder> {
    private Context context;
    private List<Order> mList;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private StorageReference storageReference;

    public CrudOrderListAdapter(Context context, List<Order> mList){
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public CrudOrderListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_order_status , parent , false);
        return new CrudOrderListAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CrudOrderListAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        storageReference = FirebaseStorage.getInstance().getReference().child("menus/" + mList.get(position).getMenuId() + ".jpg");
        String by = "";
        if (mList.get(position).getOrderBy() == 0){
            by = "Pesan melalui GoFood";
        }
        else if (mList.get(position).getOrderBy() == 1){
            by = "Pesan melalui GrabFood";
        }
        else if (mList.get(position).getOrderBy() == 2){
            by = "Pesan melalui ShopeeFood";
        }
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy, HH:mm:ss");
        String strDate = dateFormat.format(mList.get(position).getTimestamp().toDate());

        holder.txtOrderName.setText(mList.get(position).getMenuName());
        holder.txtOrderBy.setText(by);
        holder.txtOrderDate.setText(strDate);
        holder.chkOrderFinished.setVisibility(View.VISIBLE);

        if (mList.get(position).getVerified() == false){
            holder.txtOrderStatus.setText("Belum Terverifikasi");
            holder.chkOrderFinished.setChecked(false);
            holder.cardOrderStatus.setCardBackgroundColor(Color.parseColor("#DE8925"));
        }
        else {
            holder.txtOrderStatus.setText("Terverifikasi");
            holder.cardOrderStatus.setCardBackgroundColor(Color.GREEN);
            holder.chkOrderFinished.setChecked(true);
        }

        GlideApp.with(context).load(storageReference).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imgOrderThumb);

        holder.chkOrderFinished.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if (isChecked){
                    mList.get(position).setVerified(true);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("verified", true);
                    firebaseFirestore.collection("Users").document(mAuth.getUid()).collection("Orders").document(mList.get(position).getId())
                            .update(map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(@NonNull Void unused) {

                                }
                            });
                }
                else{
                    mList.get(position).setVerified(false);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("verified", false);
                    firebaseFirestore.collection("Users").document(mAuth.getUid()).collection("Orders").document(mList.get(position).getId())
                            .update(map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(@NonNull Void unused) {

                                }
                            });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imgOrderThumb;
        MaterialCardView cardOrderStatus;
        TextView txtOrderName;
        TextView txtOrderDate;
        TextView txtOrderBy;
        TextView txtOrderStatus;
        CheckBox chkOrderFinished;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgOrderThumb = itemView.findViewById(R.id.imgOrderThumb);
            cardOrderStatus = itemView.findViewById(R.id.cardOrderStatus);
            txtOrderName = itemView.findViewById(R.id.txtOrderName);
            txtOrderDate = itemView.findViewById(R.id.txtOrderDate);
            txtOrderBy = itemView.findViewById(R.id.txtOrderBy);
            txtOrderStatus = itemView.findViewById(R.id.txtOrderStatus);
            chkOrderFinished = itemView.findViewById(R.id.chkOrderFinished);
        }
    }
}
