package com.example.appbanhang1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.appbanhang1.R;
import com.example.appbanhang1.retrofit.ApiBanHang;
import com.example.appbanhang1.retrofit.RetrofitClient;
import com.example.appbanhang1.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ResetPassActivity extends AppCompatActivity {
    EditText email;
    AppCompatButton btreset;
    ProgressBar progressBar;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable=new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        init();
        initControl();
    }

    private void initControl() {
        btreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_email=email.getText().toString().trim();
                if(TextUtils.isEmpty(str_email)){
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập email",Toast.LENGTH_SHORT).show();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    // reset pass onfirebase
                    FirebaseAuth.getInstance().sendPasswordResetEmail(str_email)
                            .addOnCompleteListener(task -> {
                               if(task.isSuccessful()){
                                   Toast.makeText(getApplicationContext(),"Kiểm tra Email của bạn",Toast.LENGTH_SHORT).show();
                               }
                               finish();
                            });
                }
            }
        });
    }

    public void init(){
        apiBanHang= RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        email=findViewById(R.id.edtresetpass);
        btreset=findViewById(R.id.btresetpass);
        progressBar=findViewById(R.id.progressbar);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}