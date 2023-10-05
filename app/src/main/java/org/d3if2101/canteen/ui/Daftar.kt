package org.d3if2101.canteen.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.d3if2101.canteen.databinding.ActivityDaftarBinding

class Daftar : AppCompatActivity() {

    private lateinit var binding: ActivityDaftarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvSubDaftar.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }
        binding.tvDaftarTitle.setOnClickListener{
            startActivity(Intent(this, Login::class.java))
        }
    }
}