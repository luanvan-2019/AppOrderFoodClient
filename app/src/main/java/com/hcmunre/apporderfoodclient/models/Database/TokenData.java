package com.hcmunre.apporderfoodclient.models.Database;

import com.hcmunre.apporderfoodclient.models.Entity.Token;
import com.hcmunre.apporderfoodclient.models.Entity.TokenModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import io.reactivex.Observable;

public class TokenData {
    Connection con;
    DataConnetion dataConnetion=new DataConnetion();
    ResultSet rs;
    PreparedStatement pst;
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
    public ArrayList<Token> updateTokenToServer1(String fbid, String token){
        ArrayList<Token> tokens=new ArrayList<>();
        try {
            String sql="Exec Sp_InsertToken '"+fbid+"','"+token+"'";
            con=dataConnetion.connectionData();
            pst=con.prepareStatement(sql);
            rs=pst.executeQuery();
            Token token1;
            if(rs.next()){
                token1=new Token();
                token1.setFBID(rs.getString("FBID"));
                token1.setToKen(rs.getString("Token"));
                tokens.add(token1);
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tokens;
    }
}
