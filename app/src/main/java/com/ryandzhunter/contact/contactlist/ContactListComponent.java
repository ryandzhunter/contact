package com.ryandzhunter.contact.contactlist;

import dagger.Subcomponent;

/**
 * Created by aryandi on 7/2/17.
 */
@Subcomponent(modules = ContactListModule.class)
public interface ContactListComponent {
        void inject(ContactListActivity activity);
}
