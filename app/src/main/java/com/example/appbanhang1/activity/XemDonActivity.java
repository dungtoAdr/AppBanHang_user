package com.example.appbanhang1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.appbanhang1.Interface.ItemClickDeleteListener;
import com.example.appbanhang1.R;
import com.example.appbanhang1.adapter.DonHangAdapter;
import com.example.appbanhang1.retrofit.ApiBanHang;
import com.example.appbanhang1.retrofit.RetrofitClient;
import com.example.appbanhang1.utils.Utils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;

public class XemDonActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    ApiBanHang apiBanHang;
    RecyclerView redonhang;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_don);
        init();
        initToolbar();
        getOrder();

    }

    private void getOrder() {
        compositeDisposable.add(apiBanHang.xemDonHang(Utils.user_current.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        donHangModel -> {
                            DonHangAdapter adapter=new DonHangAdapter(getApplicationContext(), donHangModel.getResult(), new ItemClickDeleteListener() {
                                @Override
                                public void onClickDelete(int iddonhang) {
                                    showDeleteOrder(iddonhang);
                                }
                            });
                            redonhang.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void showDeleteOrder(int iddonhang) {
        PopupMenu popupMenu=new PopupMenu(this,redonhang.findViewById(R.id.trangthaidon));
        popupMenu.inflate(R.menu.menu_delete);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.deleteOrder){
                    deleteOrder(iddonhang);
                }
                return false;
            }
        });
    }

    private void deleteOrder(int iddonhang) {
        compositeDisposable.add(apiBanHang.deleteOder(iddonhang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        messageModel -> {
                            if(messageModel.isSuccess()){
                                getOrder();
                            }
                        },throwable -> {
                            Log.d("logg",throwable.getMessage());
                        }
                ));
    }

    private void initToolbar() {
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
        apiBanHang= RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        redonhang=findViewById(R.id.recycleview_donhang);
        toolbar=findViewById(R.id.toolbar);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        redonhang.setLayoutManager(manager);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}