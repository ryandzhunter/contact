package com.ryandzhunter.contact.dagger.module;

import com.ryandzhunter.contact.data.datastore.ContactListDataStore;
import com.ryandzhunter.contact.usecase.GetContactListUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aryandi on 7/2/17.
 */

@Module
public class UseCaseModule {

    @Provides
    public GetContactListUseCase provideGetContactListUseCase(ContactListDataStore dataStore) {
        return new GetContactListUseCase(dataStore);
    }
}
