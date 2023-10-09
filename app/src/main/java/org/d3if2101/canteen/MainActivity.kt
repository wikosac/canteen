package org.d3if2101.canteen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import org.d3if2101.canteen.databinding.ActivityMainBinding
import org.d3if2101.canteen.ui.login.Login

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper())
            .postDelayed(
                { startActivity(Intent(this, Login::class.java)) },
                3000
            )
    }
}