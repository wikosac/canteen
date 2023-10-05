package org.d3if2101.canteenpenjual.ui.homeadminproduk

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.d3if2101.canteenpenjual.databinding.ActivityHomeProdukBinding
import org.d3if2101.canteenpenjual.ui.homeadminproduk.pilihmenu.PilihMenuActivity

class HomeProduk : AppCompatActivity() {

    private lateinit var binding: ActivityHomeProdukBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeProdukBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.linearLayout.setOnClickListener {
            changeActivity("makanan")
        }

        binding.linearLayout2.setOnClickListener {
            changeActivity("minuman")
        }

        binding.linearLayout3.setOnClickListener {
            changeActivity("camilan")
        }

    }

    private fun searchNamaProduk() {

    }

    private fun changeActivity(name: String) {
        val intent = Intent(this, PilihMenuActivity::class.java)
        intent.putExtra("produk", name)
        startActivity(intent)
    }

    private fun menuBar() {

    }
}