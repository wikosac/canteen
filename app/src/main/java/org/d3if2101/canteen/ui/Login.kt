package org.d3if2101.canteen.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.d3if2101.canteen.databinding.ActivityLoginBinding
import org.d3if2101.canteen.ui.dashboard.DashboardActivity

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvButtonLogin.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }
        binding.tvTidakAdaAkun.setOnClickListener {
            startActivity(Intent(this, Daftar::class.java))
        }
    }
}