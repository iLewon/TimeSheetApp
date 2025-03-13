package com.mobileexam.timesheetapp.ui.screens.ForgotPassword

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mobileexam.timesheetapp.R
import com.mobileexam.timesheetapp.ui.theme.*

@Composable
fun ForgotPasswordScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    val isDarkTheme = isSystemInDarkTheme()

    // Background & Input Colors
    val backgroundColor = if (isDarkTheme) DarkBackground else Color(0xFFE0E0E0) // Light Grey in Light Mode
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val buttonColor = Color(0xFF3478F6)
    val topBarColor = if (isDarkTheme) DarkBackground else backgroundColor
    val inputFieldColor = if (isDarkTheme) Color.White else Color(0xFFF0F0F0) // White in Dark Mode, Grey in Light Mode

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor) // Light grey for contrast
    ) {
        // Top Navigation Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(topBarColor)
                .padding(16.dp)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = if (isDarkTheme) Color.White else Color.Black
                )
            }

            Row(
                modifier = Modifier.align(Alignment.Center),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.jairo_logo),
                    contentDescription = "Jairosoft Logo",
                    modifier = Modifier.size(36.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "Jairosoft",
                    fontSize = 18.sp,
                    color = if (isDarkTheme) Color.White else Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Title
        Text(
            text = "Forgot password?",
            fontSize = 24.sp,
            color = textColor,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        // Subtitle
        Text(
            text = "Enter your email to receive a confirmation email",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Email Input Field (White in Dark Mode, Light Grey in Light Mode)
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Enter email address", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .background(inputFieldColor, shape = RoundedCornerShape(10.dp)),
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = inputFieldColor,
                unfocusedContainerColor = inputFieldColor,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Send Button
        Button(
            onClick = { /* Handle forgot password */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text("Send", fontSize = 16.sp, color = Color.White)
        }
    }
}