package com.example.appbanhang1.utils;

import com.example.appbanhang1.model.GioHang;
import com.example.appbanhang1.model.User;

import java.util.ArrayList;
import java.util.List;

public class Utils {
//    public static final String BASE_URL="http://192.168.1.194/banhang/";
    public static final String BASE_URL="https://banhangonline112.000webhostapp.com/";
    public static List<GioHang> manggiohang;
    public static List<GioHang> mangmuahang=new ArrayList<>();
    // o truong: 192.168.25.210
    // o nha: 192.168.1.194
    public static User user_current=new User();


    public static String ID_RECEIVED;
    public static final String SENDID="idsend";
    public static final String RECEIVEDID="idreceived";
    public static final String MESS="message";
    public static final String DATETIME="datetime";
    public static final String PATH_CHAT="chat";
    public static String statusOder(int status){
        String result="";
        switch (status){
            case 0:
                result ="Đơn hàng đang được xử lí";
                break;
            case 1:
                result="Đơn hàng đã chấp nhận";
                break;
            case 2:
                result="Đơn hàng đã giao cho đơn vị vận chuyển";
                break;
            case 3:
                result="Đơn hàng đã giao thành công";
                break;
            case 4:
                result="Đơn hàng đã hủy";
                break;
        }
        return result;
    }

}
