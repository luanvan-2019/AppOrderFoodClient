package com.hcmunre.apporderfoodclient.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.models.Entity.Restaurant;
import com.hcmunre.apporderfoodclient.models.eventbus.MenuItemEvent;
import com.hcmunre.apporderfoodclient.views.activities.GetCurrentUser;
import com.hcmunre.apporderfoodclient.views.activities.MenuActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
    private Context mContext;
    private  ArrayList<Restaurant> restaurantArrayList;
    public RestaurantAdapter(Context context, ArrayList<Restaurant> restaurantArrayList) {
        mContext = context;
        this.restaurantArrayList = restaurantArrayList;
    }
    @NonNull
    @Override
    public RestaurantAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant, parent, false);
        return new RestaurantAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantAdapter.ViewHolder holder, int position) {
        Common.currentRestaurant = restaurantArrayList.get(position);
        if(Common.currentRestaurant.getmImage()!=null){
            holder.imageRes.setImageBitmap(Common.getBitmap(Common.currentRestaurant.getmImage()));
        }
        holder.txtName_Res.setText(Common.currentRestaurant.getmName());
        holder.txtAddress_Res.setText(Common.currentRestaurant.getmAddress());
        holder.itemView.setOnClickListener(view -> {
            Common.currentRestaurant = restaurantArrayList.get(position);
            EventBus.getDefault().postSticky(new MenuItemEvent(true,Common.currentRestaurant));
            mContext.startActivity(new Intent(mContext, MenuActivity.class));

        });
        GetCurrentUser getCurrentUser=new GetCurrentUser(mContext);
        String fullname=getCurrentUser.getuser();
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" +fullname+ "&destinations=" +Common.currentRestaurant.getmAddress()+ "&mode=driving&language=fr-FR&avoid=tolls&key="+mContext.getString(R.string.map_key)+"";
        new GetTask(mContext,holder.txt_km,holder.txt_minutes).execute(url);

    }
    public void addRestaurant(ArrayList<Restaurant> restaurants){
        restaurantArrayList.clear();
        restaurantArrayList.addAll(restaurants);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {

        return restaurantArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageRes)
        ImageView imageRes;
        @BindView(R.id.txtName_Res)
        TextView txtName_Res;
        @BindView(R.id.txtAddress_Res)
        TextView txtAddress_Res;
        @BindView(R.id.txt_km)
        TextView txt_km;
        @BindView(R.id.txt_minutes)
        TextView txt_minutes;
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }

    }
    public class GetTask extends AsyncTask<String, Void, String> {
        Context mContext;
        Double duration;
        TextView txt_km,txt_minutes;

        public GetTask(Context mContext, TextView txt_km,TextView txt_minutes) {
            this.mContext = mContext;
            this.txt_km = txt_km;
            this.txt_minutes=txt_minutes;
        }
        @Override
        protected void onPostExecute(String aDouble) {
            super.onPostExecute(aDouble);
            if (aDouble != null) {
                String res[]=aDouble.split(",");
                double min=Double.parseDouble(res[0])/60;
                double dist=Double.parseDouble(res[1])/1000;
                txt_km.setText((double)Math.round(dist * 10) / 10+" km");
                txt_minutes.setText(Math.round(min)+" phút");
            } else
                Toast.makeText(mContext, "Error4!Please Try Again wiht proper values", Toast.LENGTH_SHORT).show();
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                int statuscode = con.getResponseCode();
                if (statuscode == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = br.readLine();
                    while (line != null) {
                        sb.append(line);
                        line = br.readLine();
                    }
                    String json = sb.toString();
                    Log.d("JSON", json);
                    JSONObject root = new JSONObject(json);
                    JSONArray array_rows = root.getJSONArray("rows");
                    Log.d("JSON", "array_rows:" + array_rows);
                    JSONObject object_rows = array_rows.getJSONObject(0);
                    Log.d("JSON", "object_rows:" + object_rows);
                    JSONArray array_elements = object_rows.getJSONArray("elements");
                    Log.d("JSON", "array_elements:" + array_elements);
                    JSONObject object_elements = array_elements.getJSONObject(0);
                    Log.d("JSON", "object_elements:" + object_elements);
                    JSONObject object_duration = object_elements.getJSONObject("duration");
                    JSONObject object_distance = object_elements.getJSONObject("distance");

                    Log.d("JSON", "object_duration:" + object_duration);
                    return object_duration.getString("value") + "," + object_distance.getString("value");

                }
            } catch (MalformedURLException e) {
                Log.d("error", "error1");
            } catch (IOException e) {
                Log.d("error", "error2");
            } catch (JSONException e) {
                Log.d("error", "error3");
            }

            return null;
        }

    }
}



