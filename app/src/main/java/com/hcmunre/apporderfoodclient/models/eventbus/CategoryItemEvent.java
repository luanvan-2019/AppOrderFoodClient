package com.hcmunre.apporderfoodclient.models.eventbus;

import com.hcmunre.apporderfoodclient.models.Entity.Category;

public class CategoryItemEvent {
    private boolean success;
    private Category category;

    public CategoryItemEvent(boolean success, Category category) {
        this.success = success;
        this.category = category;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}