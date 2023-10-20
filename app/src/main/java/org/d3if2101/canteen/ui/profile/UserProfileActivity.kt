package org.d3if2101.canteen.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import org.d3if2101.canteen.R
import org.d3if2101.canteen.databinding.ActivityUserProfileBinding
import org.d3if2101.canteen.ui.ViewModelFactory
import org.d3if2101.canteen.ui.login.LoginViewModel


class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this.application)
    }
    private val viewModel: LoginViewModel by viewModels { factory }

    override fun onStart() {
        super.onStart()
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnUpdate.setOnClickListener {
            // Update Profile Activity
            startActivity(Intent(this@UserProfileActivity, UpdateProfileActivity::class.java))
        }
        setContent()
    }

    private fun setContent() {
        viewModel.getTokenPref().observe(this) {
            viewModel.getUserWithToken(it!!)
                .observe(this) { user ->
                    with(binding) {
                        Glide.with(this@UserProfileActivity)
                            .load(user.foto)
                            .error(R.drawable.default_item_image)
                            .into(profileUserIcon)
                        profileTopNameTv.text = user.nama
                        profileTopEmailTv.text = user.email
                        txtNameProfile.text = user.nama
                        txtTelpProfile.text = user.noTelpon
                    }
                }
        }
    }

    fun goBack(view: View) {onBackPressed()}
}