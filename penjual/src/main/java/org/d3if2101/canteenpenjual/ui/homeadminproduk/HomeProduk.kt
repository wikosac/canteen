package org.d3if2101.canteenpenjual.ui.homeadminproduk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.d3if2101.canteenpenjual.R
import org.d3if2101.canteenpenjual.databinding.ActivityHomeAdminBinding
import org.d3if2101.canteenpenjual.databinding.ActivityHomeProdukBinding

class HomeProduk : AppCompatActivity() {

    private lateinit var binding: ActivityHomeProdukBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeProdukBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}