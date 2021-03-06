package com.ryandzhunter.contact.contactlist;

import android.content.Context;

import com.ryandzhunter.contact.contactlist.ContactListViewModel;
import com.ryandzhunter.contact.dagger.module.UseCaseModule;
import com.ryandzhunter.contact.usecase.GetContactListUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aryandi on 7/2/17.
 */

@Module(includes = UseCaseModule.class)
public class ContactListModule {

    private Context context;

    ContactListModule(Context context) {
        this.context = context;
    }

    @Provides
    ContactListViewModel providesViewModel(GetContactListUseCase usecase) {
        return new ContactListViewModel(context, usecase);
    }
}
