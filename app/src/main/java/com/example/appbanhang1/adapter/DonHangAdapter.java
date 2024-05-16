package com.example.appbanhang1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang1.Interface.ItemClickDeleteListener;
import com.example.appbanhang1.R;
import com.example.appbanhang1.model.DonHang;
import com.example.appbanhang1.utils.Utils;

import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.MyViewHolder> {
    private RecyclerView.RecycledViewPool recycledViewPool=new RecyclerView.RecycledViewPool();
    Context context;
    List<DonHang> list;
    ItemClickDeleteListener itemClickDeleteListener;

    public DonHangAdapter(Context context, List<DonHang> list, ItemClickDeleteListener itemClickDeleteListener) {
        this.context = context;
        this.list = list;
        this.itemClickDeleteListener=itemClickDeleteListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_donhang,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DonHang donHang=list.get(position);
        holder.txtdonhang.setText("Đơn hàng: "+donHang.getId());
        holder.txttrangthai.setText(Utils.statusOder(donHang.getTrangthai()));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                itemClickDeleteListener.onClickDelete(donHang.getId());
                return false;
            }
        });
        LinearLayoutManager manager=new LinearLayoutManager(
          holder.reChitiet.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        manager.setInitialPrefetchItemCount(donHang.getItem().size());
        // adapter chi tiet
        ChiTietAdapter adapter=new ChiTietAdapter(context,donHang.getItem());
        holder.reChitiet.setAdapter(adapter);
        holder.reChitiet.setLayoutManager(manager);
        holder.reChitiet.setRecycledViewPool(recycledViewPool);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtdonhang,txttrangthai;
        RecyclerView reChitiet;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtdonhang=itemView.findViewById(R.id.iddonhang);
            reChitiet=itemView.findViewById(R.id.recycleview_chitiet);
            txttrangthai=itemView.findViewById(R.id.trangthaidon);
        }
    }
}
