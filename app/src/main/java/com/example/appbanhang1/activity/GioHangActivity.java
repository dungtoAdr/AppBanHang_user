package com.example.appbanhang1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.appbanhang1.R;
import com.example.appbanhang1.adapter.GioHangAdapter;
import com.example.appbanhang1.model.EventBus.TinhTongEvent;
import com.example.appbanhang1.model.GioHang;
import com.example.appbanhang1.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GioHangActivity extends AppCompatActivity {
    TextView giohangtrong, tongtien;
    Toolbar toolbar;
    RecyclerView recyclerView;
    Button btmuahang;
    GioHangAdapter gioHangAdapter;
    long tongtiensp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        init();
        initControl();
        tinhTongTien();

        if(Utils.mangmuahang!=null){
            Utils.mangmuahang.clear();
        }
        tinhTongTien();
    }

    private void tinhTongTien() {
        tongtiensp=0;
        for (int i = 0; i < Utils.mangmuahang.size(); i++) {
            tongtiensp=tongtiensp+Utils.mangmuahang.get(i).getGiasp()*Utils.mangmuahang.get(i).getSoluong();
        }
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        tongtien.setText(decimalFormat.format(tongtiensp));
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        if(Utils.manggiohang.size()==0){
            giohangtrong.setVisibility(View.VISIBLE);
        }else{
            gioHangAdapter =new GioHangAdapter(getApplicationContext(),Utils.manggiohang);
            recyclerView.setAdapter(gioHangAdapter);
        }

        btmuahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), ThanhToanActivity.class);
                intent.putExtra("tongtien",tongtiensp);
                Utils.manggiohang.clear();
                startActivity(intent);
            }
        });
    }

    public void init(){
        giohangtrong=findViewById(R.id.txtgiohangtrong);
        toolbar=findViewById(R.id.toolbar);
        recyclerView=findViewById(R.id.recycleviewgiohang);
        tongtien=findViewById(R.id.txttongtien);
        btmuahang=findViewById(R.id.btmuahang);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void eventTinhTien(TinhTongEvent tinhTongEvent){
        if(tinhTongEvent!=null){
            tinhTongTien();
        }
    }
}