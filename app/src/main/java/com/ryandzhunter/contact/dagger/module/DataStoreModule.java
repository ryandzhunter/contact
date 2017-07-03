package com.ryandzhunter.contact.dagger.module;

import com.ryandzhunter.contact.data.datastore.ContactListDataStore;
import com.ryandzhunter.contact.http.RetrofitService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aryandi on 7/2/17.
 */

@Module(includes = RetrofitModule.class)
public class DataStoreModule {

    @Provides
    @Singleton
    public ContactListDataStore providesContactListDataStore(RetrofitService service) {
        return new ContactListDataStore(service);
    }
}
