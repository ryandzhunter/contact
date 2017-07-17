package com.ryandzhunter.contact.data.datastore;

import com.ryandzhunter.contact.data.room.ContactDatabase;
import com.ryandzhunter.contact.http.RetrofitService;
import com.ryandzhunter.contact.data.model.Contact;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Part;

/**
 * Created by aryandi on 7/1/17.
 */

public class ContactListDataStore {

    private RetrofitService service;
    private ContactDatabase roomDatabase;

    public ContactListDataStore(RetrofitService service, ContactDatabase roomDatabase) {
        this.service = service;
        this.roomDatabase = roomDatabase;
    }

    public Flowable<List<Contact>> getAPIContactList(){
        return service.getAllContacts().toFlowable(BackpressureStrategy.BUFFER);
    }

    public Flowable<Contact> getContactDetail(int id){
        return service.getContact(id).toFlowable(BackpressureStrategy.BUFFER);
    }

    public Flowable<Contact> updateContact(int id, Contact contact){
        return service.updateContact(id, contact).toFlowable(BackpressureStrategy.BUFFER);
    }

    public Flowable<List<Contact>> getCachedContactList(){
        return roomDatabase.contactDao().getAllCachedContact();
    }

    public Completable saveCachedContact(Contact contact){
        return Completable.fromAction(() -> roomDatabase.contactDao().addCachedContact(contact));
    }

    public Completable updateCachedContact(Contact contact){
        return Completable.fromAction(() -> roomDatabase.contactDao().updateCachedContactRoom(contact));
    }

    public Flowable<Contact> addContact(Contact contact){
        return service.addContact(contact).toFlowable(BackpressureStrategy.BUFFER);
    }

    public Observable<Void> deleteContact(int id){
        return service.deleteContact(id);
    }

    public Flowable<Contact> addContactWithImage(MultipartBody.Part image, RequestBody firstName,
                                                   RequestBody lastName, RequestBody email,
                                                   RequestBody phoneNumber){
        return service.addContactWithImage(image, firstName, lastName, email, phoneNumber).toFlowable(BackpressureStrategy.BUFFER);
    }

}
