package com.abidev.myusergithubhcs


import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.abidev.myusergithubhcs.view.home.UserAdapter
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {

    @Test
    fun when_screen_loads_list_should_appear_and_navigate_to_detail() {
        launchActivity<MainActivity>()
        Thread.sleep(5000)

        onView(withId(R.id.recyclerViewUsers)).perform(
            RecyclerViewActions.actionOnItemAtPosition<UserAdapter.UserViewHolder>(0, click())
        )

        Thread.sleep(5000)

        onView(withId(R.id.textUsername)).check(matches(isDisplayed()))
    }
}

