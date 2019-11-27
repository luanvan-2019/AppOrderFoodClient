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
    public Shipper getInforShipper(int orderId){
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
}
