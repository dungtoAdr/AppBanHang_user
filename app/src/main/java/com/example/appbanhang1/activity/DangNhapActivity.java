package com.example.appbanhang1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbanhang1.R;
import com.example.appbanhang1.retrofit.ApiBanHang;
import com.example.appbanhang1.retrofit.RetrofitClient;
import com.example.appbanhang1.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DangNhapActivity extends AppCompatActivity {
    TextView txtdangki,txtresetpass;
    EditText email,pass;
    AppCompatButton btdangnhap;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    boolean isLogin=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        init();
        initControl();
    }

    private void initControl() {
        txtdangki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), DangKiActivity.class);
                startActivity(intent);
            }
        });
        txtresetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), ResetPassActivity.class);
                startActivity(intent);
            }
        });
        btdangnhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_email=email.getText().toString().trim();
                String str_pass=pass.getText().toString().trim();
                if(TextUtils.isEmpty(str_email)){
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập Email",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(str_pass)){
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập Pass",Toast.LENGTH_SHORT).show();
                }else{
                    //save
                    Paper.book().write("email",str_email);
                    Paper.book().write("pass",str_pass);
                    if (user != null){
                        // user da co dang nhap firebase chua sign out
                        dangnhap(str_email,str_pass);
                    }else{
                        //user da signout
                        // co 2 noi quan ly mat khau 1 la firebase, 2 la host
                        firebaseAuth.signInWithEmailAndPassword(str_email,str_pass)
                                .addOnCompleteListener(DangNhapActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            dangnhap(str_email,str_pass);
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }

    public void init(){
        Paper.init(this);
        apiBanHang= RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        txtdangki=findViewById(R.id.txtdangki);
        email=findViewById(R.id.email);
        pass=findViewById(R.id.pass);
        btdangnhap=findViewById(R.id.btdangnhap);
        txtresetpass=findViewById(R.id.txtresetpass);
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        //read data
        if(Paper.book().read("email") != null && Paper.book().read("pass") !=null){
            email.setText(Paper.book().read("email"));
            pass.setText(Paper.book().read("pass"));
            if (Paper.book().read("isLogin") != null){
                boolean flag=Paper.book().read("isLogin");
                if(flag){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //dangnhap(Paper.book().read("email"),Paper.book().read("pass"));
                        }
                    },1000);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Utils.user_current.getEmail()!= null && Utils.user_current.getPass() != null){
            email.setText(Utils.user_current.getEmail());
            pass.setText(Utils.user_current.getPass());
        }
    }

    private void dangnhap(String email,String pass) {
        compositeDisposable.add(apiBanHang.dangnhap(email,pass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if(userModel.isSuccess()){
                                isLogin=true;
                                Paper.book().write("isLogin",isLogin);
                                Toast.makeText(getApplicationContext(),"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                                Utils.user_current=userModel.getResult().get(0);
                                // luu lai thong tin nguoi dung
                                Paper.book().write("user", userModel.getResult().get(0));
                                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),userModel.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                        }

                ));
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}