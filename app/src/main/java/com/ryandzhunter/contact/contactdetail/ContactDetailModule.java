package com.ryandzhunter.contact.contactdetail;

import android.content.Context;

import com.ryandzhunter.contact.contactlist.ContactListViewModel;
import com.ryandzhunter.contact.dagger.module.UseCaseModule;
import com.ryandzhunter.contact.usecase.GetContactListUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aryandi on 7/4/17.
 */

@Module(includes = UseCaseModule.class)
public class ContactDetailModule {

    private Context context;

    public ContactDetailModule(ContactDetailActivity context) {
        this.context = context;
    }

    @Provides
    ContactDetailViewModel providesViewModel(GetContactListUseCase usecase) {
        return new ContactDetailViewModel(context, usecase);
    }
}
