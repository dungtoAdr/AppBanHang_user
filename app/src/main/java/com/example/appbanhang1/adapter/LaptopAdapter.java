package com.example.appbanhang1.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class LaptopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<SanPhamMoi> list;
    private static final int VIEW_TYPE_DATA=0;
    private static final int VIEW_TYPE_LOADING=1;
    public LaptopAdapter(Context context, List<SanPhamMoi> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==VIEW_TYPE_DATA){
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_laptop,parent,false);
            return new MyViewHolder(view);
        }else{
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loaading,parent,false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            MyViewHolder viewHolder= (MyViewHolder) holder;
            SanPhamMoi sanPhamMoi=list.get(position);
            viewHolder.tensp.setText(sanPhamMoi.getTensp());
            DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
            double gia=Double.parseDouble(sanPhamMoi.getGiasp());
            viewHolder.giasp.setText(decimalFormat.format(gia));
            viewHolder.mota.setText(sanPhamMoi.getMota());
            viewHolder.id.setText(sanPhamMoi.getId()+"");
            if(sanPhamMoi.getHinhanh().contains("http")){
                Glide.with(context).load(sanPhamMoi.getHinhanh()).into(viewHolder.hinhanh);
            }else {
                Glide.with(context).load(Utils.BASE_URL+"images/"+sanPhamMoi.getHinhanh()).into(viewHolder.hinhanh);
            }
            viewHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    if (!isLongClick){
                        //click
                        Intent intent=new Intent(context, ChiTietActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }else{
            LoadingViewHolder loadingViewHolder= (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position) ==null ? VIEW_TYPE_LOADING:VIEW_TYPE_DATA;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar=itemView.findViewById(R.id.progressbar);
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tensp,giasp,mota,id;
        ImageView hinhanh;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id=itemView.findViewById(R.id.itemlaptop_id);
            tensp=itemView.findViewById(R.id.itemlaptop_ten);
            giasp=itemView.findViewById(R.id.itemlaptop_gia);
            mota=itemView.findViewById(R.id.itemlaptop_mota);
            hinhanh=itemView.findViewById(R.id.itemlaptop_image);
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
