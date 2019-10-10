package com.hcmunre.apporderfoodclient.models.Entity;

import java.util.ArrayList;

public class HomePageModel {
    public static final int SLIDER_TYPE = 0;
    public static final int LISTMENU_TYPE=1;
    private int type;
    private ArrayList<Slider> homePageModelList;

    public HomePageModel(int type, ArrayList<Slider> homePageModelList) {
        this.type = type;
        this.homePageModelList = homePageModelList;
    }
    /////////////Slider header
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ArrayList<Slider> gethomePageModelList() {
        return homePageModelList;
    }

    public void sethomePageModelList(ArrayList<Slider> homePageModelList) {
        this.homePageModelList = homePageModelList;
    }
    /////////////Slider header

    ////////////ADD List Menu horizontal
    private String title;
    private ArrayList<ListMenu> listMenus;

    public HomePageModel(int type, String title, ArrayList<ListMenu> listMenus) {
        this.type = type;
        this.title = title;
        this.listMenus = listMenus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<ListMenu> getListMenus() {
        return listMenus;
    }

    public void setListMenus(ArrayList<ListMenu> listMenus) {
        this.listMenus = listMenus;
    }
    ////////////ADD List Menu horizontal
}
