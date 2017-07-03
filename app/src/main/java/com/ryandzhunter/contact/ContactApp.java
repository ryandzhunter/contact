package com.ryandzhunter.contact;

import android.app.Application;

import com.ryandzhunter.contact.dagger.component.AppComponent;
import com.ryandzhunter.contact.dagger.component.DaggerAppComponent;
import com.ryandzhunter.contact.dagger.module.AppModule;
import com.ryandzhunter.contact.dagger.module.DataStoreModule;
import com.ryandzhunter.contact.dagger.module.RetrofitModule;

/**
 * Created by aryandi on 7/1/17.
 */

public class ContactApp extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initDaggerComponent();
    }

    private void initDaggerComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .retrofitModule(new RetrofitModule())
                .dataStoreModule(new DataStoreModule())
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
