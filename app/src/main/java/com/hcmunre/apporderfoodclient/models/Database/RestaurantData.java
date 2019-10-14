package com.hcmunre.apporderfoodclient.models.Database;

import com.hcmunre.apporderfoodclient.interfaces.LoadingProgress;
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
    LoadingProgress loadingProgress;

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
            restaurant.setmLat(rs.getDouble("Lat"));
            restaurant.setmLng(rs.getDouble("Lng"));
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
                restaurant.setmLat(rs.getDouble("Lat"));
                restaurant.setmLng(rs.getDouble("Lng"));
                restaurant.setmImage(rs.getString("Image"));
                restaurant.setOpening_Closing_Time(rs.getTime("Opening_Closing_Time"));
                restaurant.setUserOwner(rs.getString("UserOwner"));
                restaurants.add(restaurant);
            }
        } catch (SQLException e) {
        }
        return restaurants;
    }
    //cửa hàng gần nhất
    public ArrayList<Restaurant> getNearbyRestaurant(Restaurant restaurant,int distance){
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        try {
            String sql="Exec Sp_SelectDistanceRes '"+restaurant.getmLat()+"','"+restaurant.getmLng()+"','"+distance+"'";
            con=dataCon.connectionData();
            pst=con.prepareStatement(sql);
            rs=pst.executeQuery();
            while (rs.next()) {
                restaurant = new Restaurant();
                restaurant.setmId(rs.getInt("Id"));
                restaurant.setmName(rs.getString("Name"));
                restaurant.setmAddress(rs.getString("Address"));
                restaurant.setmPhone(rs.getString("Phone"));
                restaurant.setmLat(rs.getDouble("Lat"));
                restaurant.setmLng(rs.getDouble("Lng"));
                restaurant.setmImage(rs.getString("Image"));
                restaurant.setOpening_Closing_Time(rs.getTime("Opening_Closing_Time"));
                restaurant.setUserOwner(rs.getString("UserOwner"));
                restaurants.add(restaurant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return restaurants;
    }

}
