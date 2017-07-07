package com.ryandzhunter.contact.addcontact;

import com.ryandzhunter.contact.contactdetail.ContactDetailActivity;
import com.ryandzhunter.contact.contactdetail.ContactDetailModule;

import dagger.Subcomponent;

/**
 * Created by aryandi on 7/6/17.
 */

@Subcomponent(modules = AddContactModule.class)
public interface AddContactComponent {
    void inject(AddContactActivity activity);
}
