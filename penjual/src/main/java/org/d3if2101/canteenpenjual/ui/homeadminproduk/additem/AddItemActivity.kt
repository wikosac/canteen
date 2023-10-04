package org.d3if2101.canteenpenjual.ui.homeadminproduk.additem

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.d3if2101.canteenpenjual.databinding.ActivityAddItemBinding

class AddItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddItemBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()


    }


}