package com.hcmunre.apporderfoodclient.views.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.models.Database.UserData;
import com.hcmunre.apporderfoodclient.models.Entity.User;
import com.hcmunre.apporderfoodclient.views.activities.PreferenceUtils;
import com.hcmunre.apporderfoodclient.views.activities.SignInActivity;
import com.hcmunre.apporderfoodclient.views.activities.UserInfoActivity;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AccountFragment extends Fragment {

    @BindView(R.id.btnlogout)
    TextView btnlogout;
    @BindView(R.id.btn_app_restaurant)
    TextView btn_app_restaurant;
    @BindView(R.id.image_profile)
    RoundedImageView image_profile;
    @BindView(R.id.main_layout)
    LinearLayout main_layout;
    @BindView(R.id.txt_login)
    TextView txt_login;
    @BindView(R.id.txt_name_user)
    TextView txt_name_user;
    byte[] byteArray;
    String encodedImage;
    private static final int RESULT_LOAD_IMAGE=1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.bind(this,view);
        init();
        eventClick();
        signOut();
        chooseImage();
        return view;
    }
    private void init(){
//        FacebookSdk.sdkInitialize(getContext());
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(PreferenceUtils.getEmail(getActivity())!=null||user!=null){
            btnlogout.setVisibility(View.VISIBLE);
            txt_login.setVisibility(View.GONE);
            txt_name_user.setText(PreferenceUtils.getName(getActivity()));
        }else{
            txt_name_user.setVisibility(View.GONE);
            btnlogout.setVisibility(View.GONE);
            txt_login.setVisibility(View.VISIBLE);
        }
        if (!Places.isInitialized()) {
            Places.initialize(getActivity(), "AIzaSyDb8vrfFWCuSNigtxvfM-zC-4MvNQlGIFQ");
        }
//        Bundle intent=getActivity().getIntent().getExtras();
//        String name=intent.get("name").toString();
//        String surname=intent.get("surname").toString();
//        String imageUrl=intent.get("imageUrl").toString();
//        Log.d("BBB",name+":"+surname);
//        new downloadImageFB(image_profile).execute(imageUrl);

    }

    private void eventClick(){
        btn_app_restaurant.setOnClickListener(v -> {
            Intent intent=getActivity().getPackageManager().getLaunchIntentForPackage("com.hcmunre.apporderfoodserver");
            if(intent!=null){
                startActivity(intent);
            }
        });
        txt_login.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(),SignInActivity.class));
            getActivity().finish();
        });
        txt_name_user.setOnClickListener(view -> startActivity(new Intent(getActivity(), UserInfoActivity.class)));
    }
    private void signOut(){
        btnlogout.setOnClickListener(view1 -> {
            AlertDialog.Builder comfirmSignOut = new AlertDialog.Builder(view1.getContext())
                    .setTitle("Đăng xuất")
                    .setMessage("Bạn có muốn đăng xuất không ?")
                    .setNegativeButton("Hủy", (dialogInterface, i) -> dialogInterface.dismiss())
                    .setPositiveButton("OK", (dialogInterface, i) -> {
                        Common.currentUser=null;
                        Common.currentRestaurant=null;
                        FirebaseAuth.getInstance().signOut();
                        LoginManager.getInstance().logOut();
                        PreferenceUtils.savePassword(null, getActivity());
                        PreferenceUtils.saveEmail(null, getActivity());
                        PreferenceUtils.saveAddress(null,getActivity());
                        PreferenceUtils.savePhone(null,getActivity());
                        PreferenceUtils.saveName(null,getActivity());
//                        Intent intent = new Intent(getActivity(), SignInActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                        getActivity().finish();
                        //logout phone
                            btnlogout.setVisibility(View.GONE);
                            txt_login.setVisibility(View.VISIBLE);
                            txt_name_user.setVisibility(View.GONE);
                    });
            comfirmSignOut.show();
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == getActivity().RESULT_OK && data != null){
            Bitmap bitmap=null;
            Uri uri=data.getData();
            InputStream imageStream;
            try {
                imageStream=getActivity().getContentResolver().openInputStream(uri);
                bitmap= BitmapFactory.decodeStream(imageStream);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if(bitmap!=null){
                image_profile.setImageBitmap(bitmap);
                try {
                    Bitmap image=((BitmapDrawable)image_profile.getDrawable()).getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG,90,byteArrayOutputStream);
                    byteArray=byteArrayOutputStream.toByteArray();
                    encodedImage= Base64.encodeToString(byteArray,Base64.DEFAULT);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }else{
            Toast.makeText(getActivity(), "Đã hủy", Toast.LENGTH_SHORT).show();
        }
    }

    private void chooseImage(){
        image_profile.setOnClickListener(v -> {
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
            &&!Environment.getExternalStorageState().equals(Environment.MEDIA_CHECKING)){
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,RESULT_LOAD_IMAGE);
            }else {
                Snackbar snackbar=Snackbar.make(main_layout,"Không tìm thấy media",Snackbar.LENGTH_LONG)
                        .setAction("Action",null);
                snackbar.show();
            }
        });
    }
//    public class downloadImageFB extends AsyncTask<String,String,Bitmap>{
//        ImageView imageView;
//
//        public downloadImageFB(ImageView imageView) {
//            this.imageView = imageView;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            imageView.setImageBitmap(bitmap);
//        }
//
//        @Override
//        protected Bitmap doInBackground(String... strings) {
//            String urlDisplay=strings[0];
//            Bitmap bitmap=null;
//            try {
//                InputStream in=new URL(urlDisplay).openStream();
//                bitmap=BitmapFactory.decodeStream(in);
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return bitmap;
//        }
//    }
}
