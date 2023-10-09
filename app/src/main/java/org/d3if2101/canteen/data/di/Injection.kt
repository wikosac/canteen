package org.d3if2101.canteen.data.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import org.d3if2101.canteen.data.repository.CanteenRepository

object Injection {
    fun provideRepository(context: Context): CanteenRepository {
        val firebaseAuth = FirebaseAuth.getInstance()
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val storageReference = FirebaseStorage.getInstance()
        return CanteenRepository.getInstance(firebaseAuth, firebaseDatabase, storageReference)
    }
}
