package com.ryandzhunter.contact.dagger;

import com.ryandzhunter.contact.contactlist.AddContactActivityTest;
import com.ryandzhunter.contact.contactlist.ContactDetailActivityTest;
import com.ryandzhunter.contact.contactlist.ContactListActivityTest;
import com.ryandzhunter.contact.dagger.component.AppComponent;
import com.ryandzhunter.contact.dagger.module.AppModule;
import com.ryandzhunter.contact.dagger.module.DataStoreModule;
import com.ryandzhunter.contact.dagger.module.PreferencesModule;
import com.ryandzhunter.contact.dagger.module.RetrofitModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ryandzhunter on 8/4/17.
 */

@Singleton
@Component(modules={AppModule.class, RetrofitModule.class, DataStoreModule.class, PreferencesModule.class})
public interface TestComponent {
    void inject(ContactListActivityTest contactListActivityTest);
    void inject(ContactDetailActivityTest contactDetailActivityTest);
    void inject(AddContactActivityTest addContactActivityTest);
}
