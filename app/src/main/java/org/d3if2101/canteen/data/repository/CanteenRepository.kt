package org.d3if2101.canteen.data.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import org.d3if2101.canteen.data.model.Message
import org.d3if2101.canteen.data.model.UserModel
import org.d3if2101.canteen.datamodels.MenuItem
import org.d3if2101.canteen.datamodels.OrderHistoryItem

class CanteenRepository private constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
    private val storageReference: FirebaseStorage,
    private val firebaseMessaging: FirebaseMessaging
) {
    private val databaseRef = firebaseDatabase.reference
    private val uidUser = MutableLiveData<String>()
    private val uid: LiveData<String> = uidUser

    fun registUser(email: String, pass: String): LiveData<Message> {
        val data = MutableLiveData<Message>()
        try {
            firebaseAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, task.result.toString())
                        data.value = Message("Success")
                    } else {
                        Log.d(TAG, task.exception?.message.toString())
                        data.value = Message("Failed")
                    }
                }
        } catch (e: Exception) {
            Log.e(TAG, e.printStackTrace().toString())
        }
        return data
    }

    fun getUIDUser(): LiveData<String> {
        val data = MutableLiveData<String>()
        Log.d(TAG, "${firebaseAuth.uid}")
        data.value = firebaseAuth.uid.toString()
        return data
    }

    fun getUser(lifecycleOwner: LifecycleOwner): LiveData<UserModel> {
        val userData = MutableLiveData<UserModel>()
        uid.observe(lifecycleOwner) {
            Log.d(TAG, "uid: $it")
            if (it != null) {
                databaseRef.child("users").child(it).addValueEventListener(
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                userData.value = snapshot.getValue(UserModel::class.java)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e(TAG, error.message)
                        }
                    }
                )
            }
        }
        return userData
    }

    fun getUsersPenjual(): LiveData<List<UserModel>> {
        val userListLiveData = MutableLiveData<List<UserModel>>()

        val usersRef = databaseRef.child("users")

        usersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = mutableListOf<UserModel>()

                for (userSnapshot in snapshot.children) {
                    val userModel = userSnapshot.getValue(UserModel::class.java)
                    userModel?.let {
                        if (it.role.lowercase() == "penjual") {
                            userList.add(it)
                        }
                    }
                }

                userListLiveData.value = userList
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, error.message)
            }
        })
        return userListLiveData
    }


    fun getUserWithToken(token: String): LiveData<UserModel> {
        val userData = MutableLiveData<UserModel>()
        databaseRef.child("users").child(token).addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        userData.value = snapshot.getValue(UserModel::class.java)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, error.message)
                }
            }
        )
        return userData
    }

    fun loginUser(email: String, pass: String): LiveData<Message> {
        val data = MutableLiveData<Message>()
        try {
            firebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        uidUser.value = firebaseAuth.currentUser?.uid.toString()
                        Log.d(TAG, "uidUser: ${uidUser.value}")

                        Log.d(TAG, task.result.toString())
                        data.value = Message("Success")
                    } else {
                        Log.d(TAG, task.exception?.message.toString())
                        data.value = Message("Failed")
                    }
                }
        } catch (e: Exception) {
            Log.e(TAG, e.printStackTrace().toString())
        }
        return data
    }

    fun signOut() {
        val user = firebaseAuth.currentUser
        if (user != null) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(user.uid)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("FCM", "Berhenti berlangganan dari topik '${user.uid}' berhasil.")
                    } else {
                        Log.e("FCM", "Gagal berhenti berlangganan dari topik '${user.uid}'.")
                    }
                }
            firebaseAuth.signOut()
        } else {
            Log.d("FCM", "Tidak ada pengguna yang masuk (sign in).")
        }
    }

    fun inputUserToDatabase(
        email: String,
        nama: String,
        noTelpon: String,
        foto: String,
        role: String
    ): LiveData<Message> {
        val data = MutableLiveData<Message>()
        try {
            firebaseDatabase.getReference("users").child(firebaseAuth.uid.toString()).setValue(
                UserModel(
                    uid = firebaseAuth.uid.toString(),
                    email = email,
                    foto = foto,
                    nama = nama,
                    noTelpon = noTelpon,
                    role = role,
                ),
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Data berhasil ditambahkan")
                    data.value = Message("Success")
                } else {
                    Log.e(TAG, "Data gagal ditambahkan")
                    data.value = Message("Failed")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, e.printStackTrace().toString())
        }
        return data
    }

    fun recoverProductDB(
        namaProduk: String,
        jenis: String,
        harga: Int,
        image: String,
        desc: String,
        state: Boolean
    ): LiveData<Message> {

        val uniqueID = firebaseDatabase.reference.push().key
        val data = MutableLiveData<Message>()

        val produk = MenuItem(
            sellerID = firebaseAuth.uid.toString(),
            itemID = uniqueID.toString(),
            imageUrl = image,
            itemName = namaProduk,
            itemPrice = harga,
            itemShortDesc = desc,
            itemTag = jenis,
            itemStars = 5.0F,
            quantity = 0,
            status = state
        )

        firebaseDatabase.getReference("produk").child(uniqueID.toString())
            .setValue(produk)
            .addOnCompleteListener { dbTask ->
                if (dbTask.isSuccessful) {
                    Log.d(TAG, "Data berhasil ditambahkan")
                    data.value = Message("Success")
                } else {
                    Log.e(TAG, "Data gagal ditambahkan")
                    data.value = Message("Failed")
                }
            }
        return data
    }

    fun inputProdukToDatabase(
        namaProduk: String, jenis: String, harga: Int, image: Uri, deskripsi: String
    ): LiveData<Message> {
        val data = MutableLiveData<Message>()
        val fileName = namaProduk + image + System.currentTimeMillis()
        // Upload gambar ke Firebase Storage
        val uploadTask = storageReference.reference.child(jenis).child(fileName).putFile(image)

        uploadTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Dapatkan URL download dari gambar
                uploadTask.result.storage.downloadUrl.addOnCompleteListener { downloadUrlTask ->
                    if (downloadUrlTask.isSuccessful) {
                        val downloadUrl = downloadUrlTask.result.toString()

                        Log.d(TAG, downloadUrl)

                        val uniqueID = firebaseDatabase.reference.push().key

                        val produk = MenuItem(
                            sellerID = firebaseAuth.uid.toString(),
                            itemID = uniqueID.toString(),
                            imageUrl = downloadUrl,
                            itemName = namaProduk,
                            itemPrice = harga,
                            itemShortDesc = deskripsi,
                            itemTag = jenis,
                            itemStars = 5.0F,
                            quantity = 0
                        )

                        firebaseDatabase.getReference("produk").child(uniqueID.toString())
                            .setValue(produk)
                            .addOnCompleteListener { dbTask ->
                                if (dbTask.isSuccessful) {
                                    Log.d(TAG, "Data berhasil ditambahkan")
                                    data.value = Message("Success")
                                } else {
                                    Log.e(TAG, "Data gagal ditambahkan")
                                    data.value = Message("Failed")
                                }
                            }
                    } else {
                        data.value = Message("Failed to get download URL")
                    }
                }
            } else {
                data.value = Message("Failed to upload image")
            }
        }

        return data
    }

    fun editState(
        idProduk: String,
        namaProduk: String,
        jenis: String,
        harga: Int,
        image: String,
        desc: String,
        state: Boolean
    ): LiveData<Message> {
        val data = MutableLiveData<Message>()

        val produk = MenuItem(
            sellerID = firebaseAuth.uid.toString(),
            itemID = idProduk,
            imageUrl = image,
            itemName = namaProduk,
            itemPrice = harga,
            itemShortDesc = desc,
            itemTag = jenis,
            itemStars = 5.0F,
            quantity = 0,
            status = state
        )

        firebaseDatabase.getReference("produk").child(idProduk)
            .setValue(produk)
            .addOnCompleteListener { dbTask ->
                if (dbTask.isSuccessful) {
                    Log.d(TAG, "Data berhasil ditambahkan")
                    data.value = Message("Success")
                } else {
                    Log.e(TAG, "Data gagal ditambahkan")
                    data.value = Message("Failed")
                }
            }
        return data
    }

    fun editProductByID(
        idProduk: String,
        namaProduk: String,
        jenis: String,
        harga: Int,
        image: Uri,
        desc: String
    ): LiveData<Message> {
        val data = MutableLiveData<Message>()
        val fileName = namaProduk + image + System.currentTimeMillis()
        val uploadTask = storageReference.reference.child(jenis).child(fileName).putFile(image)

        uploadTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Dapatkan URL download dari gambar
                uploadTask.result.storage.downloadUrl.addOnCompleteListener { downloadUrlTask ->
                    if (downloadUrlTask.isSuccessful) {
                        val downloadUrl = downloadUrlTask.result.toString()

                        Log.d(TAG, downloadUrl)

                        val uniqueID = firebaseDatabase.reference.push().key

                        val produk = MenuItem(
                            sellerID = firebaseAuth.uid.toString(),
                            itemID = uniqueID.toString(),
                            imageUrl = downloadUrl,
                            itemName = namaProduk,
                            itemPrice = harga,
                            itemShortDesc = desc,
                            itemTag = jenis,
                            itemStars = 5.0F,
                            quantity = 0
                        )

                        firebaseDatabase.getReference("produk").child(idProduk)
                            .setValue(produk)
                            .addOnCompleteListener { dbTask ->
                                if (dbTask.isSuccessful) {
                                    Log.d(TAG, "Data berhasil ditambahkan")
                                    data.value = Message("Success")
                                } else {
                                    Log.e(TAG, "Data gagal ditambahkan")
                                    data.value = Message("Failed")
                                }
                            }
                    } else {
                        data.value = Message("Failed to get download URL")
                    }
                }
            } else {
                data.value = Message("Failed to upload image")
            }
        }

        return data
    }

    fun deleteProductByID(idProduk: String): LiveData<Message> {
        val data = MutableLiveData<Message>()
        val produkRef = firebaseDatabase.getReference("produk")
        produkRef.child(idProduk).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                data.value = Message("Success")
            } else {
                data.value = Message("Failed")
            }
        }
        return data
    }

    fun deleteOrderById(orderId: String): LiveData<Message> {
        val data = MutableLiveData<Message>()
        val orderRef = firebaseDatabase.getReference("orders")
        orderRef.child(orderId).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                data.value = Message("Success")
            } else {
                data.value = Message("Failed")
            }
        }
        return data
    }

    fun getProdukWithID(id: String): LiveData<MenuItem> {
        val data = MutableLiveData<MenuItem>()
        val produkRef = firebaseDatabase.getReference("produk")

        produkRef.child(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    data.value = snapshot.getValue(MenuItem::class.java)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "onCancelled: ${error.message}")
            }

        })

        return data
    }

    fun updateOrderStateByID(
        orderId: String,
        orderState: String,
        orderPayment: String,
    ): LiveData<Message> {
        val data = MutableLiveData<Message>()
        val orderRef = firebaseDatabase.getReference("orders/$orderId")

        orderRef.updateChildren(
            mapOf(
                "orderStatus" to orderState,
                "orderPayment" to orderPayment,
            )
        ).addOnSuccessListener {
            Log.d(TAG, "Order successfully updated")
            data.value = Message("Success")
        }.addOnFailureListener {
            Log.e(TAG, "Failed to update order")
            data.value = Message("Error: ${it.message}")
        }

        return data
    }

    fun updateProfileUser(
        nama: String,
        noTelpon: String,
        foto: Uri
    ): LiveData<Message> {
        // UPLOAD IMAGE FIRST
        val data = MutableLiveData<Message>()
        val fileName = nama + foto + System.currentTimeMillis()
        // Upload gambar ke Firebase Storage
        val uploadTask = storageReference.reference.child("profile").child(fileName).putFile(foto)

        uploadTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Dapatkan URL download dari gambar
                uploadTask.result.storage.downloadUrl.addOnCompleteListener { downloadUrlTask ->
                    if (downloadUrlTask.isSuccessful) {
                        val downloadUrl = downloadUrlTask.result.toString()
                        val userRef = firebaseDatabase.getReference("users/${firebaseAuth.uid}")
                        userRef.updateChildren(
                            mapOf(
                                "nama" to nama,
                                "noTelpon" to noTelpon,
                                "foto" to downloadUrl
                            )
                        ).addOnSuccessListener {
                            data.value = Message("Success")
                        }.addOnFailureListener {
                            data.value = Message("Failed")
                        }
                    } else {
                        data.value = Message("Failed to get download URL")
                    }
                }
            } else {
                data.value = Message("Failed to upload image")
            }
        }




        return data
    }

    fun getProdukFromDB(): LiveData<List<MenuItem>> {
        val data = MutableLiveData<List<MenuItem>>()
        val produkRef = firebaseDatabase.getReference("produk")
        val firebaseAuthID = firebaseAuth.uid.toString()

        produkRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val produkList = mutableListOf<MenuItem>()
                val dataSnapshot = task.result

                Log.d(TAG, firebaseAuthID.toString())

                // Periksa apakah DataSnapshot ada
                if (dataSnapshot.exists()) {
                    for (childSnapshot in dataSnapshot.children) {
                        val id = childSnapshot.child("itemID").getValue(String::class.java) ?: ""
                        val imageUrl = childSnapshot.child("imageUrl").getValue(String::class.java)
                            ?: "IMAGE_URL"
                        val itemPrice =
                            childSnapshot.child("itemPrice").getValue(Int::class.java) ?: 0
                        val itemTag =
                            childSnapshot.child("itemTag").getValue(String::class.java) ?: ""
                        val itemShortDesc =
                            childSnapshot.child("itemShortDesc").getValue(String::class.java) ?: ""
                        val itemName =
                            childSnapshot.child("itemName").getValue(String::class.java) ?: ""
                        val sellerID =
                            childSnapshot.child("sellerID").getValue(String::class.java) ?: ""
                        val quantity =
                            childSnapshot.child("quantity").getValue(Int::class.java) ?: 0
                        val state =
                            childSnapshot.child("status").getValue(Boolean::class.java) ?: false

                        if (firebaseAuthID == sellerID) {
                            val produk = MenuItem(
                                sellerID,
                                id,
                                imageUrl,
                                itemName,
                                itemPrice,
                                itemShortDesc,
                                itemTag,
                                5.0F, // Misalkan nilai itemStars tetap 5.0F
                                quantity,
                                state
                            )
                            produkList.add(produk)
                        }
                    }

                    data.value = produkList
                } else {
                    Log.e(TAG, "DataSnapshot tidak ada")
                }
            } else {
                Log.e(TAG, "Gagal mengambil data")
            }
        }
        return data
    }

    //    pesanan
    fun insertOrderRecord(order: OrderHistoryItem) {
        Log.d("insertOrderRecord", order.orderId)
        databaseRef.child("orders").child(order.orderId)
            .setValue(order).addOnCompleteListener {
                Log.d(TAG, "insertOrderRecord: ${it.result}")
                if (it.isSuccessful) {
                    Log.d(TAG, "insertOrderRecord: Success insert record")
                } else {
                    Log.e(TAG, "insertOrderRecord: error", it.exception)
                }
            }
    }

    fun getOrderRecord(): LiveData<List<OrderHistoryItem>> {
        val orders = MutableLiveData<List<OrderHistoryItem>>()
        databaseRef.child("orders")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d(TAG, "onDataChange orders: $snapshot")
                    if (snapshot.exists()) {
                        val orderList = mutableListOf<OrderHistoryItem>()
                        for (record in snapshot.children) {
                            Log.d(TAG, "onDataChange orders child: $record")
                            val orderRecord = record.getValue(OrderHistoryItem::class.java)
                            if (orderRecord != null) {
                                orderList.add(orderRecord)
                            }
                        }
                        orders.value = orderList
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        return orders
    }


    fun getOrderPendapatan(): LiveData<List<OrderHistoryItem>> {
        val orders = MutableLiveData<List<OrderHistoryItem>>()
        databaseRef.child("orders")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d(TAG, "onDataChange orders: $snapshot")
                    if (snapshot.exists()) {
                        val orderList = mutableListOf<OrderHistoryItem>()
                        for (record in snapshot.children) {
                            Log.d(TAG, "onDataChange orders child: $record")
                            val orderRecord = record.getValue(OrderHistoryItem::class.java)
                            if (orderRecord != null && orderRecord.orderStatus.lowercase()
                                    .contains("selesai")
                            ) {
                                orderList.add(orderRecord)
                            }
                        }
                        orders.value = orderList
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        return orders
    }

    fun setFCM() {
        // Mendapatkan UID pengguna
        val uidUser = FirebaseAuth.getInstance().uid

        // Mendaftar ke topik dengan UID pengguna
        firebaseMessaging.subscribeToTopic(uidUser.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Berlangganan ke topik '${uidUser}' berhasil.")
                } else {
                    Log.e("FCM", "Gagal berlangganan ke topik 'penjual'.")
                }
            }
    }

    fun unsubFCM() {
        // Mendapatkan UID pengguna
        val uidUser = FirebaseAuth.getInstance().uid

        // Mendaftar ke topik dengan UID pengguna
        firebaseMessaging.unsubscribeFromTopic(uidUser.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Unsubscribe ke topik '${uidUser}' berhasil.")
                } else {
                    Log.e("FCM", "Gagal Unsubscribe ke topik 'penjual'.")
                }
            }
    }


    companion object {
        private const val TAG = "CanteenRepository"

        @Volatile
        private var instance: CanteenRepository? = null
        fun getInstance(
            firebaseAuth: FirebaseAuth,
            firebaseDatabase: FirebaseDatabase,
            storageReference: FirebaseStorage,
            firebaseMessaging: FirebaseMessaging
        ): CanteenRepository =
            instance ?: synchronized(this) {
                instance ?: CanteenRepository(firebaseAuth, firebaseDatabase, storageReference, firebaseMessaging)
            }.also { instance = it }
    }
}