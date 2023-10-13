package org.d3if2101.canteen.ui.penjual.homeadminproduk.pilihmenu

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import org.d3if2101.canteen.R
import org.d3if2101.canteen.data.model.Produk
import org.d3if2101.canteen.databinding.ActivityPilihMenuBinding
import org.d3if2101.canteen.ui.ViewModelFactory
import org.d3if2101.canteen.ui.penjual.homeadminproduk.additem.AddItemActivity
import org.d3if2101.canteen.ui.penjual.homeadminproduk.edititem.EditItemActivity

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

        // Run RV
        viewModel.getDataFromDB()
        viewModel.getFilteredData().observe(this) {
            recyclerViewOn(it)
        }

        binding.btnAdd.setOnClickListener {
            startActivity(Intent(this@PilihMenuActivity, AddItemActivity::class.java))
        }
    }


    private fun recyclerViewOn(produk: List<Produk>) {
        val adapter = PilihMenuAdapter(
            produk,
            object : PilihMenuAdapter.OnItemClickCallback {
                override fun onItemClick(data: Produk) {
                    val dialogBuilder = AlertDialog.Builder(this@PilihMenuActivity)
                    val inflater = LayoutInflater.from(this@PilihMenuActivity)
                    val dialogView = inflater.inflate(R.layout.popup_pilihmenu, null)
                    dialogBuilder.setView(dialogView)

                    val alertDialog = dialogBuilder.create()
                    alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    alertDialog.show()


                    val photoViewPopup = dialogView.findViewById<ImageView>(R.id.iv_Produk)
                    Picasso.get()
                        .load(data.gambar)
                        .into(photoViewPopup)

                    val textTitle = dialogView.findViewById<TextView>(R.id.tv_Produk)
                    textTitle.text = data.nama

                    val textHarga = dialogView.findViewById<TextView>(R.id.tv_ProdukHarga)
                    textHarga.text = data.harga.toString()

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
                        viewModel.deleteProdukByID(data.id)
                        viewModel.getDataFromDB()
                        showDeleteConfirmationSnackbar(
                            data
                        )
                        alertDialog.cancel()
                    }

                }
            })
        binding.rv.adapter = adapter
    }

    private fun showDeleteConfirmationSnackbar(
        deletedProduct: Produk
    ) {
        val rootView = findViewById<View>(android.R.id.content)
        val snackbar = Snackbar.make(rootView, "Undo Delete this item?", Snackbar.LENGTH_LONG)
            .setAction("YES") {
                Snackbar.make(rootView, "Undo Deleted Item !", Snackbar.LENGTH_SHORT).show()
                // Run Insert Data
                viewModel.inputProduktoDB(
                    deletedProduct.nama,
                    deletedProduct.jenis,
                    deletedProduct.harga.toString(),
                    deletedProduct.gambar,
                    deletedProduct.stok.toString()
                ).observe(this) {
                    if (it.message == "Success") {
                        viewModel.getDataFromDB()
                    } else {
                        Toast.makeText(this, "Failed Undo Deleted Produk", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        snackbar.show()
    }


    private fun getStringIntent() {
        stringReceived = intent.getStringExtra("produk") ?: ""
        viewModel.setStringReceived(stringReceived)
        Log.d("PilihMenuActivity", stringReceived)
    }


}