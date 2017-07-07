package com.ryandzhunter.contact;

import android.content.Context;
import android.util.Patterns;

import com.ryandzhunter.contact.addcontact.AddContactView;
import com.ryandzhunter.contact.addcontact.AddContactViewModel;
import com.ryandzhunter.contact.data.datastore.ContactListDataStore;
import com.ryandzhunter.contact.data.model.Contact;
import com.ryandzhunter.contact.usecase.GetContactListUseCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Completable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by aryandi on 7/7/17.
 */

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

    @Before
    public void setUpDetailViewModelTest() {
        MockitoAnnotations.initMocks(this);
        addContactViewModel = new AddContactViewModel(fakeContext, useCase, addContactView);
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

//        Field field = field(Patterns.class, "EMAIL_ADDRESS");
//        field.set(Patterns.class, mock(Pattern.class));
        Field field = mock(Field.class);
        Patterns patterns = mock(Patterns.class);

        // prepare matcher
        Matcher matcher = mock(Matcher.class);
        when(matcher.matches())
                .thenReturn(true);

        // final mock
        when(Patterns.EMAIL_ADDRESS.matcher("aryandi@gmail.com"))
                .thenReturn(matcher);

//        assertTrue(addContactViewModel.isValidEmail("aryandi@gmail.com"));
//        assertFalse(addContactViewModel.isValidEmail("aryandi"));
//        assertFalse(addContactViewModel.isValidEmail("@aryandi"));
//        assertFalse(addContactViewModel.isValidEmail("aryandi@gmail."));
    }

    @Test
    public void addContactToCache() {
        when(useCase.saveCachedContact(contact)).thenReturn(Completable.complete());
        addContactViewModel.addContactToCache(contact);
        verify(useCase).saveCachedContact(contact);
    }
}
