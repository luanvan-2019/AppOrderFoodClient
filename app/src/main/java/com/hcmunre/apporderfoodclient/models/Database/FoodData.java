package com.hcmunre.apporderfoodclient.models.Database;

import com.hcmunre.apporderfoodclient.models.Entity.Food;
import com.hcmunre.apporderfoodclient.models.Entity.Menu;

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
    public ArrayList<Menu> getMenuFood(int restaurantId){
        ArrayList<Menu> listMenuFood=new ArrayList();
        try {
            String sql="Exec Sp_SelectMenu '"+restaurantId+"'";
            con=dataConnetion.connectionData();
            pst=con.prepareStatement(sql);
            rs=pst.executeQuery();
            Menu menu;
            while (rs.next()){
                menu=new Menu();
                menu.setmId(rs.getInt("Id"));
                menu.setmName(rs.getString("Name"));
                listMenuFood.add(menu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listMenuFood;
    }

    public ArrayList<Food> getFoodOfMenu(int menuId) throws SQLException {
        ArrayList<Food> listFoodOfMenu=new ArrayList<>();
        String sql="Exec Sp_SelectFood '"+menuId+"'";
        con=dataConnetion.connectionData();
        pst=con.prepareStatement(sql);
        rs=pst.executeQuery();
        Food food;
        while(rs.next()){
            food=new Food();
            food.setName(rs.getString("Name"));
            food.setPrice(rs.getFloat("Price"));
            listFoodOfMenu.add(food);
        }
        return listFoodOfMenu;
    }
    //server localhost
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
                listMenuFood.add(menu);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listMenuFood;
    }

}