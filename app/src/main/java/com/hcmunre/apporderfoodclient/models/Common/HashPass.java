package com.hcmunre.apporderfoodclient.models.Common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashPass {
    public static final String md5(String s){
        final String md5="MD5";
        try{
            //Tạo md5 hash
            MessageDigest digest= MessageDigest
                    .getInstance(md5);
            digest.update(s.getBytes());
            byte[] messageDigest  =digest.digest();
            //chuyển mảng byte thành hex string
            StringBuilder sb=new StringBuilder();
            for(byte b : messageDigest){
//                String b=Integer.toHexString(0xFF & aMessageDigest);//chuyển đổi int thành Hexa
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
