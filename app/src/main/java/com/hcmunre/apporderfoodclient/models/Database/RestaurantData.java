package com.hcmunre.apporderfoodclient.models.Database;

import com.hcmunre.apporderfoodclient.interfaces.LoadingProgress;
import com.hcmunre.apporderfoodclient.models.Entity.Category;
import com.hcmunre.apporderfoodclient.models.Entity.ChatMessage;
import com.hcmunre.apporderfoodclient.models.Entity.Restaurant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RestaurantData {
    Connection con;
    DataConnetion dataCon = new DataConnetion();
    PreparedStatement pst;
    ResultSet rs;
    LoadingProgress loadingProgress;

    //phương thức để lấy dữ liệu
    public ArrayList<Restaurant> getRestaurant(Integer cateId) throws SQLException {
        ArrayList<Restaurant> listRestaurant = new ArrayList();
        String sql = "Exec Sp_SelectRestaurant '"+cateId+"'";
        con = dataCon.connectionData();
        pst = con.prepareStatement(sql);
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
            restaurant.setUserOwner(rs.getString("UserOwner"));
            restaurant.setOpening(rs.getTime("Opening"));
            restaurant.setClosing(rs.getTime("Closing"));
            listRestaurant.add(restaurant);
        }
        return listRestaurant;

    }

    public ArrayList<Restaurant> SearchFoodRes(String Search) {
        ArrayList<Restaurant> restaurants = new ArrayList<>();//tạo mảng đề lưu khach hàng
        try {
            String sql = "EXEC Sp_SearchRestaurant '" + Search + "'";
            con = dataCon.connectionData();
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
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
                restaurant.setUserOwner(rs.getString("UserOwner"));
                restaurant.setOpening(rs.getTime("Opening"));
                restaurant.setClosing(rs.getTime("Closing"));
                restaurants.add(restaurant);
            }
        } catch (SQLException e) {
        }
        return restaurants;
    }

    //cửa hàng gần nhất
    public ArrayList<Restaurant> getNearbyRestaurant(Restaurant restaurant) {
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        try {
            String sql = "Exec Sp_SelectDistanceRestaurant '" + restaurant.getmLat() + "','" + restaurant.getmLng() + "','" + restaurant.getDistance() + "'";
            con = dataCon.connectionData();
            PreparedStatement pst = con.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                restaurant = new Restaurant();
                restaurant.setmId(rs.getInt("Id"));
                restaurant.setmName(rs.getString("Name"));
                restaurant.setmAddress(rs.getString("Address"));
                restaurant.setmPhone(rs.getString("Phone"));
                restaurant.setmLat(rs.getDouble("Lat"));
                restaurant.setmLng(rs.getDouble("Lng"));
                restaurant.setmImage(rs.getString("Image"));
                restaurant.setUserOwner(rs.getString("UserOwner"));
                restaurant.setOpening(rs.getTime("Opening"));
                restaurant.setClosing(rs.getTime("Closing"));
                restaurant.setDistance(rs.getDouble("distance_in_km"));
                restaurants.add(restaurant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return restaurants;
    }

    public ArrayList<Restaurant> getRestaurantById(String id) {
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        try {
            String sql = "Exec Sp_SelectRestaurantbyId '" + Integer.parseInt(id) + "'";
            con = dataCon.connectionData();
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            Restaurant restaurant;
            if (rs.next()) {
                restaurant = new Restaurant();
                restaurant.setmId(rs.getInt("Id"));
                restaurant.setmName(rs.getString("Name"));
                restaurant.setmAddress(rs.getString("Address"));
                restaurant.setmPhone(rs.getString("Phone"));
                restaurant.setmLat(rs.getDouble("Lat"));
                restaurant.setmLng(rs.getDouble("Lng"));
                restaurant.setmImage(rs.getString("Image"));
                restaurant.setUserOwner(rs.getString("UserOwner"));
                restaurant.setOpening(rs.getTime("Opening"));
                restaurant.setClosing(rs.getTime("Closing"));
                restaurants.add(restaurant);
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return restaurants;
    }
    public ArrayList<Category> getCategory(){
        ArrayList<Category> categories=new ArrayList<>();
        try {
            String sql="Exec Sp_SelectCategory";
            con=dataCon.connectionData();
            PreparedStatement pst=con.prepareStatement(sql);
            ResultSet rs=pst.executeQuery();
            while (rs.next()){
                Category category=new Category();
                category.setId(rs.getInt("Id"));
                category.setName(rs.getString("Name"));
                category.setImage(rs.getString("Image"));
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
    public ChatMessage getReceiver(String email){
        ChatMessage chatMessage = null;
        try {
            String sql="Exec Sp_SelectReceiverStaff '"+email+"'";
            con=dataCon.connectionData();
            PreparedStatement pst=con.prepareStatement(sql);
            ResultSet rs=pst.executeQuery();
            if(rs.next()){
                chatMessage=new ChatMessage();
                chatMessage.setRestaurantId(rs.getString("NameOwner"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chatMessage;
    }
}
