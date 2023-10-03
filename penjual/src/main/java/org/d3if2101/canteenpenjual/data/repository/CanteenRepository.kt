package org.d3if2101.canteenpenjual.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.d3if2101.canteenpenjual.data.model.Message
import org.d3if2101.canteenpenjual.data.model.UserModel

class CanteenRepository private constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDatabase: FirebaseDatabase,
) {
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

    fun loginUser(email: String, pass: String): LiveData<Message> {
        val data = MutableLiveData<Message>()
        try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, pass)
            if (result.isSuccessful) {
                Log.d(TAG, "Login Berhasil")
                data.value = Message("Success")
            } else {
                Log.d(TAG, "Login Gagal")
                data.value = Message("Failed")
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
                    role = "penjual",
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
            firebaseDatabase: FirebaseDatabase
        ): CanteenRepository =
            instance ?: synchronized(this) {
                instance ?: CanteenRepository(firebaseAuth, firebaseDatabase)
            }.also { instance = it }
    }
}