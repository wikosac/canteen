package org.d3if2101.canteenpenjual.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.d3if2101.canteenpenjual.R
import org.d3if2101.canteenpenjual.databinding.ActivityLoginBinding
import org.d3if2101.canteenpenjual.ui.daftar.DaftarActivity
import org.d3if2101.canteenpenjual.ui.homeadmin.HomeAdminActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var email: String
    private lateinit var password: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvTidakAdaAkun.setOnClickListener {
            startActivity(Intent(this@LoginActivity, DaftarActivity::class.java)) // Move to Daftar Activity
        }

        binding.tvButtonLogin.setOnClickListener {
            getTextInput()
            startActivity(Intent(this@LoginActivity, HomeAdminActivity::class.java)) // Move to HomeAdmin Activity
        }

    }

    private fun getTextInput(){
        email = binding.addEmail.text.toString()
        password = binding.addPassword.text.toString()
    }
}