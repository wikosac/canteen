package org.d3if2101.canteen.ui.penjual.dashboard

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.DEFAULT_SETTINGS_REQ_CODE
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import org.d3if2101.canteen.R
import org.d3if2101.canteen.databinding.ActivityDashboardPenjualBinding
import org.d3if2101.canteen.ui.ViewModelFactory
import org.d3if2101.canteen.ui.dashboard.DashboardActivity
import org.d3if2101.canteen.ui.login.Login
import org.d3if2101.canteen.ui.login.LoginViewModel
import org.d3if2101.canteen.ui.penjual.homeadminproduk.HomeProduk
import org.d3if2101.canteen.ui.penjual.order.OrderPenjualActivity
import org.d3if2101.canteen.ui.penjual.pendapatan.PendapatanActivity
import org.d3if2101.canteen.ui.profile.UserProfileActivity

class DashboardPenjualActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

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

        viewModel.setFCM()
        viewModel.getTokenPref().observe(this) {
            if (it != null) {
                viewModel.getUserWithToken(it).observe(this) { user ->
                    binding.txtGreetingName.text = this.getString(R.string.hi, user.nama)
                }
            }
        }

        binding.penjualCardProduk.setOnClickListener {
            startActivity(Intent(this@DashboardPenjualActivity, HomeProduk::class.java))
        }

        binding.penjualCardPendapatan.setOnClickListener {
            startActivity(Intent(this@DashboardPenjualActivity, PendapatanActivity::class.java))
        }

        binding.penjualCardOrder.setOnClickListener {
            startActivity(Intent(this@DashboardPenjualActivity, OrderPenjualActivity::class.java))
        }

        // Check if the notification permission is granted
        if (!permissionCheck()) {
            ActivityCompat.requestPermissions(
                this,
                DashboardActivity.REQUIRED_PERMISSIONS,
                DashboardActivity.REQUEST_CODE_PERMISSIONS
            )
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

        val drawerDelay: Long = 150 //delay of the drawer to close
        navView.setNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.nav_profile -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    startActivity(Intent(this, UserProfileActivity::class.java))
                }

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
        AlertDialog.Builder(this)
            .setTitle("Peringatan")
            .setMessage("Apa kamu yakin untuk keluar?")
            .setPositiveButton("Ya") { _, _ ->
                viewModel.unsubFCM()
                Firebase.auth.signOut()
                viewModel.deleteTokenPref()
                Toast.makeText(this, "Berhasil keluar", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this, Login::class.java))
                finish()
            }
            .setNegativeButton("Tidak") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }.create().show()
    }

    private fun permissionCheck() = DashboardActivity.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.aplikasi_ini_membutuhkan_izin_notifikasi),
                DashboardActivity.REQUEST_CODE_PERMISSIONS,
                Manifest.permission.POST_NOTIFICATIONS
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(this)
                .title(getString(R.string.izin_diperlukan))
                .rationale(getString(R.string.dialog_desc))
                .negativeButtonText(getString(R.string.batal))
                .positiveButtonText(getString(R.string.buka_pengaturan))
                .build().show()
        } else {
            requestNotificationPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(this, "Izin diberikan!", Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(
                this,
                getString(
                    R.string.returned_from_app_settings_to_activity,
                    if (permissionCheck()) getString(R.string.diberikan) else getString(R.string.ditolak)
                ),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}