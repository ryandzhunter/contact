package com.ryandzhunter.contact.contactdetail;

import dagger.Subcomponent;

@Subcomponent(modules = ContactDetailModule.class)
public interface ContactDetailComponent {
        void inject(ContactDetailActivity activity);
}