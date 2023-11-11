package org.d3if2101.canteen.ui.menu

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.ViewGroup
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.d3if2101.canteen.R
import org.d3if2101.canteen.adapters.RecyclerFoodItemAdapter
import org.d3if2101.canteen.databinding.ActivityMainMenuBinding
import org.d3if2101.canteen.datamodels.CartItem
import org.d3if2101.canteen.datamodels.MenuItem
import org.d3if2101.canteen.services.DatabaseHandler
import org.d3if2101.canteen.ui.ViewModelFactory
import org.d3if2101.canteen.ui.login.Login
import org.d3if2101.canteen.ui.login.LoginViewModel
import org.d3if2101.canteen.ui.pesanan.MyCurrentOrdersActivity
import org.d3if2101.canteen.ui.pesanan.OrdersHistoryActivity
import org.d3if2101.canteen.ui.profile.UserProfileActivity

class MenuActivity : AppCompatActivity(), RecyclerFoodItemAdapter.OnItemClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var itemRecyclerView: RecyclerView
    private lateinit var recyclerFoodAdapter: RecyclerFoodItemAdapter
    private lateinit var showAllSwitch: SwitchCompat
    private lateinit var binding: ActivityMainMenuBinding

    private var allItems = ArrayList<MenuItem>()
    private var searchIsActive = false
    private var dataIsEmpty = false
    private val db = DatabaseHandler(this)
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this.application)
    }
    private val viewModel: LoginViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser!!
        databaseRef = FirebaseDatabase.getInstance().reference
        drawerLayout = binding.drawerLayout

        db.clearCartTable()
        setMenu()
        loadSearchTask()

        GlobalScope.launch {
            if (dataIsEmpty) binding.txtEmptyView.visibility = View.VISIBLE
        }

        viewModel.getUserWithToken(user.uid).observe(this@MenuActivity) {
            binding.topWishNameTv.text = this.getString(R.string.hi, it.nama)
        }

        binding.topNamaKantinTv.text = intent.getStringExtra("namaKantin")
        binding.btnCart.setOnClickListener { showBottomDialog() }
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
                        val idSeller = intent.getStringExtra("idKantin")
                        if (list!!.sellerID == idSeller) {
                            if (list.status) {
                                allItems.add(list)

                            }
                        }
                    }
                    loadMenu(allItems)
                } else {
                    dataIsEmpty = true
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MenuActivity, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
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

        if (listItems.isNotEmpty()) binding.txtEmptyView.visibility = View.GONE
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
                recyclerFoodAdapter.filter.filter(p0)
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
            .setTitle("Peringatan")
            .setMessage("Apa kamu yakin untuk Log Out?")
            .setPositiveButton("Ya") { _, _ ->
                viewModel.unsubFCM()

                Firebase.auth.signOut()

                getSharedPreferences("settings", MODE_PRIVATE).edit().clear().apply()
                getSharedPreferences("user_profile_details", MODE_PRIVATE).edit().clear().apply()

                viewModel.deleteTokenPref()
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()

//                hapus table
                db.dropCurrentOrdersTable()
                db.dropOrderHistoryTable()
                db.clearSavedCards()

                startActivity(Intent(this, Login::class.java))
                finish()
            }
            .setNegativeButton("Tidak") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }.create().show()
    }

    private fun showBottomDialog() {
        val bottomDialog = BottomSheetSelectedItemDialog()
        val bundle = Bundle()

        var totalPrice = 0
        var totalItems = 0

        for (item in db.readCartData()) {
            totalPrice += item.itemPrice * item.quantity
            totalItems += item.quantity
        }

        bundle.putInt("totalPrice", totalPrice)
        bundle.putInt("totalItems", totalItems)

        bottomDialog.arguments = bundle
        bottomDialog.show(supportFragmentManager, "BottomSheetDialog")
    }

    override fun onItemClick(item: MenuItem) {
        //klik satu item, akan ditampilkan detail lebih
    }

    override fun onPlusBtnClick(item: MenuItem) {
        item.quantity += 1
        db.insertCartItem(
            CartItem(
                itemID = item.itemID,
                itemName = item.itemName,
                imageUrl = item.imageUrl,
                itemPrice = item.itemPrice,
                quantity = item.quantity,
                itemStars = item.itemStars,
                itemShortDesc = item.itemShortDesc,
                idPenjual = item.sellerID
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





