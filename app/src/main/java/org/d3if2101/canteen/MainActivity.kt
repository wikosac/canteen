package org.d3if2101.canteen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import org.d3if2101.canteen.databinding.ActivityMainBinding
import org.d3if2101.canteen.ui.ViewModelFactory
import org.d3if2101.canteen.ui.dashboard.DashboardActivity
import org.d3if2101.canteen.ui.login.Login
import org.d3if2101.canteen.ui.login.LoginViewModel
import org.d3if2101.canteen.ui.penjual.dashboard.DashboardPenjualActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this.application)
    }
    private val viewModel: LoginViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({ navigate() }, 2000)
    }

    private fun navigate() {
        viewModel.getTokenPref().observe(this) { token ->
            Log.d(TAG, "onCreateToken: $token")
            if (token == null) startActivity(Intent(this, Login::class.java))

            viewModel.getUserWithToken(token!!).observe(this) { user ->
                if (user.role == "penjual") {
                    startActivity(Intent(this, DashboardPenjualActivity::class.java))
                } else {
                    startActivity(Intent(this, DashboardActivity::class.java))
                }
                Log.d(TAG, "navigate user: $user")
            }
        }
    }

    companion object {
        const val TAG = "testo"
    }
}