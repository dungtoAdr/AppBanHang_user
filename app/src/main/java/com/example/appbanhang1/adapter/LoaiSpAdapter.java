package com.example.appbanhang1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appbanhang1.R;
import com.example.appbanhang1.model.LoaiSp;

import java.util.List;

public class LoaiSpAdapter extends BaseAdapter {
    List<LoaiSp> list;
    Context context;

    public LoaiSpAdapter( Context context,List<LoaiSp> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public class ViewHolder{
        TextView textensp;
        ImageView imghinhanh;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView == null){
            viewHolder=new ViewHolder();
            LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.item_sanpham,null);
            viewHolder.textensp=convertView.findViewById(R.id.item_tensp);
            viewHolder.imghinhanh=convertView.findViewById(R.id.item_image);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();

        }
        viewHolder.textensp.setText(list.get(position).getTensanpham());
        Glide.with(context).load(list.get(position).getHinhanh()).into(viewHolder.imghinhanh);
        return convertView;
    }
}
