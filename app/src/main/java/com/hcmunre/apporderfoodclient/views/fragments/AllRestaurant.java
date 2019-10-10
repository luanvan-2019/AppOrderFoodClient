package com.hcmunre.apporderfoodclient.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hcmunre.apporderfoodclient.R;

public class AllRestaurant extends Fragment {

//
//    private ArrayList<Restaurant> restaurantArrayList;
//    private RestaurantAdapter restaurantAdapter;
//    private RestaurantViewModel mViewModel;
//    @BindView(R.id.recyclerview)
//    RecyclerView recyclerView;
//    @BindView(R.id.imageSlider)
//    SliderLayout sliderLayout;
//
//    @SuppressLint("CheckResult")
//    @Nullable
//    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_allfood, container, false);
        //
//        ButterKnife.bind(this, view);
//        //
//
//        mViewModel = new RestaurantViewModel();
//        mViewModel = ViewModelProviders.of(this).get(RestaurantViewModel.class);
//
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        restaurantArrayList = new ArrayList<>();
//        RestaurantData restaurantData = new RestaurantData();
//        try {
//            restaurantArrayList = restaurantData.getRestaurant();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        restaurantAdapter = new RestaurantAdapter(getActivity());
//        restaurantAdapter.setData(restaurantArrayList);
//        recyclerView.setAdapter(restaurantAdapter);
//        restaurantAdapter.setData(restaurantArrayList);
//        recyclerView.setAdapter(restaurantAdapter);
//        mViewModel.mRestaurant.observe(AllRestaurant.this, restaurant -> {
//            if (restaurant != null){
//                restaurantArrayList = (ArrayList<Restaurant>)restaurant ;
//                restaurantAdapter.setData(restaurantArrayList);
//            }
//
//        });
//        try {
//            mViewModel.getRestaurants();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        creatingObserable();
//        sliderLayout.setScrollTimeInSec(1);
//        setSliderViews();
        return view;


    }

//    private void setSliderViews() {
//        for (int i = 0; i <= 3; i++) {
//
//            DefaultSliderView sliderView = new DefaultSliderView(getContext());
//
//            switch (i) {
//                case 0:
//                    sliderView.setImageUrl("https://images.pexels.com/photos/547114/pexels-photo-547114.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
//                    break;
//                case 1:
//                    sliderView.setImageUrl("https://images.pexels.com/photos/218983/pexels-photo-218983.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
//                    break;
//                case 2:
//                    sliderView.setImageUrl("https://images.pexels.com/photos/747964/pexels-photo-747964.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260");
//                    break;
//                case 3:
//                    sliderView.setImageUrl("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
//                    break;
//            }
//
//            sliderView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
//            sliderView.setDescription("setDescription " + (i + 1));
//            sliderLayout.addSliderView(sliderView);
//        }
//    }
//    private void creatingObserable() {
//        RestaurantData restaurantData = new RestaurantData();
//        try {
//            final Observable<ArrayList<Restaurant>> listObserable = Observable.just(restaurantData.getRestaurant());
//            listObserable.subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Observer<ArrayList<Restaurant>>() {
//                @Override
//                public void onSubscribe(Disposable d) {
//
//                }
//
//                @Override
//                public void onNext(ArrayList<Restaurant> restaurants) {
//                    restaurantAdapter.setData(restaurants);
//                }
//
//                @Override
//                public void onError(Throwable e) {
//                    Log.d("BBB", "onError()",e);
//                }
//
//                @Override
//                public void onComplete() {
//
//                }
//            });
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

}
