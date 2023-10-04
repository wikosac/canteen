package org.d3if2101.canteenpenjual.ui.daftar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import org.d3if2101.canteenpenjual.databinding.ActivityDaftarBinding
import org.d3if2101.canteenpenjual.ui.ViewModelFactory
import org.d3if2101.canteenpenjual.ui.login.LoginActivity

class DaftarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDaftarBinding

    private lateinit var nama: String
    private lateinit var email: String
    private lateinit var noTelpon: String
    private lateinit var password: String

    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this.application)
    }
    private val viewModel: DaftarViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.tvButtonLogin.setOnClickListener {
            getTextInput()

            viewModel.signUpUser(email, password).observe(this) {
                Log.d("DaftarActivity", it.message)
                if (it.message.lowercase().equals("success")) {
                    viewModel.insertToDB(email, nama, noTelpon)
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@DaftarActivity, LoginActivity::class.java))
                } else {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }

            }
        }

    }

    private fun getTextInput() {
        nama = binding.addNama.text.toString()
        email = binding.addEmail.text.toString()
        noTelpon = binding.addTelepon.text.toString()
        password = binding.addPassword.text.toString()
    }
}