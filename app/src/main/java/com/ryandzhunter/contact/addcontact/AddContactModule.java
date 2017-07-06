package com.ryandzhunter.contact.addcontact;

import android.content.Context;

import com.ryandzhunter.contact.contactdetail.ContactDetailView;
import com.ryandzhunter.contact.contactdetail.ContactDetailViewModel;
import com.ryandzhunter.contact.dagger.module.UseCaseModule;
import com.ryandzhunter.contact.data.model.Contact;
import com.ryandzhunter.contact.usecase.GetContactListUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aryandi on 7/6/17.
 */

@Module(includes = UseCaseModule.class)
public class AddContactModule {

    private Context context;
    private AddContactView view;

    public AddContactModule(Context context, AddContactView view) {
        this.context = context;
        this.view = view;
    }

    @Provides
    AddContactViewModel providesViewModel(GetContactListUseCase usecase) {
        return new AddContactViewModel(context, usecase, view);
    }
}
