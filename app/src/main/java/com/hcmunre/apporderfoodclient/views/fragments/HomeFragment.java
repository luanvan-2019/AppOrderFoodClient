package com.hcmunre.apporderfoodclient.views.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmunre.apporderfoodclient.views.activities.NearbyRestaurantActivity;
import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.models.Database.RestaurantData;
import com.hcmunre.apporderfoodclient.models.Entity.HomePageModel;
import com.hcmunre.apporderfoodclient.models.Entity.ListMenu;
import com.hcmunre.apporderfoodclient.models.Entity.Restaurant;
import com.hcmunre.apporderfoodclient.models.Entity.Slider;
import com.hcmunre.apporderfoodclient.viewmodels.RestaurantViewModel;
import com.hcmunre.apporderfoodclient.views.adapters.HomePageAdapter;
import com.hcmunre.apporderfoodclient.views.adapters.ListMenuAdapter;
import com.hcmunre.apporderfoodclient.views.adapters.RestaurantAdapter;

import java.sql.SQLException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HomeFragment extends Fragment{
    @BindView(R.id.layout_search)
    LinearLayout layout_search;
    @BindView(R.id.txtsearch)
    TextView txtsearch;
    @BindView(R.id.txtNearbyRes)
    TextView txtNearbyRes;
    @BindView(R.id.recyc_listmenu)
    RecyclerView recyc_listmenu;
    @BindView(R.id.recyc_ListRestaurant)
    RecyclerView recyc_listRestaurant;
    @BindView(R.id.testing)
    RecyclerView  testing;
    @BindView(R.id.progress_loading)
    ProgressBar progress_loading;
    TextView  txtCountRestaurant;
    Unbinder unbinder;
    ListMenuAdapter listMenuAdapter;
    private ArrayList<Slider> sliderListModels;
    private ArrayList<ListMenu> listMenuArrayList;
    private ArrayList<Restaurant> restaurantArrayList;
    private RestaurantAdapter restaurantAdapter, restaurantSearch;
    private RestaurantViewModel mViewModel;
    String[] mName = {"Đồ ăn", "Đồ Uống", "Đô chay", "Pizza", "Món Lẩu", "Món Gà", "Món hấp"};
    Integer[] mImage = {R.drawable.ic_eat, R.drawable.ic_dishs, R.drawable.ic_eat, R.drawable.ic_eat, R.drawable.ic_eat, R.drawable.ic_eat, R.drawable.ic_eat};
    Dialog dialog;
    RecyclerView recyc_search;
    CompositeDisposable compositeDisposable;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,
                container, false);
        unbinder= ButterKnife.bind(this,view);
        searchRestaurant();
        LinearLayoutManager testingLayoutManager = new LinearLayoutManager(getContext());
        testingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        testing.setLayoutManager(testingLayoutManager);
        ArrayList<HomePageModel> homePageModels = new ArrayList<>();
        sliderListModels = new ArrayList<>();
        sliderListModels.add(new Slider(R.drawable.ic_eat));
        sliderListModels.add(new Slider(R.drawable.ic_drink));
        sliderListModels.add(new Slider(R.drawable.ic_eat));
        sliderListModels.add(new Slider(R.drawable.ic_eat));
        //List menu
        homePageModels.add(new HomePageModel(0, sliderListModels));
        HomePageAdapter homePageAdapter = new HomePageAdapter(homePageModels);
        testing.setAdapter(homePageAdapter);
        homePageAdapter.notifyDataSetChanged();
        getListMenuAdapter();
        try {
            getListRestaurant();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        init();
        return view;
    }

    private void getListMenuAdapter() {
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyc_listmenu.setLayoutManager(layoutManager);
        recyc_listmenu.setItemAnimator(new DefaultItemAnimator());
        listMenuArrayList = new ArrayList<>();
        for (int i = 0; i < mImage.length; i++) {
            ListMenu listMenus = new ListMenu();
            listMenus.setmName(mName[i]);
            listMenus.setmImage(mImage[i]);
            listMenuArrayList.add(listMenus);
        }
        listMenuAdapter = new ListMenuAdapter(getActivity(), listMenuArrayList);
        recyc_listmenu.setAdapter(listMenuAdapter);
    }
    private void init(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyc_listRestaurant.setLayoutManager(layoutManager);
        recyc_listRestaurant.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyc_listRestaurant.getContext(), layoutManager.getOrientation());
        recyc_listRestaurant.addItemDecoration(dividerItemDecoration);
        txtNearbyRes.setOnClickListener(view -> {
                startActivity(new Intent(getActivity(), NearbyRestaurantActivity.class));
        });
    }
    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    private void getListRestaurant() throws SQLException {
        RestaurantData rd=new RestaurantData();
        if(Common.isConnectedToInternet(getContext())){
            progress_loading.setVisibility(View.VISIBLE);
            compositeDisposable=new CompositeDisposable();
            final Observable<ArrayList<Restaurant>> listRestaurant = Observable.just(rd.getRestaurant());
            compositeDisposable.add(
                    listRestaurant
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(restaurant->{
                        restaurantAdapter=new RestaurantAdapter(getActivity(),restaurant);
                        recyc_listRestaurant.setAdapter(restaurantAdapter);
                        progress_loading.setVisibility(View.GONE);
                    },throwable -> {
                        Toast.makeText(getActivity(), "Lỗi"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }));
        }

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
            mClose.setOnClickListener(view1 -> dialog.dismiss());
            EditText editSearch = dialog.findViewById(R.id.editSearch);
            recyc_search = dialog.findViewById(R.id.recyc_search);
            txtCountRestaurant = dialog.findViewById(R.id.txtCountRestaurant);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyc_search.setLayoutManager(layoutManager);
            recyc_search.setHasFixedSize(true);
            editSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    filter(editable.toString());

                }


            });
        });


    }

    @SuppressLint("CheckResult")
    private void filter(String search) {
        RestaurantData restaurantData = new RestaurantData();
        Observable<ArrayList<Restaurant>> listObserable = Observable.just(restaurantData.SearchFoodRes(search));
        listObserable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(restaurants -> {
                    restaurantSearch = new RestaurantAdapter(getActivity(), restaurants);
                    restaurantSearch.notifyDataSetChanged();
                    recyc_search.setAdapter(restaurantSearch);
                    txtCountRestaurant.setText(restaurantSearch.getItemCount() + "");
                });

    }

}
