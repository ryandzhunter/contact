package com.ryandzhunter.contact;

import android.content.Context;

import com.ryandzhunter.contact.addcontact.AddContactView;
import com.ryandzhunter.contact.addcontact.AddContactViewModel;
import com.ryandzhunter.contact.contactdetail.ContactDetailView;
import com.ryandzhunter.contact.contactdetail.ContactDetailViewModel;
import com.ryandzhunter.contact.data.datastore.ContactListDataStore;
import com.ryandzhunter.contact.data.model.Contact;
import com.ryandzhunter.contact.http.RetrofitService;
import com.ryandzhunter.contact.usecase.GetContactListUseCase;

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

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.schedulers.Schedulers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by aryandi on 7/7/17.
 */
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

        contactDetailViewModel = new ContactDetailViewModel(fakeContext, useCase, contactDetailView);

    }

    @AfterClass
    public static void tearDownClass() {
        RxAndroidPlugins.reset();
    }

    @Test
    public void testFetchContactDetail() {

        when(useCase.getContactDetail(contact.id)).thenReturn(new Flowable<Contact>() {
            @Override
            protected void subscribeActual(Subscriber<? super Contact> s) {

            }
        });

        contactDetailViewModel.fetchContactDetail(contact.id);

        verify(useCase).getContactDetail(contact.id);
    }

    @Test
    public void testUpdateContact() {

        when(useCase.updateContact(contact.id, contact)).thenReturn(new Flowable<Contact>() {
            @Override
            protected void subscribeActual(Subscriber<? super Contact> s) {

            }
        });

        contactDetailViewModel.updateContact(contact.id, contact);

        verify(useCase).updateContact(contact.id,contact);
    }

    @Test
    public void testUpdateCachedContact() {
        when(useCase.updateCachedContact(contact)).thenReturn(Completable.complete());
        contactDetailViewModel.updateCachedContact(contact);
        verify(useCase).updateCachedContact(contact);
    }

    @Test
    public void deleteContact() {
        when(useCase.deleteContact(contact.id)).thenReturn(Completable.complete());
        contactDetailViewModel.deleteContact(contact.id);
        verify(useCase).deleteContact(contact.id);
    }
}
