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
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Payment // Para métodos de pago
import androidx.compose.material.icons.filled.Notifications // Para notificaciones
import androidx.compose.material.icons.filled.History // Para historial de pedidos
import androidx.compose.material.icons.filled.ListAlt // Para pedidos pendientes/activos
import androidx.compose.material.icons.filled.Search
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

// Firebase related imports
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// Data class para almacenar la información del usuario de Firestore
data class UserProfile(
    val email: String = "",
    val username: String = "",
    val phoneNumber: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(navController: NavController) {
    // Estados para la información del perfil del usuario y el estado de carga
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val currentUser = auth.currentUser // Obtener el usuario autenticado actualmente

    // Efecto para cargar la información del usuario de Firestore
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

    // Acción para cerrar sesión
    val onLogoutClick: () -> Unit = {
        auth.signOut() // Cierra la sesión en Firebase Authentication
        navController.navigate("welcome4") { // O "login_screen"
            popUpTo(navController.graph.id) { inclusive = true }
        }
    }

    // Acción para eliminar cuenta (placeholder, debería tener confirmación robusta)
    val onDeleteAccountClick: () -> Unit = {
        // TODO: Implementar lógica para eliminar cuenta
        // eliminación de todos sus datos en Auth y Firestore.
        println("Eliminar cuenta clickeado")
    }

    // Llama al Composable de contenido puro de UI
    AccountScreenContent(
        navController = navController,
        userProfile = userProfile,
        isLoading = isLoading,
        errorMessage = errorMessage,
        onLogoutClick = onLogoutClick,
        onDeleteAccountClick = onDeleteAccountClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreenContent(
    navController: NavController,
    userProfile: UserProfile?,
    isLoading: Boolean,
    errorMessage: String?,
    onLogoutClick: () -> Unit,
    onDeleteAccountClick: () -> Unit
) {
    // Estado para el ítem seleccionado en la barra de navegación inferior
    var selectedItem by remember { mutableIntStateOf(4) }

    Scaffold(
        topBar = {
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
                    IconButton(onClick = { /* TODO: Navegar a ajustes de la app */ }) {
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
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground
            ) {
                NavigationBarItem(
                    selected = selectedItem == 0,
                    onClick = {
                        selectedItem = 0
                        navController.navigate("home_screen") {
                            popUpTo("home_screen") { inclusive = true }
                        }
                    },
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                        unselectedTextColor = MaterialTheme.colorScheme.onBackground,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    )
                )
                NavigationBarItem(
                    selected = selectedItem == 1,
                    onClick = {
                        selectedItem = 1
                        navController.navigate("category_screen") {
                            popUpTo("category_screen") { inclusive = true }
                        }
                    },
                    icon = { Icon(Icons.Filled.Apps, contentDescription = "Categorías") },
                    label = { Text("Categorias") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                        unselectedTextColor = MaterialTheme.colorScheme.onBackground,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    )
                )
                NavigationBarItem(
                    selected = selectedItem == 2,
                    onClick = {
                        selectedItem = 2
                        navController.navigate("search_screen") {
                        }
                    },
                    icon = { Icon(Icons.Filled.Search, contentDescription = "Buscar") },
                    label = { Text("Buscar") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                        unselectedTextColor = MaterialTheme.colorScheme.onBackground,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    )
                )
                NavigationBarItem(
                    selected = selectedItem == 3,
                    onClick = {
                        selectedItem = 3
                        navController.navigate("cart_screen") {
                        }
                    },
                    icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrito") },
                    label = { Text("Carrito") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                        unselectedTextColor = MaterialTheme.colorScheme.onBackground,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    )
                )
                NavigationBarItem(
                    selected = selectedItem == 4,
                    onClick = { selectedItem = 4 /* Ya está en cuenta */ },
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Cuenta") },
                    label = { Text("Cuenta") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = MaterialTheme.colorScheme.onBackground,
                        unselectedTextColor = MaterialTheme.colorScheme.onBackground,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    )
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))

                // Sección 1: Información de Perfil
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
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally // Centrar contenido de la tarjeta
                            ) {
                                // Foto de Perfil (circular y centrada)
                                Image(
                                    painter = painterResource(id = R.drawable.logomaison), // Reemplaza con tu imagen de perfil
                                    contentDescription = "Foto de Perfil",
                                    modifier = Modifier
                                        .size(96.dp) // Tamaño de la imagen
                                        .clip(CircleShape) // Forma circular
                                        .background(Color.LightGray) // Fondo para el círculo si la imagen es transparente
                                        .padding(bottom = 16.dp), // Espacio debajo de la foto
                                    contentScale = ContentScale.Crop
                                )

                                ProfileDetailRow(label = "Nombre:", value = profile.username)
                                ProfileDetailRow(label = "Email:", value = profile.email)
                                ProfileDetailRow(label = "Teléfono:", value = profile.phoneNumber)

                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { /* TODO: Navegar a pantalla de edición de perfil */ },
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

                Spacer(modifier = Modifier.height(32.dp))

                // Sección 2: Mis Pedidos
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

                Spacer(modifier = Modifier.height(32.dp))

                // Sección 3: Configuraciones y Ajustes
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

                Spacer(modifier = Modifier.height(32.dp))

                // Sección 4: Soporte y Legal
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

                Spacer(modifier = Modifier.height(32.dp))

                // Sección 5: Acciones Clave (Cerrar Sesión y Eliminar Cuenta)
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
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE57373)), // tono de rojo diferente para eliminar
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(56.dp)
                ) {
                    Icon(Icons.Default.DeleteForever, contentDescription = "Eliminar Cuenta")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Eliminar Cuenta", color = Color.White)
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

// Composable para una fila de detalle de perfil (sin cambios)
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
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Text(
            text = value,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 16.sp,
            textAlign = TextAlign.End
        )
    }
}

// Composable para un ítem de sección de cuenta (sin cambios importantes, solo iconos)
@Composable
fun AccountSectionItem(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
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
                color = MaterialTheme.colorScheme.onSurface,
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
        AccountScreenContent(
            navController = rememberNavController(),
            userProfile = UserProfile(
                email = "usuario@gmail.com",
                username = "Usuario",
                phoneNumber = "987654321"
            ),
            isLoading = false,
            errorMessage = null,
            onLogoutClick = {},
            onDeleteAccountClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAccountScreenLoading() {
    MaisonFlowersTheme {
        AccountScreenContent(
            navController = rememberNavController(),
            userProfile = null,
            isLoading = true,
            errorMessage = null,
            onLogoutClick = {},
            onDeleteAccountClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAccountScreenError() {
    MaisonFlowersTheme {
        AccountScreenContent(
            navController = rememberNavController(),
            userProfile = null,
            isLoading = false,
            errorMessage = "No se pudo cargar el perfil.",
            onLogoutClick = {},
            onDeleteAccountClick = {}
        )
    }
}
