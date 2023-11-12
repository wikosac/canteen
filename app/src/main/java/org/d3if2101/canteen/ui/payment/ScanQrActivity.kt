package org.d3if2101.canteen.ui.payment

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import org.d3if2101.canteen.databinding.ActivityScanQrBinding

class ScanQrActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private lateinit var binding: ActivityScanQrBinding
    private lateinit var scannerView: ZXingScannerView

    // FOR INTENT
    private var totalItemPrice = 0
    private var takeAwayTime = ""
    private var paymentMethod = ""
    private var method: String = ""

    companion object {
        private const val CAMERA_PERMISSION_REQUEST = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanQrBinding.inflate(layoutInflater)
        setContentView(binding.root)


        totalItemPrice = intent.getIntExtra("totalItemPrice", 0)
        paymentMethod = intent?.getStringExtra("paymentMethod").toString()
        takeAwayTime = intent?.getStringExtra("takeAwayTime").toString()
        method = intent?.getStringExtra("method").toString()

        scannerView = ZXingScannerView(this)
        binding.scannerLayout.addView(scannerView)

        if (checkCameraPermission()) {
            startScanner()
        } else {
            requestCameraPermission()
        }


    }


    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST
        )
    }

    override fun onResume() {
        super.onResume()
        if (checkCameraPermission()) {
            startScanner()
        } else {
            requestCameraPermission()
        }
    }

    override fun onPause() {
        super.onPause()
        scannerView.stopCamera()
    }

    private fun startScanner() {
        scannerView.setResultHandler(this)
        scannerView.startCamera()
    }


    override fun handleResult(result: Result?) {
        val scannedData = result?.text ?: ""
        Toast.makeText(this, "Scanned: $scannedData", Toast.LENGTH_SHORT).show()
        if (scannedData == "payment=true") {
            scannerView.stopCamera()
            val intent = Intent(this, OrderDoneActivity::class.java)
            intent.putExtra("totalItemPrice", totalItemPrice)
            intent.putExtra("takeAwayTime", takeAwayTime)
            paymentMethod = "Sukses: Pembayaran QRIS"
            intent.putExtra("paymentMethod", paymentMethod)
            intent.putExtra("method", method)
            startActivity(intent)
            finish()
        } else {
            scannerView.resumeCameraPreview(this)
        }
        scannerView.resumeCameraPreview(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanner()
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}