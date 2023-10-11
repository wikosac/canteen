package org.d3if2101.canteen.ui.penjual.daftar

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import org.d3if2101.canteen.databinding.ActivityDaftarPenjualBinding
import org.d3if2101.canteen.ui.penjual.ViewModelFactory

class DaftarPenjualActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDaftarPenjualBinding

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
        binding = ActivityDaftarPenjualBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.tvSudahAdaAkun.setOnClickListener { finish() }

        binding.tvButtonLogin.setOnClickListener {
            getTextInput()

            viewModel.signUpUser(email, password).observe(this) {
                Log.d("DaftarActivity", it.message)
                if (it.message.lowercase() == "success") {
                    viewModel.insertToDB(email, nama, noTelpon)
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    finish()
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