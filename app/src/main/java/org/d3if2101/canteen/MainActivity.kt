package org.d3if2101.canteen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import org.d3if2101.canteen.databinding.ActivityMainBinding
import org.d3if2101.canteen.ui.ViewModelFactory
import org.d3if2101.canteen.ui.dashboard.DashboardActivity
import org.d3if2101.canteen.ui.login.LoginViewModel
import org.d3if2101.canteen.ui.penjual.dashboard.DashboardPenjualActivity
import org.d3if2101.canteen.ui.penjual.login.LoginPenjualActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this.application)
    }
    private val viewModel: LoginViewModel by viewModels { factory }
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        token = viewModel.getTokenPref().value.toString()

        Handler(Looper.getMainLooper()).postDelayed({ navigate() }, 3000)
    }

    private fun navigate() {
        if (token != null) {
            viewModel.checkRole().observe(this) { role ->
                if (role == "penjual") {
                    startActivity(Intent(this, DashboardPenjualActivity::class.java))
                } else {
                    startActivity(Intent(this, DashboardActivity::class.java))
                }
            }
        }
        startActivity(Intent(this, LoginPenjualActivity::class.java))
    }
}