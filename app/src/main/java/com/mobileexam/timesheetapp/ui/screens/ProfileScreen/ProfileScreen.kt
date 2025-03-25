 package com.mobileexam.timesheetapp.ui.screens.ProfileScreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.mobileexam.timesheetapp.R
import com.mobileexam.timesheetapp.ui.theme.DarkBackground
import android.content.Context
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.Navigation
import com.mobileexam.timesheetapp.ui.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch

 @Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel) {

    val fullName by viewModel.fullName.collectAsState()
    val username by viewModel.username.collectAsState()
    val email by viewModel.email.collectAsState()
    val phoneNumber by viewModel.phoneNumber.collectAsState()
    val profileImageUri by viewModel.profileImageUri.collectAsState()

    var isEditing by remember { mutableStateOf(false) }
    var isChangingPassword by remember { mutableStateOf(false) }

    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) DarkBackground else Color(0xFFE0E0E0)
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val buttonColor = Color(0xFF3478F6)
    val context = LocalContext.current

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Logout function
    fun logout() {
        val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().remove("auth_token").apply()
        Log.d("Logout", "User has successfully logged out. Token removed.")
        navController.navigate("login") {
            popUpTo("profile") { inclusive = true }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        ProfileTopBar(navController, textColor, backgroundColor)
        Spacer(modifier = Modifier.height(10.dp))

        if (isChangingPassword) {
            ChangePasswordSection(
                currentPassword = currentPassword,
                newPassword = newPassword,
                confirmPassword = confirmPassword,
                onCurrentPasswordChange = { currentPassword = it },
                onNewPasswordChange = { newPassword = it },
                onConfirmPasswordChange = { confirmPassword = it },
                onSavePassword = { isChangingPassword = false },
                textColor = textColor,
                isDarkTheme = isDarkTheme
            )
        }

        else {
            ProfileDetailsSection(
                isEditing = isEditing,
                fullName = fullName,
                username = username,
                email = email,
                phoneNumber = phoneNumber,
                onFullNameChange = viewModel::updateFullName,
                onUsernameChange = viewModel::updateUsername,
                onEmailChange = viewModel::updateEmail,
                onPhoneNumberChange = viewModel::updatePhoneNumber,
                onEditToggle = { isEditing = !isEditing },
                onChangePassword = { isChangingPassword = true },
                buttonColor = buttonColor,
                textColor = textColor,
                onLogout = {
                    logout()
                }
            )
        }
    }
}

@Composable
fun ProfileTopBar(navController: NavController, textColor: Color, topBarColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(topBarColor)
            .padding(16.dp)
    ) {
//        IconButton(onClick = { navController.navigateUp() }) {
//            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = textColor)
//        }
        Text("Profile", fontSize = 18.sp, color = textColor, modifier = Modifier.align(Alignment.Center))
    }
}

//@Composable
//fun ProfilePicture(profileImageUri: Uri?, onImageSelected: (Uri) -> Unit) {
//    val imagePickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        uri?.let { onImageSelected(it) }
//    }
//
//    Box(
//        modifier = Modifier.fillMaxWidth(),
//        contentAlignment = Alignment.Center
//    ) {
//        Image(
//            painter = rememberAsyncImagePainter(
//                model = profileImageUri ?: R.drawable.ic_profile_placeholder
//            ),
//            contentDescription = "Profile Picture",
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                .size(100.dp)
//                .clip(CircleShape)
//                .background(Color.Gray)
//                .clickable { imagePickerLauncher.launch("image/*") }
//        )
//    }
//}


@Composable
fun ProfileDetailsSection(
    isEditing: Boolean,
    fullName: String,
    username: String,
    email: String,
    phoneNumber: String,
    onFullNameChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onEditToggle: () -> Unit,
    onChangePassword: () -> Unit,
    buttonColor: Color,
    textColor: Color,
    onLogout: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        if (isEditing) {
            ProfileInputField("Full Name", fullName, onFullNameChange, textColor)
            ProfileInputField("Username", username, onUsernameChange, textColor)
            ProfileInputField("Email", email, onEmailChange, textColor)
            ProfileInputField("Phone Number", phoneNumber, onPhoneNumberChange, textColor)
        } else {
            ProfileInfoField("Full Name", fullName, textColor)
            ProfileInfoField("Username", username, textColor)
            ProfileInfoField("Email", email, textColor)
            ProfileInfoField("Phone Number", phoneNumber, textColor)
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = onEditToggle,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = if (isEditing) Color.Green else buttonColor),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text(if (isEditing) "Save Profile" else "Edit Profile", fontSize = 16.sp, color = Color.White)
        }
        Spacer(modifier = Modifier.height(10.dp))
        if (!isEditing) {
            Button(
                onClick = onChangePassword,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Change Password", fontSize = 16.sp, color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { onLogout() },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text("Logout", fontSize = 16.sp, color = Color.White)
        }

    }
}

@Composable
fun ProfileInputField(label: String, value: String, onValueChange: (String) -> Unit, textColor: Color) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textColor)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            textStyle = androidx.compose.ui.text.TextStyle(color = textColor),
            colors = TextFieldDefaults.colors(
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
fun ProfileInfoField(label: String, value: String, textColor: Color) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textColor)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Normal, color = textColor)
        Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))
    }
}

@Composable
fun ChangePasswordSection(
    currentPassword: String, newPassword: String, confirmPassword: String,
    onCurrentPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSavePassword: () -> Unit,
    textColor: Color,
    isDarkTheme: Boolean
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Change Password",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
        Spacer(modifier = Modifier.height(10.dp))

        PasswordInputField("Current Password", currentPassword, onCurrentPasswordChange, textColor, isDarkTheme)
        PasswordInputField("New Password", newPassword, onNewPasswordChange, textColor, isDarkTheme)
        PasswordInputField("Confirm New Password", confirmPassword, onConfirmPasswordChange, textColor, isDarkTheme)

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onSavePassword,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text("Save Password", fontSize = 16.sp, color = Color.White)
        }
    }
}

@Composable
fun PasswordInputField(label: String, value: String, onValueChange: (String) -> Unit, textColor: Color, isDarkTheme: Boolean) {
    val backgroundColor = if (isDarkTheme) Color(0xFF3D3B40) else Color.White // Match the input field color

    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textColor)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            textStyle = androidx.compose.ui.text.TextStyle(color = textColor),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = backgroundColor,
                unfocusedContainerColor = backgroundColor,
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}