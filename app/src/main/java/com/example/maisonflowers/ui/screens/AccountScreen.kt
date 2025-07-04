package com.example.maisonflowers.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.maisonflowers.R
import com.example.maisonflowers.ui.theme.MaisonFlowersTheme

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class UserProfile(
    val email: String = "",
    val username: String = "",
    val phoneNumber: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    navController: NavController,
    paddingValues: PaddingValues
) {
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val currentUser = auth.currentUser

    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            val userId = currentUser.uid
            db.collection("users").document(userId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        userProfile = documentSnapshot.toObject(UserProfile::class.java)
                        isLoading = false
                    } else {
                        errorMessage = "Datos de perfil no encontrados."
                        isLoading = false
                    }
                }
                .addOnFailureListener { e ->
                    errorMessage = "Error al cargar el perfil: ${e.message}"
                    isLoading = false
                }
        } else {
            errorMessage = "No hay usuario autenticado."
            isLoading = false
        }
    }

    val onLogoutClick: () -> Unit = {
        auth.signOut()
        navController.navigate("welcome4") {
            popUpTo(navController.graph.id) { inclusive = true }
        }
    }

    val onDeleteAccountClick: () -> Unit = {
        println("Eliminar cuenta clickeado")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Mi Cuenta",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            actions = {
                IconButton(onClick = { navController.navigate("settings_screen") }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Ajustes de la App",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.onBackground,
                actionIconContentColor = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.fillMaxWidth()
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                Text(
                    text = "Mi Perfil",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    textAlign = TextAlign.Start
                )
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                } else if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "Error desconocido",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    userProfile?.let { profile ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), // Usar color de superficie del tema
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.logomaison),
                                    contentDescription = "Foto de Perfil",
                                    modifier = Modifier
                                        .size(96.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surfaceVariant) // Usar un color de superficie variante del tema
                                        .padding(bottom = 16.dp),
                                    contentScale = ContentScale.Crop
                                )

                                ProfileDetailRow(label = "Nombre:", value = profile.username)
                                ProfileDetailRow(label = "Email:", value = profile.email)
                                ProfileDetailRow(label = "Teléfono:", value = profile.phoneNumber)

                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { navController.navigate("edit_profile_screen") },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar Perfil")
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Editar Perfil", color = MaterialTheme.colorScheme.onPrimary)
                                }
                            }
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            item {
                Text(
                    text = "Mis Pedidos",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    textAlign = TextAlign.Start
                )
                AccountSectionItem(
                    text = "Ver Historial de Pedidos",
                    icon = Icons.Default.History
                ) { /* TODO: Navegar a historial de pedidos */ }
                AccountSectionItem(
                    text = "Pedidos Pendientes/Activos",
                    icon = Icons.Default.ListAlt
                ) { /* TODO: Navegar a pedidos pendientes/activos */ }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            item {
                Text(
                    text = "Configuración y Ajustes",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    textAlign = TextAlign.Start
                )
                AccountSectionItem(
                    text = "Mis Direcciones",
                    icon = Icons.Default.LocationOn
                ) { /* TODO: Navegar a gestión de direcciones */ }
                AccountSectionItem(
                    text = "Métodos de Pago",
                    icon = Icons.Default.Payment
                ) { /* TODO: Navegar a métodos de pago */ }
                AccountSectionItem(
                    text = "Notificaciones",
                    icon = Icons.Default.Notifications
                ) { /* TODO: Navegar a configuraciones de notificaciones */ }
                AccountSectionItem(
                    text = "Cambiar Contraseña",
                    icon = Icons.Default.Lock
                ) { /* TODO: Navegar a pantalla de cambio de contraseña */ }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            item {
                Text(
                    text = "Soporte y Legal",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    textAlign = TextAlign.Start
                )
                AccountSectionItem(
                    text = "Ayuda y Soporte",
                    icon = Icons.Default.Help
                ) { /* TODO: Navegar a sección de ayuda */ }
                AccountSectionItem(
                    text = "Acerca de la App",
                    icon = Icons.Default.Info
                ) { /* TODO: Navegar a sección 'Acerca de' */ }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            item {
                Text(
                    text = "Acciones Clave",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    textAlign = TextAlign.Start
                )
                Button(
                    onClick = onLogoutClick,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(56.dp)
                ) {
                    Icon(Icons.Default.Logout, contentDescription = "Cerrar Sesión")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Cerrar Sesión", color = MaterialTheme.colorScheme.onError)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDeleteAccountClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373)), // Mantener este color si es específico para eliminar
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(56.dp)
                ) {
                    Icon(Icons.Default.DeleteForever, contentDescription = "Eliminar Cuenta")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Eliminar Cuenta", color = Color.White)
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun ProfileDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurface, // Usar onSurface para el texto
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onSurfaceVariant, // Usar onSurfaceVariant para el valor
            fontSize = 16.sp,
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun AccountSectionItem(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), // Usar color de superficie del tema
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurface, // Usar onSurface para el texto
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = "Ir",
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAccountScreen() {
    MaisonFlowersTheme {
        AccountScreen(
            navController = rememberNavController(),
            paddingValues = PaddingValues(0.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAccountScreenLoading() {
    MaisonFlowersTheme {
        AccountScreen(
            navController = rememberNavController(),
            paddingValues = PaddingValues(0.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAccountScreenError() {
    MaisonFlowersTheme {
        AccountScreen(
            navController = rememberNavController(),
            paddingValues = PaddingValues(0.dp)
        )
    }
}
