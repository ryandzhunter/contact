package com.ryandzhunter.contact.contactdetail;

import android.app.Activity;
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
    private ContactDetailView view;

    public ContactDetailModule(Context context, ContactDetailView view) {
        this.context = context;
        this.view = view;
    }

    @Provides
    ContactDetailViewModel providesViewModel(GetContactListUseCase usecase) {
        return new ContactDetailViewModel(context, usecase, view);
    }
}
