package com.ryandzhunter.contact.data.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.ryandzhunter.contact.data.model.Contact;

import java.util.List;

import io.reactivex.Flowable;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by aryandi on 7/4/17.
 */

@Dao
public interface ContactDao {

    @Query("SELECT * FROM " + Contact.TABLE_NAME + " ORDER BY " + Contact.FAVORITE + " DESC, " + Contact.FIRST_NAME + " COLLATE NOCASE ASC")
    Flowable<List<Contact>> getAllCachedContact();

    @Insert(onConflict = REPLACE)
    void addCachedContact(Contact contact);

    @Insert
    void addMultipleCachedContact(Contact... contacts);

    @Insert
    void addMultipleListCachedContact(List<Contact> contacts);

    @Query("SELECT * FROM " + Contact.TABLE_NAME + " WHERE id = :id")
    Contact getCachedContactbyId(String id);

    @Delete
    void deleteCachedContact(Contact event);

    @Query("DELETE FROM " + Contact.TABLE_NAME)
    void deleteAllCachedContact();

    @Update(onConflict = REPLACE)
    void updateCachedContactRoom(Contact event);
}
