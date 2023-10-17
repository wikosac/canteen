package org.d3if2101.canteen.ui.penjual.homeadminproduk.edititem

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import org.angmarch.views.NiceSpinner
import org.d3if2101.canteen.R
import org.d3if2101.canteen.databinding.ActivityEditItemBinding
import org.d3if2101.canteen.ui.ViewModelFactory
import org.d3if2101.canteen.ui.penjual.homeadminproduk.HomeProduk
import org.d3if2101.canteen.ui.penjual.homeadminproduk.additem.AddItemActivity
import org.d3if2101.canteen.ui.penjual.homeadminproduk.additem.AddItemViewModel
import java.util.LinkedList

class EditItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditItemBinding
    private lateinit var image: Uri
    private lateinit var namaItem: String
    private lateinit var harga: String
    private var kategori: String = "Pilih Kategori"
    private lateinit var desc: String
    private lateinit var idProduct: String


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
            startActivityForResult(intent, AddItemActivity.REQUEST_CODE_CHOOSE_IMAGE)

        }
        idProduct = intent.getStringExtra("produkID") ?: ""

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

        if (namaItem.isNotEmpty() && harga.isNotEmpty() && kategori != "Pilih Kategori" && ::image.isInitialized && desc.isNotEmpty()) {
            binding.btnUpload.isEnabled = false
            binding.btnUpload.text = "Please Wait..."

            viewModel.editProductbyID(idProduct, namaItem, kategori, harga.toInt(), image, desc)
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

                        Toast.makeText(this, "Upload Gagal ${message}", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(
                this,
                "Data Tidak Boleh Kosong atau Gambar belum dipilih",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    companion object {
        const val REQUEST_CODE_CHOOSE_IMAGE = 100
    }
}