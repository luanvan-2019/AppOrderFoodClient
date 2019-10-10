package com.hcmunre.apporderfoodclient.views.adapters;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.models.Entity.HomePageModel;
import com.hcmunre.apporderfoodclient.models.Entity.Slider;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class HomePageAdapter extends RecyclerView.Adapter {
    private ArrayList<HomePageModel> homePageModelList;
    public HomePageAdapter(ArrayList<HomePageModel> homePageModelList) {
        this.homePageModelList = homePageModelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (homePageModelList.get(position).getType()) {
            case 0:
                return HomePageModel.SLIDER_TYPE;
            case 1:
                return HomePageModel.LISTMENU_TYPE;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case HomePageModel.SLIDER_TYPE:
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.slider_viewpager, parent, false);
                return new BannerSliderViewHolder(view);
//            case HomePageModel.LISTMENU_TYPE:
//                View viewListMenu = LayoutInflater.from(parent.getContext())
//                        .inflate(R.layout.recyclerview_listmenu, parent, false);
//                return new ListMenuViewHolder(viewListMenu);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (homePageModelList.get(position).getType()) {
            case HomePageModel.SLIDER_TYPE:
                ArrayList<Slider> sliderArrayList = homePageModelList.get(position).gethomePageModelList();
                ((BannerSliderViewHolder) holder).setBannderSlider(sliderArrayList);
                break;
//            case HomePageModel.LISTMENU_TYPE:
//                String title=homePageModelList.get(position).getTitle();
//                ArrayList<ListMenu> listMenus = homePageModelList.get(position).getListMenus();
//                ((ListMenuViewHolder)holder).setRecycListMenu(listMenus,title);
//                break;
            default:
                return;
        }
    }


    @Override
    public int getItemCount() {
        return homePageModelList.size();
    }


    public class ListMenuViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyc_listmenu;
        private TextView txtListMenu;
        public ListMenuViewHolder(@NonNull View itemView) {
            super(itemView);
            recyc_listmenu = itemView.findViewById(R.id.recyc_listmenu);
            txtListMenu=itemView.findViewById(R.id.txtListMenu);
        }

//        private void setRecycListMenu(ArrayList<ListMenu> listMenus,String title) {
//            txtListMenu.setText(title);
//            ListMenuAdapter listMenuAdapter=new ListMenuAdapter(itemView.getContext(),listMenus);
//            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(itemView.getContext());
//            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//            recyc_listmenu.setLayoutManager(linearLayoutManager);
//            recyc_listmenu.setAdapter(listMenuAdapter);
//            listMenuAdapter.notifyDataSetChanged();
//        }
    }

    public class BannerSliderViewHolder extends RecyclerView.ViewHolder {
        private int currentPage = 2;
        private Timer timer;
        final private long DELAY_TIME = 2000;
        final private long PERIOD_TIME = 2000;
        private ViewPager sliderViewpager;

        public BannerSliderViewHolder(@NonNull View itemView) {
            super(itemView);
            sliderViewpager = itemView.findViewById(R.id.viewpager_slider_testing);
        }

        private void setBannderSlider(final ArrayList<Slider> sliderListModels) {
            SliderAdapter sliderAdapter = new SliderAdapter(sliderListModels);
            sliderViewpager.setAdapter(sliderAdapter);
            sliderViewpager.setClipToPadding(false);
            sliderViewpager.setPageMargin(20);
            ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    currentPage = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == ViewPager.SCROLL_STATE_IDLE) {
                        pageLooger(sliderListModels);
                    }
                }
            };
            sliderViewpager.addOnPageChangeListener(onPageChangeListener);
            startBannerSliderShow(sliderListModels);
            sliderViewpager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    pageLooger(sliderListModels);
                    stopBannerSliderShow();
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        startBannerSliderShow(sliderListModels);
                    }
                    return false;
                }
            });
        }

        private void pageLooger(ArrayList<Slider> sliderListModels) {
            if (currentPage == sliderListModels.size() - 2) {
                currentPage = 2;
                sliderViewpager.setCurrentItem(currentPage, false);
            } else if (currentPage == 1) {
                currentPage = sliderListModels.size() - 3;
                sliderViewpager.setCurrentItem(currentPage, false);
            }
        }

        private void startBannerSliderShow(ArrayList<Slider> sliderListModels) {
            final Handler handler = new Handler();
            final Runnable update = new Runnable() {
                @Override
                public void run() {
                    if (currentPage >= sliderListModels.size()) {
                        currentPage = 1;
                    }
                    sliderViewpager.setCurrentItem(currentPage++, true);
                }
            };
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(update);
                }
            }, DELAY_TIME, PERIOD_TIME);

        }

        private void stopBannerSliderShow() {
            timer.cancel();
        }
    }

}
