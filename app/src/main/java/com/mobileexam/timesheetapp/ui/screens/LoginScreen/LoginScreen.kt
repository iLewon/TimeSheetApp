package com.mobileexam.timesheetapp.ui.screens.LoginScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mobileexam.timesheetapp.R
import com.mobileexam.timesheetapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isDarkTheme = isSystemInDarkTheme()

    // Updated Background Color for Light Mode
    val backgroundColor = if (isDarkTheme) DarkBackground else Color(0xFFE0E0E0) // Light gray in light mode
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val buttonColor = Color(0xFF3478F6)
    val topBarColor = if (isDarkTheme) DarkBackground else Color.White
    val inputFieldColor = if (isDarkTheme) Color.White else Color(0xFFF0F0F0) // White in dark mode, Grey in light mode

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor) // Light gray in light mode for better contrast
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        Image(
            painter = painterResource(id = R.drawable.jairo_logo),
            contentDescription = "Logo",
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Jairosoft",
            color = textColor,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Let’s bring your digital dreams to life",
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Username Input Field (White in Dark Mode, Grey in Light Mode)
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Username", color = Color.Gray) },
            modifier = Modifier
                .fillMaxWidth()
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

        Spacer(modifier = Modifier.height(10.dp))

        // Password Input Field (White in Dark Mode, Grey in Light Mode)
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Password", color = Color.Gray) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
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

        Spacer(modifier = Modifier.height(5.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = { navController.navigate("forgot_password") }) {
                Text("Forgot your password?", color = buttonColor, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { navController.navigate("home") { popUpTo("login") { inclusive = true } } },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text("Login", fontSize = 16.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Don't have an account?", color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)

        TextButton(onClick = { /*ONCLICK FUNC*/ }) {
            Text("Sign up here.", color = buttonColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}
