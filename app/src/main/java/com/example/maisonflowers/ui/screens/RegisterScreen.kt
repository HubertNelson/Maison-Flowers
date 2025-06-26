package com.example.maisonflowers.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.maisonflowers.R
import com.example.maisonflowers.ui.theme.MaisonFlowersTheme

// Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun RegisterScreen(navController: NavController) {
    // Estados para los campos de entrada
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Estado para la visibilidad de la contraseña
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Estado para mensajes de error
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Obtener las instancias de Firebase
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    // Acciones para pasar al composable de contenido
    val onEmailChange: (String) -> Unit = { email = it }
    val onUsernameChange: (String) -> Unit = { username = it }
    val onPhoneNumberChange: (String) -> Unit = { phoneNumber = it }
    val onPasswordChange: (String) -> Unit = { password = it }
    val onConfirmPasswordChange: (String) -> Unit = { confirmPassword = it }
    val onTogglePasswordVisibility: () -> Unit = { passwordVisible = !passwordVisible }
    val onToggleConfirmPasswordVisibility: () -> Unit = { confirmPasswordVisible = !confirmPasswordVisible }
    val onRegisterClick: () -> Unit = {
        errorMessage = null
        if (password != confirmPassword) {
            errorMessage = "Las contraseñas no coinciden."
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        user?.let { firebaseUser ->
                            val userData = hashMapOf(
                                "email" to firebaseUser.email,
                                "username" to username,
                                "phoneNumber" to phoneNumber,
                                "registrationDate" to System.currentTimeMillis()
                            )

                            db.collection("users").document(firebaseUser.uid)
                                .set(userData)
                                .addOnSuccessListener {
                                    navController.navigate("home_screen") {
                                        popUpTo(navController.graph.id) { inclusive = true }
                                    }
                                }
                                .addOnFailureListener { e ->
                                    errorMessage = "Error al guardar datos de usuario: ${e.message}"
                                }
                        }
                    } else {
                        errorMessage = when ((task.exception as? FirebaseAuthException)?.errorCode) {
                            "ERROR_WEAK_PASSWORD" -> "La contraseña es demasiado débil. Debe tener al menos 6 caracteres."
                            "ERROR_INVALID_EMAIL" -> "Formato de correo electrónico inválido."
                            "ERROR_EMAIL_ALREADY_IN_USE" -> "Este correo electrónico ya está registrado."
                            else -> "Error de registro: ${task.exception?.message}"
                        }
                    }
                }
        }
    }
    val onLoginLinkClick: () -> Unit = {
        navController.navigate("login_screen")
    }

    // Llama al Composable de contenido puro de UI
    RegisterScreenContent(
        email = email,
        onEmailChange = onEmailChange,
        username = username,
        onUsernameChange = onUsernameChange,
        phoneNumber = phoneNumber,
        onPhoneNumberChange = onPhoneNumberChange,
        password = password,
        onPasswordChange = onPasswordChange,
        confirmPassword = confirmPassword,
        onConfirmPasswordChange = onConfirmPasswordChange,
        passwordVisible = passwordVisible,
        onTogglePasswordVisibility = onTogglePasswordVisibility,
        confirmPasswordVisible = confirmPasswordVisible,
        onToggleConfirmPasswordVisibility = onToggleConfirmPasswordVisibility,
        errorMessage = errorMessage,
        onRegisterClick = onRegisterClick,
        onLoginLinkClick = onLoginLinkClick
    )
}

@Composable
fun RegisterScreenContent(
    email: String,
    onEmailChange: (String) -> Unit,
    username: String,
    onUsernameChange: (String) -> Unit,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    confirmPasswordVisible: Boolean,
    onToggleConfirmPasswordVisibility: () -> Unit,
    errorMessage: String?,
    onRegisterClick: () -> Unit,
    onLoginLinkClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logomaison),
                contentDescription = "Maison Flowers Sketch Logo",
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 8.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 160.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "¡Regístrate aquí!",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Introduce tus datos para abrir una nueva cuenta y\nempieza a comprar encantadoras flores.",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(0.9f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = { Text("Gmail") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )

            OutlinedTextField(
                value = username,
                onValueChange = onUsernameChange,
                label = { Text("Nombre de Usuario") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = onPhoneNumberChange,
                label = { Text("Número de Teléfono") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )

            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text("Contraseña") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

                    IconButton(onClick = onTogglePasswordVisibility) {
                        Icon(imageVector = image, contentDescription = "Toggle password visibility")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = onConfirmPasswordChange,
                label = { Text("Confirmar contraseña") },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

                    IconButton(onClick = onToggleConfirmPasswordVisibility) {
                        Icon(imageVector = image, contentDescription = "Toggle confirm password visibility")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )

            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onRegisterClick,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(56.dp)
            ) {
                Text(
                    text = "Registrarse",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                Spacer(modifier = Modifier.weight(1f).height(1.dp).background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)))
                Text(
                    text = " o registrarte con ",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Spacer(modifier = Modifier.weight(1f).height(1.dp).background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(0.8f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botones de redes sociales
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(0.8f),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "¿Ya tienes una cuenta? ",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp
                )
                Text(
                    text = "Inicia sesión",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable(onClick = onLoginLinkClick)
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    MaisonFlowersTheme {
        // Pasa datos de prueba (mock data) a RegisterScreenContent para la previsualización
        RegisterScreenContent(
            email = "test@example.com",
            onEmailChange = {},
            username = "UsuarioDemo",
            onUsernameChange = {},
            phoneNumber = "123456789",
            onPhoneNumberChange = {},
            password = "password123",
            onPasswordChange = {},
            confirmPassword = "password123",
            onConfirmPasswordChange = {},
            passwordVisible = false,
            onTogglePasswordVisibility = {},
            confirmPasswordVisible = false,
            onToggleConfirmPasswordVisibility = {},
            errorMessage = null,
            onRegisterClick = {},
            onLoginLinkClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreenWithError() {
    MaisonFlowersTheme {
        RegisterScreenContent(
            email = "invalid@example.com",
            onEmailChange = {},
            username = "UsuarioError",
            onUsernameChange = {},
            phoneNumber = "987654321",
            onPhoneNumberChange = {},
            password = "short",
            onPasswordChange = {},
            confirmPassword = "short",
            onConfirmPasswordChange = {},
            passwordVisible = false,
            onTogglePasswordVisibility = {},
            confirmPasswordVisible = false,
            onToggleConfirmPasswordVisibility = {},
            errorMessage = "La contraseña es demasiado débil.",
            onRegisterClick = {},
            onLoginLinkClick = {}
        )
    }
}
