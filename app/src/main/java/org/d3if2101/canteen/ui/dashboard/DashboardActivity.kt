package org.d3if2101.canteen.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import org.d3if2101.canteen.MainPenjualActivity
import org.d3if2101.canteen.databinding.ActivityDashboardBinding
import org.d3if2101.canteen.ui.ViewModelFactory
import org.d3if2101.canteen.ui.login.LoginViewModel
import org.d3if2101.canteen.ui.menu.MenuActivity

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this.application)
    }
    private val viewModel: LoginViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getTokenPref().observe(this) { token ->
            viewModel.getUserWithToken(token!!).observe(this) {
                binding.txtGreeting.text = "Selamat datang, ${it.nama}"
            }
        }

        with(binding) {
            btnKantin1.setOnClickListener { navTo("kantin1") }
            btnKantin2.setOnClickListener { navTo("kantin2") }
            btnKantin3.setOnClickListener { navTo("kantin3") }
            btnKantin4.setOnClickListener { navTo("kantin4") }
        }
    }

    private fun navTo(idKantin: String) {
        val intent = Intent(this, MenuActivity::class.java)
        intent.putExtra("idKantin", idKantin)
        startActivity(intent)
    }

    companion object {
        const val TAG = "testo"
    }
}