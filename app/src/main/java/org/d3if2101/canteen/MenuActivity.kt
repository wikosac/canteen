package org.d3if2101.canteen

import adapters.RecyclerFoodItemAdapter
import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.os.PersistableBundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.core.view.children
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView
import datamodels.CartItem
import interfaces.RequestType
import org.d3if2101.canteen.databinding.ActivityMenuBinding
import services.FirebaseDBService

class MenuActivity : AppCompatActivity(), RecyclerFoodItemAdapter.onItemClickListener{

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout

    private var allItems = ArrayList<MenuItem>()
    private lateinit var itemRecyclerView: RecyclerView
    private lateinit var recyclerFoodAdapter: RecyclerFoodItemAdapter

    private lateinit var topHeaderLL: LinearLayout
    private lateinit var topSearchLL: LinearLayout
    private lateinit var searchBox: SearchView
    private lateinit var foodCategoryCV: CardView
    private lateinit var showAllLL: LinearLayout

    private var searchIsActive = false
    private var doubleBackToExit = false

    private lateinit var progressDialog: ProgressDialog
    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onBackPressed() {
        if (searchIsActive) {
            //menyembunyikan tampilan di atas itemm
            recyclerFoodAdapter.filter.filter("")
            topHeaderLL.visibility = ViewGroup.VISIBLE
            foodCategoryCV.visibility = ViewGroup.VISIBLE
            showAllLL.visibility = ViewGroup.VISIBLE
            topSearchLL.visibility = ViewGroup.GONE
            searchIsActive = false
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
        Handler().postDelayed({ doubleBackToExit = false }, 2000)
    }

    private fun LoadMenu() {
        progressDialog = ProgressDialog(this)
        progressDialog.setCancelable(false)
        progressDialog.setTitle("Loading Menu...")
        progressDialog.setMessage("For fast and smooth experience, you can download Menu for Offline.")
        progressDialog.create()
        progressDialog.show()

        FirebaseDBService().readAllMenu(this, RequestType.READ)
    }

    private fun loadSearchTask() {
        topHeaderLL = findViewById(R.id.main_activity_top_header_ll)
        topSearchLL = findViewById(R.id.main_activity_top_search_ll)
        searchBox = findViewById(R.id.search_menu_items)
        foodCategoryCV = findViewById(R.id.main_activity_food_categories_cv)
        showAllLL = findViewById(R.id.main_activity_show_all_ll)

        findViewById<ImageView>(R.id.main_activity_search_iv).setOnClickListener {
            //menyembunyikan items
            topHeaderLL.visibility = ViewGroup.GONE
            foodCategoryCV.visibility = ViewGroup.GONE
            showAllLL.visibility = ViewGroup.GONE
            topSearchLL.visibility = ViewGroup.VISIBLE
            recyclerFoodAdapter.filterList(allItems)
            searchIsActive = true
        }
        searchBox.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
//        val container: LinearLayout = findViewById(R.id.food_categories_container_ll)
//        for (ll in container.children) {
//            ll.alpha = 1.0f
//        }
//        (view as LinearLayout).alpha = 0.5f
//        val tag = ((view as LinearLayout).getChildAt(1) as TextView).text.toString()
//        val filterList = ArrayList<datamodels.MenuItem>()
//        for (item in allItems) {
//            if (item.itemTag == tag) filterList.add(item)
//        }
//        recyclerFoodAdapter.filterList(filterList)
//        showAllSwitch.isChecked = false
    }

    fun showBottomDialog(view: View) {
//        val bottomDialog = BottomSheetSelectedDialog()
//        val bundle = Bundle()
//
//        var totalPrice = 0.0f
//        var totalItems = 0
//
//        for (item in db.readCartData()) {
//            totalPrice += item.itemPrice
//            totalItems += item.quantity
//        }
//
//        bundle.putFloat("totalPrice", totalPrice)
//        bundle.putInt("totalItems", totalItems)
//        // bundle.putParcelableArrayList("orderedList", recyclerFoodAdapter.getOrderedList() as ArrayList<out Parcelable?>?)
//
//        bottomDialog.arguments = bundle
//        bottomDialog.show(supportFragmentManager, "BottomSheetDialog")
    }


    override fun onItemClick(item: MenuItem) {
        //klik satu item, akan ditampilkan detail lebih
    }

    override fun onPlusBtnClick(item: MenuItem) {
//        item.quantity += 1
//        db.insertCartItem (
//            CartItem(
//                itemID = item.itemId,
//                itemName = item.itemName
//            )
//        )
    }

    override fun onMinusBtnClick(item: MenuItem) {
        TODO("Not yet implemented")
    }
}





