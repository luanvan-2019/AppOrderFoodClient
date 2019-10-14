package com.hcmunre.apporderfoodclient.models.eventbus;

import com.hcmunre.apporderfoodclient.models.Entity.Menu;

public class FoodListEvent {
    private boolean success;
    private Menu category;

    public FoodListEvent(boolean success, Menu category) {
        this.success = success;
        this.category = category;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Menu getCategory() {
        return category;
    }

    public void setCategory(Menu category) {
        this.category = category;
    }
}