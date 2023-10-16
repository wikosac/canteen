package org.d3if2101.canteen.ui.penjual.daftar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import org.d3if2101.canteen.databinding.ActivityDaftarPenjualBinding
import org.d3if2101.canteen.ui.ViewModelFactory
import org.d3if2101.canteen.ui.daftar.DaftarViewModel
import org.d3if2101.canteen.ui.login.Login

class DaftarPenjualActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDaftarPenjualBinding
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this.application)
    }
    private val viewModel: DaftarViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarPenjualBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvSudahAdaAkun.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }

        binding.tvButtonRegister.setOnClickListener { register() }
    }

    private fun register() {
        val nama = binding.addNama.text.toString()
        val email = binding.addEmail.text.toString()
        val noTelpon = binding.addTelepon.text.toString()
        val password = binding.addPassword.text.toString()
        val role = "Penjual"

        viewModel.signUpUser(email, password).observe(this) {
            Log.d("DaftarActivity", it.message)
            if (it.message.lowercase() == "success") {
                viewModel.insertToDB(email, nama, noTelpon, role)
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, Login::class.java))
                finish()
            } else {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }

        }
    }
}