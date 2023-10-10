package org.d3if2101.canteenpenjual.ui.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.d3if2101.canteenpenjual.databinding.ActivityDashboardBaruBinding
import org.d3if2101.canteenpenjual.ui.homeadminproduk.HomeProduk
import org.d3if2101.canteenpenjual.ui.pendapatan.PendapatanActivity
import org.d3if2101.canteenpenjual.ui.rating.RatingActivity
import org.d3if2101.canteenpenjual.ui.riwayat.RiwayatActivity

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBaruBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBaruBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.penjualCardProduk.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, HomeProduk::class.java))
        }

        binding.penjualCardPendapatan.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, PendapatanActivity::class.java))
        }

        binding.penjualCardOrder.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, RiwayatActivity::class.java))
        }

        binding.penjualCardRating.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, RatingActivity::class.java))
        }
    }
}