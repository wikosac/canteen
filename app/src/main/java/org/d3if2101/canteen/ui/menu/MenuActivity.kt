package org.d3if2101.canteen.ui.menu

import android.app.ActivityOptions
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.GravityCompat
import androidx.core.view.children
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import org.d3if2101.canteen.R
import org.d3if2101.canteen.adapters.RecyclerFoodItemAdapter
import org.d3if2101.canteen.databinding.ActivityMainMenuBinding
import org.d3if2101.canteen.databinding.NavHeaderBinding
import org.d3if2101.canteen.datamodels.CartItem
import org.d3if2101.canteen.datamodels.MenuItem
import org.d3if2101.canteen.interfaces.MenuApi
import org.d3if2101.canteen.interfaces.RequestType
import org.d3if2101.canteen.services.DatabaseHandler
import org.d3if2101.canteen.services.FirebaseDBService
import org.d3if2101.canteen.ui.ViewModelFactory
import org.d3if2101.canteen.ui.login.Login
import org.d3if2101.canteen.ui.login.LoginViewModel
import org.d3if2101.canteen.ui.profile.UserProfileActivity

class MenuActivity : AppCompatActivity(), RecyclerFoodItemAdapter.OnItemClickListener, MenuApi {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference

    private val db = DatabaseHandler(this)

    private lateinit var toggle: ActionBarDrawerToggle
//    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout

    private var allItems = ArrayList<MenuItem>()
    private lateinit var itemRecyclerView: RecyclerView
    private lateinit var recyclerFoodAdapter: RecyclerFoodItemAdapter

//    private lateinit var userIcon: CircleImageView
    private lateinit var showAllSwitch: SwitchCompat

//    private lateinit var topHeaderLL: LinearLayout
//    private lateinit var topSearchLL: LinearLayout
//    private lateinit var searchBox: SearchView
//    private lateinit var foodCategoryCV: CardView
//    private lateinit var showAllLL: LinearLayout

    private var searchIsActive = false
    private var doubleBackToExit = false

    private lateinit var progressDialog: ProgressDialog

