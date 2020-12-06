/*
package team.returnteamname.servicenovigrad.fragment;

import androidx.test.espresso.action.ViewActions;
import androidx.test.rule.ActivityTestRule;

import junit.framework.TestCase;

import org.junit.Rule;
import org.junit.Test;

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.activity.MainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class CustomerRatingBranchFragmentTest extends TestCase
{
    @Rule
    public ActivityTestRule<MainActivity> mainActivityTestRule = new
        ActivityTestRule<>(MainActivity.class);

    @Test
    public void loginIsvalid(){
        // log in
        onView(withId(R.id.buttonLogin)).perform(click());

        // navi to login in page
        onView(withId(R.id.layoutLoginActivity)).check(matches(isDisplayed()));

        // input correct username and password
        onView(withId(R.id.editTextUsername)).perform(typeText("JinGuo"),
                                                      ViewActions.closeSoftKeyboard());
        onView(withId(R.id.editTextPassword)).perform(typeText("123456789"),
                                                      ViewActions.closeSoftKeyboard());

        onView(withId(R.id.buttonLogin)).perform(click());


        // navi to welcome page
        onView(withId(R.id.welcomePage)).check(matches(isDisplayed()));

        // checking different functions
        //
    }
}

 */