package org.d3if2101.canteen.services

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.d3if2101.canteen.datamodels.MenuItem

class FirebaseDBService {
    private var databaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun readAllMenu() {
        val menuList = ArrayList<MenuItem>()

        val menuDbRef = databaseRef.child("produk")
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
                menuList.shuffle()
//                menuApi.onFetchSuccessListener(menuList, requestType)
            }

            override fun onCancelled(error: DatabaseError) {
                // HANDLE ERROR
            }
        })
    }

    fun insertMenuItem(item: MenuItem) {
        val menuRef = databaseRef.child("produk")

        menuRef.setValue(item)
    }
}