    private lateinit var binding: ActivityMainMenuBinding
    private lateinit var bindingNav: NavHeaderBinding
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this.application)
    }
    private val viewModel: LoginViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        bindingNav = NavHeaderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser!!
        databaseRef = FirebaseDatabase.getInstance().reference

        drawerLayout = binding.drawerLayout

        with (binding) {
            topWishNameTv.text = user.displayName
            btnCart.setOnClickListener { showBottomDialog() }
            menuUserIcon.setOnClickListener { openUserProfileActivity() }
        }

        with (bindingNav) {
            navHeaderUserName.text
        }

        db.clearCartTable()
        setMenu()
        loadNavigationDrawer()
        loadSearchTask()
    }

    companion object {
        const val TAG = "testoMenu"
    }

    private fun setMenu() {
        databaseRef.child("produk").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(TAG, "onDataChange: ${snapshot.children}")
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val list = dataSnapshot.getValue(MenuItem::class.java)
                        allItems.add(list!!)
                    }
                    loadMenu(allItems)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MenuActivity, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadNavigationDrawer() {
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val drawerDelay: Long = 150 //delay of the drawer to close
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_food_menu -> {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
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
                    logOutUser()
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

    override fun onBackPressed() {
        if (searchIsActive) {
            //menyembunyikan tampilan di atas itemm
            recyclerFoodAdapter.filter.filter("")
            searchIsActive = false
            with(binding) {
                mainActivityTopHeaderLl.visibility = ViewGroup.VISIBLE
                mainActivityFoodCategoriesCv.visibility = ViewGroup.VISIBLE
                mainActivityShowAllLl.visibility = ViewGroup.VISIBLE
                mainActivityTopSearchLl.visibility = ViewGroup.GONE
            }
            return
        }
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
            return
        }
        if (doubleBackToExit) {
            super.onBackPressed()
            return
        }
        doubleBackToExit = true
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({ doubleBackToExit = false }, 2000)

    }

    private fun loadMenu(listItems: ArrayList<MenuItem>) {
        val sharedPref: SharedPreferences = getSharedPreferences("settings", MODE_PRIVATE)

        itemRecyclerView = binding.itemsRecyclerView
        recyclerFoodAdapter = RecyclerFoodItemAdapter(
            applicationContext,
            listItems,
            sharedPref.getInt("loadItemImages", 0),
            this
        )
        itemRecyclerView.adapter = recyclerFoodAdapter
        itemRecyclerView.layoutManager = LinearLayoutManager(this)

        showAllSwitch = binding.showAllItemsSwitch
        showAllSwitch.setOnClickListener {
            if (showAllSwitch.isChecked) {
                recyclerFoodAdapter.filterList(allItems) //display complete list
                val container: LinearLayout = findViewById(R.id.food_categories_container_ll)
                for (ll in container.children) {
                    ll.alpha = 1.0f // indicate that they are not pressed
                }
            }
        }
    }

    private fun loadOnlineMenu() {
        FirebaseDBService().readAllMenu(this, RequestType.READ)
    }

    private fun loadSearchTask() {

        binding.mainActivitySearchIv.setOnClickListener {
            //menyembunyikan items
            with(binding) {
                mainActivityTopHeaderLl.visibility = ViewGroup.GONE
                mainActivityFoodCategoriesCv.visibility = ViewGroup.GONE
                mainActivityShowAllLl.visibility = ViewGroup.GONE
                mainActivityTopSearchLl.visibility = ViewGroup.VISIBLE
            }
            recyclerFoodAdapter.filterList(allItems)
            searchIsActive = true
        }
        binding.searchMenuItems.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
//                recyclerFoodAdapter.filter.filter(p0)
                return false
            }
        })
    }

    fun showTagItems(view: View) {
        //menampilkan kategori yang sama
        val container: LinearLayout = findViewById(R.id.food_categories_container_ll)
        for (ll in container.children) {
            ll.alpha = 1.0f
        }
        (view as LinearLayout).alpha = 0.5f
        val tag = (view.getChildAt(1) as TextView).text.toString()
        val filterList = ArrayList<MenuItem>()
        for (item in allItems) {
            if (item.itemTag == tag) filterList.add(item)
        }
        recyclerFoodAdapter.filterList(filterList)
        binding.showAllItemsSwitch.isChecked = false
    }

    private fun logOutUser() {
        AlertDialog.Builder(this)
            .setTitle("Attention")
            .setMessage("Are you sure you want to Log Out ? You will lose all your Orders, as it is a demo App")
            .setPositiveButton("Yes") { _, _ ->
                Firebase.auth.signOut()

                getSharedPreferences("settings", MODE_PRIVATE).edit().clear()
                    .apply() //deleting settings from offline
                getSharedPreferences("user_profile_details", MODE_PRIVATE).edit().clear()
                    .apply() //deleting user details from offline

                viewModel.deleteTokenPref()
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()

                //removing tables
                db.dropCurrentOrdersTable()
                db.dropOrderHistoryTable()
                db.clearSavedCards()

                startActivity(Intent(this, Login::class.java))
                finish()
            }
            .setNegativeButton("No") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }.create().show()
    }

    fun showBottomDialog() {
        val bottomDialog = BottomSheetSelectedItemDialog()
        val bundle = Bundle()

        var totalPrice = 0.0f
        var totalItems = 0

        for (item in db.readCartData()) {
            totalPrice += item.itemPrice
            totalItems += item.quantity
        }

        bundle.putFloat("totalPrice", totalPrice)
        bundle.putInt("totalItems", totalItems)
        // bundle.putParcelableArrayList("orderedList", recyclerFoodAdapter.getOrderedList() as ArrayList<out Parcelable?>?)

        bottomDialog.arguments = bundle
        bottomDialog.show(supportFragmentManager, "BottomSheetDialog")
    }

    private fun openUserProfileActivity() {
        val intent = Intent(this, UserProfileActivity::class.java)
//        intent.putExtra("gender", this.empGender)

        val options = ActivityOptions.makeSceneTransitionAnimation(
            this,
            binding.menuUserIcon,
            "userIconTransition"
        )
        startActivity(intent, options.toBundle())
    }

    override fun onFetchSuccessListener(list: ArrayList<MenuItem>, requestType: RequestType) {

        if (requestType == RequestType.READ) {
            for (item in list) {
                allItems.add(item)
            }
            recyclerFoodAdapter.notifyItemRangeInserted(0, allItems.size)
        }

        if (requestType == RequestType.OFFLINE_UPDATE) {
            for (item in list) {
                db.insertOfflineMenuData(item)
            }
            Toast.makeText(applicationContext, "Offline Menu Updated", Toast.LENGTH_LONG).show()
//            loadOfflineMenu()
        }

        progressDialog.dismiss()
    }

    override fun onItemClick(item: MenuItem) {
        //klik satu item, akan ditampilkan detail lebih
    }

    override fun onPlusBtnClick(item: MenuItem) {
        item.quantity += 1
        db.insertCartItem (
            CartItem(
                itemID = item.itemID,
                itemName = item.itemName,
                imageUrl = item.imageUrl,
                itemPrice = item.itemPrice,
                quantity = item.quantity,
                itemStars = item.itemStars,
                itemShortDesc = item.itemShortDesc
            )
        )
    }

    override fun onMinusBtnClick(item: MenuItem) {
        if (item.quantity == 0) return
        item.quantity -= 1

        val cartItem = CartItem(
            itemID = item.itemID,
            itemName = item.itemName,
            imageUrl = item.imageUrl,
            itemPrice = item.itemPrice,
            quantity = item.quantity,
            itemStars = item.itemStars,
            itemShortDesc = item.itemShortDesc
        )

        if (item.quantity == 0) {
            db.deleteCartItem(cartItem)
        } else {
            db.insertCartItem(cartItem) // Update
        }
    }
}





