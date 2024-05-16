package com.example.appbanhang1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appbanhang1.R;
import com.example.appbanhang1.model.NotiSendData;
import com.example.appbanhang1.retrofit.ApiBanHang;
import com.example.appbanhang1.retrofit.ApiPushNofication;
import com.example.appbanhang1.retrofit.RetrofitClient;
import com.example.appbanhang1.retrofit.RetrofitClientNoti;
import com.example.appbanhang1.utils.Utils;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
//import vn.momo.momo_partner.AppMoMoLib;

public class ThanhToanActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txttongtien,txtsdt,txtemail;
    EditText edtdiachi;
    AppCompatButton btmomo,btdathang;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    ApiBanHang apiBanHang;
    long tongtien;
    int totalItem;
    private String amount = "10000";
    private String fee = "0";
    int environment = 0;//developer default
    private String merchantName = "Thanh toán đơn hàng";
    private String merchantCode = "SCB01";
    private String merchantNameLabel = "DungTo";
    private String description = "Mua hàng online";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
//        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT); // AppMoMoLib.ENVIRONMENT.PRODUCTION
        init();
        countItem();
        initControl();
    }

    private void countItem() {
        totalItem=0;
        for (int i = 0; i < Utils.mangmuahang.size(); i++) {
            totalItem=totalItem+Utils.mangmuahang.get(i).getSoluong();
        }
    }
    //Get token through MoMo app
//    private void requestPayment() {
//        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
//        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);
//
//
//        Map<String, Object> eventValue = new HashMap<>();
//        //client Required
//        eventValue.put("merchantname", merchantName); //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
//        eventValue.put("merchantcode", merchantCode); //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
//        eventValue.put("amount", total_amount); //Kiểu integer
//        eventValue.put("orderId", "orderId123456789"); //uniqueue id cho Bill order, giá trị duy nhất cho mỗi đơn hàng
//        eventValue.put("orderLabel", "Mã đơn hàng"); //gán nhãn
//
//        //client Optional - bill info
//        eventValue.put("merchantnamelabel", "Dịch vụ");//gán nhãn
//        eventValue.put("fee", total_fee); //Kiểu integer
//        eventValue.put("description", description); //mô tả đơn hàng - short description
//
//        //client extra data
//        eventValue.put("requestId",  merchantCode+"merchant_billId_"+System.currentTimeMillis());
//        eventValue.put("partnerCode", merchantCode);
//        //Example extra data
//        JSONObject objExtraData = new JSONObject();
//        try {
//            objExtraData.put("site_code", "008");
//            objExtraData.put("site_name", "CGV Cresent Mall");
//            objExtraData.put("screen_code", 0);
//            objExtraData.put("screen_name", "Special");
//            objExtraData.put("movie_name", "Kẻ Trộm Mặt Trăng 3");
//            objExtraData.put("movie_format", "2D");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        eventValue.put("extraData", objExtraData.toString());
//
//        eventValue.put("extra", "");
//        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);
//
//
//    }
//    //Get token callback from MoMo app an submit to server side
//    void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
//            if(data != null) {
//                if(data.getIntExtra("status", -1) == 0) {
//                    //TOKEN IS AVAILABLE
//                    tvMessage.setText("message: " + "Get token " + data.getStringExtra("message"));
//                    String token = data.getStringExtra("data"); //Token response
//                    String phoneNumber = data.getStringExtra("phonenumber");
//                    String env = data.getStringExtra("env");
//                    if(env == null){
//                        env = "app";
//                    }
//
//                    if(token != null && !token.equals("")) {
//                        // TODO: send phoneNumber & token to your server side to process payment with MoMo server
//                        // IF Momo topup success, continue to process your order
//                    } else {
//                        tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
//                    }
//                } else if(data.getIntExtra("status", -1) == 1) {
//                    //TOKEN FAIL
//                    String message = data.getStringExtra("message") != null?data.getStringExtra("message"):"Thất bại";
//                    tvMessage.setText("message: " + message);
//                } else if(data.getIntExtra("status", -1) == 2) {
//                    //TOKEN FAIL
//                    tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
//                } else {
//                    //TOKEN FAIL
//                    tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
//                }
//            } else {
//                tvMessage.setText("message: " + this.getString(R.string.not_receive_info));
//            }
//        } else {
//            tvMessage.setText("message: " + this.getString(R.string.not_receive_info_err));
//        }
//    }
    ////////
    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        DecimalFormat decimalFormat=new DecimalFormat("###,###,###");
        tongtien=getIntent().getLongExtra("tongtien",0);
        txttongtien.setText(decimalFormat.format(tongtien));
        txtemail.setText(Utils.user_current.getEmail());
        txtsdt.setText(Utils.user_current.getMobile());
        btdathang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_diachi=edtdiachi.getText().toString().trim();
                if(TextUtils.isEmpty(str_diachi)){
                    Toast.makeText(getApplicationContext(),"Bạn chưa nhập địa chỉ",Toast.LENGTH_SHORT).show();
                }else{
                    //post data
                    String str_email=Utils.user_current.getEmail();
                    String str_sdt=Utils.user_current.getMobile();
                    int id=Utils.user_current.getId();
                    Log.d("test",new Gson().toJson(Utils.mangmuahang)); // kiem tra thoi e
                    compositeDisposable.add(apiBanHang.createOder(str_email,str_sdt,String.valueOf(tongtien),id,str_diachi,totalItem,new Gson().toJson(Utils.mangmuahang))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    userModel -> {
                                        pushNotiToUser();
                                        Toast.makeText(getApplicationContext(),"Thanh cong",Toast.LENGTH_SHORT).show();
                                        Utils.mangmuahang.clear();
                                        Paper.book().write("giohang",Utils.manggiohang);
                                        Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                            ));
                }
            }
        });
    }

    private void pushNotiToUser() {
        compositeDisposable.add(apiBanHang.getToken(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userModel -> {
                            if(userModel.isSuccess()){
                               for (int i=0;i<userModel.getResult().size();i++){
                                   Map<String, String> data=new HashMap<>();
                                   data.put("title","thong bao");
                                   data.put("body","Ban co don hang moi");
                                   NotiSendData notiSendData=new NotiSendData(userModel.getResult().get(i).getToken(),data);
                                   ApiPushNofication apiPushNofication= RetrofitClientNoti.getInstance().create(ApiPushNofication.class);
                                   compositeDisposable.add(apiPushNofication.sendNofitication(notiSendData)
                                           .subscribeOn(Schedulers.io())
                                           .observeOn(AndroidSchedulers.mainThread())
                                           .subscribe(
                                                   notiResponse -> {

                                                   },
                                                   throwable -> {
                                                       Log.d("log",throwable.getMessage());
                                                   }
                                           ));
                                }
                            }
                        },throwable -> {
                            Log.d("log",throwable.getMessage());
                        }
                ));

    }

    public void init(){
        apiBanHang= RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        toolbar=findViewById(R.id.toolbar);
        txttongtien=findViewById(R.id.txttongtien);
        txtsdt=findViewById(R.id.txtdienthoai);
        txtemail=findViewById(R.id.txtmail);
        edtdiachi=findViewById(R.id.edtdiachi);
        btdathang=findViewById(R.id.btdathang);
        btmomo=findViewById(R.id.btmomo);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}