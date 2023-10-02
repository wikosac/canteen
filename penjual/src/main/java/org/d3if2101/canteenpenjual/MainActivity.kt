package org.d3if2101.canteenpenjual



import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.d3if2101.canteenpenjual.ui.homeadminproduk.edititem.EditItemActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startActivity(Intent(this@MainActivity, EditItemActivity::class.java))
    }
}