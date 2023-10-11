package org.d3if2101.canteen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.d3if2101.canteen.ui.login.Login

class MainPenjualActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_penjual)

        startActivity(Intent(this@MainPenjualActivity, Login::class.java))
    }
}