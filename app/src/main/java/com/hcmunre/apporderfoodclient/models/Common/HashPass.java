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
            byte messageDigest[] =digest.digest();
            //Tạo hex string
            StringBuilder hexString=new StringBuilder();
            for(byte aMessageDigest : messageDigest){
                String h=Integer.toHexString(0xFF & aMessageDigest);
                while (h.length()<2)
                    h="0"+h;
                hexString.append(h);
            }
            return hexString.toString();

        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
