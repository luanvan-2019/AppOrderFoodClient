package com.hcmunre.apporderfoodclient.models.Database;

import com.hcmunre.apporderfoodclient.models.Common.HashPass;
import com.hcmunre.apporderfoodclient.models.Entity.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class SignInData {
    Connection con;
    DataConnetion dataCon = new DataConnetion();
    String z = "";
//    boolean isSuccess=false;
    public String DangNhap(User user) {
        try {
            con = dataCon.connectionData();
            if (con == null) {
                z = "Vui lòng kiểm tra kết nối";
            } else {
                String query="EXEC Sp_SelectUser '"+user.getmEmail().toString()+"','"+ HashPass.md5(user.getmPassword().toString())+"'";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    z = "success";
                    con.close();
                } else {
                    z = "Không đúng tên đăng nhập hoặc mật khẩu";
                }
            }
        } catch (Exception e) {
            z = "Lỗi";
        }
        return z;
    }

}
