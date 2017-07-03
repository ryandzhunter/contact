package com.ryandzhunter.contact.contactlist;

import android.databinding.BaseObservable;

import com.ryandzhunter.contact.ILifecycleViewModel;
import com.ryandzhunter.contact.model.Contact;

/**
 * Created by aryandi on 7/2/17.
 */

public class ContactItemViewModel  extends BaseObservable implements ILifecycleViewModel {

    private Contact contact;

    public ContactItemViewModel(Contact contact) {
        this.contact = contact;
    }

    @Override
    public void onDestroy() {

    }
}
