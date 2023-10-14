package org.d3if2101.canteen.ui.penjual.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import org.d3if2101.canteen.R
import org.d3if2101.canteen.databinding.ActivityDashboardPenjualBinding
import org.d3if2101.canteen.ui.ViewModelFactory
import org.d3if2101.canteen.ui.login.Login
import org.d3if2101.canteen.ui.login.LoginViewModel
import org.d3if2101.canteen.ui.penjual.homeadminproduk.HomeProduk
import org.d3if2101.canteen.ui.penjual.order.OrderPenjualActivity
import org.d3if2101.canteen.ui.penjual.pendapatan.PendapatanActivity
import org.d3if2101.canteen.ui.penjual.rating.RatingActivity

class DashboardPenjualActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardPenjualBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private  var stringReceived: String = ""
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this.application)
    }
    private val viewModel: LoginViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardPenjualBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadNavigationDrawer()

        binding.penjualCardProduk.setOnClickListener {
            startActivity(Intent(this@DashboardPenjualActivity, HomeProduk::class.java))
        }

        binding.penjualCardPendapatan.setOnClickListener {
            startActivity(Intent(this@DashboardPenjualActivity, PendapatanActivity::class.java))
        }

        binding.penjualCardOrder.setOnClickListener {
            startActivity(Intent(this@DashboardPenjualActivity, OrderPenjualActivity::class.java))
        }

        binding.penjualCardRating.setOnClickListener {
            startActivity(Intent(this@DashboardPenjualActivity, RatingActivity::class.java))
        }

    }

    private fun loadNavigationDrawer() {
        navView = findViewById(R.id.nav_view)
        drawerLayout = findViewById(R.id.drawer_layout)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val drawerDelay: Long = 150 //delay of the drawer to close
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_food_menu -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.nav_log_out -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    logout()
                }
            }
            true
        }

        findViewById<ImageView>(R.id.nav_drawer_opener_iv).setOnClickListener {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
    }

    private fun logout() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
        viewModel.deleteTokenPref()
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
    }



}