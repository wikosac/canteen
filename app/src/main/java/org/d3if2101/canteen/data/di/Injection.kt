package org.d3if2101.canteen.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import org.d3if2101.canteen.data.repository.CanteenRepository
import org.d3if2101.canteen.ui.SettingPreferences

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

object Injection {
    fun provideRepository(context: Context): CanteenRepository {
        val firebaseAuth = FirebaseAuth.getInstance()
        val firebaseDatabase = FirebaseDatabase.getInstance()
        val storageReference = FirebaseStorage.getInstance()
        return CanteenRepository.getInstance(firebaseAuth, firebaseDatabase, storageReference)
    }

    fun providePreferences(context: Context): SettingPreferences {
        return SettingPreferences.getInstance(context.dataStore)
    }
}
