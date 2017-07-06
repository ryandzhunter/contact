package com.ryandzhunter.contact.data.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import static com.ryandzhunter.contact.data.model.Contact.TABLE_NAME;

/**
 * Created by aryandi on 7/1/17.
 */

@Parcel
@Entity(tableName = TABLE_NAME)
public class Contact {

    public static final String TABLE_NAME = "contacts";
    public static final String FIRST_NAME = "firstName";
    public static final String FAVORITE = "favorite";

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    public Integer id;
    @SerializedName("first_name")
    public String firstName;
    @SerializedName("last_name")
    public String lastName;
    @SerializedName("email")
    public String email;
    @SerializedName("phone_number")
    public String phoneNumber;
    @SerializedName("profile_pic")
    public String profilePic;
    @SerializedName("favorite")
    public Boolean favorite;
    @SerializedName("created_at")
    public String createdAt;
    @SerializedName("updated_at")
    public String updatedAt;

    @Ignore
    public Contact() {
    }

    public Contact(Integer id, String firstName, String lastName, String email, String phoneNumber, String profilePic, Boolean favorite, String createdAt, String updatedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.profilePic = profilePic;
        this.favorite = favorite;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
