package org.d3if2101.canteen.ui.penjual.order

import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import org.d3if2101.canteen.databinding.ActivityQrGenerateBinding

class QrGenerateActivity : AppCompatActivity() {

    private lateinit var binding : ActivityQrGenerateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrGenerateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val qrInfo = "payment=true"
        val qrCodeBitmap = generateQRCode(qrInfo)
        binding.qrCodeImageView.setImageBitmap(qrCodeBitmap)
    }

    private fun generateQRCode(data: String): Bitmap? {
        val multiFormater = MultiFormatWriter()
        return try {
            val bitMatrix: BitMatrix = multiFormater.encode(data, BarcodeFormat.QR_CODE, 500, 500)
            val barcodeEncoder = BarcodeEncoder()
            barcodeEncoder.createBitmap(bitMatrix)
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }
}