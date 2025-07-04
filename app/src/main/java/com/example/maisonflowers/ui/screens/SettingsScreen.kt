package com.example.maisonflowers.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.maisonflowers.ui.theme.MaisonFlowersTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    paddingValues: PaddingValues // Recibe los paddingValues del Scaffold externo
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
        // No aplicar paddingValues aquí, la TopAppBar debe ir arriba
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Ajustes",
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
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.onBackground,
                actionIconContentColor = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Contenido de la pantalla de ajustes
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding()) // Solo padding inferior
                .padding(horizontal = 16.dp, vertical = 8.dp), // Padding interno
            verticalArrangement = Arrangement.spacedBy(8.dp) // Espacio entre ítems
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) } // Espacio inicial

            item {
                SettingsSectionTitle(title = "Apariencia")
                SettingsItem(
                    text = "Tema de la aplicación",
                    icon = Icons.Default.Palette,
                    onClick = { /* TODO: Implementar cambio de tema (modo oscuro/claro) */ }
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                SettingsSectionTitle(title = "Preferencias")
                SettingsItem(
                    text = "Notificaciones",
                    icon = Icons.Default.Notifications,
                    onClick = { /* TODO: Navegar a pantalla de configuración de notificaciones */ }
                )
                SettingsItem(
                    text = "Idioma",
                    icon = Icons.Default.Language,
                    onClick = { /* TODO: Abrir selector de idioma */ }
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                SettingsSectionTitle(title = "Almacenamiento")
                SettingsItem(
                    text = "Borrar caché",
                    icon = Icons.Default.Storage,
                    onClick = { /* TODO: Implementar lógica para borrar caché */ }
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                SettingsSectionTitle(title = "Información")
                SettingsItem(
                    text = "Acerca de la App",
                    icon = Icons.Default.Info,
                    onClick = { /* TODO: Mostrar información de la app (versión, créditos) */ }
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) } // Espacio final
        }
    }
}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun SettingsItem(text: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
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
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }
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
fun PreviewSettingsScreen() {
    MaisonFlowersTheme {
        SettingsScreen(navController = rememberNavController(), paddingValues = PaddingValues(0.dp))
    }
}
