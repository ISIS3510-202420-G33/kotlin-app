package com.example.artlens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurar el botón para lanzar MapsActivity
        val viewMapButton = findViewById<Button>(R.id.view_map_button)
        viewMapButton.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        // Configurar el botón de la cámara para lanzar QRScannerActivity
        val cameraButton = findViewById<ImageButton>(R.id.camera_icon)
        cameraButton.setOnClickListener {
            val intent = Intent(this, QRScannerActivity::class.java)
            startActivity(intent)
        }

        // Configurar el ViewPager2 para el carrusel de imágenes
        val viewPager = findViewById<ViewPager2>(R.id.image_carousel)

        // Lista de URLs de imágenes mock
        val imageUrls = listOf(
            "https://th.bing.com/th/id/R.ac9e679812389a4b4abb15c8cf4705ee?rik=7Gp6HUNIzI7AxA&riu=http%3a%2f%2fmedia.architecturaldigest.com%2fphotos%2f5900cc370638dd3b70018b33%2fmaster%2fpass%2fSecrets+of+Louvre+1.jpg&ehk=hAKFCm8l7I4rrpUDUohq%2bqtJ%2f%2b4bovQlQM2lH3C4fCk%3d&risl=&pid=ImgRaw&r=0",
            "https://th.bing.com/th/id/R.f53b1a450649b368e2ab3f84efea6000?rik=5PTXwoJnk%2bwbvQ&pid=ImgRaw&r=0",
            "https://th.bing.com/th/id/OIP.v8lqJMlsSFHFBnIMj2z3jgHaDv?rs=1&pid=ImgDetMain",
            "https://via.placeholder.com/400x200.png?text=Image4",
            "https://via.placeholder.com/400x200.png?text=Image5",
            "https://via.placeholder.com/400x200.png?text=Image6",
            "https://via.placeholder.com/400x200.png?text=Image7",
            "https://via.placeholder.com/400x200.png?text=Image8",
            "https://via.placeholder.com/400x200.png?text=Image9",
            "https://via.placeholder.com/400x200.png?text=Image10"
        )

        // Configurar el adaptador para ViewPager2
        val adapter = ImageCarouselAdapter(this, imageUrls)
        viewPager.adapter = adapter

        // Aplicar PageTransformer para el efecto de "ver qué viene después"
        viewPager.setPageTransformer { page, position ->
            val absPosition = Math.abs(position)
            page.scaleY = 0.85f + (1 - absPosition) * 0.15f  // Efecto de escalado
        }

        // Configurar opciones adicionales
        viewPager.offscreenPageLimit = 3  // Pre-cargar páginas antes y después
        viewPager.clipToPadding = false
        viewPager.clipChildren = false
    }
}
