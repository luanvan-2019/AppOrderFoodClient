package com.hcmunre.apporderfoodclient.models.Database;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataConnetion {
    String ip, db, DBUsername, DBPassword;

    @SuppressLint("NewApi")
    public Connection connectionData() {

        ip = "115.84.182.60";
        db = "hcmunrec_appfood";
        DBUsername = "huutrong";
        DBPassword = "Luanvan2019@";
//        ip = "192.168.43.138";
//        db = "hcmunrec_appfood";
//        DBUsername = "sa";
//        DBPassword = "123456789";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + ip + ";databaseName=" + db + ";user=" + DBUsername + ";password=" + DBPassword + ";";
            connection = DriverManager.getConnection(ConnectionURL);
        } catch (SQLException se) {
            Log.e("error here 1 : ", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("error here 2 : ", e.getMessage());
        } catch (Exception e) {
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }
}
