package com.example.appbanhang1.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
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
import com.example.appbanhang1.Interface.ImageClickListener;
import com.example.appbanhang1.R;
import com.example.appbanhang1.model.EventBus.TinhTongEvent;
import com.example.appbanhang1.model.GioHang;
import com.example.appbanhang1.model.Item;
import com.example.appbanhang1.model.SanPhamMoi;
import com.example.appbanhang1.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

import io.paperdb.Paper;

public class GioHangAdapter extends RecyclerView.Adapter<GioHangAdapter.MyViewHolder>{
    Context context;
    List<GioHang> list;

    public GioHangAdapter(Context context, List<GioHang> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_giohang,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GioHang gioHang=list.get(position);
        holder.item_giohang_tensp.setText(gioHang.getTensp()+"");
        holder.item_giohang_soluong.setText(gioHang.getSoluong()+" ");
        if(gioHang.getHinhsp().contains("http")){
            Glide.with(context).load(gioHang.getHinhsp()).into(holder.imageView);
        }else{
            Glide.with(context).load(Utils.BASE_URL+"images/"+gioHang.getHinhsp()).into(holder.imageView);
        }
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        holder.item_giohang_gia.setText(decimalFormat.format(gioHang.getGiasp()));
        long gia=gioHang.getSoluong() * gioHang.getGiasp();
        holder.item_giohang_gia2.setText(decimalFormat.format(gia));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Utils.manggiohang.get(holder.getAdapterPosition()).setChecked(true);
                    if(!Utils.mangmuahang.contains(gioHang)){
                        Utils.mangmuahang.add(gioHang);
                    }
                    EventBus.getDefault().postSticky(new TinhTongEvent());
                }else{
                    Utils.manggiohang.get(holder.getAdapterPosition()).setChecked(false);
                    for (int i = 0; i < Utils.mangmuahang.size(); i++) {
                        if(Utils.mangmuahang.get(i).getIdsp() == gioHang.getIdsp()){
                            Utils.mangmuahang.remove(i);
                            EventBus.getDefault().postSticky(new TinhTongEvent());
                        }
                    }
                }
            }
        });

        holder.checkBox.setChecked(gioHang.isChecked());
        holder.setImageClickListener(new ImageClickListener() {
            @Override
            public void onImageClick(View view, int position, int giatri) {
                if(giatri==1){
                    if(list.get(position).getSoluong()>1){
                        int soluongmoi=list.get(position).getSoluong()-1;
                        list.get(position).setSoluong(soluongmoi);
                        holder.item_giohang_soluong.setText(list.get(position).getSoluong()+" ");
                        long gia=list.get(position).getSoluong() * list.get(position).getGiasp();
                        holder.item_giohang_gia2.setText(decimalFormat.format(gia));
                        EventBus.getDefault().postSticky(new TinhTongEvent());
                    }else if(list.get(position).getSoluong()==1){
                        AlertDialog.Builder builder=new AlertDialog.Builder(view.getRootView().getContext());
                        builder.setTitle("Thông báo");
                        builder.setMessage("Bạn có muốn xóa sản phẩm này khỏi giỏ hàng");
                        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.mangmuahang.remove(gioHang);
                                Utils.manggiohang.remove(position);
                                Paper.book().write("giohang",Utils.manggiohang);
                                notifyDataSetChanged();
                                EventBus.getDefault().postSticky(new TinhTongEvent());
                            }
                        });
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                }else if(giatri==2){
                    if(list.get(position).getSoluong()<list.get(position).getSltonkho()){
                        int soluongmoi=list.get(position).getSoluong()+1;
                        list.get(position).setSoluong(soluongmoi);
                    }
                    holder.item_giohang_soluong.setText(list.get(position).getSoluong()+" ");
                    long gia=list.get(position).getSoluong() * list.get(position).getGiasp();
                    holder.item_giohang_gia2.setText(decimalFormat.format(gia));
                    EventBus.getDefault().postSticky(new TinhTongEvent());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView,giohangtru,giohangcong;
        TextView item_giohang_tensp,item_giohang_gia,item_giohang_soluong,item_giohang_gia2;
        ImageClickListener imageClickListener;
        CheckBox checkBox;

        public void setImageClickListener(ImageClickListener imageClickListener) {
            this.imageClickListener = imageClickListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_giohang_tensp=itemView.findViewById(R.id.item_giohang_tensp);
            item_giohang_gia=itemView.findViewById(R.id.item_giohang_gia);
            item_giohang_soluong=itemView.findViewById(R.id.item_giohang_soluong);
            item_giohang_gia2=itemView.findViewById(R.id.item_giohang_giasp2);
            imageView=itemView.findViewById(R.id.item_giohang_image);
            giohangcong=itemView.findViewById(R.id.item_giohang_cong);
            giohangtru=itemView.findViewById(R.id.item_giohang_tru);
            checkBox=itemView.findViewById(R.id.item_giohang_check);

            //event click
            giohangcong.setOnClickListener(this);
            giohangtru.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v==giohangtru){
                //1 la tru
                imageClickListener.onImageClick(v,getAdapterPosition(),1);
            }else if(v==giohangcong){
                // 2 la cong
                imageClickListener.onImageClick(v,getAdapterPosition(),2);
            }
        }
    }
}
