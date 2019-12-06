package com.hcmunre.apporderfoodclient.models.Database;

import com.hcmunre.apporderfoodclient.models.Entity.Favorite;
import com.hcmunre.apporderfoodclient.models.Entity.Food;
import com.hcmunre.apporderfoodclient.models.Entity.Menu;
import com.hcmunre.apporderfoodclient.models.Entity.FavoriteOnlyId;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FoodData {
    DataConnetion dataConnetion=new DataConnetion();
    Connection con;
    ResultSet rs;
    PreparedStatement pst;

    public ArrayList<Food> getFoodOfMenu(int menuId) throws SQLException {
        ArrayList<Food> listFoodOfMenu=new ArrayList<>();
        String sql="Exec Sp_SelectFood '"+menuId+"'";
        con=dataConnetion.connectionData();
        pst=con.prepareStatement(sql);
        rs=pst.executeQuery();
        Food food;
        while(rs.next()){
            food=new Food();
            food.setId(rs.getInt("Id"));
            food.setName(rs.getString("Name"));
            food.setImage(rs.getString("Image"));
            food.setDescription(rs.getString("Description"));
            food.setPrice(rs.getFloat("Price"));
            food.setStatus(rs.getInt("Status"));
            listFoodOfMenu.add(food);
        }
        return listFoodOfMenu;
    }
    public ArrayList<Menu> getMenuResFood(int restaurantId){
        ArrayList<Menu> listMenuFood=new ArrayList();
        try {
            String sql="Exec Sp_SelectMenuRes '"+restaurantId+"'";
            con=dataConnetion.connectionData();
            pst=con.prepareStatement(sql);
            rs=pst.executeQuery();
            Menu menu;
            while (rs.next()){
                menu=new Menu();
                menu.setmId(rs.getInt("Id"));
                menu.setmName(rs.getString("Name"));
                menu.setmImage(rs.getString("Image"));
                listMenuFood.add(menu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listMenuFood;
    }
    public ArrayList<Favorite> getFavoriteByUser(int userId){
        ArrayList<Favorite> favorites=new ArrayList<>();
        try {
            String sql="Exec Sp_SelectFavoriteFood '"+userId+"'";
            con=dataConnetion.connectionData();
            PreparedStatement pst=con.prepareStatement(sql);
            ResultSet rs=pst.executeQuery();
            Favorite favorite;
            while(rs.next()){
                favorite=new Favorite();
                favorite.setUserId(rs.getInt("UserId"));
                favorite.setRestaurantId(rs.getInt("RestaurantId"));
                favorite.setFoodId(rs.getInt("FoodId"));
                favorite.setFoodName(rs.getString("FoodName"));
                favorite.setRestaurantName(rs.getString("RestaurantName"));
                favorite.setFoodImage(rs.getString("FoodImage"));
                favorite.setPrice(rs.getFloat("Price"));
                favorites.add(favorite);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return favorites;
    }
    public ArrayList<FavoriteOnlyId> getFavoriteByRestaurant(int userId,int restaurantId){
        ArrayList<FavoriteOnlyId> favorites=new ArrayList<>();
        try {
            String sql="Exec Sp_SelectFavoriteByRestaurant '"+userId+"','"+restaurantId+"'";
            con=dataConnetion.connectionData();
            PreparedStatement pst=con.prepareStatement(sql);
            ResultSet rs=pst.executeQuery();
            FavoriteOnlyId favorite;
            while(rs.next()){
                favorite=new FavoriteOnlyId(rs.getInt("FoodId"));
                favorites.add(favorite);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return favorites;
    }
    public boolean deleteFavorite(Favorite favorite){
        boolean success=false;
        try {
            String sql="Exec Sp_DeleteFavorite '"+favorite.getUserId()+"','"+favorite.getRestaurantId()+"'," +
                    "'"+favorite.getFoodId()+"'";
            con=dataConnetion.connectionData();
            PreparedStatement pst=con.prepareStatement(sql);
            ResultSet rs=pst.executeQuery();
            if(rs.next()){
                con.close();
                success=true;
            }else {
                con.close();
                success=false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  success;
    }

    public boolean insertFavorite(Favorite favorite) {
        boolean success=false;
        try {
            String sql="{call Sp_InsertFavoriteFood (?,?,?,?,?,?,?)}";
            con=dataConnetion.connectionData();
            PreparedStatement pst=con.prepareCall(sql);
            pst.setInt(1,favorite.getUserId());
            pst.setInt(2,favorite.getFoodId());
            pst.setInt(3,favorite.getRestaurantId());
            pst.setString(4,favorite.getRestaurantName());
            pst.setString(5,favorite.getFoodName());
            pst.setString(6,favorite.getFoodImage());
            pst.setFloat(7,favorite.getPrice());
            ResultSet rs=pst.executeQuery();
            if(rs.next()){
                con.close();
                success=true;
            }else {
                con.close();
                success=false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }
}
