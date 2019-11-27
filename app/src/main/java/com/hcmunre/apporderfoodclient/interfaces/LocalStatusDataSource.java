package com.hcmunre.apporderfoodclient.interfaces;

import com.hcmunre.apporderfoodclient.models.Entity.Status;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class LocalStatusDataSource implements StatusDataSource {
    private StatusDao statusDao;

    public LocalStatusDataSource(StatusDao statusDao) {
        this.statusDao=statusDao;
    }

    @Override
    public Flowable<List<Status>> getAllStatus(int orderId) {
        return statusDao.getAllStatus(orderId);
    }

    @Override
    public Completable insertStatus(Status... statuses) {
        return statusDao.insertStatus(statuses);
    }
}
