package com.example.appbanhang1.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang1.Interface.ItemClickListener;
import com.example.appbanhang1.R;
import com.example.appbanhang1.activity.ChiTietActivity;
import com.example.appbanhang1.model.SanPhamMoi;
import com.example.appbanhang1.utils.Utils;

import java.text.DecimalFormat;
import java.util.List;

public class SanPhamMoiAdapter extends RecyclerView.Adapter<SanPhamMoiAdapter.MyViewHolder> {
    Context context;
    List<SanPhamMoi> list;

    public SanPhamMoiAdapter(Context context, List<SanPhamMoi> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_sp_moi,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SanPhamMoi sanPhamMoi=list.get(position);
        holder.txtten.setText(sanPhamMoi.getTensp());
        try {
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            String g=sanPhamMoi.getGiasp();
            double gia=Double.parseDouble(g);
            String formattedGia = "Giá: " + decimalFormat.format(gia) + " Đ";
            holder.txtgia.setText(formattedGia);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        if(sanPhamMoi.getHinhanh().contains("http")){
            Glide.with(context).load(sanPhamMoi.getHinhanh()).into(holder.imghinhanh);
        }else{
            Glide.with(context).load(Utils.BASE_URL+"images/"+sanPhamMoi.getHinhanh()).into(holder.imghinhanh);
        }
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (!isLongClick){
                    //click
                    Intent intent=new Intent(context, ChiTietActivity.class);
                    intent.putExtra("chitiet",sanPhamMoi);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }






    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtgia,txtten;
        ImageView imghinhanh;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtgia=itemView.findViewById(R.id.itemsp_gia);
            txtten=itemView.findViewById(R.id.itemsp_ten);
            imghinhanh=itemView.findViewById(R.id.itemsp_image);
            itemView.setOnClickListener(this);

        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(),false);
        }
    }
}
