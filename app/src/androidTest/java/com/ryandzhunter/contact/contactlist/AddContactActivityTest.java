package com.ryandzhunter.contact.contactlist;


import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.ryandzhunter.contact.R;
import com.ryandzhunter.contact.addcontact.AddContactActivity;
import com.ryandzhunter.contact.addcontact.AddContactViewModel;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddContactActivityTest {

    @Inject
    AddContactViewModel viewModel;

    @Rule
    public ActivityTestRule<AddContactActivity> mActivityTestRule = new ActivityTestRule<>(AddContactActivity.class);

    @Before
    public void setUp(){

    }

    @Test
    public void CheckValidNameTest(){
        ViewInteraction textFirstNameError = onView(
                allOf(withId(R.id.add_contact_first_name_error), withText("First name should be more than 3 characters"),
                        isDisplayed()));
        textFirstNameError.check(matches(isDisplayed()));

        ViewInteraction textLastNameError = onView(
                allOf(withId(R.id.add_contact_last_name_error), withText("Last name should be more than 3 characters"),
                        isDisplayed()));
        textLastNameError.check(matches(isDisplayed()));

        ViewInteraction editFirstName = onView(allOf(withId(R.id.add_contact_first_name), isDisplayed()));

        editFirstName.perform(typeText("aa"), closeSoftKeyboard());

        textFirstNameError.check(matches(isDisplayed()));

        ViewInteraction editLastName = onView(allOf(withId(R.id.add_contact_last_name), isDisplayed()));

        editLastName.perform(typeText("bb"), closeSoftKeyboard());

        textLastNameError.check(matches(isDisplayed()));

        editFirstName.perform(clearText());

        editFirstName.perform(typeText("aaa"), closeSoftKeyboard());

        textFirstNameError.check(doesNotExist());

        editLastName.perform(clearText());

        editLastName.perform(typeText("bbb"), closeSoftKeyboard());

        textLastNameError.check(doesNotExist());

    }

    @Test
    public void checkValidPhoneTest(){

        ViewInteraction textPhoneError = onView(
                allOf(withId(R.id.add_contact_phone_error), withText("Phone number should be of 10 digits"),
                        isDisplayed()));
        textPhoneError.check(matches(isDisplayed()));

        ViewInteraction editPhone = onView(allOf(withId(R.id.add_contact_phone), isDisplayed()));

        editPhone.perform(typeText("08122222"), closeSoftKeyboard());

        textPhoneError.check(matches(isDisplayed()));

        editPhone.perform(clearText());

        editPhone.perform(typeText("081282100812"), closeSoftKeyboard());

        textPhoneError.check(doesNotExist());

    }

    @Test
    public void checkValidEmailTest(){

        Espresso.onView(ViewMatchers.withId(R.id.scroll_view)).perform(ViewActions.swipeUp());

        ViewInteraction textEmailError = onView(
                allOf(withId(R.id.add_contact_email_error), withText("Email should be in email format"),
                        isDisplayed()));
        textEmailError.check(matches(isDisplayed()));

        ViewInteraction editEmail = onView(allOf(withId(R.id.add_contact_email), isDisplayed()));

        editEmail.perform(typeText("asasas"), closeSoftKeyboard());

        textEmailError.check(matches(isDisplayed()));

        editEmail.perform(clearText());

        editEmail.perform(typeText("gaga@gm"), closeSoftKeyboard());

        textEmailError.check(matches(isDisplayed()));

        editEmail.perform(clearText());

        editEmail.perform(typeText("gaga@gmail.com"), closeSoftKeyboard());

        textEmailError.check(doesNotExist());

    }


    @Test
    public void addContactActivityTest() {

        String firstName = "Aryandi";
        String lastName = "Putra";
        String phone = "085768818982";
        String email = "aryandi2712@gmail.com";

        ViewInteraction textFirstNameError = onView(
                allOf(withId(R.id.add_contact_first_name_error), withText("First name should be more than 3 characters"),
                        isDisplayed()));
        textFirstNameError.check(matches(isDisplayed()));

        ViewInteraction textLastNameError = onView(
                allOf(withId(R.id.add_contact_last_name_error), withText("Last name should be more than 3 characters"),
                        isDisplayed()));
        textLastNameError.check(matches(isDisplayed()));

        ViewInteraction textPhoneError = onView(
                allOf(withId(R.id.add_contact_phone_error), withText("Phone number should be of 10 digits"),
                        isDisplayed()));
        textPhoneError.check(matches(isDisplayed()));

        ViewInteraction textEmailError = onView(
                allOf(withId(R.id.add_contact_email_error), withText("Email should be in email format"),
                        isDisplayed()));
        textEmailError.check(matches(isDisplayed()));

        ViewInteraction editFirstName = onView(allOf(withId(R.id.add_contact_first_name), isDisplayed()));

        editFirstName.perform(typeText(firstName), closeSoftKeyboard());

        textFirstNameError.check(doesNotExist());

        ViewInteraction editLastName = onView(allOf(withId(R.id.add_contact_last_name), isDisplayed()));

        editLastName.perform(typeText(lastName), closeSoftKeyboard());

        textLastNameError.check(doesNotExist());

        ViewInteraction editPhone = onView(allOf(withId(R.id.add_contact_phone), isDisplayed()));

        editPhone.perform(typeText(phone), closeSoftKeyboard());

        textPhoneError.check(doesNotExist());

        ViewInteraction editEmail = onView(allOf(withId(R.id.add_contact_email), isDisplayed()));

        editEmail.perform(typeText(email), closeSoftKeyboard());

        textEmailError.check(doesNotExist());

    }


}
