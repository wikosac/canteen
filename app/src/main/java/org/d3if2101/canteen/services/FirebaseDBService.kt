package org.d3if2101.canteen.services

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.d3if2101.canteen.datamodels.MenuItem
import org.d3if2101.canteen.interfaces.RequestType
import org.d3if2101.canteen.ui.menu.MenuActivity

class FirebaseDBService {
    private var databaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference

    private val foodMenu = "food_menu"

    fun readAllMenu(menuApi: MenuActivity, requestType: RequestType) {
        val menuList = ArrayList<MenuItem>()

        val menuDbRef = databaseRef.child(foodMenu)
        menuDbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children) {
                    val item = MenuItem(
                        itemID = (1..1000).random().toString(), // TODO NEED TO UPDATE
                        imageUrl = snap.child("item_image_url").value.toString(),
                        itemName = snap.child("item_name").value.toString(),
                        itemPrice = snap.child("item_price").value.toString().toFloat(),
                        itemShortDesc = snap.child("item_desc").value.toString(),
                        itemTag = snap.child("item_category").value.toString(),
                        itemStars = snap.child("stars").value.toString().toFloat()
                    )
                    menuList.add(item)
                }
                menuList.shuffle() //so that every time user can see different items on opening app
//                menuApi.onFetchSuccessListener(menuList, requestType)
            }

            override fun onCancelled(error: DatabaseError) {
                // HANDLE ERROR
            }
        })
    }

    fun insertMenuItem(item: MenuItem) {
        val menuRef = databaseRef.child(foodMenu)

        menuRef.setValue(item)
    }
}