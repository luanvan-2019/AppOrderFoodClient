package com.hcmunre.apporderfoodclient.models.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Status")
public class Status {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    private int id;
    @ColumnInfo(name = "orderId")
    private int orderId;
    @ColumnInfo(name = "status")
    private int status;
    @ColumnInfo (name="statusRes")
    private int statusRes;

    public Status() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatusRes() {
        return statusRes;
    }

    public void setStatusRes(int statusRes) {
        this.statusRes = statusRes;
    }
}
