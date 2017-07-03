package com.ryandzhunter.contact.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by aryandi on 7/1/17.
 */

public class Contact {

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

}
