package org.d3if2101.canteen.ui.daftar

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import org.d3if2101.canteen.databinding.ActivityDaftarBinding
import org.d3if2101.canteen.ui.ViewModelFactory
import org.d3if2101.canteen.ui.login.Login
import org.d3if2101.canteenpenjual.ui.daftar.DaftarActivity

class Daftar : AppCompatActivity() {

    private lateinit var binding: ActivityDaftarBinding
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this.application)
    }
    private val viewModel: DaftarViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvSudahAdaAkun.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }

        binding.txtDaftarPenjual.setOnClickListener {
            startActivity(Intent(this, DaftarActivity::class.java))
        }

        binding.tvButtonLogin.setOnClickListener { login() }

    }

    private fun login() {
        val nama = binding.addNama.text.toString()
        val email = binding.addEmail.text.toString()
        val telp = binding.addTelepon.text.toString()
        val sandi = binding.addPassword.text.toString()

        viewModel.signUpUser(email, sandi).observe(this) {
            if (it.message.lowercase() == "success") {
                viewModel.insertToDB(email, nama, telp)
                startActivity(Intent(this, Login::class.java))
            }
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        }
    }
}