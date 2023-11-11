package org.d3if2101.canteen.ui.login

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import org.d3if2101.canteen.databinding.ActivityResetPasswordBinding
import org.d3if2101.canteen.ui.ViewModelFactory

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this.application)
    }
    private val viewModel: LoginViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnReset.setOnClickListener {
            val email = binding.edEmailReset.text.toString()
            viewModel.resetPass(email).observe(this) { msg ->
                Toast.makeText(this, msg.message, Toast.LENGTH_SHORT).show()
            }
        }
        binding.tvKembaliReset.setOnClickListener { finish() }
    }
}