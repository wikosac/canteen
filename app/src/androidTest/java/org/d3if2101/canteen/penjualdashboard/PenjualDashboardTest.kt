package org.d3if2101.canteen

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.d3if2101.canteen.ui.penjual.dashboard.DashboardPenjualActivity
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PenjualDashboardTest {
    @Test
    fun testOpenHomeProdukActivity() {
        ActivityScenario.launch(DashboardPenjualActivity::class.java)

        // Menekan tampilan (View) dengan id binding.penjualCardProduk
        Espresso.onView(withId(R.id.penjual_card_produk)).perform(click())

        // Memeriksa bahwa aktivitas HomeProduk terbuka
        Espresso.onView(withId(R.id.activity_home_produk_layout)).check(matches(isDisplayed()))
    }

    @Test
    fun testOpenPendapatanActivity() {
        ActivityScenario.launch(DashboardPenjualActivity::class.java)
        // Menekan tampilan (View) dengan id binding.penjualCardPendapatan
        Espresso.onView(withId(R.id.penjual_card_pendapatan)).perform(click())

        // Memeriksa bahwa aktivitas PendapatanActivity terbuka
        Espresso.onView(withId(R.id.cardView_layout)).check(matches(isDisplayed()))
    }

    @Test
    fun testOpenOrderPenjualActivity() {
        ActivityScenario.launch(DashboardPenjualActivity::class.java)
        // Menekan tampilan (View) dengan id binding.penjualCardOrder
        Espresso.onView(withId(R.id.penjual_card_order)).perform(click())

        // Memeriksa bahwa aktivitas OrderPenjualActivity terbuka
        Espresso.onView(withId(R.id.riwayat_layout)).check(matches(isDisplayed()))
    }

}
