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
import com.example.maisonflowers.R
import com.example.maisonflowers.ui.theme.MaisonFlowersTheme

// Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

@Composable
fun LoginScreen(navController: NavController) {
    // Estados para los campos de entrada
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Estado para la visibilidad de la contraseña
    var passwordVisible by remember { mutableStateOf(false) }

    // Estado para mensajes de error
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Obtener la instancia de Firebase Auth
    val auth = FirebaseAuth.getInstance()

    // Acciones para pasar al composable de contenido
    val onEmailChange: (String) -> Unit = { email = it }
    val onPasswordChange: (String) -> Unit = { password = it }
    val onTogglePasswordVisibility: () -> Unit = { passwordVisible = !passwordVisible }
    val onLoginClick: () -> Unit = {
        errorMessage = null // Limpiar errores anteriores

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso, navega a la pantalla principal (home)
                    navController.navigate("home_screen") {
                        // Limpia el back stack para que el usuario no pueda volver a las pantallas de autenticación
                        popUpTo(navController.graph.id) {
                            inclusive = true
                        }
                    }
                } else {
                    // Si el inicio de sesión falla, muestra un mensaje al usuario.
                    errorMessage = when ((task.exception as? FirebaseAuthException)?.errorCode) {
                        "ERROR_INVALID_EMAIL" -> "Formato de correo electrónico inválido."
                        "ERROR_WRONG_PASSWORD" -> "Contraseña incorrecta."
                        "ERROR_USER_NOT_FOUND" -> "No hay usuario registrado con este correo electrónico."
                        "ERROR_USER_DISABLED" -> "Esta cuenta de usuario ha sido deshabilitada."
                        else -> "Error de inicio de sesión: ${task.exception?.message}"
                    }
                    // Puedes añadir Log.e("LoginScreen", "Login failed", task.exception) para depuración
                }
            }
    }
    val onRegisterClick: () -> Unit = {
        navController.navigate("register_screen") // Navega a la pantalla de registro
    }

    // Llama al Composable de contenido puro de UI
    LoginScreenContent(
        email = email,
        onEmailChange = onEmailChange,
        password = password,
        onPasswordChange = onPasswordChange,
        passwordVisible = passwordVisible,
        onTogglePasswordVisibility = onTogglePasswordVisibility,
        errorMessage = errorMessage,
        onLoginClick = onLoginClick,
        onRegisterClick = onRegisterClick
    )
}

@Composable
fun LoginScreenContent(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    errorMessage: String?,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Background color from theme
            .padding(24.dp) // General padding
    ) {
        // "MAISON FLOWERS" logo at the top
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 80.dp), // Adjust padding to position logo
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logomaison), // Your logo
                contentDescription = "Maison Flowers Logo",
                modifier = Modifier
                    .size(120.dp) // Size for logo
                    .padding(bottom = 16.dp)
            )

        }

        // Main content column centered vertically
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 220.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "¡Inicia Sesión!",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "Bienvenido de nuevo a nuestra app, inicia\nsesión para continuar.",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(0.9f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Email Input Field
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

            // Password Input Field
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

            Text(
                text = "Olvidé mi contraseña",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 4.dp, bottom = 16.dp)
                    .clickable { /* TODO: Navegar a pantalla de recuperación de contraseña */ }
            )

            // mensaje de error
            errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // "Iniciar Sesión"
            Button(
                onClick = onLoginClick,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(56.dp)
            ) {
                Text(
                    text = "Iniciar Sesión",
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
                    text = " o iniciar sesión con ",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Spacer(modifier = Modifier.weight(1f).height(1.dp).background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Social Login Buttons (Ignored as per request, but keeping structure for future)
            Row(
                modifier = Modifier.fillMaxWidth(0.8f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // You can add Facebook and Google buttons here if needed later
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(0.8f),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "¿No tienes una cuenta? ",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp
                )
                Text(
                    text = "Regístrate",
                    color = MaterialTheme.colorScheme.primary, // Accent color for the link
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable(onClick = onRegisterClick) // Usa la acción pasada como parámetro
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    MaisonFlowersTheme {
        LoginScreenContent(
            email = "usuario@gmail.com",
            onEmailChange = {},
            password = "123456789",
            onPasswordChange = {},
            passwordVisible = false,
            onTogglePasswordVisibility = {},
            errorMessage = null,
            onLoginClick = {},
            onRegisterClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreenWithError() {
    MaisonFlowersTheme {
        LoginScreenContent(
            email = "usuario@gmail.com",
            onEmailChange = {},
            password = "482t8432",
            onPasswordChange = {},
            passwordVisible = false,
            onTogglePasswordVisibility = {},
            errorMessage = "Correo o contraseña incorrectos.",
            onLoginClick = {},
            onRegisterClick = {}
        )
    }
}
