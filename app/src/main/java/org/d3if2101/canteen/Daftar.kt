package org.d3if2101.canteen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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