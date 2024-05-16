package com.example.appbanhang1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appbanhang1.R;
import com.example.appbanhang1.model.GioHang;
import com.example.appbanhang1.model.SanPhamMoi;
import com.example.appbanhang1.utils.Utils;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class ChiTietActivity extends AppCompatActivity {
    TextView tensp,giasp,mota;
    ImageView hinhanh;
    Button btthem,btxemvideo;
    Spinner spinner;
    Toolbar toolbar;
    SanPhamMoi sanPhamMoi;
    NotificationBadge notificationBadge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        init();
        ActionToolBar();
        initData();
        initControl();
    }

    private void initControl() {
        btthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themGioHang();
                Paper.book().write("giohang",Utils.manggiohang);
            }
        });
        btxemvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),YoutubeActivity.class);
                intent.putExtra("linkvideo",sanPhamMoi.getLinkvideo());
                startActivity(intent);
            }
        });
    }

    private void themGioHang() {
        if(Utils.manggiohang.size()>0){
            boolean flag=false;
            int soluong=Integer.parseInt(spinner.getSelectedItem().toString());
            for (int i = 0; i < Utils.manggiohang.size(); i++) {
                if(Utils.manggiohang.get(i).getIdsp()== sanPhamMoi.getId()){
                    Utils.manggiohang.get(i).setSoluong(soluong+Utils.manggiohang.get(i).getSoluong());
                    flag=true;
                }
            }
            if(flag==false){
                long gia=Long.parseLong(sanPhamMoi.getGiasp());
                GioHang gioHang = new GioHang();
                gioHang.setTensp(sanPhamMoi.getTensp());
                gioHang.setGiasp(gia);
                gioHang.setSoluong(soluong);
                gioHang.setIdsp(sanPhamMoi.getId());
                gioHang.setHinhsp(sanPhamMoi.getHinhanh());
                gioHang.setSltonkho(sanPhamMoi.getSltonkho());
                Utils.manggiohang.add(gioHang);
            }
        }else{
            int soluong=Integer.parseInt(spinner.getSelectedItem().toString());
            long gia=Long.parseLong(sanPhamMoi.getGiasp());
            GioHang gioHang = new GioHang();
            gioHang.setTensp(sanPhamMoi.getTensp());
            gioHang.setGiasp(gia);
            gioHang.setSoluong(soluong);
            gioHang.setIdsp(sanPhamMoi.getId());
            gioHang.setHinhsp(sanPhamMoi.getHinhanh());
            gioHang.setSltonkho(sanPhamMoi.getSltonkho());
            Utils.manggiohang.add(gioHang);
        }
        int totalItem=0;
        for (int i = 0; i < Utils.manggiohang.size(); i++) {
            totalItem=totalItem+Utils.manggiohang.get(i).getSoluong();
        }
        notificationBadge.setText(String.valueOf(Utils.manggiohang.size()));
    }

    private void initData() {
        sanPhamMoi= (SanPhamMoi) getIntent().getSerializableExtra("chitiet");
        tensp.setText(sanPhamMoi.getTensp());
        mota.setText(sanPhamMoi.getMota());
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        double gia=Double.parseDouble(sanPhamMoi.getGiasp());
        giasp.setText("Giá: "+decimalFormat.format(gia)+" Đ");
        if(sanPhamMoi.getHinhanh().contains("http")){
            Glide.with(getApplicationContext()).load(sanPhamMoi.getHinhanh()).into(hinhanh);
        }else{
            Glide.with(getApplicationContext()).load(Utils.BASE_URL+"images/"+sanPhamMoi.getHinhanh()).into(hinhanh);
        }
        List<Integer> so=new ArrayList<>();
        for (int i=1;i<= sanPhamMoi.getSltonkho();i++){
            so.add(i);
        }
        ArrayAdapter<Integer> adapter=new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,so);
        spinner.setAdapter(adapter);
    }

    public void init(){
        tensp=findViewById(R.id.txttentensp);
        giasp=findViewById(R.id.txtgiasp);
        mota=findViewById(R.id.txtmotachitiet);
        hinhanh=findViewById(R.id.imgchitiet);
        btthem=findViewById(R.id.btthemvaogiohang);
        spinner=findViewById(R.id.spinner);
        toolbar=findViewById(R.id.toolbar);
        notificationBadge=findViewById(R.id.menu_sl);
        btxemvideo=findViewById(R.id.btxemvideo);
        FrameLayout frameLayoutgiohng=findViewById(R.id.fraamegiohang);
        frameLayoutgiohng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(intent);
            }
        });
        if(Utils.manggiohang != null){
            int totalItem=0;
            for (int i = 0; i < Utils.manggiohang.size(); i++) {
                totalItem=totalItem+Utils.manggiohang.get(i).getSoluong();
            }
            notificationBadge.setText(String.valueOf(totalItem));
        }
    }
    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Utils.manggiohang != null){
            int totalItem=0;
            for (int i = 0; i < Utils.manggiohang.size(); i++) {
                totalItem=totalItem+Utils.manggiohang.get(i).getSoluong();
            }
            notificationBadge.setText(String.valueOf(totalItem));
        }
    }
}