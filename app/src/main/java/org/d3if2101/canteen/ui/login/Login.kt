package org.d3if2101.canteen.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import org.d3if2101.canteen.databinding.ActivityLoginBinding
import org.d3if2101.canteen.ui.ViewModelFactory
import org.d3if2101.canteen.ui.daftar.Daftar
import org.d3if2101.canteen.ui.dashboard.DashboardActivity
import org.d3if2101.canteen.ui.penjual.dashboard.DashboardPenjualActivity

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    var message: String = ""

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

        viewModel.loginUser(email, sandi).observe(this) { msg ->
            message = msg.message
            if (message == "Success") moveTo()
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun moveTo() {
        viewModel.getUser(this).observe(this) { user ->
            Log.d(TAG, "login: $user")
            if (user != null) {
                viewModel.saveTokenPref(user.uid)
                viewModel.getUserWithToken(user.uid).observe(this) {
                    if (it.role.lowercase() == "penjual") {
                        val intent = Intent(this, DashboardPenjualActivity::class.java)
                        intent.putExtra("userName", user.nama)
                        startActivity(intent)
                        finish()
                    } else {
                        startActivity(Intent(this, DashboardActivity::class.java))
                        finish()
                    }
                }
                Toast.makeText(this, "Selamat Datang ${user.nama}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val TAG = "testo"
    }

}