package org.d3if2101.canteen.ui.daftar

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.d3if2101.canteen.R
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DaftarTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(Daftar::class.java)

    @Test
    fun testRegisterSuccess() {

        // Masukkan data
        Espresso.onView(ViewMatchers.withId(R.id.add_nama))
            .perform(ViewActions.typeText("upin"))
        Espresso.onView(ViewMatchers.withId(R.id.add_email))
            .perform(ViewActions.typeText("upin123@gmail.com"))
        Espresso.onView(ViewMatchers.withId(R.id.add_telepon))
            .perform(ViewActions.typeText("0123456789"))
        Espresso.onView(ViewMatchers.withId(R.id.add_password))
            .perform(ViewActions.typeText("upin123"))

        Thread.sleep(2000)

        Espresso.onView(ViewMatchers.withId(R.id.tv_button_register))
            .perform(ViewActions.click())

        Thread.sleep(2000)

        // Menuju Activity Login
        Espresso.onView(ViewMatchers.withId(R.id.tv_login)).check(
            ViewAssertions.matches(
                ViewMatchers.isDisplayed()
            )
        )
    }
}