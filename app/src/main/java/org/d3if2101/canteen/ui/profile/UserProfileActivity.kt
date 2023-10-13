package org.d3if2101.canteen.ui.profile

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import de.hdodenhof.circleimageview.CircleImageView
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

        setContent()
    }

    private fun setContent() {
        viewModel.getUserWithToken(FirebaseAuth.getInstance().currentUser!!.uid).observe(this) { user ->
            with(binding) {
                Glide.with(this@UserProfileActivity).load(user.foto).into(profileUserIcon)
                profileTopNameTv.text = user.nama
                profileTopEmailTv.text = user.email
                txtTelpProfile.text = user.noTelpon
//                txtNameProfile.text
            }
        }
    }

    fun goBack(view: View) {onBackPressed()}
}