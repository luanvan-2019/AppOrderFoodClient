package com.hcmunre.apporderfoodclient.interfaces;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.hcmunre.apporderfoodclient.models.Entity.Status;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
@Dao
public interface StatusDao {
    //
    @Query("SELECT * FROM Status WHERE orderId=:orderId")
    Flowable<List<Status>> getAllStatus(int orderId);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertStatus(Status... statuses);
}
