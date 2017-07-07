package com.ryandzhunter.contact.dagger.module;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.ryandzhunter.contact.data.datastore.ContactListDataStore;
import com.ryandzhunter.contact.data.room.ContactDatabase;
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
    ContactListDataStore providesContactListDataStore(RetrofitService service, ContactDatabase roomDatabase) {
        return new ContactListDataStore(service, roomDatabase);
    }

    @Provides
    @Singleton
    ContactDatabase providesEventDatabase(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(), ContactDatabase.class, "event_db").build();
    }
}
