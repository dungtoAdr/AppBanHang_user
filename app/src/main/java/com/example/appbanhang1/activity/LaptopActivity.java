package com.example.appbanhang1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.example.appbanhang1.R;
import com.example.appbanhang1.adapter.DienThoaiAdapter;
import com.example.appbanhang1.adapter.LaptopAdapter;
import com.example.appbanhang1.model.SanPhamMoi;
import com.example.appbanhang1.retrofit.ApiBanHang;
import com.example.appbanhang1.retrofit.RetrofitClient;
import com.example.appbanhang1.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LaptopActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ApiBanHang apiBanHang;
    LaptopAdapter laptopAdapter;
    List<SanPhamMoi> list;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    int page=1;
    int loai;
    LinearLayoutManager manager;
    Handler handler=new Handler();
    boolean isLoading=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laptop);
        apiBanHang= RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        loai=getIntent().getIntExtra("loai",2);
        init();
        ActionToolBar();
        getData(page);
        addEventLoad();
    }

    private void addEventLoad() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isLoading==false){
                    if(manager.findLastCompletelyVisibleItemPosition() == list.size()-1){
                        isLoading=true;
                        loadMore();
                    }
                }
            }
        });
    }

    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                //add null
                list.add(null);
                laptopAdapter.notifyItemInserted(list.size()-1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //remover null
                list.remove(list.size()-1);
                laptopAdapter.notifyItemRemoved(list.size());
                page=page+1;
                getData(page);
                laptopAdapter.notifyDataSetChanged();
                isLoading=false;
            }
        },2000);
    }

    private void getData(int page) {
        compositeDisposable.add(apiBanHang.getSanPham(page,loai)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if(sanPhamMoiModel.isSuccess()){
                                if(laptopAdapter==null) {
                                    list = sanPhamMoiModel.getResult();
                                    laptopAdapter = new LaptopAdapter(getApplicationContext(), list);
                                    recyclerView.setAdapter(laptopAdapter);
                                }else {
                                    int vitri=list.size()-1;
                                    int soluongdd=sanPhamMoiModel.getResult().size();
                                    for(int i=0;i<soluongdd;i++){
                                        list.add(sanPhamMoiModel.getResult().get(i));
                                    }
                                    laptopAdapter.notifyItemRangeChanged(vitri,soluongdd);
                                }
                            }else{
                                Toast.makeText(getApplicationContext(),"Het du lieu",Toast.LENGTH_SHORT).show();
                                isLoading=true;
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),"Không kết nối sever",Toast.LENGTH_SHORT).show();
                        }
                ));
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

    public void init(){
        toolbar=findViewById(R.id.toolbar);
        recyclerView=findViewById(R.id.recycleviewd_laptop);
        manager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        list=new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}