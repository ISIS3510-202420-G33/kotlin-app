package com.artlens.view.composables

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import com.artlens.R
import com.artlens.data.models.CreateUserResponse

@Composable
fun CreateAccountScreen(
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onRecommendationClick: () -> Unit,
    onCreateAccount: (String, String, String, String) -> Unit,
    userResponse: LiveData<CreateUserResponse>
) {

    var userName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConf by remember { mutableStateOf("") }
    var feedbackMessage by remember { mutableStateOf("") }
    var isFeedbackVisible by remember { mutableStateOf(false) }

    val response by userResponse.observeAsState()

    // Check the response and show feedback
    response?.let {
        when (it) {
            is CreateUserResponse.Success -> {
                feedbackMessage = "Account created successfully!"
                isFeedbackVisible = true
            }
            is CreateUserResponse.Failure -> {
                feedbackMessage = it.error
                isFeedbackVisible = true
            }
        }
    }

    // Feedback Dialog
    if (isFeedbackVisible) {
        FeedbackDialog(
            message = feedbackMessage,
            onDismiss = onBackClick  // Close the dialog
        )
    }


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
                IconButton(onClick = { /* Acción de perfil */ }) {
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
            verticalArrangement = Arrangement.Center // This centers the column vertically
        ) {

            Text(
                text = "Email address",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
            TextField(
                value = email,
                onValueChange = { newText -> email = newText },
                placeholder = { Text("pepito.email@gmail.com", color = Color.Gray) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White, // White background
                    textColor = Color.Black, // Grey text
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(18.dp)
                    )
                    .clip(RoundedCornerShape(18.dp))
            )

            Text(
                text = "Username",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
            TextField(
                value = userName,
                onValueChange = { newText -> userName = newText },
                placeholder = { Text("pepitoArt", color = Color.Gray) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White, // White background
                    textColor = Color.Black, // Grey text
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(18.dp)
                    )
                    .clip(RoundedCornerShape(18.dp))
            )

            Text(
                text = "Name",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
            TextField(
                value = name,
                onValueChange = { newText -> name = newText },
                placeholder = { Text("Pepito Perez", color = Color.Gray) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White, // White background
                    textColor = Color.Black, // Grey text
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(18.dp)
                    )
                    .clip(RoundedCornerShape(18.dp))
            )
            Text(
                text = "Password",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
            TextField(
                value = password,
                onValueChange = { newText -> password = newText },
                placeholder = { Text("***********", color = Color.Gray) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White, // White background
                    textColor = Color.Black, // Grey text
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(18.dp)
                    )
                    .clip(RoundedCornerShape(18.dp))
            )
            Text(
                text = "Confirm password",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
            TextField(
                value = passwordConf,
                onValueChange = { newText -> passwordConf = newText },
                placeholder = { Text("***********", color = Color.Gray) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White, // White background
                    textColor = Color.Black, // Grey text
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                ),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(18.dp)
                    )
                    .clip(RoundedCornerShape(18.dp))
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { onCreateAccount(email, userName, name, password) },
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Black,   // Button background color
                    contentColor = Color.White       // Text color inside the button
                ),
                modifier = Modifier
                    .width(175.dp)
                    .height(35.dp)
                    .align(Alignment.CenterHorizontally)

            ){
                Text("Sign up")
            }

        }

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

@Composable
fun FeedbackDialog(
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title ={},
        text = { Text(
            text = message,
            color = Color.Black,
            fontSize = 20.sp) },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Black,   // Button background color
                contentColor = Color.White       // Text color inside the button
            ),

            ) {
                Text("OK")

            }

        },
        shape = RoundedCornerShape(8.dp)
    )
}
