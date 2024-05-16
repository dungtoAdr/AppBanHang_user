package com.example.appbanhang1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.appbanhang1.R;
import com.example.appbanhang1.adapter.LoaiSpAdapter;
import com.example.appbanhang1.adapter.SanPhamMoiAdapter;
import com.example.appbanhang1.model.LoaiSp;
import com.example.appbanhang1.model.LoaiSpModel;
import com.example.appbanhang1.model.SanPhamMoi;
import com.example.appbanhang1.model.SanPhamMoiModel;
import com.example.appbanhang1.model.User;
import com.example.appbanhang1.retrofit.ApiBanHang;
import com.example.appbanhang1.retrofit.RetrofitClient;
import com.example.appbanhang1.utils.Utils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerViewManHinhChinh;
    NavigationView navigationView;
    ListView listViewManHinhChinh;
    DrawerLayout drawerLayout;
    LoaiSpAdapter loaiSpAdapter;
    List<LoaiSp> mangloaisp;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<SanPhamMoi> mangSpmoi;
    SanPhamMoiAdapter sanPhamMoiAdapter;
    NotificationBadge notificationBadge;
    FrameLayout frameLayout;
    ImageView imgsearch,img_mess;
    ImageSlider imageSlider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiBanHang= RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        Paper.init(this);
        if(Paper.book().read("user") != null){
            User user=Paper.book().read("user");
            Utils.user_current=user;
        }
        getToken();
        init();
        ActionBar();
        if(isConnected(this)){
            ActionViewFlipper();
            getLoaiSanPham();
            getSpMoi();
            getEvemtClick();
        }else{
            Toast.makeText(getApplicationContext(),"Kiểm tra kết nối internet",Toast.LENGTH_SHORT).show();
        }
    }
    private void getToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        if(!TextUtils.isEmpty(s)){
                            compositeDisposable.add(apiBanHang.updateToken(Utils.user_current.getId(),s)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            messageModel -> {

                                            },
                                            throwable -> {
                                                Log.d("log123",throwable.getMessage());
                                            }
                                    ));
                        }
                    }
                });
        compositeDisposable.add(apiBanHang.getToken(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if (userModel.isSuccess()){
                                Utils.ID_RECEIVED=String.valueOf(userModel.getResult().get(0).getId());
                            }
                        },throwable -> {

                        }
                ));
    }
    private void getEvemtClick() {
        listViewManHinhChinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent trangchu=new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent dienthoai=new Intent(getApplicationContext(), DienThoaiActivity.class);
                        dienthoai.putExtra("loai",1);
                        startActivity(dienthoai);
                        break;
                    case 2:
                        Intent laptop=new Intent(getApplicationContext(), LaptopActivity.class);
                        laptop.putExtra("loai",2);
                        startActivity(laptop);
                        break;
                    case 5:
                        Intent donhang=new Intent(getApplicationContext(), XemDonActivity.class);
                        startActivity(donhang);
                        break;
                    case 6:
                        //Xoa key user
                        Paper.book().delete("user");
                        Intent dangnhap=new Intent(getApplicationContext(), DangNhapActivity.class);
                        startActivity(dangnhap);
                        FirebaseAuth.getInstance().signOut();
                        finish();
                        break;
                    case 7:
                        Intent live=new Intent(getApplicationContext(), MeetingActivity.class);
                        startActivity(live);
                        break;

                }
            }
        });
    }

    private void getSpMoi() {
        compositeDisposable.add(apiBanHang.getspmoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if(sanPhamMoiModel.isSuccess()){
                                mangSpmoi=sanPhamMoiModel.getResult();
                                sanPhamMoiAdapter=new SanPhamMoiAdapter(getApplicationContext(),mangSpmoi);
                                recyclerViewManHinhChinh.setAdapter(sanPhamMoiAdapter);
                            }
                        }
                ));
    }

    private void getLoaiSanPham() {
        compositeDisposable.add(apiBanHang.getloaisp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        loaiSpModel -> {
                            if(loaiSpModel.isSuccess()){
                                mangloaisp=loaiSpModel.getResult();
                                mangloaisp.add(new LoaiSp("Đăng xuất",""));
                                mangloaisp.add(new LoaiSp("Live",""));
                                loaiSpAdapter=new LoaiSpAdapter(getApplicationContext(),mangloaisp);
                                listViewManHinhChinh.setAdapter(loaiSpAdapter);
                            }
                        }


                ));
    }

    private void ActionViewFlipper() {
        List<SlideModel> imagelist=new ArrayList<>();
        compositeDisposable.add(apiBanHang.getkhuyenmai()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        khuyenMaiModel -> {
                            if(khuyenMaiModel.isSuccess()){
                                for(int i=0;i<khuyenMaiModel.getResult().size();i++){
                                    imagelist.add(new SlideModel(khuyenMaiModel.getResult().get(i).getUrl(),null));
                                }
                                imageSlider.setImageList(imagelist, ScaleTypes.CENTER_CROP);
                                imageSlider.setItemClickListener(new ItemClickListener() {
                                    @Override
                                    public void onItemSelected(int i) {
                                        Intent km=new Intent(getApplicationContext(), KhuyenMaiActivity.class);
                                        km.putExtra("noidung",khuyenMaiModel.getResult().get(i).getThongtin());
                                        km.putExtra("url",khuyenMaiModel.getResult().get(i).getUrl());
                                        startActivity(km);
                                    }

                                    @Override
                                    public void doubleClick(int i) {

                                    }
                                });
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Không có chương trình khuyến mãi",Toast.LENGTH_SHORT).show();
                            }
                        },throwable -> {
                            Log.d("log",throwable.getMessage());
                        }
                ));


    }
    public void ActionBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }
    public void init(){
        imageSlider=findViewById(R.id.image_slider);
        toolbar=findViewById(R.id.toolbarmanhinhchinh);
        recyclerViewManHinhChinh=findViewById(R.id.recycleview);
        RecyclerView.LayoutManager manager=new GridLayoutManager(this,2);
        recyclerViewManHinhChinh.setLayoutManager(manager);
        recyclerViewManHinhChinh.setHasFixedSize(true);
        navigationView=findViewById(R.id.navigationview);
        listViewManHinhChinh=findViewById(R.id.listviewmanhinhchinh);
        drawerLayout=findViewById(R.id.drawerlayout);
        notificationBadge=findViewById(R.id.menu_sl);
        frameLayout=findViewById(R.id.fraamegiohang);
        imgsearch=findViewById(R.id.imgsearch);
        img_mess=findViewById(R.id.img_mess);
        //Khoi tao list
        mangloaisp=new ArrayList<>();
        mangSpmoi=new ArrayList<>();
        if(Paper.book().read("giohang")!= null){
            Utils.manggiohang=Paper.book().read("giohang");
        }
        //khoi tao adapter
        loaiSpAdapter=new LoaiSpAdapter(getApplicationContext(),mangloaisp);
        listViewManHinhChinh.setAdapter(loaiSpAdapter);
        if(Utils.manggiohang==null){
            Utils.manggiohang=new ArrayList<>();
        }else{
            int totalItem=0;
            for (int i = 0; i < Utils.manggiohang.size(); i++) {
                totalItem=totalItem+Utils.manggiohang.get(i).getSoluong();
            }
            notificationBadge.setText(String.valueOf(totalItem));
        }
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(intent);
            }
        });

        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

        img_mess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int totalItem=0;
        for (int i = 0; i < Utils.manggiohang.size(); i++) {
            totalItem=totalItem+Utils.manggiohang.get(i).getSoluong();
        }
        notificationBadge.setText(String.valueOf(totalItem));
    }

    private boolean isConnected(Context context){
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi=connectivityManager.getNetworkInfo(connectivityManager.TYPE_WIFI); //them quyen vao
        NetworkInfo mobile=connectivityManager.getNetworkInfo(connectivityManager.TYPE_MOBILE);
        if((wifi != null && wifi.isConnected()) || (mobile !=null && mobile.isConnected())){
            return true;
        }else {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}