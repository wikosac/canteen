package org.d3if2101.canteenpenjual.ui.daftar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.d3if2101.canteenpenjual.databinding.ActivityDaftarBinding

class DaftarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDaftarBinding

    private lateinit var nama: String
    private lateinit var email: String
    private lateinit var noTelpon: String
    private lateinit var password: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.tvButtonLogin.setOnClickListener {
            getTextInput()
        }

    }

    private fun getTextInput() {
        nama = binding.addNama.text.toString()
        email = binding.addEmail.text.toString()
        noTelpon = binding.addTelepon.text.toString()
        password = binding.addPassword.text.toString()
    }
}