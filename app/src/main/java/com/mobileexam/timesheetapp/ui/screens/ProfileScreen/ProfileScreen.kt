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

@Composable
fun ProfileScreen(navController: NavController) {
    var isEditing by remember { mutableStateOf(false) }
    var isChangingPassword by remember { mutableStateOf(false) }

    var fullName by remember { mutableStateOf("John Doe") }
    var username by remember { mutableStateOf("johndoe") }
    var email by remember { mutableStateOf("johndoe@example.com") }
    var phoneNumber by remember { mutableStateOf("+63 912 345 6789") }

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var profileImageUri by remember { mutableStateOf<Uri?>(null) }

    val isDarkTheme = isSystemInDarkTheme()
    val backgroundColor = if (isDarkTheme) DarkBackground else Color(0xFFE0E0E0)
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val buttonColor = Color(0xFF3478F6)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        ProfileTopBar(navController, textColor, backgroundColor)
        Spacer(modifier = Modifier.height(20.dp))
        ProfilePicture(profileImageUri) { newUri -> profileImageUri = newUri }
        Spacer(modifier = Modifier.height(10.dp))

        if (isChangingPassword) {
            ChangePasswordSection(
                currentPassword, newPassword, confirmPassword,
                { currentPassword = it }, { newPassword = it }, { confirmPassword = it },
                onSavePassword = { isChangingPassword = false },
                textColor = textColor, // ✅ Dynamic text color
                isDarkTheme = isDarkTheme // ✅ Pass dark mode state
            )
        }
        else {
            ProfileDetailsSection(
                isEditing, fullName, username, email, phoneNumber,
                { fullName = it }, { username = it }, { email = it }, { phoneNumber = it },
                { isEditing = !isEditing }, { isChangingPassword = true },
                buttonColor = buttonColor,
                textColor = textColor // ✅ Pass textColor
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
        IconButton(onClick = { navController.navigateUp() }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = textColor)
        }
        Text("Profile", fontSize = 18.sp, color = textColor, modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun ProfilePicture(profileImageUri: Uri?, onImageSelected: (Uri) -> Unit) {
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onImageSelected(it) }
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = profileImageUri ?: R.drawable.ic_profile_placeholder
            ),
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .clickable { imagePickerLauncher.launch("image/*") }
        )
    }
}

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
    textColor: Color // ✅ New parameter
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
        if (!isEditing) { // ✅ Only show when NOT editing the profile
            Button(
                onClick = onChangePassword,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Change Password", fontSize = 16.sp, color = Color.White)
            }
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
    textColor: Color, // ✅ Dynamic text color
    isDarkTheme: Boolean // ✅ Detect dark mode
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Change Password",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = textColor // ✅ Adjusted for dark mode
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