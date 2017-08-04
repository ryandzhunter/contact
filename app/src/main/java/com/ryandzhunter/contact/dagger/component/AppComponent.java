package com.ryandzhunter.contact.dagger.component;

import com.ryandzhunter.contact.addcontact.AddContactComponent;
import com.ryandzhunter.contact.addcontact.AddContactModule;
import com.ryandzhunter.contact.contactdetail.ContactDetailComponent;
import com.ryandzhunter.contact.contactdetail.ContactDetailModule;
import com.ryandzhunter.contact.contactlist.ContactListComponent;
import com.ryandzhunter.contact.contactlist.ContactListModule;
import com.ryandzhunter.contact.dagger.module.AppModule;
import com.ryandzhunter.contact.dagger.module.DataStoreModule;
import com.ryandzhunter.contact.dagger.module.PreferencesModule;
import com.ryandzhunter.contact.dagger.module.RetrofitModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by aryandi on 7/1/17.
 */

@Singleton
@Component(modules={AppModule.class, RetrofitModule.class, DataStoreModule.class, PreferencesModule.class})
public interface AppComponent {
    ContactListComponent contactListComponent(ContactListModule contactListModule);
    ContactDetailComponent contactDetailComponent(ContactDetailModule contactDetailModule);
    AddContactComponent addContactComponent(AddContactModule addContactModule);
}
