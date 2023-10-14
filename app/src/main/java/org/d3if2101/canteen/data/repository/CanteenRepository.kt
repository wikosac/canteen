package org.d3if2101.canteen.data.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import org.d3if2101.canteen.data.model.Message
import org.d3if2101.canteen.data.model.Produk
import org.d3if2101.canteen.data.model.UserModel
import org.d3if2101.canteen.datamodels.OrderHistoryItem

class CanteenRepository private constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
    private val storageReference: FirebaseStorage
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
        firebaseAuth.signOut()
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
        harga: String,
        image: String,
        stock: String
    ): LiveData<Message> {

        val uniqueID = firebaseDatabase.reference.push().key
        val data = MutableLiveData<Message>()

        val produk = Produk(
            id = uniqueID.toString(),
            nama = namaProduk,
            harga = harga.toFloat(),
            stok = stock.toInt(),
            penjualId = firebaseAuth.uid.toString(),
            jenis = jenis, // Use the provided 'jenis' parameter
            gambar = image
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
        namaProduk: String, jenis: String, harga: String, image: Uri, stock: String
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

                        val produk = Produk(
                            id = uniqueID.toString(),
                            nama = namaProduk,
                            harga = harga.toFloat(),
                            stok = stock.toInt(),
                            penjualId = firebaseAuth.uid.toString(),
                            jenis = jenis, // Use the provided 'jenis' parameter
                            gambar = downloadUrl
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

    fun editProductByID(
        idProduk: String,
        namaProduk: String,
        jenis: String,
        harga: String,
        image: Uri,
        stock: String
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

                        val produk = Produk(
                            id = uniqueID.toString(),
                            nama = namaProduk,
                            harga = harga.toFloat(),
                            stok = stock.toInt(),
                            penjualId = firebaseAuth.uid.toString(),
                            jenis = jenis, // Use the provided 'jenis' parameter
                            gambar = downloadUrl
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

    fun getProdukFromDB(): LiveData<List<Produk>> {
        val data = MutableLiveData<List<Produk>>()
        val produkRef = firebaseDatabase.getReference("produk")
        val firebaseAuthID = firebaseAuth.uid.toString()

        produkRef.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val produkList = mutableListOf<Produk>()
                val dataSnapshot = task.result

                // Periksa apakah DataSnapshot ada
                if (dataSnapshot.exists()) {
                    for (childSnapshot in dataSnapshot.children) {
                        val id = childSnapshot.key ?: ""
                        val gambar =
                            childSnapshot.child("gambar").getValue(String::class.java) ?: ""
                        val harga = childSnapshot.child("harga").getValue(Float::class.java) ?: 0.0f
                        val jenis = childSnapshot.child("jenis").getValue(String::class.java) ?: ""
                        val nama = childSnapshot.child("nama").getValue(String::class.java) ?: ""
                        val penjualId =
                            childSnapshot.child("penjualId").getValue(String::class.java) ?: ""
                        val stok = childSnapshot.child("stok").getValue(Int::class.java) ?: 0

                        if (firebaseAuthID == penjualId) {
                            val produk = Produk(
                                id,
                                nama,
                                harga,
                                stok,
                                penjualId,
                                jenis,
                                gambar
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

    companion object {
        private const val TAG = "testo"

        @Volatile
        private var instance: CanteenRepository? = null
        fun getInstance(
            firebaseAuth: FirebaseAuth,
            firebaseDatabase: FirebaseDatabase,
            storageReference: FirebaseStorage
        ): CanteenRepository =
            instance ?: synchronized(this) {
                instance ?: CanteenRepository(firebaseAuth, firebaseDatabase, storageReference)
            }.also { instance = it }
    }
}