package com.hcmunre.apporderfoodclient.views.activities;

public interface SignInView {
    public void showProgress();

    public void hideProgress();

    public void setUsernameError();

    public void setPasswordError();

    public void navigateToHome();
}
