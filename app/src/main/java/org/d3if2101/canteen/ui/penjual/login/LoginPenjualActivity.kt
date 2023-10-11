package org.d3if2101.canteen.ui.penjual.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import org.d3if2101.canteen.databinding.ActivityLoginPenjualBinding
import org.d3if2101.canteen.ui.ViewModelFactory
import org.d3if2101.canteen.ui.login.LoginViewModel
import org.d3if2101.canteen.ui.penjual.daftar.DaftarPenjualActivity
import org.d3if2101.canteen.ui.penjual.dashboard.DashboardPenjualActivity

class LoginPenjualActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginPenjualBinding
    private lateinit var email: String
    private lateinit var password: String

    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this.application)
    }
    private val viewModel: LoginViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginPenjualBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.tvTidakAdaAkun.setOnClickListener {
            startActivity(
                Intent(
                    this@LoginPenjualActivity,
                    DaftarPenjualActivity::class.java
                )
            ) // Move to Daftar Activity
        }

        binding.tvButtonLogin.setOnClickListener {
            getTextInput()
            viewModel.loginUser(email, password).observe(this) {
                if (it.message.lowercase().equals("success")) {
                    Toast.makeText(this, "Selamat Datang ${email}", Toast.LENGTH_SHORT).show()
                    startActivity(
                        Intent(
                            this@LoginPenjualActivity,
                            DashboardPenjualActivity::class.java
                        )
                    ) // Move to Dashboard Admin Activity
                } else {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }

            }

        }

    }

    private fun getTextInput() {
        email = binding.addEmail.text.toString()
        password = binding.addPassword.text.toString()
    }
}