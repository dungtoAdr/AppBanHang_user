package com.example.appbanhang1.retrofit;

import com.example.appbanhang1.model.DonHangModel;
import com.example.appbanhang1.model.KhuyenMaiModel;
import com.example.appbanhang1.model.LoaiSpModel;
import com.example.appbanhang1.model.MeetingModel;
import com.example.appbanhang1.model.MessageModel;
import com.example.appbanhang1.model.SanPhamMoiModel;
import com.example.appbanhang1.model.UserModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiBanHang {
    //GET data
    @GET("getloaisp.php")
    Observable<LoaiSpModel> getloaisp();
    // khuyen mai
    @GET("khuyenmai.php")
    Observable<KhuyenMaiModel> getkhuyenmai();

    //meeting
    @GET("getmeeting.php")
    Observable<MeetingModel> getMeeting();

    @GET("getspmoi.php")
    Observable<SanPhamMoiModel> getspmoi();

    //POST data
    @POST("chitiet.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> getSanPham(
            @Field("page") int page,
            @Field("loai") int loai
    );



    @POST("dangki.php")
    @FormUrlEncoded
    Observable<UserModel> dangki(
            @Field("email") String email,
            @Field("pass") String pass,
            @Field("username") String username,
            @Field("mobile") String mobile,
            @Field("uid") String uid
    );


    @POST("dangnhap.php")
    @FormUrlEncoded
    Observable<UserModel> dangnhap(
            @Field("email") String email,
            @Field("pass") String pass
    );


    @POST("reset.php")
    @FormUrlEncoded
    Observable<UserModel> resetpass(
            @Field("email") String email
    );

    @POST("donhang.php")
    @FormUrlEncoded
    Observable<UserModel> createOder(
            @Field("email") String email,
            @Field("sdt") String sdt,
            @Field("tongtien") String tongtien,
            @Field("iduser") int id,
            @Field("diachi") String diachi,
            @Field("soluong") int soluong,
            @Field("chitiet") String chitiet
    );


    @POST("xemdonhang.php")
    @FormUrlEncoded
    Observable<DonHangModel> xemDonHang(
            @Field("iduser") int id
    );


    @POST("timkiem.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> search(
            @Field("search") String search
    );

    @POST("gettoken.php")
    @FormUrlEncoded
    Observable<UserModel> getToken(
            @Field("status") int status
    );

    @POST("updatetoken.php")
    @FormUrlEncoded
    Observable<MessageModel> updateToken(
            @Field("id") int id,
            @Field("token") String token

    );

    @POST("deteleeorder.php")
    @FormUrlEncoded
    Observable<MessageModel> deleteOder(
            @Field("iddonhang") int id
    );
}
