package org.d3if2101.canteen.ui.login

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.d3if2101.canteen.R
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(Login::class.java)

    @Test
    fun testLoginSuccess() {

        // Masukkan email dan kata sandi yang valid
        Espresso.onView(ViewMatchers.withId(R.id.add_email))
            .perform(ViewActions.typeText("ehsan@gmail.com"))
        Espresso.onView(ViewMatchers.withId(R.id.add_password))
            .perform(ViewActions.typeText("ehsan123"))

        // Klik tombol login
        Espresso.onView(ViewMatchers.withId(R.id.tv_button_login))
            .perform(ViewActions.click())

        Thread.sleep(2000)

//        // Menuju dashboard
//        Espresso.onView(ViewMatchers.withId(R.id.txt_kantin1)).check(
//            ViewAssertions.matches(
//                ViewMatchers.isDisplayed()
//            )
//        )
    }
}