package com.example.dictionary.ui.fragments

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.dictionary.FragmentTestHelper
import com.example.dictionary.R
import com.example.dictionary.ui.activities.MainActivity
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by PraNeeTh on 4/13/20
 */
@RunWith(AndroidJUnit4::class)
class SearchFragmentTests {
    @get:Rule
    var mActivityTestRule = FragmentTestHelper(MainActivity::class.java)

    @Test
    fun whenLaunchedCheckIfEditTextAndButtonArePresentWithCorrectTextValuesAndList() {
        onView(withId(R.id.et_searchWord))
            .check(matches(isDisplayed()))
        onView(withId(R.id.btn_search))
            .check(matches(isDisplayed()))
        onView(withId(R.id.btn_search))
            .check(matches(withText("Search")))
        onView(withId(R.id.btn_search))
            .check(matches(isClickable()))
        onView(withId(R.id.list_results)).check(matches(not(isDisplayed())))
    }
}