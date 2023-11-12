package org.d3if2101.canteen.ui.dashboard

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.DEFAULT_SETTINGS_REQ_CODE
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import org.d3if2101.canteen.R
import org.d3if2101.canteen.data.model.UserModel
import org.d3if2101.canteen.databinding.ActivityDashboardBinding
import org.d3if2101.canteen.ui.ViewModelFactory
import org.d3if2101.canteen.ui.login.Login
import org.d3if2101.canteen.ui.login.LoginViewModel
import org.d3if2101.canteen.ui.menu.MenuActivity
import org.d3if2101.canteen.ui.pesanan.MyCurrentOrdersActivity
import org.d3if2101.canteen.ui.pesanan.OrdersHistoryActivity
import org.d3if2101.canteen.ui.profile.UserProfileActivity

class DashboardActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
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

        viewModel.setFCM()

        val user = FirebaseAuth.getInstance().currentUser!!
        viewModel.getUserWithToken(user.uid).observe(this) {
            binding.txtGreeting.text = getString(R.string.selamat_datang, it.nama)
        }
        viewModel.getUserReturnList().observe(this) {
            setRV(it)
        }

        binding.navDrawerOpenerIv.setOnClickListener {
            loadNavigationDrawer()
        }

        // Check if the notification permission is granted
        if (!permissionCheck()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun navTo(idKantin: String, namaKantin: String) {
        val intent = Intent(this, MenuActivity::class.java)
        intent.putExtra("idKantin", idKantin)
        intent.putExtra("namaKantin", namaKantin)
        startActivity(intent)
    }

    private fun setRV(dataList: List<UserModel>) {
        val adapter = DashboardAdapter(dataList, object : DashboardAdapter.OnItemClickCallback {
            override fun onItemClick(data: UserModel) {
                navTo(data.uid, data.nama) // Sending And Intent to MenuActivity
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
                    logOutUser()
                }
            }
            true
        }

        binding.navDrawerOpenerIv.setOnClickListener {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
    }

    private fun logOutUser() {
        AlertDialog.Builder(this)
            .setTitle("Peringatan")
            .setMessage("Apa kamu yakin untuk keluar?")
            .setPositiveButton("Ya") { _, _ ->
                viewModel.unsubFCM()

                Firebase.auth.signOut()

                getSharedPreferences("settings", MODE_PRIVATE).edit().clear().apply()
                getSharedPreferences("user_profile_details", MODE_PRIVATE).edit().clear().apply()

                viewModel.deleteTokenPref()
                Toast.makeText(this, "Berhasil keluar", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this, Login::class.java))
                finish()
            }
            .setNegativeButton("Tidak") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }.create().show()
    }

    private fun permissionCheck() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.aplikasi_ini_membutuhkan_izin_notifikasi),
                REQUEST_CODE_PERMISSIONS,
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

    companion object {
        private const val TAG = "DashboardActivity"
        val REQUIRED_PERMISSIONS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            arrayOf(Manifest.permission.RECEIVE_BOOT_COMPLETED)
        }
        const val REQUEST_CODE_PERMISSIONS = 10
    }
}