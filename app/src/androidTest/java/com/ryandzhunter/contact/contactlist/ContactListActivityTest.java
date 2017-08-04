package com.ryandzhunter.contact.contactlist;

import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.ryandzhunter.contact.ContactApp;
import com.ryandzhunter.contact.R;
import com.ryandzhunter.contact.dagger.DaggerTestComponent;
import com.ryandzhunter.contact.dagger.TestComponent;
import com.ryandzhunter.contact.dagger.module.AppModule;
import com.ryandzhunter.contact.dagger.module.DataStoreModule;
import com.ryandzhunter.contact.dagger.module.RetrofitModule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import okhttp3.mockwebserver.MockWebServer;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ContactListActivityTest {

    @Rule
    public ActivityTestRule<ContactListActivity> mActivityTestRule = new ActivityTestRule<>(ContactListActivity.class);
    private MockWebServer mMockWebServer;
    private TestComponent testComponent;

    @Before
    public void setUp() throws IOException {

        mMockWebServer = new MockWebServer();
        mMockWebServer.start();

        testComponent = DaggerTestComponent.builder()
                .appModule(new AppModule(ContactApp.getApp()))
                .retrofitModule(new RetrofitModule())
                .dataStoreModule(new DataStoreModule()).build();
        testComponent.inject(this);
    }


    @Test
    public void contactListActivityTest() {
        ViewInteraction relativeLayout = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.include),
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.view.View.class),
                                        0)),
                        0),
                        isDisplayed()));
        relativeLayout.check(matches(isDisplayed()));

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.fab_add_contact),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.rv_contact_list),
                        childAtPosition(
                                allOf(withId(R.id.swipeContainer),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.view.View.class),
                                                1)),
                                0),
                        isDisplayed()));
        recyclerView.check(doesNotExist());

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.rv_contact_list),
                        childAtPosition(
                                allOf(withId(R.id.swipeContainer),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.view.View.class),
                                                1)),
                                0),
                        isDisplayed()));
        recyclerView2.check(doesNotExist());

        ViewInteraction relativeLayout2 = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.rv_contact_list),
                                childAtPosition(
                                        withId(R.id.swipeContainer),
                                        0)),
                        0),
                        isDisplayed()));
        relativeLayout2.check(matches(isDisplayed()));

        ViewInteraction imageView = onView(
                allOf(withId(R.id.favorite_check_box),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.rv_contact_list),
                                        0),
                                0),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.contact_list_alphabet),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.rv_contact_list),
                                        0),
                                1),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.contact_name),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.rv_contact_list),
                                        0),
                                3),
                        isDisplayed()));
        textView2.check(matches(isDisplayed()));

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

    @After
    public void tearDown() throws Exception {
        mMockWebServer.shutdown();
    }
}
