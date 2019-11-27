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

}
