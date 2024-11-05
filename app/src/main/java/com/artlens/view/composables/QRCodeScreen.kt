package com.artlens.view.composables

import android.Manifest
import android.content.pm.PackageManager
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.artlens.R
import com.artlens.utils.UserPreferences
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executors

@OptIn(ExperimentalGetImage::class)
@Composable
fun QRCodeScanner(
    onQRCodeScanned: (String) -> Unit,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onRecommendationClick: () -> Unit,
    onUSerClick: () -> Unit,
    showDialog: Boolean,
    onDismissDialog: () -> Unit,
    logOutClick: () -> Unit,
    onViewFavoritesClick: () -> Unit
) {



    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismissDialog,
            buttons = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp) // Add padding to the box
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.Center), // Center Column within Box
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Hi ${UserPreferences.getUsername()}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )


                        Spacer(modifier = Modifier.height(8.dp)) // Add space between the button and the line

                        // Horizontal Divider
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color.Black) // Color of the divider
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        // First Option Button
                        Button(
                            onClick = {
                                onDismissDialog()// Dismiss the dialog It should navigate to favourites page
                                onViewFavoritesClick() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clip(CircleShape), // Set a fixed height for the button
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Transparent,
                                contentColor = Color.Black
                            ),
                            elevation = ButtonDefaults.elevation(0.dp)
                        ) {
                            Text("View Favorites")
                        }

                        // Second Option Button
                        Button(
                            onClick = {
                                logOutClick()
                                onDismissDialog()} // Dismiss the dialog
                            ,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clip(CircleShape), // Set a fixed height for the button
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Transparent,
                                contentColor = Color.Black
                            ),
                            elevation = ButtonDefaults.elevation(0.dp)
                        ) {
                            Text("Log Out")
                        }
                    }
                }
            }
        )
    }


    val context = LocalContext.current
    val lifecycleOwner = LocalContext.current as LifecycleOwner
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val scanExecutor = Executors.newSingleThreadExecutor()

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Permission launcher
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
        }
    )

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }

    if (hasCameraPermission) {



        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Barra superior
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .background(Color.White),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Flecha de retroceso
                    IconButton(onClick = onBackClick) {
                        Image(
                            painter = painterResource(id = R.drawable.arrow),
                            contentDescription = "Back Arrow",
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    // Título centrado
                    Text(
                        text = "SIGN UP",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Icono de perfil a la derecha
                    IconButton(onClick = onUSerClick) {
                        Image(
                            painter = painterResource(id = R.drawable.profile),
                            contentDescription = "Profile Icon",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(horizontal = 30.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center

            ){

            AndroidView(

                factory = { viewContext ->
                    val previewView = androidx.camera.view.PreviewView(viewContext)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()

                        // Preview Use Case
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

                        // ImageAnalysis Use Case for QR code scanning
                        val imageAnalysis = ImageAnalysis.Builder()
                            .setTargetResolution(Size(1280, 720))
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()

                        imageAnalysis.setAnalyzer(scanExecutor) { imageProxy ->
                            processImageProxy(imageProxy, onQRCodeScanned)
                        }

                        // Select the back camera as default
                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner, cameraSelector, preview, imageAnalysis
                        )
                    }, ContextCompat.getMainExecutor(viewContext))

                    previewView
                }
            )}

            // Barra de navegación inferior sobrepuesta
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(vertical = 8.dp)
                    .height(50.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onHomeClick) {
                    Image(
                        painter = painterResource(id = R.drawable.house),
                        contentDescription = "Home",
                        modifier = Modifier.size(30.dp)
                    )
                }

                IconButton(onClick = { /* Acción para ir a Museos */ }) {
                    Image(
                        painter = painterResource(id = R.drawable.camera),
                        contentDescription = "Museos",
                        modifier = Modifier.size(30.dp)
                    )
                }

                IconButton(onClick = onRecommendationClick) {
                    Image(
                        painter = painterResource(id = R.drawable.fire),
                        contentDescription = "Recommendations",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }

    }
}

// Process image to scan QR code
@OptIn(ExperimentalGetImage::class)
private fun processImageProxy(
    imageProxy: ImageProxy,
    onQRCodeScanned: (String) -> Unit
) {
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        val scanner = BarcodeScanning.getClient()

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    when (barcode.valueType) {
                        Barcode.TYPE_URL -> {
                            onQRCodeScanned(barcode.url!!.url!!)
                        }
                        Barcode.TYPE_TEXT -> {
                            onQRCodeScanned(barcode.displayValue!!)
                        }
                        // Handle other barcode types as needed
                    }
                }
            }
            .addOnFailureListener {
                // Handle failure
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }
}
