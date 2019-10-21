package com.hcmunre.apporderfoodclient.models.Database;

import android.util.Log;

import com.hcmunre.apporderfoodclient.models.Common.HashPass;
import com.hcmunre.apporderfoodclient.models.Entity.User;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignUpData {
    Connection con;
    DataConnetion dataCon=new DataConnetion();
    CallableStatement callable;

    public SignUpData() {
        con = dataCon.connectionData();
    }

    public int SignUp(User user){
        int res=0;
        try {
            String sql = "{call Sp_InsertUser (?,?,?,?,?,?)}";
            callable = con.prepareCall(sql);
            callable.setString(1, user.getmName());
            callable.setString(2, user.getmAddress());
            callable.setString(3, user.getmPhone());
            callable.setString(4, user.getmImage());
            callable.setString(5,user.getmEmail());
            callable.setString(6, HashPass.md5(user.getmPassword()));
            res = callable.executeUpdate();
            con.close();
        } catch (SQLException e) {
            Log.w("Lỗi Kết nối","" + e.getMessage());
        }
        return res;
    }
    public boolean updateUser(User user){
        try {
            String sql="Exec Sp_UpdateUser '"+user.getmName().toString()+"'," +
                    "'"+user.getmAddress().toString()+"','"+user.getmPhone()+"'," +
                    "'"+user.getmImage()+"','"+user.getId()+"'";
            PreparedStatement pst=con.prepareStatement(sql);
            if(pst.executeUpdate()>0){
                con.close();
                return true;
            }else {
                con.close();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
    public boolean updatePhone(User user){
        try {
            String sql="Exec Sp_UpdateUserPhone '"+user.getmPhone()+"','"+user.getId()+"'";
            PreparedStatement pst=con.prepareStatement(sql);
            if(pst.executeUpdate()>0){
                con.close();
                return true;
            }else {
                con.close();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
