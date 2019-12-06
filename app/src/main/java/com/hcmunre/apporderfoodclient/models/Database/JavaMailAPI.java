package com.hcmunre.apporderfoodclient.models.Database;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.hcmunre.apporderfoodclient.commons.Common;
import com.hcmunre.apporderfoodclient.models.Entity.User;
import com.hcmunre.apporderfoodclient.views.activities.OTPActivity;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMailAPI extends AsyncTask<Void, Void, Void> {

    private Context context;
    private Session mSession;
    private String mEmail;
    private String mSubject;
    private String mMessage;
    private boolean resend;
    private ProgressDialog mProgressDialog;

    public JavaMailAPI(Context context,String mEmail, String mSubject, String mMessage,boolean resend) {
        this.context = context;
        this.mEmail = mEmail;
        this.mSubject = mSubject;
        this.mMessage = mMessage;
        this.resend=resend;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Vui lòng chờ...");
        mProgressDialog.show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mProgressDialog.dismiss();
        Toast.makeText(context, "Đã gửi", Toast.LENGTH_SHORT).show();
        Common.currentUser = new User();
        Common.currentUser.setmEmail(mEmail);
        if (resend==false){
            context.startActivity(new Intent(context, OTPActivity.class));
        }else {

        }

    }

    @Override
    protected Void doInBackground(Void... params) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        mSession = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("appfood1097@gmail.com", "01699458496");
                    }
                });

        try {
            MimeMessage mm = new MimeMessage(mSession);
            mm.setFrom(new InternetAddress("appfood1097@gmail.com"));
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(mEmail));
            mm.setSubject(mSubject);
            mm.setText(mMessage);
            Transport.send(mm);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}