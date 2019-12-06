package com.hcmunre.apporderfoodclient.views.fragments;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.interfaces.ShowFragment;
import com.hcmunre.apporderfoodclient.models.Database.RestaurantData;
import com.hcmunre.apporderfoodclient.models.Entity.HomePageModel;
import com.hcmunre.apporderfoodclient.models.Entity.Order;
import com.hcmunre.apporderfoodclient.models.Entity.Restaurant;
import com.hcmunre.apporderfoodclient.models.Entity.Slider;
import com.hcmunre.apporderfoodclient.models.eventbus.AddressEvent;
import com.hcmunre.apporderfoodclient.models.eventbus.FoodListEvent;
import com.hcmunre.apporderfoodclient.views.activities.GetCurrentUser;
import com.hcmunre.apporderfoodclient.views.adapters.HomePageAdapter;
import com.hcmunre.apporderfoodclient.views.adapters.RestaurantAdapter;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends Fragment {
    @BindView(R.id.layout_search)
    LinearLayout layout_search;
    @BindView(R.id.txtsearch)
    TextView txtsearch;
    @BindView(R.id.recyc_slider)
    RecyclerView recyc_slider;
    @BindView(R.id.txt_address)
    TextView txt_address;
    TextView txtCountRestaurant;
    Unbinder unbinder;
    private ArrayList<Slider> sliderListModels;
    private RestaurantAdapter restaurantSearch;
    Dialog dialog;
    RecyclerView recyc_search;
    ProgressBar progress_search;
    RestaurantData restaurantData = new RestaurantData();
    String address;
    AVLoadingIndicatorView av;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,
                container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        fragment();
        searchRestaurant();
        return view;
    }

    private void init() {
        //category restaurant
        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyc_slider.setLayoutManager(testingLayoutManager);
        ArrayList<HomePageModel> homePageModels = new ArrayList<>();
        sliderListModels = new ArrayList<>();
        sliderListModels.add(new Slider(R.drawable.ic_eat));
        sliderListModels.add(new Slider(R.drawable.ic_drink));
        sliderListModels.add(new Slider(R.drawable.ic_eat));
        sliderListModels.add(new Slider(R.drawable.ic_eat));
        //List menu
        homePageModels.add(new HomePageModel(0, sliderListModels));
        HomePageAdapter homePageAdapter = new HomePageAdapter(homePageModels);
        recyc_slider.setAdapter(homePageAdapter);
        homePageAdapter.notifyDataSetChanged();
        //
        GetCurrentUser getCurrentUser=new GetCurrentUser(getActivity());
        address=getCurrentUser.getuser();
        EventBus.getDefault().postSticky(new AddressEvent(address));
//        Common.curentOrder=new Order();
//        Common.curentOrder.setOrderAddress(address);
        txt_address.setText(address);
    }
    private void fragment(){
        CategoryFragment categoryFragment =new CategoryFragment();
        RestaurantFragment restaurantFragment =new RestaurantFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_a, categoryFragment)
                .replace(R.id.container_b, restaurantFragment)
                .commit();
    }
    private void searchRestaurant() {
        txtsearch.setOnClickListener(view -> {
            dialog = new Dialog(getActivity(), R.style.CustomDialogAnimation);
            dialog.setContentView(R.layout.dialog_search);
            Window window = dialog.getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            final WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.gravity = Gravity.BOTTOM;
            dialog.getWindow().setAttributes(layoutParams);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
            TextView mClose = dialog.findViewById(R.id.txtCancel);
            av=dialog.findViewById(R.id.progress_bar);
            mClose.setOnClickListener(view1 -> dialog.dismiss());
            EditText editSearch = dialog.findViewById(R.id.editSearch);
            progress_search = dialog.findViewById(R.id.progress);
            recyc_search = dialog.findViewById(R.id.recyc_search);
            txtCountRestaurant = dialog.findViewById(R.id.txtCountRestaurant);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyc_search.setLayoutManager(layoutManager);
            recyc_search.setHasFixedSize(true);
            av.hide();
            editSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    new filter(editable.toString()).execute();

                }


            });
        });


    }

    public class filter extends AsyncTask<String,String,ArrayList<Restaurant>>{
        String search;

        public filter(String search) {
            this.search = search;
        }

        @Override
        protected void onPreExecute() {
            av.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<Restaurant> restaurants) {
            super.onPostExecute(restaurants);
            restaurantSearch = new RestaurantAdapter(getActivity(), restaurants);
            restaurantSearch.notifyDataSetChanged();
            recyc_search.setAdapter(restaurantSearch);
            txtCountRestaurant.setText(restaurantSearch.getItemCount() + "");
            progress_search.setVisibility(View.GONE);
            av.hide();
        }

        @Override
        protected ArrayList<Restaurant> doInBackground(String... strings) {
            ArrayList<Restaurant> restaurants;
            restaurants=restaurantData.SearchFoodRes(search);
            return restaurants;
        }
    }

}
