package com.zerones.auth0example;

import android.app.Application;

import com.auth0.lock.Lock;
import com.auth0.lock.LockProvider;

/**
 * Created by kishan on 4/2/16.
 */
public class MyApplication extends Application implements LockProvider {

    private Lock lock;

    public void onCreate() {
        super.onCreate();
        lock = new Lock.Builder()
                .loadFromApplication(this)
                        /** Other configuration goes here */
                .closable(true)
                .build();
    }

    @Override
    public Lock getLock() {
        return lock;
    }
}