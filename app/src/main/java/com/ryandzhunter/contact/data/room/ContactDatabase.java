package com.ryandzhunter.contact.data.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.ryandzhunter.contact.data.model.Contact;

/**
 * Created by aryandi on 7/4/17.
 */

@Database(entities = {Contact.class}, version = 1)
public abstract class ContactDatabase extends RoomDatabase {

    public abstract ContactDao contactDao();

}
