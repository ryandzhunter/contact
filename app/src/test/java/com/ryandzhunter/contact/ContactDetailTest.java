package com.ryandzhunter.contact;

import android.content.Context;

import com.ryandzhunter.contact.contactdetail.ContactDetailView;
import com.ryandzhunter.contact.contactdetail.ContactDetailViewModel;
import com.ryandzhunter.contact.data.datastore.ContactListDataStore;
import com.ryandzhunter.contact.data.model.Contact;
import com.ryandzhunter.contact.http.RetrofitService;
import com.ryandzhunter.contact.usecase.GetContactListUseCase;
import com.ryandzhunter.contact.util.Preferences;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.reactivestreams.Subscriber;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by aryandi on 7/7/17.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ContactDetailTest {

    private ContactDetailViewModel contactDetailViewModel;
    @Mock
    private ContactDetailView contactDetailView;
    @Mock
    private Contact contact;
    @Mock
    private GetContactListUseCase useCase;
    @Mock
    private ContactListDataStore dataStore;
    @Mock
    private RetrofitService service;
    @Mock
    Context fakeContext;
    @Mock
    Preferences pref;


    @BeforeClass
    public static void setupClass() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(
                __ -> Schedulers.trampoline());
    }

    @Before
    public void setUpDetailViewModelTest() {
        MockitoAnnotations.initMocks(this);

        contact = new Contact();
        contact.id = 214;
        contact.firstName = "Aryandi";
        contact.lastName = "Putra";
        contact.email = "aryandi2712@gmail.com";
        contact.phoneNumber = "085768818982";

        contactDetailViewModel = new ContactDetailViewModel(fakeContext, useCase, contactDetailView, pref);

    }

    @AfterClass
    public static void tearDownClass() {
        RxAndroidPlugins.reset();
    }

    @Test
    public void testFetchContactDetail() {

        when(useCase.getAPIContactDetail(contact.id)).thenReturn(new Flowable<Contact>() {
            @Override
            protected void subscribeActual(Subscriber<? super Contact> s) {

            }
        });

        contactDetailViewModel.fetchContactDetail(contact.id);

        verify(useCase).getAPIContactDetail(contact.id);
    }

    @Test
    public void testUpdateContact() {

        when(useCase.updateContactToAPI(contact.id, contact)).thenReturn(new Flowable<Contact>() {
            @Override
            protected void subscribeActual(Subscriber<? super Contact> s) {

            }
        });

        contactDetailViewModel.updateContactToAPI(contact.id, contact);

        verify(useCase).updateContactToAPI(contact.id,contact);
    }

    @Test
    public void testUpdateCachedContact() {
        when(useCase.updateCachedContact(contact)).thenReturn(Completable.complete());
        contactDetailViewModel.updateCachedContact(contact);
        verify(useCase).updateCachedContact(contact);
    }

    @Test
    public void deleteContactToAPI() {
        when(useCase.deleteContactToAPI(contact.id)).thenReturn(Completable.complete());
        contactDetailViewModel.deleteContactToAPI(contact.id);
        verify(useCase).deleteContactToAPI(contact.id);
    }

    @Test
    public void deleteCacheContact(){
        when(useCase.deleteCachedContact(contact)).thenReturn(Completable.complete());
        contactDetailViewModel.deleteCachedContact(contact);
        verify(useCase).deleteCachedContact(contact);
    }
}
