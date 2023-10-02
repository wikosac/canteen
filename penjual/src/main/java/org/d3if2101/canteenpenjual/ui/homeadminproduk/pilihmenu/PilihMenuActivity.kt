package org.d3if2101.canteenpenjual.ui.homeadminproduk.pilihmenu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.d3if2101.canteenpenjual.R
import org.d3if2101.canteenpenjual.databinding.ActivityPilihMenuBinding

class PilihMenuActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPilihMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPilihMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}