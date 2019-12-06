package com.hcmunre.apporderfoodclient.models.Database;

import com.hcmunre.apporderfoodclient.models.Entity.Shipper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ShipperData {
    Connection con;
    DataConnetion dataConnetion=new DataConnetion();

    public ShipperData() {
        con=dataConnetion.connectionData();
    }
    public Shipper getInforShipperOrder(int orderId){
        Shipper shipper = null;
        try {
            String sql="Exec Sp_SelectInforShipper '"+orderId+"'";
            PreparedStatement pst=con.prepareStatement(sql);
            ResultSet rs=pst.executeQuery();
            if(rs.next()){
                shipper=new Shipper();
                shipper.setShippingStatus(rs.getInt("ShippingStatus"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  shipper;
    }
    public Shipper getInforShipper(int orderId,int status){
        Shipper shipper = null;
        try {
            String sql="Exec Sp_SelectShipperOrder '"+orderId+"','"+status+"'";
            PreparedStatement pst=con.prepareStatement(sql);
            ResultSet rs=pst.executeQuery();
            if(rs.next()){
                shipper=new Shipper();
                shipper.setName(rs.getString("Name"));
                shipper.setAddress(rs.getString("Address"));
                shipper.setImage(rs.getString("Image"));
                shipper.setLat(rs.getDouble("Lat"));
                shipper.setLng(rs.getDouble("Lng"));
                shipper.setPhone(rs.getString("Phone"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  shipper;
    }
}
