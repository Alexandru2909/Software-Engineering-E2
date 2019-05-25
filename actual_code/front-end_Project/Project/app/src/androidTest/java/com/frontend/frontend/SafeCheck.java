package com.frontend.frontend.Main;


import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.frontend.frontend.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SafeCheck {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.INTERNET",
                    "android.permission.CAMERA",
                    "android.permission.READ_EXTERNAL_STORAGE",
                    "android.permission.WRITE_EXTERNAL_STORAGE");

    @Test
    public void safeCheck() {
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.startOcrBtn),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.getRoomTimeTable), withText("C309  - See info"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.subject1), withText("FREE ROOM."),
                        childAtPosition(
                                allOf(withId(R.id.first_period_card),
                                        childAtPosition(
                                                withId(R.id.first_period),
                                                0)),
                                1),
                        isDisplayed()));
        textView.check(matches(withText("FREE ROOM.")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.hour08), withText("08:00 10:00"),
                        childAtPosition(
                                allOf(withId(R.id.first_period_card),
                                        childAtPosition(
                                                withId(R.id.first_period),
                                                0)),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("08:00 10:00")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.subject2), withText("FREE ROOM."),
                        childAtPosition(
                                allOf(withId(R.id.second_period_card),
                                        childAtPosition(
                                                withId(R.id.second_period),
                                                0)),
                                1),
                        isDisplayed()));
        textView3.check(matches(withText("FREE ROOM.")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.hour10), withText("10:00 12:00"),
                        childAtPosition(
                                allOf(withId(R.id.second_period_card),
                                        childAtPosition(
                                                withId(R.id.second_period),
                                                0)),
                                0),
                        isDisplayed()));
        textView4.check(matches(withText("10:00 12:00")));

        ViewInteraction viewGroup = onView(
                allOf(withId(R.id.third_period_card),
                        childAtPosition(
                                allOf(withId(R.id.third_period),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class),
                                                0)),
                                0),
                        isDisplayed()));
        viewGroup.check(matches(isDisplayed()));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.tv), withText("Wednesday"),
                        childAtPosition(
                                withParent(withId(R.id.dayPicker)),
                                0),
                        isDisplayed()));
        textView5.check(matches(withText("Wednesday")));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.subject5), withText("METODOLOGII DE LUCRU IN MEDIUL OPEN SOURCE"),
                        childAtPosition(
                                allOf(withId(R.id.fifth_period_card),
                                        childAtPosition(
                                                withId(R.id.fifth_period),
                                                0)),
                                1),
                        isDisplayed()));
        textView6.check(matches(withText("METODOLOGII DE LUCRU IN MEDIUL OPEN SOURCE")));

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.hour16), withText("16:00 18:00"),
                        childAtPosition(
                                allOf(withId(R.id.fifth_period_card),
                                        childAtPosition(
                                                withId(R.id.fifth_period),
                                                0)),
                                0),
                        isDisplayed()));
        textView7.check(matches(withText("16:00 18:00")));

        pressBack();
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
