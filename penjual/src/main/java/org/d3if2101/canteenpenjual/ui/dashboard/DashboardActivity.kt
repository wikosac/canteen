package org.d3if2101.canteenpenjual.ui.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.d3if2101.canteenpenjual.R
import org.d3if2101.canteenpenjual.databinding.ActivityDashboardBinding
import org.d3if2101.canteenpenjual.ui.homeadminproduk.HomeProduk

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.cardProduk.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, HomeProduk::class.java))
        }
    }
}