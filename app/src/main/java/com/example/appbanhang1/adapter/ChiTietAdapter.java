package com.example.appbanhang1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang1.R;
import com.example.appbanhang1.model.Item;
import com.example.appbanhang1.utils.Utils;

import java.util.List;

public class ChiTietAdapter extends RecyclerView.Adapter<ChiTietAdapter.MyViewHolder> {
    Context context;
    List<Item> list;

    public ChiTietAdapter(Context context, List<Item> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chitiet,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Item item=list.get(position);
        holder.ten.setText(item.getTensp()+" ");
        holder.soluong.setText("Số lượng: "+item.getSoluong()+" ");
        if(item.getHinhanh().contains("http")){
            Glide.with(context).load(item.getHinhanh()).into(holder.imagechitiet);
        }else{
            Glide.with(context).load(Utils.BASE_URL+"images/"+item.getHinhanh()).into(holder.imagechitiet);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imagechitiet;
        TextView ten,soluong;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imagechitiet=itemView.findViewById(R.id.item_imgchitiet);
            ten=itemView.findViewById(R.id.item_tenspchitiet);
            soluong=itemView.findViewById(R.id.item_soluongchitiet);
        }
    }
}
