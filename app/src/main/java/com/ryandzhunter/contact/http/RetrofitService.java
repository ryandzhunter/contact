package com.ryandzhunter.contact.http;

import com.ryandzhunter.contact.data.model.Contact;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by aryandi on 7/1/17.
 */

public interface RetrofitService {

    @GET("/contacts.json")
    Observable<List<Contact>> getAllContacts();

    @GET("/contacts/{id}.json")
    Observable<Contact> getContact(@Path("id") int id);

    @POST("/contacts.json")
    Observable<Contact> addContact(@Body Contact contact);

    @PUT("/contacts/{id}.json")
    Observable<Contact> updateContact(@Path("id") int id, @Body Contact contact);

    @DELETE("/contacts/{id}.json")
    Completable deleteContact(@Path("id") int id);

    @Multipart
    @POST("/contacts.json")
    Observable<Contact> addContactWithImage(@Part("contact[first_name]") RequestBody firstName,
                                            @Part("contact[last_name]") RequestBody lastName,
                                            @Part("contact[email]") RequestBody email,
                                            @Part("contact[phone_number]") RequestBody phoneNumber,
                                            @Part MultipartBody.Part image);

}
