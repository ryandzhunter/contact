package com.ryandzhunter.contact.datastore;

import com.ryandzhunter.contact.http.RetrofitService;
import com.ryandzhunter.contact.model.Contact;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by aryandi on 7/1/17.
 */

public class ContactListDataStore {

    private RetrofitService service;

    public ContactListDataStore(RetrofitService service) {
        this.service = service;
    }

    public Observable<List<Contact>> getContactList(){
        return service.getAllContacts();
    }
}
