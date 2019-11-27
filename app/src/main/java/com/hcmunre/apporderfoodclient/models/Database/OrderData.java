package com.hcmunre.apporderfoodclient.models.Database;

import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.models.Entity.Order;
import com.hcmunre.apporderfoodclient.models.Entity.OrderDetail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderData {
    Connection con;
    DataConnetion dataConnetion=new DataConnetion();
    PreparedStatement pst;
    public boolean insertOrder(Order order){
        boolean success=false;
        try {
            String sql = "Exec Sp_InsertOrder (?,?,?,?,?,?,?,?,?,?)";
            con = dataConnetion.connectionData();
            pst = con.prepareCall(sql);
            pst.setInt(1,order.getUserId());
            pst.setInt(2, order.getRestaurantId());
            pst.setString(3,order.getOrderName());
            pst.setString(4,order.getOrderPhone());
            pst.setString(5,order.getOrderAddress());
            pst.setInt(6,order.getOrderStatus());
            pst.setString(7,order.getOrderDate());
            pst.setFloat(8,order.getTotalPrice());
            pst.setInt(9,order.getNumberOfItem());
            pst.setInt(10,order.getPayment());
            ResultSet rs=pst.executeQuery();
            if(rs.next()){
                Common.curentOrder=new Order();
                Common.curentOrder.setId(rs.getInt("OrderNumber"));
                Common.curentOrder.setOrderName("OrderName");
                con.close();
                success=true;
            } else{
                con.close();
                success=false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }
    //insert multi order
    public void insertOrderDetail(List<OrderDetail> listOrder){
        try {
            String sql = "Exec Sp_InsertOrderDetail (?,?,?,?)";
            con = dataConnetion.connectionData();
            PreparedStatement pst = con.prepareCall(sql);
            for(OrderDetail orderDetail: listOrder){
                pst.setInt(1,orderDetail.getOrderId());
                pst.setInt(2, orderDetail.getFoodId());
                pst.setInt(3,orderDetail.getQuantity());
                pst.setFloat(4,orderDetail.getPrice());
                pst.addBatch();
            }
            pst.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public ArrayList<Order> getAllOrder(int userId){
        ArrayList<Order> listOrders=new ArrayList<>();
        try {
            String sql="Exec Sp_SelectOrder '"+userId+"'";
            con=dataConnetion.connectionData();
            PreparedStatement pst=con.prepareStatement(sql);
            ResultSet rs=pst.executeQuery();
            Order order1;
            while(rs.next()){//phải sử dụng while vì dùng if thì chỉ thực hiện 1 lần
                order1=new Order();
                order1.setId(rs.getInt("OrderId"));
                order1.setUserId(rs.getInt("UserId"));
                order1.setRestaurantId(rs.getInt("RestaurantId"));
                order1.setNameRestaurant(rs.getString("Name"));
                order1.setImage(rs.getString("Image"));
                order1.setOrderName(rs.getString("OrderName"));
                order1.setOrderPhone(rs.getString("OrderPhone"));
                order1.setOrderAddress(rs.getString("OrderAddress"));
                order1.setOrderStatus(rs.getInt("OrderStatus"));
                order1.setOrderDate(rs.getString("OrderDate"));
                order1.setTotalPrice(rs.getFloat("TotalPrice"));
                order1.setNumberOfItem(rs.getInt("NumberOfItem"));
                listOrders.add(order1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOrders;
    }
}
