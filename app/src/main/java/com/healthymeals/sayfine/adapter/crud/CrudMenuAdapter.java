package com.healthymeals.sayfine.adapter.crud;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.healthymeals.sayfine.activity.CrudMenuActivity;

import java.util.List;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.model.Menu;

public class CrudMenuAdapter extends RecyclerView.Adapter<CrudMenuAdapter.MyViewHolder> {
    private CrudMenuActivity activity;
    private Context context;
    private List<Menu> mList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    public CrudMenuAdapter(Context context, CrudMenuActivity activity, List<Menu> mList){
        this.context = context;
        this.activity = activity;
        this.mList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.list_menu , parent , false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CrudMenuAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtMenuTitle.setText(mList.get(position).getTitle());
        holder.txtMenuPrice.setText(mList.get(position).getPrice());
        Glide.with(context).load(mList.get(position).getThumbUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imgMenuThumb);
        holder.cardMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*activity.selectMenu(Integer.valueOf(position), mList.get(position));*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtMenuTitle;
        TextView txtMenuPrice;
        ImageView imgMenuThumb;
        MaterialCardView cardMenu;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMenuTitle = itemView.findViewById(R.id.txtMenuTitle);
            txtMenuPrice = itemView.findViewById(R.id.txtMenuPrice);
            imgMenuThumb = itemView.findViewById(R.id.imgThumb);
            cardMenu = itemView.findViewById(R.id.cardMenu);
        }
    }
}