package com.ryandzhunter.contact.usecase;

import com.ryandzhunter.contact.datastore.ContactListDataStore;
import com.ryandzhunter.contact.model.Contact;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by aryandi on 7/1/17.
 */

public class GetContactListUseCase {

    private ContactListDataStore dataStore;

    public GetContactListUseCase(ContactListDataStore dataStore) {
        this.dataStore = dataStore;
    }

    public Observable<List<Contact>> getContactList() {
        return dataStore.getContactList();
   }
}
