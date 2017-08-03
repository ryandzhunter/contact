package com.ryandzhunter.contact;

import android.content.Context;
import android.util.Patterns;

import com.ryandzhunter.contact.addcontact.AddContactView;
import com.ryandzhunter.contact.addcontact.AddContactViewModel;
import com.ryandzhunter.contact.data.datastore.ContactListDataStore;
import com.ryandzhunter.contact.data.model.Contact;
import com.ryandzhunter.contact.usecase.GetContactListUseCase;
import com.ryandzhunter.contact.util.Preferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.reactivestreams.Subscriber;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by aryandi on 7/7/17.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class AddContactTest {

    private AddContactViewModel addContactViewModel;
    @Mock
    private AddContactView addContactView;
    @Mock
    private Contact contact;
    @Mock
    private GetContactListUseCase useCase;
    @Mock
    private ContactListDataStore dataStore;
    @Mock
    Context fakeContext;
    @Mock
    Preferences pref;

    @Before
    public void setUpDetailViewModelTest() {
        MockitoAnnotations.initMocks(this);

        contact = new Contact();
        contact.id = 214;
        contact.firstName = "Aryandi";
        contact.lastName = "Putra";
        contact.email = "aryandi2712@gmail.com";
        contact.phoneNumber = "085768818982";

        addContactViewModel = new AddContactViewModel(fakeContext, useCase, addContactView, pref);
    }

    @Test
    public void testCheckIsValidName() throws Exception{
        assertTrue(addContactViewModel.isValidName("aryandi"));
        assertFalse(addContactViewModel.isValidName("abc"));
    }

    @Test
    public void testCheckIsValidPhoneNumber() throws Exception{
        assertTrue(addContactViewModel.isValidPhoneNumber("081234567123"));
        assertFalse(addContactViewModel.isValidPhoneNumber("08123"));
        assertTrue(addContactViewModel.isValidPhoneNumber("+6285768818982"));
        assertFalse(addContactViewModel.isValidPhoneNumber("asddssasasa"));
    }

    @Test
    public void testCheckIsValidEmail() throws Exception{
        assertTrue(addContactViewModel.isValidEmail("aryandi@gmail.com"));
        assertFalse(addContactViewModel.isValidEmail("aryandi"));
        assertFalse(addContactViewModel.isValidEmail("@aryandi"));
        assertFalse(addContactViewModel.isValidEmail("aryandi@gmail."));
    }

    @Test
    public void testAddContact() {

        when(useCase.addContactToAPI(contact)).thenReturn(new Flowable<Contact>() {
            @Override
            protected void subscribeActual(Subscriber<? super Contact> s) {

            }
        });

        addContactViewModel.addNewContactToAPI(contact);

        verify(useCase).addContactToAPI(contact);
    }

    @Test
    public void testAddContactWithImage() throws Exception {
        File file = new File("/path");
        RequestBody imageBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("contact[profile_pic]", file.getName(), imageBody);
        RequestBody firstNameBody = RequestBody.create(MediaType.parse("text/plain"), contact.firstName);
        RequestBody lastNameBody = RequestBody.create(MediaType.parse("text/plain"), contact.lastName);
        RequestBody phoneBody = RequestBody.create(MediaType.parse("text/plain"), contact.phoneNumber);
        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), contact.email);

        when(useCase.addContactWithImage(imagePart, firstNameBody, lastNameBody, emailBody, phoneBody))
                .thenReturn(new Flowable<Contact>() {
                    @Override
                    protected void subscribeActual(Subscriber<? super Contact> s) {

                    }
                });

        addContactViewModel.addNewContactWithPhotoToAPI(imagePart, firstNameBody, lastNameBody, emailBody, phoneBody);

        verify(useCase).addContactWithImage(imagePart, firstNameBody, lastNameBody, emailBody, phoneBody);

    }


    @Test
    public void addContactToCache() {
        when(useCase.saveCachedContact(contact)).thenReturn(Completable.complete());
        addContactViewModel.addContactToCache(contact);
        verify(useCase).saveCachedContact(contact);
    }

    @Test
    public void testUpdateContact() {

        when(useCase.updateContactToAPI(contact.id, contact)).thenReturn(new Flowable<Contact>() {
            @Override
            protected void subscribeActual(Subscriber<? super Contact> s) {

            }
        });

        addContactViewModel.updateContactToAPI(contact);

        verify(useCase).updateContactToAPI(contact.id,contact);
    }

    @Test
    public void testUpdateCachedContact() {
        when(useCase.updateCachedContact(contact)).thenReturn(Completable.complete());
        addContactViewModel.updateCachedContact(contact);
        verify(useCase).updateCachedContact(contact);
    }
}
