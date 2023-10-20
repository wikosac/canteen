package org.d3if2101.canteen.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import org.d3if2101.canteen.R
import org.d3if2101.canteen.data.model.UserModel
import org.d3if2101.canteen.databinding.ActivityDashboardBinding
import org.d3if2101.canteen.ui.ViewModelFactory
import org.d3if2101.canteen.ui.login.LoginViewModel
import org.d3if2101.canteen.ui.menu.MenuActivity
import org.d3if2101.canteen.ui.pesanan.MyCurrentOrdersActivity
import org.d3if2101.canteen.ui.pesanan.OrdersHistoryActivity
import org.d3if2101.canteen.ui.profile.UserProfileActivity

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this.application)
    }
    private val viewModel: LoginViewModel by viewModels { factory }

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rv.layoutManager = LinearLayoutManager(this)
        drawerLayout = binding.drawerLayout

        viewModel.getTokenPref().observe(this) { token ->
            viewModel.getUserWithToken(token!!).observe(this) {
                binding.txtGreeting.text = "Selamat datang, ${it.nama}"
            }
        }
        viewModel.getUserReturnList().observe(this) {
            setRV(it)
        }

        binding.navDrawerOpenerIv.setOnClickListener {
            loadNavigationDrawer()
        }



    }

    private fun navTo(idKantin: String) {
        val intent = Intent(this, MenuActivity::class.java)
        intent.putExtra("idKantin", idKantin)
        startActivity(intent)
    }

    private fun setRV(dataList: List<UserModel>) {
        val adapter = DashboardAdapter(dataList, object : DashboardAdapter.OnItemClickCallback {
            override fun onItemClick(data: UserModel) {
                navTo(data.uid) // Sending And Intent to MenuActivity
            }
        })
        binding.rv.adapter = adapter
    }

    private fun loadNavigationDrawer() {
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val drawerDelay: Long = 150
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_food_menu -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }

                R.id.nav_profile -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    Handler(Looper.getMainLooper()).postDelayed({
                        startActivity(Intent(this, UserProfileActivity::class.java))
                    }, drawerDelay)
                }

                R.id.nav_my_orders -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    Handler(Looper.getMainLooper()).postDelayed({
                        startActivity(Intent(this, MyCurrentOrdersActivity::class.java))
                    }, drawerDelay)
                }

                R.id.nav_orders_history -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    Handler(Looper.getMainLooper()).postDelayed({
                        startActivity(Intent(this, OrdersHistoryActivity::class.java))
                    }, drawerDelay)
                }

                R.id.nav_log_out -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
//                    logOutUser()
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

    companion object {
        const val TAG = "testo"
    }
}