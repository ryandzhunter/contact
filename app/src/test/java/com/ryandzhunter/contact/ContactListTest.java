package com.ryandzhunter.contact;

import android.content.Context;

import com.google.common.collect.ImmutableSortedMultiset;
import com.ryandzhunter.contact.contactlist.ContactListViewModel;
import com.ryandzhunter.contact.data.datastore.ContactListDataStore;
import com.ryandzhunter.contact.data.model.Contact;
import com.ryandzhunter.contact.http.RetrofitService;
import com.ryandzhunter.contact.usecase.GetContactListUseCase;
import com.ryandzhunter.contact.util.Preferences;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;
import org.mockito.quality.Strictness;
import org.reactivestreams.Subscriber;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by aryandi on 7/7/17.
 */

public class ContactListTest {

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
    private ContactListViewModel viewModel;
    private List<Contact> contactList;
    @Mock
    Preferences pref;


    @BeforeClass
    public static void setupClass() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(
                __ -> Schedulers.trampoline());
    }

    @Before
    public void setUpTest() {
        MockitoAnnotations.initMocks(this);

        contact = new Contact();
        contact.id = 214;
        contact.firstName = "Aryandi";
        contact.lastName = "Putra";
        contact.email = "aryandi2712@gmail.com";
        contact.phoneNumber = "085768818982";

        contactList.add(contact);

        viewModel = new ContactListViewModel(fakeContext, useCase, pref);
    }

    @AfterClass
    public static void tearDownClass() {
        RxAndroidPlugins.reset();
    }

    @Test
    public void testFetchContactList(){

        when(useCase.getAPIContactList()).thenReturn(new Flowable<List<Contact>>() {
            @Override
            protected void subscribeActual(Subscriber<? super List<Contact>> s) {

            }
        });

        viewModel.fetchContactList();

        verify(useCase).getAPIContactList();

    }

    @Test
    public void testGetCacheContactList(){

        when(useCase.getCachedContactList()).thenReturn(new Flowable<List<Contact>>() {
            @Override
            protected void subscribeActual(Subscriber<? super List<Contact>> s) {

            }
        });

        viewModel.getCachedContactList();

        verify(useCase).getCachedContactList();
    }

    @Test
    public void testSaveCachedContact(){

        when(useCase.saveMultipleListCachedContact(contactList)).thenReturn(new Completable() {
            @Override
            protected void subscribeActual(CompletableObserver s) {

            }
        });

        viewModel.saveCachedContact(contactList);

        verify(useCase).saveMultipleListCachedContact(contactList);
    }

}
