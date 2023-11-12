package org.d3if2101.canteen.ui.penjual.homeadminproduk.edititem

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import org.angmarch.views.NiceSpinner
import org.d3if2101.canteen.R
import org.d3if2101.canteen.databinding.ActivityEditItemBinding
import org.d3if2101.canteen.datamodels.MenuItem
import org.d3if2101.canteen.ui.ViewModelFactory
import org.d3if2101.canteen.ui.penjual.homeadminproduk.HomeProduk
import org.d3if2101.canteen.ui.penjual.homeadminproduk.additem.AddItemViewModel
import java.util.LinkedList

class EditItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditItemBinding
    private lateinit var image: Uri
    private lateinit var namaItem: String
    private lateinit var harga: String
    private var kategori: String = "Pilih Kategori"
    private lateinit var desc: String
    private var menuItem: MenuItem? = null


    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this.application)
    }
    private val viewModel: AddItemViewModel by viewModels { factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val niceSpinner = findViewById<View>(R.id.pilihKategori) as NiceSpinner
        val dataset: List<String> =
            LinkedList(mutableListOf("Pilih Kategori", "Makanan", "Minuman", "Camilan"))
        niceSpinner.attachDataSource(dataset)


        niceSpinner.setOnSpinnerItemSelectedListener { parent, view, position, id ->
            // This example uses String, but your type can be any
            kategori = parent.getItemAtPosition(position) as String
        }

        binding.previewImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_CHOOSE_IMAGE)

        }
        // Get Intent
        menuItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("data_menuItem", MenuItem::class.java)
        } else {
            intent.getParcelableExtra("data_menuItem")
        }

        // set the view from intent
        Picasso.get().load(menuItem!!.imageUrl).into(binding.previewImageView)

        binding.namaItem.setText(menuItem!!.itemName)
        binding.namaHarga.setText(menuItem!!.itemPrice.toString())
        binding.desc.setText(menuItem!!.itemShortDesc)
//        image = menuItem!!.imageUrl

        binding.btnUpload.setOnClickListener {
            uploadProduk()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_CHOOSE_IMAGE && resultCode == Activity.RESULT_OK) {
            // Gambar dipilih
            image = data?.data ?: return

            // Set gambar ke ImageView
            binding.previewImageView.setImageURI(image)
        }
    }

    private fun uploadProduk() {
        namaItem = binding.namaItem.text.toString()
        harga = binding.namaHarga.text.toString()
        desc = binding.desc.text.toString()

        if (namaItem.isNotEmpty() && harga.isNotEmpty() && kategori != "Pilih Kategori" && desc.isNotEmpty()) {
            binding.btnUpload.isEnabled = false
            binding.btnUpload.text = "Please Wait..."
            if (::image.isInitialized) {
                viewModel.editProductbyID(
                    menuItem!!.itemID,
                    namaItem,
                    kategori,
                    harga.toInt(),
                    image,
                    desc
                ).observe(this) { message ->
                        if (message.message == "Success") {
                            binding.btnUpload.isEnabled = true
                            binding.btnUpload.text = "Upload"

                            Toast.makeText(this, "Upload Berhasil", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, HomeProduk::class.java))
                            finish()
                        } else {
                            binding.btnUpload.isEnabled = true
                            binding.btnUpload.text = "Upload"

                            Toast.makeText(this, "Upload Gagal ${message}", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            } else {
                viewModel.editProductbyID(
                    menuItem!!.itemID,
                    namaItem,
                    kategori,
                    harga.toInt(),
                    image = null,
                    desc,
                    imageStringURL = menuItem!!.imageUrl
                )
                    .observe(this) { message ->
                        if (message.message == "Success") {
                            binding.btnUpload.isEnabled = true
                            binding.btnUpload.text = "Upload"

                            Toast.makeText(this, "Upload Berhasil", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, HomeProduk::class.java))
                            finish()
                        } else {
                            binding.btnUpload.isEnabled = true
                            binding.btnUpload.text = "Upload"

                            Toast.makeText(this, "Upload Gagal ${message}", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            }

        } else {
            Toast.makeText(
                this,
                "Kategori, Data Tidak Boleh Kosong atau Gambar belum dipilih",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        const val REQUEST_CODE_CHOOSE_IMAGE = 100
    }
}