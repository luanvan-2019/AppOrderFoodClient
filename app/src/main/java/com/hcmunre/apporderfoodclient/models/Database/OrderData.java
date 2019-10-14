package com.hcmunre.apporderfoodclient.models.Database;

import com.hcmunre.apporderfoodclient.models.Entity.Order;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderData {
    Connection con;
    DataConnetion dataConnetion=new DataConnetion();
    PreparedStatement pst;
    CallableStatement callable;
    ResultSet rs;
    public int insertOrder(Order order){
        int res=0;
        try {
            String sql = "{call Sp_InsertOrder (?,?,?,?,?,?,?,?)}";
            con = dataConnetion.connectionData();
            callable = con.prepareCall(sql);
            callable.setInt(1,order.getUserId());
            callable.setInt(2, order.getRestaurantId());
            callable.setString(3,order.getOrderName());
            callable.setString(4,order.getOrderPhone());
            callable.setString(5,order.getOrderAddress());
            callable.setDate(6,order.getOrderDate());
            callable.setFloat(7,order.getTotalPrice());
            callable.setInt(8,order.getNumberOfItem());
            res = callable.executeUpdate();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
}
