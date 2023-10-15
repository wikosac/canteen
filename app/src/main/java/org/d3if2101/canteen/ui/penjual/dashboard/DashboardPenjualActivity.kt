package org.d3if2101.canteen.ui.penjual.dashboard

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import org.d3if2101.canteen.R
import org.d3if2101.canteen.databinding.ActivityDashboardPenjualBinding
import org.d3if2101.canteen.ui.ViewModelFactory
import org.d3if2101.canteen.ui.login.Login
import org.d3if2101.canteen.ui.login.LoginViewModel
import org.d3if2101.canteen.ui.penjual.homeadminproduk.HomeProduk
import org.d3if2101.canteen.ui.penjual.order.OrderPenjualActivity
import org.d3if2101.canteen.ui.penjual.pendapatan.PendapatanActivity
import org.d3if2101.canteen.ui.penjual.rating.RatingActivity
import org.d3if2101.canteen.ui.profile.UserProfileActivity

class DashboardPenjualActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardPenjualBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout

    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this.application)
    }
    private val viewModel: LoginViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardPenjualBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadNavigationDrawer()

        // Inisialisasi Firebase Messaging dan berlangganan topik dalam onCreate

        val user = FirebaseAuth.getInstance().currentUser
        viewModel.getUserWithToken(user!!.uid).observe(this) {
            binding.txtGreetingName.text = this.getString(R.string.hi, it.nama)
        }
        viewModel.setFCM()

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

        // Jika Ingin Set Text $name
//        val navHeaderUserName = navView.getHeaderView(0).findViewById<TextView>(R.id.nav_header_user_name)
//        navHeaderUserName.text = "Teks yang Anda inginkan"

        val drawerDelay: Long = 150
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_profile -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    Handler(Looper.getMainLooper()).postDelayed({
                        startActivity(Intent(this, UserProfileActivity::class.java))
                    }, drawerDelay)
                }
//                R.id.nav_my_orders -> {
//                    startActivity(Intent(this, OrderPenjualActivity::class.java))
//                }
//                R.id.nav_orders_history -> { // UBAH MENJADI Pendapatan
//
//                }
                R.id.nav_dashboard -> {
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