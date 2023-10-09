package org.d3if2101.canteen.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import org.d3if2101.canteen.databinding.ActivityLoginBinding
import org.d3if2101.canteen.ui.ViewModelFactory
import org.d3if2101.canteen.ui.daftar.Daftar
import org.d3if2101.canteen.ui.dashboard.DashboardActivity

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this.application)
    }
    private val viewModel: LoginViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvTidakAdaAkun.setOnClickListener {
            startActivity(Intent(this, Daftar::class.java))
        }

        binding.tvButtonLogin.setOnClickListener { login() }
    }

    private fun login() {
        val email = binding.addEmail.text.toString()
        val sandi = binding.addPassword.text.toString()

        viewModel.loginUser(email, sandi).observe(this) {
            if (it.message.lowercase().equals("success")) {
                Toast.makeText(this, "Selamat Datang ${email}", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, DashboardActivity::class.java))
            } else {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}