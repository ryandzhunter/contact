package com.ryandzhunter.contact.dagger.component;

import com.ryandzhunter.contact.contactlist.ContactListComponent;
import com.ryandzhunter.contact.contactlist.ContactListModule;
import com.ryandzhunter.contact.dagger.module.AppModule;
import com.ryandzhunter.contact.dagger.module.DataStoreModule;
import com.ryandzhunter.contact.dagger.module.RetrofitModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by aryandi on 7/1/17.
 */

@Singleton
@Component(modules={AppModule.class, RetrofitModule.class, DataStoreModule.class})
public interface AppComponent {
    ContactListComponent splashComponent(ContactListModule module);
}
