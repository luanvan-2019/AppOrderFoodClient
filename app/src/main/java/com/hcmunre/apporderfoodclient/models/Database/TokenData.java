package com.hcmunre.apporderfoodclient.models.Database;

import com.hcmunre.apporderfoodclient.models.Entity.Token;
import com.hcmunre.apporderfoodclient.models.Entity.TokenModel;
import com.hcmunre.apporderfoodclient.models.Entity.User;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;

public class TokenData {
    Connection con;
    DataConnetion dataConnetion=new DataConnetion();
    ResultSet rs;
    PreparedStatement pst;
    CallableStatement callable;
    public void getToken(String fbid){
        try {
            String sql="Exec Sp_SelectToken '"+fbid+"'";
            con=dataConnetion.connectionData();
            pst=con.prepareStatement(sql);
            rs=pst.executeQuery();
            if(rs.next()){
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Observable<TokenModel> updateTokenToServer(String fbid, String token){
        try {
            String sql="Exec Sp_InsertToken '"+fbid+"','"+token+"'";
            con=dataConnetion.connectionData();
            pst=con.prepareStatement(sql);
            rs=pst.executeQuery();
            if(rs.next()){
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updateTokenToServer(fbid,token);
    }
    public int updateTokenToServer1(Token token){
        int res=0;
        try {
            String sql="{call Sp_InsertToken (?,?)}";
            con=dataConnetion.connectionData();
            callable = con.prepareCall(sql);
            callable.setString(1, token.getFBID());
            callable.setString(2, token.getToKen());
            res=callable.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    public ArrayList<User> getUser(String fbid){
        ArrayList<User> users=new ArrayList<>();
        try {
            String sql="Exec Sp_SelectUserFBID '"+fbid+"'";
            con=dataConnetion.connectionData();
            pst=con.prepareStatement(sql);
            rs=pst.executeQuery();
            User user;
            if(rs.next()){
                user=new User();
                user.setFbid(rs.getString("FBID"));
                users.add(user);
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
