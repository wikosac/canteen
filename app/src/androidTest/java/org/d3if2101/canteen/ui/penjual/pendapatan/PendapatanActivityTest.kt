package org.d3if2101.canteen.ui.penjual.pendapatan

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.d3if2101.canteen.R
import org.d3if2101.canteen.datamodels.OrderHistoryItem
import org.junit.Test

class PendapatanActivityTest {

    @Test
    fun testRecyclerViewScrollAndDataInsertion() {
        // Memulai aktivitas PendapatanActivity
        val activityScenario = ActivityScenario.launch(PendapatanActivity::class.java)

        // Menambahkan data palsu sebanyak 20 item ke RecyclerView
        val fakeData = generateFakeData(20)
        activityScenario.onActivity { activity ->
            activity.setItemData(fakeData)
        }

        // Memeriksa apakah RecyclerView dengan ID R.id.rv ditampilkan
        Espresso.onView(withId(R.id.rv)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // Melakukan scroll ke item ke-19 (indeks 18)
        Espresso.onView(withId(R.id.rv)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(18)
        )

        // Anda dapat menambahkan asser sesuai kebutuhan untuk memeriksa item-item yang ditampilkan.
    }

    private fun generateFakeData(count: Int): List<OrderHistoryItem> {
        val fakeData = mutableListOf<OrderHistoryItem>()
        for (i in 1..count) {
            fakeData.add(
                OrderHistoryItem(
                    id = i,
                    orderId = "ORDER_ID $i",
                    date = "DATE $i",
                    orderStatus = "ORDER_STATUS $i",
                    orderPayment = "ORDER_PAYMENT $i",
                    price = "Rp ${i}0,000"
                )
            )
        }
        return fakeData
    }
}
