package com.hcmunre.apporderfoodclient.models.Database;

import com.hcmunre.apporderfoodclient.models.Entity.Restaurant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RestaurantData {
    Connection con;
    DataConnetion dataCon=new DataConnetion();
    PreparedStatement pst;
    ResultSet rs;
    //phương thức để lấy dữ liệu
    public ArrayList<Restaurant> getRestaurant() throws SQLException {
        ArrayList<Restaurant> listRestaurant=new ArrayList();
        String sql="Exec Sp_SelectRestaurant";
        con=dataCon.connectionData();
        pst=con.prepareStatement(sql);
        rs=pst.executeQuery();
        Restaurant restaurant;
        while(rs.next()){
            restaurant=new Restaurant();
            restaurant.setmId(rs.getInt("Id"));
            restaurant.setmName(rs.getString("Name"));
            restaurant.setmAddress(rs.getString("Address"));
            restaurant.setmPhone(rs.getString("Phone"));
            restaurant.setmLat(rs.getFloat("Lat"));
            restaurant.setmLng(rs.getFloat("Lng"));
            restaurant.setmImage(rs.getString("Image"));
            restaurant.setOpening_Closing_Time(rs.getTime("Opening_Closing_Time"));
            restaurant.setUserOwner(rs.getString("UserOwner"));
            listRestaurant.add(restaurant);
        }
        return listRestaurant;
    }
    public ArrayList<Restaurant> SearchFoodRes(String Search) {
        ArrayList<Restaurant> restaurants = new ArrayList<>();//tạo mảng đề lưu khach hàng
        try {
            String sql = "EXEC Sp_SearchRestaurant '"+Search+"'";
            con=dataCon.connectionData();
            pst=con.prepareStatement(sql);
            rs = pst.executeQuery();
            Restaurant restaurant;
            while (rs.next()) {
                restaurant = new Restaurant();
                restaurant.setmId(rs.getInt("Id"));
                restaurant.setmName(rs.getString("Name"));
                restaurant.setmAddress(rs.getString("Address"));
                restaurant.setmPhone(rs.getString("Phone"));
                restaurant.setmLat(rs.getFloat("Lat"));
                restaurant.setmLng(rs.getFloat("Lng"));
                restaurant.setmImage(rs.getString("Image"));
                restaurant.setOpening_Closing_Time(rs.getTime("Opening_Closing_Time"));
                restaurant.setUserOwner(rs.getString("UserOwner"));
                restaurants.add(restaurant);
            }
        } catch (SQLException e) {
        }
        return restaurants;
    }
    //server localhost

}
