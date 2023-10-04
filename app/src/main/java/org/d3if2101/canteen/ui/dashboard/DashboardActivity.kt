package org.d3if2101.canteen.ui.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.d3if2101.canteen.databinding.ActivityDashboardBinding
import org.d3if2101.canteen.ui.menu.MenuActivity

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

       binding.btnKantin1.setOnClickListener {
           startActivity(Intent(this, MenuActivity::class.java))
       }
    }
}