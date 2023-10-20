package org.d3if2101.canteen.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import org.d3if2101.canteen.databinding.ActivityUpdateProfileBinding
import org.d3if2101.canteen.ui.ViewModelFactory
import org.d3if2101.canteen.ui.dashboard.DashboardActivity
import org.d3if2101.canteen.ui.login.LoginViewModel
import org.d3if2101.canteen.ui.penjual.homeadminproduk.additem.AddItemActivity

class UpdateProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateProfileBinding
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this.application)
    }
    private val viewModel: LoginViewModel by viewModels { factory }
    private lateinit var image: Uri
    private lateinit var nama: String
    private lateinit var noTelpon: String
    private lateinit var email: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivFotoProfil.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, AddItemActivity.REQUEST_CODE_CHOOSE_IMAGE)

        }

        binding.btnSelesaiPerbaruiProfil.setOnClickListener {
            nama = binding.etNama.text.toString()
            noTelpon = binding.etNoTelp.text.toString()
            if (nama.isNotEmpty() && noTelpon.isNotEmpty() && ::image.isInitialized) {
                binding.btnSelesaiPerbaruiProfil.isEnabled = false
                binding.btnSelesaiPerbaruiProfil.text = "Please Wait..."

                viewModel.updateProfileUser(nama, noTelpon, image).observe(this) {
                    binding.btnSelesaiPerbaruiProfil.isEnabled = true
                    binding.btnSelesaiPerbaruiProfil.text = "Upload"
                    Toast.makeText(this, "Update Berhasil", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@UpdateProfileActivity, DashboardActivity::class.java))
                    finish()
                }
            } else {
                binding.btnSelesaiPerbaruiProfil.isEnabled = true
                binding.btnSelesaiPerbaruiProfil.text = "Upload"

                Toast.makeText(this, "Update Gagal ", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AddItemActivity.REQUEST_CODE_CHOOSE_IMAGE && resultCode == Activity.RESULT_OK) {
            // Gambar dipilih
            image = data?.data ?: return

            // Set gambar ke ImageView
            binding.ivFotoProfil.setImageURI(image)
        }
    }

}