package com.example.artlens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView


class QRScannerActivity : AppCompatActivity() {

    private lateinit var barcodeScannerView: DecoratedBarcodeView
    private val CAMERA_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_scanner)

        // Solicitar permiso para la cámara si no está concedido
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        } else {
            iniciarEscaneoDeQR() // Si ya se tiene permiso, inicia el escaneo de QR
        }

        // Configurar el botón de volver
        findViewById<ImageButton>(R.id.back_button).setOnClickListener {
            finish() // Cierra la actividad y vuelve a la anterior
        }
    }

    private fun iniciarEscaneoDeQR() {
        barcodeScannerView = findViewById(R.id.barcode_scanner)
        barcodeScannerView.decodeContinuous(callback)
    }

    // Callback para manejar el resultado del escaneo
    private val callback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            val qrCodeValue = result.text
            // Aquí puedes realizar la acción basada en el valor del QR
            if (qrCodeValue.isNotEmpty()) {
                // Aquí rediriges a ArtDetailView o cualquier otra actividad
                // val intent = Intent(this@QRScannerActivity, ArtDetailView::class.java)
                // startActivity(intent)
            }
        }
    }

    // Manejar el resultado de la solicitud de permisos
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                iniciarEscaneoDeQR() // Si se otorga el permiso, inicia el escaneo de QR
            } else {
                // Manejar el caso en que el permiso es denegado
            }
        }
    }
}
