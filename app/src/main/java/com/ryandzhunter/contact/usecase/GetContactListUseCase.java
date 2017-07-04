package com.ryandzhunter.contact.usecase;

import com.ryandzhunter.contact.data.datastore.ContactListDataStore;
import com.ryandzhunter.contact.data.model.Contact;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * Created by aryandi on 7/1/17.
 */

public class GetContactListUseCase {

    private ContactListDataStore dataStore;

    public GetContactListUseCase(ContactListDataStore dataStore) {
        this.dataStore = dataStore;
    }

    public Flowable<List<Contact>> getAPIContactList() {
        return dataStore.getAPIContactList();
   }

   public Flowable<List<Contact>> getCachedContactList(){
       return dataStore.getCachedContactList();
   }

   public Completable saveCachedContact(Contact contact){
       return dataStore.saveCachedContact(contact);
   }
}
