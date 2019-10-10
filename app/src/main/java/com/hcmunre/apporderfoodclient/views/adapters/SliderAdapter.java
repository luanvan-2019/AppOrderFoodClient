package com.hcmunre.apporderfoodclient.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.models.Entity.Slider;

import java.util.ArrayList;

public class SliderAdapter extends PagerAdapter {
    private ArrayList<Slider> sliders;

    public SliderAdapter(ArrayList<Slider> sliders) {
        this.sliders = sliders;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view= LayoutInflater.from(container.getContext())
                .inflate(R.layout.slider_layout,container,false);
        ImageView banner=view.findViewById(R.id.imageSlider);
        banner.setImageResource(sliders.get(position).getmImage());
        container.addView(view,0);
        return view;
    }


    @Override
    public int getCount() {
        return sliders.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
