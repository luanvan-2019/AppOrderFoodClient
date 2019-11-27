package com.hcmunre.apporderfoodclient.views.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.models.Database.RestaurantData;
import com.hcmunre.apporderfoodclient.models.Entity.Category;
import com.hcmunre.apporderfoodclient.views.adapters.CategoryAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CategoryFragment extends Fragment {

    @BindView(R.id.recyc_listmenu)
    RecyclerView recyc_listmenu;
    public CategoryFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a,
                container, false);
        ButterKnife.bind(this,view);
        init();

        return view;
    }
    private void init() {
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyc_listmenu.setLayoutManager(layoutManager);
        recyc_listmenu.setItemAnimator(new DefaultItemAnimator());
        if(Common.isConnectedToInternet(getActivity())){
            new getCategory().execute();
        }else {
            Common.showToast(getActivity(),getString(R.string.check_internet));
        }

    }
    public class getCategory extends AsyncTask<String, String, ArrayList<Category>> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(ArrayList<Category> categories) {
            CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(), categories);
            recyc_listmenu.setAdapter(categoryAdapter);
        }

        @Override
        protected ArrayList<Category> doInBackground(String... strings) {
            ArrayList<Category> restaurants ;
            RestaurantData restaurantData = new RestaurantData();
            restaurants = restaurantData.getCategory();
            return restaurants;
        }
    }

}
