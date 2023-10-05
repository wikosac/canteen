package org.d3if2101.canteenpenjual.ui.homeadminproduk.pilihmenu

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import org.d3if2101.canteenpenjual.R
import org.d3if2101.canteenpenjual.data.model.Produk
import org.d3if2101.canteenpenjual.databinding.ActivityPilihMenuBinding
import org.d3if2101.canteenpenjual.ui.ViewModelFactory
import org.d3if2101.canteenpenjual.ui.homeadminproduk.additem.AddItemActivity
import org.d3if2101.canteenpenjual.ui.homeadminproduk.edititem.EditItemActivity

class PilihMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPilihMenuBinding
    private lateinit var stringReceived: String

    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this.application)
    }
    private val viewModel: PilihMenuViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPilihMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        // Get the string from the intent
        getStringIntent()

        // Set the layout manager for the recyclerview
        binding.rv.layoutManager = LinearLayoutManager(this)

        val adapter = PilihMenuAdapter(viewModel.produkList,
            object : PilihMenuAdapter.OnItemClickCallback {
                override fun onItemClick(data: Produk) {
                    val dialogBuilder = AlertDialog.Builder(this@PilihMenuActivity)
                    val inflater = LayoutInflater.from(this@PilihMenuActivity)
                    val dialogView = inflater.inflate(R.layout.popup_pilihmenu, null)
                    dialogBuilder.setView(dialogView)

                    val alertDialog = dialogBuilder.create()
                    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    alertDialog.show()


//                    val photoViewPopup = dialogView.findViewById<ImageView>(R.id.photoViewPopup)
//                    Glide.with(this@BelajarActivity).asGif().load(data.gif).into(photoViewPopup)
//
//                    val textTitle = dialogView.findViewById<TextView>(R.id.textTitle)
//
//                    textTitle.text = data.text.uppercase()

                    val btnEdit = dialogView.findViewById<Button>(R.id.btnEdit)
                    btnEdit.setOnClickListener {
                        startActivity(
                            Intent(
                                this@PilihMenuActivity,
                                EditItemActivity::class.java
                            )
                        )
                    }

                    val btnDelete = dialogView.findViewById<Button>(R.id.btnDelete)
                    btnDelete.setOnClickListener {
                        showDeleteConfirmationSnackbar()
                        alertDialog.cancel()
                    }

                }
            })
        binding.rv.adapter = adapter

        binding.btnAdd.setOnClickListener {
            startActivity(Intent(this@PilihMenuActivity, AddItemActivity::class.java))
        }


    }

    private fun showDeleteConfirmationSnackbar() {
        val rootView = findViewById<View>(android.R.id.content)
        val snackbar = Snackbar.make(rootView, "Delete this item?", Snackbar.LENGTH_LONG)
            .setAction("DELETE") {
                Snackbar.make(rootView, "Item deleted.", Snackbar.LENGTH_SHORT).show()
            }

        snackbar.show()
    }

    private fun getStringIntent() {
        stringReceived = intent.getStringExtra("produk") ?: ""
    }


}