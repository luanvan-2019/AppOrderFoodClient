package com.hcmunre.apporderfoodclient.interfaces;

import com.hcmunre.apporderfoodclient.models.Entity.Status;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public interface StatusDataSource {

    Flowable<List<Status>> getAllStatus(int orderId);
    Completable insertStatus(Status... statuses);
}
