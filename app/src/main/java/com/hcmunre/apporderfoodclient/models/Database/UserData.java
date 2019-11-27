package com.hcmunre.apporderfoodclient.models.Database;

import android.util.Log;

import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.models.Common.HashPass;
import com.hcmunre.apporderfoodclient.models.Entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UserData {
    Connection con;
    DataConnetion dataCon = new DataConnetion();
    String z = "";

    public UserData() {
        con = dataCon.connectionData();
    }
    public User createNewUserInfo(User user){
        User user1=null;
        try {
            String sql = "Exec Sp_InsertUser (?,?,?,?)";
            PreparedStatement pst = con.prepareCall(sql);
            pst.setString(1, user.getmName().toString());
            pst.setString(2, user.getmAddress().toString());
            pst.setString(3, user.getmPhone());
            pst.setString(4,user.getmEmail());
            ResultSet rs=pst.executeQuery();
            if(rs.next()){
                user1=new User();
                user1.setId(rs.getInt("Id"));
                user1.setmName(rs.getString("Name"));
                user1.setmAddress(rs.getString("Address"));
                user1.setmPhone(rs.getString("Phone"));
                user1.setmEmail(rs.getString("Email"));
                con.close();
            }else {
                user1=null;
                con.close();
            }
        } catch (SQLException e) {
            Log.w("Lỗi Kết nối","" + e.getMessage());
        }
        return user1;
    }

    public boolean SignUp(User user){
        boolean success = false;
        try {
            String sql = "Exec Sp_InsertUser (?,?,?,?,?)";
            PreparedStatement pst = con.prepareCall(sql);
            pst.setString(1, user.getmName().toString());
            pst.setString(2, user.getmAddress().toString());
            pst.setString(3, user.getmPhone());
            pst.setString(4,user.getmEmail());
            pst.setString(5, HashPass.md5(user.getmPassword()));
            ResultSet rs=pst.executeQuery();
            if(rs.next()){
                success=true;
            }else {
                success=false;
            }
        } catch (SQLException e) {
            Log.w("Lỗi Kết nối","" + e.getMessage());
        }
        return success;
    }
    public boolean updateUser(User user) {
        boolean success = false;
        try {
            String sql = "Exec Sp_UpdateUser '" + user.getId() + "','" + user.getmName().toString() + "'," +
                    "'" + user.getmAddress().toString() + "','" + user.getmPhone() + "'," +
                    "'" + user.getmImage() + "'";
            PreparedStatement pst = con.prepareStatement(sql);
            if (pst.executeUpdate() > 0) {
                con.close();
                success = true;
            } else {
                con.close();
                success = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }
    public User getInforUser(User user){
        User user1 = null;
        try {
            String sql="EXEC Sp_SelectUser '"+user.getmEmail().toString()+"'," +
                    "'"+ HashPass.md5(user.getmPassword())+"'";
            PreparedStatement pst=con.prepareStatement(sql);
            ResultSet rs=pst.executeQuery();
            if(rs.next()){
                user1=new User();
                user1.setId(rs.getInt("Id"));
                user1.setmName(rs.getString("Name"));
                user1.setmAddress(rs.getString("Address"));
                user1.setmPhone(rs.getString("Phone"));
                user1.setmImage(rs.getString("Image"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user1;
    }
    public boolean checkExistUser(String email){
        boolean success=false;
        try {
            String sql="Exec Sp_CheckExistUser '"+email+"'";
            PreparedStatement pst=con.prepareStatement(sql);
            ResultSet rs=pst.executeQuery();
            if(rs.next()){
                success=true;
            }else {
                success=false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }
}
