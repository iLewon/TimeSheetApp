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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mobileexam.timesheetapp.R
import com.mobileexam.timesheetapp.data.api.ApiService
import com.mobileexam.timesheetapp.models.LoginResponse
import com.mobileexam.timesheetapp.ui.theme.*
import com.mobileexam.timesheetapp.viewmodel.LoginViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Context


fun loginUser(email: String, password: String, context: Context, navController: NavController) {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://timesheet-63231.bubbleapps.io/api/1.1/wf/") // Ensure it ends with "/"
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService = retrofit.create(ApiService::class.java)
    val call = apiService.login(email, password)

    call.enqueue(object : Callback<LoginResponse> {
        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
            if (response.isSuccessful) {
                val loginResponse = response.body()
                if (loginResponse?.status == "success") {
                    val token = loginResponse.response.token
                    println("Login Successful! Token: $token")

                    // ✅ Save token in SharedPreferences
                    val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                    sharedPreferences.edit().putString("auth_token", token).apply()

                    // Navigate to Home screen
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                } else {
                    println("Login Failed: ${response.message()}")
                }
            } else {
                println("API Error: ${response.errorBody()?.string()}")
            }
        }

        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
            println("API Request Failed: ${t.message}")
        }
    })
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val isDarkTheme = isSystemInDarkTheme()

    // Updated Background Color for Light Mode
    val backgroundColor = if (isDarkTheme) DarkBackground else Color(0xFFE0E0E0) // Light gray in light mode
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val buttonColor = Color(0xFF3478F6)
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
            onClick = {
                loginUser(email, password, context, navController)
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text("Login", fontSize = 16.sp, color = Color.White)
        }


        if (loginError) {
            Spacer(modifier = Modifier.height(10.dp))
            Text("Invalid username or password", color = Color.Red, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text("Don't have an account?", color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)

        TextButton(onClick = { /*ONCLICK FUNC*/ }) {
            Text("Sign up here.", color = buttonColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}
