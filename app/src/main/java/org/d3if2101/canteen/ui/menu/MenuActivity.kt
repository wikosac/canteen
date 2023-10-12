package org.d3if2101.canteen.ui.menu

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.d3if2101.canteen.R
import org.d3if2101.canteen.data.model.Produk
import org.d3if2101.canteen.databinding.ActivityMenuBinding
import org.d3if2101.canteen.ui.ViewModelFactory
import org.d3if2101.canteen.ui.login.Login
import org.d3if2101.canteen.ui.login.LoginViewModel

class MenuActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout

//    private var allItems = ArrayList<MenuItem>()
//    private lateinit var recyclerFoodAdapter: RecyclerFoodItemAdapter
//
    private lateinit var topHeaderLL: LinearLayout
    private lateinit var topSearchLL: LinearLayout
    private lateinit var searchBox: SearchView
    private lateinit var foodCategoryCV: CardView
    private lateinit var showAllLL: LinearLayout

    private var searchIsActive = false
    private var doubleBackToExit = false

    private lateinit var progressDialog: ProgressBar

    private lateinit var listProduk: ArrayList<Produk>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var binding: ActivityMenuBinding
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this.application)
    }
    private val viewModel: LoginViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadNavigationDrawer()

        binding.itemsRecyclerView.layoutManager = LinearLayoutManager(this)
        listProduk = arrayListOf()

        databaseReference = FirebaseDatabase.getInstance().getReference("produk")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val list = dataSnapshot.getValue(Produk::class.java)
                        listProduk.add(list!!)
                    }
                    binding.itemsRecyclerView.adapter = MenuAdapter(this@MenuActivity, listProduk)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MenuActivity, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        const val TAG = "MenuActivity"
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
//                R.id.nav_profile -> {
//                    drawerLayout.closeDrawer(GravityCompat.START)
//                    Handler(Looper.getMainLooper()).postDelayed({ openUserProfileActivity() }, drawerDelay)
//                }
//                R.id.nav_my_orders -> {
//                    drawerLayout.closeDrawer(GravityCompat.START)
//                    Handler().postDelayed({
//                        startActivity(Intent(this, MyCurrentOrdersActivity::class.java))
//                    }, drawerDelay)
//                }
//                R.id.nav_orders_history -> {
//                    drawerLayout.closeDrawer(GravityCompat.START)
//                    Handler().postDelayed({
//                        startActivity(Intent(this, OrdersHistoryActivity::class.java))
//                    }, drawerDelay)
//                }
//                R.id.nav_share_app -> {
//                    shareApp()
//                }
//                R.id.nav_report_bug -> {
//                    Toast.makeText(this, "Not Available", Toast.LENGTH_SHORT).show()
//                }
//                R.id.nav_contact_us -> {
//                    drawerLayout.closeDrawer(GravityCompat.START)
//                    Handler().postDelayed({
//                        startActivity(Intent(this, ContactUsActivity::class.java))
//                    }, drawerDelay)
//                }
//                R.id.nav_update_menu -> {
//                    drawerLayout.closeDrawer(GravityCompat.START)
//                    updateOfflineFoodMenu()
//                }
//                R.id.nav_settings -> {
//                    drawerLayout.closeDrawer(GravityCompat.START)
//                    Handler().postDelayed({
//                        startActivity(Intent(this, SettingsActivity::class.java))
//                    }, drawerDelay)
//                }
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


//    override fun onBackPressed() {
//        if (searchIsActive) {
//            //menyembunyikan tampilan di atas itemm
//            recyclerFoodAdapter.filter.filter("")
//            topHeaderLL.visibility = ViewGroup.VISIBLE
//            foodCategoryCV.visibility = ViewGroup.VISIBLE
//            showAllLL.visibility = ViewGroup.VISIBLE
//            topSearchLL.visibility = ViewGroup.GONE
//            searchIsActive = false
//            return
//        }
//        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
//            drawerLayout.closeDrawer(GravityCompat.START)
//            return
//        }
//        if (doubleBackToExit) {
//            super.onBackPressed()
//            return
//        }
//        doubleBackToExit = true
//        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show()
//        Handler().postDelayed({ doubleBackToExit = false }, 2000)
//    }
//
//    private fun LoadMenu() {
//        progressDialog = ProgressDialog(this)
//        progressDialog.setCancelable(false)
//        progressDialog.setTitle("Loading Menu...")
//        progressDialog.setMessage("For fast and smooth experience, you can download Menu for Offline.")
//        progressDialog.create()
//        progressDialog.show()
//
//        FirebaseDBService().readAllMenu(this, RequestType.READ)
//    }
//
//    private fun loadSearchTask() {
//        topHeaderLL = findViewById(R.id.main_activity_top_header_ll)
//        topSearchLL = findViewById(R.id.main_activity_top_search_ll)
////        searchBox = findViewById(R.id.search_menu_items)
//        foodCategoryCV = findViewById(R.id.main_activity_food_categories_cv)
//        showAllLL = findViewById(R.id.main_activity_show_all_ll)
//
//        findViewById<ImageView>(R.id.main_activity_search_iv).setOnClickListener {
//            //menyembunyikan items
//            topHeaderLL.visibility = ViewGroup.GONE
//            foodCategoryCV.visibility = ViewGroup.GONE
//            showAllLL.visibility = ViewGroup.GONE
//            topSearchLL.visibility = ViewGroup.VISIBLE
//            recyclerFoodAdapter.filterList(allItems)
//            searchIsActive = true
//        }
//        searchBox.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(p0: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(p0: String?): Boolean {
//                recyclerFoodAdapter.filter.filter(p0)
//                return false
//            }
//        })
//    }
//    fun showTagItems(view: View) {
//        //menampilkan kategori yang sama
////        val container: LinearLayout = findViewById(R.id.food_categories_container_ll)
////        for (ll in container.children) {
////            ll.alpha = 1.0f
////        }
////        (view as LinearLayout).alpha = 0.5f
////        val tag = ((view as LinearLayout).getChildAt(1) as TextView).text.toString()
////        val filterList = ArrayList<datamodels.MenuItem>()
////        for (item in allItems) {
////            if (item.itemTag == tag) filterList.add(item)
////        }
////        recyclerFoodAdapter.filterList(filterList)
////        showAllSwitch.isChecked = false
//    }
//
//    fun showBottomDialog(view: View) {
////        val bottomDialog = BottomSheetSelectedDialog()
////        val bundle = Bundle()
////
////        var totalPrice = 0.0f
////        var totalItems = 0
////
////        for (item in db.readCartData()) {
////            totalPrice += item.itemPrice
////            totalItems += item.quantity
////        }
////
////        bundle.putFloat("totalPrice", totalPrice)
////        bundle.putInt("totalItems", totalItems)
////        // bundle.putParcelableArrayList("orderedList", recyclerFoodAdapter.getOrderedList() as ArrayList<out Parcelable?>?)
////
////        bottomDialog.arguments = bundle
////        bottomDialog.show(supportFragmentManager, "BottomSheetDialog")
//    }


//    override fun onItemClick(item: MenuItem) {
//        //klik satu item, akan ditampilkan detail lebih
//    }
//
//    override fun onPlusBtnClick(item: MenuItem) {
////        item.quantity += 1
////        db.insertCartItem (
////            CartItem(
////                itemID = item.itemId,
////                itemName = item.itemName
////            )
////        )
//    }

//    override fun onMinusBtnClick(item: MenuItem) {
//    }
}





