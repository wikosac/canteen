package org.d3if2101.canteenpenjual.ui.homeadminproduk.additem

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.d3if2101.canteenpenjual.databinding.ActivityAddItemBinding

class AddItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddItemBinding
    private lateinit var image: Uri
    private lateinit var namaItem: String
    private lateinit var harga: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()



        binding.previewImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_CHOOSE_IMAGE)

        }

        binding.footer.setOnClickListener {
            // Send image to repository
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

        if (namaItem != null && harga != null) {

        } else {
            Toast.makeText(this, "Data Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
        }
    }


    companion object {
        const val REQUEST_CODE_CHOOSE_IMAGE = 100
    }


}