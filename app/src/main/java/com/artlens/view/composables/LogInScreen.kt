package com.artlens.view.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import com.artlens.R
import com.artlens.data.models.CreateUserResponse

@Composable
fun LogInScreen(
    onBackClick: () -> Unit,
    onCreateAccount: () -> Unit,
    onLogInClick: (String, String) -> Unit,
    userResponse: LiveData<CreateUserResponse>
) {

    var pass by remember { mutableStateOf("") }
    var user by remember { mutableStateOf("")}
    var feedbackMessage by remember { mutableStateOf("") }
    var isFeedbackVisible by remember { mutableStateOf(false) }

    val response by userResponse.observeAsState()

    // Check the response and show feedback
    response?.let {
        when (it) {
            is CreateUserResponse.Success -> {
                feedbackMessage = "Account authenticated successfully!"
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
                    text = "LOG IN",
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
                painter = painterResource(id = R.drawable.profile), // Replace with your drawable resource
                contentDescription = "Sample Image",
                modifier = Modifier
                    .size(150.dp) // Adjust the size of the image
                    .clip(RoundedCornerShape(12.dp)) // Optional: Rounded corners
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = user,
                onValueChange = { newText -> user = newText },
                label = { Text("Username or Email", color = Color.Gray) },
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

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = pass,
                onValueChange = { newText -> pass = newText },
                label = { Text("Enter Password", color = Color.Gray) },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White, // White background
                    textColor = Color.Black, // Grey text
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                ),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(18.dp)
                    )
                    .clip(RoundedCornerShape(18.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {onLogInClick(user, pass)},
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Black,   // Button background color
                    contentColor = Color.White       // Text color inside the button
                ),
                modifier = Modifier
                    .width(175.dp)
                    .height(35.dp)

            ){
                Text("Log in")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Forgot Password?",
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable(onClick = { })
            )

            Spacer(modifier = Modifier.height(60.dp))

        }




        // Bottom Navigation Bar
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.White)
                .padding(vertical = 8.dp)
                .height(50.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Button(
                onClick = onCreateAccount,
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White,   // Button background color
                    contentColor = Color.Black       // Text color inside the button
                ),
                modifier = Modifier
                    .width(200.dp)
                    .height(35.dp)
                    .border(
                        BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(18.dp)
                    )
                    .clip(RoundedCornerShape(18.dp))

            ){
                Text("Create new account")
            }

            Spacer(modifier = Modifier.height(100.dp))

        }



    }
}


