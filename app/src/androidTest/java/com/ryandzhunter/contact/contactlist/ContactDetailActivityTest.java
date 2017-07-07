package com.ryandzhunter.contact.contactlist;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

import com.ryandzhunter.contact.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ContactDetailActivityTest {

    @Rule
    public ActivityTestRule<ContactListActivity> mActivityTestRule = new ActivityTestRule<>(ContactListActivity.class);

    @Test
    public void contactDetailActivityTest() {
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.rv_contact_list),
                        withParent(withId(R.id.swipeContainer)),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(14, click()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.contact_detail_fullname), withText("Jamie  carragher"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.contact_detail_toolbar_layout),
                                        1),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Jamie  carragher")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.contact_detail_email), withText("jamie@carragher.com"),
                        childAtPosition(
                                allOf(withId(R.id.contact_detail_email_content),
                                        childAtPosition(
                                                withId(R.id.content_detail_email_layout),
                                                1)),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("jamie@carragher.com")));

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.contact_detail_call),
                        childAtPosition(
                                allOf(withId(R.id.contact_detail_phone_layout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                0)),
                                0),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction imageButton2 = onView(
                allOf(withId(R.id.contact_detail_iv_email),
                        childAtPosition(
                                allOf(withId(R.id.content_detail_email_layout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                1)),
                                0),
                        isDisplayed()));
        imageButton2.check(matches(isDisplayed()));

        ViewInteraction imageButton3 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.contact_detail_phone_layout),
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        0)),
                        2),
                        isDisplayed()));
        imageButton3.check(matches(isDisplayed()));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.action_favourite), withContentDescription("Favourite"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.contact_detail_toolbar),
                                        1),
                                0),
                        isDisplayed()));
        textView3.check(matches(withText("")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.action_edit), withContentDescription("Edit"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.contact_detail_toolbar),
                                        1),
                                1),
                        isDisplayed()));
        textView4.check(matches(withText("")));

        ViewInteraction imageView = onView(
                allOf(withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.contact_detail_toolbar),
                                        1),
                                2),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

        ViewInteraction imageView2 = onView(
                allOf(withContentDescription("More options"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.contact_detail_toolbar),
                                        1),
                                2),
                        isDisplayed()));
        imageView2.check(matches(isDisplayed()));

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.contact_detail_call),
                        withParent(withId(R.id.contact_detail_phone_layout)),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.contact_detail_iv_email),
                        withParent(withId(R.id.content_detail_email_layout)),
                        isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withClassName(is("android.support.v7.widget.AppCompatImageButton")),
                        withParent(withId(R.id.contact_detail_phone_layout)),
                        isDisplayed()));
        appCompatImageButton3.perform(click());

        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.action_favourite), withContentDescription("Favourite"), isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction actionMenuItemView2 = onView(
                allOf(withId(R.id.action_edit), withContentDescription("Edit"), isDisplayed()));
        actionMenuItemView2.perform(click());

        ViewInteraction editText = onView(
                allOf(withId(R.id.add_contact_first_name), withText("Jamie "),
                        childAtPosition(
                                allOf(withId(R.id.add_contact_first_name_layout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                                1)),
                                0),
                        isDisplayed()));
        editText.check(matches(withText("Jamie ")));

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.add_contact_last_name), withText("carragher"),
                        childAtPosition(
                                allOf(withId(R.id.add_contact_last_name_layout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                                1)),
                                0),
                        isDisplayed()));
        editText2.check(matches(withText("carragher")));

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.add_contact_phone), withText("1234567891"),
                        childAtPosition(
                                allOf(withId(R.id.add_contact_phone_layout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                                1)),
                                0),
                        isDisplayed()));
        editText3.check(matches(withText("1234567891")));

        ViewInteraction editText4 = onView(
                allOf(withId(R.id.add_contact_email), withText("jamie@carragher.com"),
                        childAtPosition(
                                allOf(withId(R.id.add_contact_email_layout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                                1)),
                                0),
                        isDisplayed()));
        editText4.check(matches(withText("jamie@carragher.com")));

        ViewInteraction imageButton4 = onView(
                allOf(childAtPosition(
                        childAtPosition(
                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                0),
                        1),
                        isDisplayed()));
        imageButton4.check(matches(isDisplayed()));

        ViewInteraction imageView3 = onView(
                allOf(withId(R.id.save),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                        2),
                                0),
                        isDisplayed()));
        imageView3.check(matches(isDisplayed()));

        ViewInteraction imageView4 = onView(
                allOf(withId(R.id.save),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                        2),
                                0),
                        isDisplayed()));
        imageView4.check(matches(isDisplayed()));

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.save), isDisplayed()));
        appCompatImageView.perform(click());

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
