package org.d3if2101.canteen.ui.penjual.homeadminproduk.edititem

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import org.angmarch.views.NiceSpinner
import org.d3if2101.canteen.R
import java.util.LinkedList

class EditItemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item)

        val niceSpinner = findViewById<View>(R.id.nice_spinner) as NiceSpinner
//        val selectedCategoryText = findViewById<TextView>(R.id.selected_category_text)
        val dataset: List<String> =
            LinkedList(mutableListOf("Pilih Kategori", "Makanan", "Minuman", "Camilan"))
        niceSpinner.attachDataSource(dataset)


        niceSpinner.setOnSpinnerItemSelectedListener { parent, view, position, id ->
            // This example uses String, but your type can be any
            val item = parent.getItemAtPosition(position) as String

//            if (selectedCategoryText != null) {
//                selectedCategoryText.text = "Kategori yang dipilih: $item"
//            }
        }

    }
}