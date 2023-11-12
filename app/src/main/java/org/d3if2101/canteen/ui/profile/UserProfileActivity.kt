package org.d3if2101.canteen.ui.profile
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MediatorLiveData
import com.bumptech.glide.Glide
import org.d3if2101.canteen.R
import org.d3if2101.canteen.data.model.UserModel
import org.d3if2101.canteen.databinding.ActivityUserProfileBinding
import org.d3if2101.canteen.ui.ViewModelFactory
import org.d3if2101.canteen.ui.login.LoginViewModel
import org.d3if2101.canteen.ui.profile.UpdateProfileActivity

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfileBinding
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(this.application)
    }
    private val viewModel: LoginViewModel by viewModels { factory }

    // MediatorLiveData to observe the token and UserModel data
    private val userData = MediatorLiveData<UserModel>()

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
            changeActivity()
        }
        setContent()

        // Observe the token
        viewModel.getTokenPref().observe(this) { token ->
            // Remove previous observer to avoid duplication
            userData.removeSource(userData)

            // Observe UserModel data based on the token
            userData.addSource(viewModel.getUserWithToken(token.toString())) { user ->
                userData.value = user
            }
        }
    }

    private fun changeActivity() {
        // Access UserModel data directly from the MediatorLiveData
        val user = userData.value

        // Check if user is not null
        user?.let {
            // Create intent and pass UserModel information as extras
            val intent = Intent(this@UserProfileActivity, UpdateProfileActivity::class.java)
            intent.putExtra("profile_name", it.nama)
            intent.putExtra("profile_telpon", it.noTelpon)
            intent.putExtra("profile_picture", it.foto)
            // Start the UpdateProfileActivity
            startActivity(intent)
        }
    }

    private fun setContent() {
        // Observe UserModel data directly from the MediatorLiveData
        userData.observe(this) { user ->
            user?.let {
                with(binding) {
                    Glide.with(this@UserProfileActivity)
                        .load(it.foto)
                        .error(R.drawable.default_item_image)
                        .into(profileUserIcon)
                    profileTopNameTv.text = it.nama
                    profileTopEmailTv.text = it.email
                    txtNameProfile.text = it.nama
                    txtTelpProfile.text = it.noTelpon
                }
            }
        }
    }

    fun goBack(view: View) {
        onBackPressed()
    }
}
