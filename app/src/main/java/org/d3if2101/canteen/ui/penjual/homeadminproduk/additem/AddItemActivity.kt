package org.d3if2101.canteen.ui.penjual.homeadminproduk.additem

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import org.d3if2101.canteen.databinding.ActivityAddItemBinding
import org.d3if2101.canteen.ui.ViewModelFactory
import org.d3if2101.canteen.ui.penjual.homeadminproduk.HomeProduk

class AddItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddItemBinding
    private lateinit var image: Uri
    private lateinit var namaItem: String
    private lateinit var harga: String
    private var kategori: String = "Pilih Kategori"
    private lateinit var desc: String


    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this.application)
    }
    private val viewModel: AddItemViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val niceSpinner = binding.pilihKategori
        niceSpinner.attachDataSource(org.d3if2101.canteen.utils.staticDataSetKategori)
        niceSpinner.setOnSpinnerItemSelectedListener { parent, view, position, id ->
            kategori = parent.getItemAtPosition(position) as String
        }

        binding.previewImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_CHOOSE_IMAGE)

        }

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
            viewModel.inputProduktoDB(namaItem, kategori, harga, image, desc)
                .observe(this) { message ->
                    if (message.message == "Success") {
                        Toast.makeText(this, "Upload Berhasil", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, HomeProduk::class.java))
                        finish()
                    } else {
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