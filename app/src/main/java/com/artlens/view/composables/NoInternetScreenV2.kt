package com.artlens.view.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artlens.R

@Composable
fun NoInternetScreenV2(
    onBackClick: () -> Unit,
    textTop: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Top Bar (Barra superior)
        Column {
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
                    text = textTop,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                // Icono de perfil a la derecha
                IconButton(onClick = { /* Acción de perfil */ }) {
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Profile Icon",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Add space after the top bar
        }

        // Centered Text Fields
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // This centers the column vertically
        ) {

            Image(
                painter = painterResource(id = R.drawable.no_wifi), // Replace with your drawable resource
                contentDescription = "Sample Image",
                modifier = Modifier
                    .size(150.dp) // Adjust the size of the image
                    .clip(RoundedCornerShape(12.dp)) // Optional: Rounded corners
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "No internet connection",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Please check your connection and try again",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(10.dp))

        }
    }
}
