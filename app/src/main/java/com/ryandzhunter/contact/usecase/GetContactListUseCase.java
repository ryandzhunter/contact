package com.ryandzhunter.contact.usecase;

import com.ryandzhunter.contact.data.datastore.ContactListDataStore;
import com.ryandzhunter.contact.data.model.Contact;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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

    public Flowable<Contact> getContactDetail(int id) {
        return dataStore.getContactDetail(id);
    }

    public Flowable<List<Contact>> getCachedContactList() {
        return dataStore.getCachedContactList();
    }

    public Flowable<Contact> updateContact(int id, Contact contact){
        return dataStore.updateContact(id, contact);
    }

    public Completable updateCachedContact(Contact contact){
        return dataStore.updateCachedContact(contact);
    }

    public Completable saveCachedContact(Contact contact) {
        return dataStore.saveCachedContact(contact);
    }

    public Flowable<Contact> addContact(Contact contact){
        return dataStore.addContact(contact);
    }

    public Observable<Void> deleteContact(int id){
        return dataStore.deleteContact(id);
    }

    public Flowable<Contact> addContactWithImage(MultipartBody.Part image, RequestBody firstName,
                                                 RequestBody lastName, RequestBody email,
                                                 RequestBody phoneNumber){
        return dataStore.addContactWithImage(image, firstName, lastName, email, phoneNumber);
    }
}
