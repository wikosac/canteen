package org.d3if2101.canteen.data.repository

import android.util.Log
import android.widget.Toast
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
import org.d3if2101.canteen.ui.menu.MenuAdapter

class CanteenRepository private constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
    private val storageReference: FirebaseStorage
) {
    private val databaseReference: DatabaseReference = firebaseDatabase.getReference("users")
    private val uid = firebaseAuth.uid

//    private val _user = MutableLiveData<UserModel>()
//    val user: LiveData<UserModel> = _user

    fun registUser(
        email: String,
        pass: String
    ): LiveData<Message> {
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

    fun getUser(): LiveData<UserModel> {
        val userData = MutableLiveData<UserModel>()
        if (uid != null) {
            databaseReference.child(uid).addValueEventListener(
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
        Log.d(TAG, "getUser: ${userData.value}")
        return userData
    }

    fun checkRole(): LiveData<String> {
        val role = MutableLiveData<String>()
        if (uid != null) {
            databaseReference.child(uid).addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            role.value = snapshot.child("role").value.toString()
                            Log.d(TAG, "onDataChange: ${role.value}")
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, error.message)
                    }
                }
            )
        } else {
            Log.e(TAG, "Error in Check Role")
        }
        return role
    }

    fun loginUser(email: String, pass: String): LiveData<Message> {
        val data = MutableLiveData<Message>()
        try {
            firebaseAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
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
        foto: String
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
                    role = "pembeli",
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

    companion object {
        private const val TAG = "CanteenRepository"

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