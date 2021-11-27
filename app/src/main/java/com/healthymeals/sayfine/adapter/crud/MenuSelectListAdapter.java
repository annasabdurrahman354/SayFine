package com.healthymeals.sayfine.adapter.crud;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.card.MaterialCardView;
import com.healthymeals.sayfine.R;
import com.healthymeals.sayfine.activity.CrudMenuActivity;
import com.healthymeals.sayfine.activity.CrudPacketActivity;
import com.healthymeals.sayfine.model.Menu;

import java.util.List;

public class MenuSelectListAdapter extends RecyclerView.Adapter<MenuSelectListAdapter.MyViewHolder> {
    private CrudPacketActivity activity;
    private Context context;
    private List<Menu> list;
    private List<String> selectedMenuList;

    public MenuSelectListAdapter(Context context, CrudPacketActivity activity, List<Menu> list, List<String> selectedMenuList){
        this.context = context;
        this.activity = activity;
        this.list = list;
        this.selectedMenuList = selectedMenuList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_menu_select , parent , false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuSelectListAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txtMenuTitle.setText(list.get(position).getTitle());
        holder.txtMenuPrice.setText("Rp " + list.get(position).getPrice().toString());
        holder.txtMenuCalorie.setText("Kalori " + list.get(position).getCalorie().toString() + " kkal");
        Glide.with(context).load(list.get(position).getThumbUrl()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.imgMenuThumb);
        if(list.get(position).getType() == true){
            holder.txtMenuType.setText("Weight Gain");
        }
        else if(list.get(position).getType() == false){
            holder.txtMenuType.setText("Weight Loss");
        }
        if(selectedMenuList.contains(list.get(position).getId())){
            holder.chkMenu.setChecked(true);
        }
        holder.chkMenu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    selectedMenuList.add(list.get(position).getId());
                }
                else{
                    selectedMenuList.remove(list.get(position).getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtMenuTitle;
        TextView txtMenuPrice;
        TextView txtMenuType;
        TextView txtMenuCalorie;
        ImageView imgMenuThumb;
        CheckBox chkMenu;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMenuTitle = itemView.findViewById(R.id.txtMenuTitle);
            txtMenuPrice = itemView.findViewById(R.id.txtMenuPrice);
            txtMenuType = itemView.findViewById(R.id.txtMenuType);
            txtMenuCalorie = itemView.findViewById(R.id.txtMenuCalorie);
            chkMenu = itemView.findViewById(R.id.chkMenu);
            imgMenuThumb = itemView.findViewById(R.id.imgMenuThumb);
        }
    }
}