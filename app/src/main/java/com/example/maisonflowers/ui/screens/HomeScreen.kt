package com.example.maisonflowers.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.maisonflowers.ui.components.FlowerCategory
import com.example.maisonflowers.ui.components.FlowerProduct
import com.example.maisonflowers.ui.components.CategoryItem
import com.example.maisonflowers.ui.components.ProductCard
import com.example.maisonflowers.ui.viewmodels.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    paddingValues: PaddingValues // Recibe los paddingValues del Scaffold externo
) {
    var searchQuery by remember { mutableStateOf("") }

    val categories = remember {
        listOf(
            FlowerCategory("ROSAS", R.drawable.logomaison),
            FlowerCategory("GIRASOLES", R.drawable.logomaison),
            FlowerCategory("LIRIOS", R.drawable.logomaison),
            FlowerCategory("BODAS", R.drawable.logomaison),
            FlowerCategory("CUMPLEAÑOS", R.drawable.logomaison),
            FlowerCategory("TULIPANES", R.drawable.logomaison)
        )
    }

    val popularProducts = remember {
        listOf(
            FlowerProduct("Ramo de 6 girasoles", "S/ 65.00", R.drawable.logomaison),
            FlowerProduct("Rosas Rojas Clásicas", "S/ 85.00", R.drawable.logomaison),
            FlowerProduct("Lirios Blancos Elegantes", "S/ 70.00", R.drawable.logomaison)
        )
    }

    // El contenido principal de la pantalla
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
        // El paddingValues se aplicará a la LazyColumn de abajo para evitar que el contenido sea cubierto por la barra inferior
        // y para que el contenido superior (buscador/engranaje) quede pegado arriba.
    ) {
        // Contenedor para el buscador y el engranaje
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp) // Padding interno para esta fila
                .padding(top = paddingValues.calculateTopPadding()), // Aplica el padding superior del Scaffold aquí
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Espacia los elementos
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar flores, ramos...", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)) },
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                modifier = Modifier
                    .weight(1f) // Ocupa el espacio disponible y empuja el icono a la derecha
                    .height(60.dp)
                    .padding(vertical = 4.dp), // Espacio entre el campo y el icono
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    errorContainerColor = Color.White
                )
            )
            IconButton(onClick = { /* TODO: Navegar a SettingsScreen */ }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Configuración",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(28.dp) // Ajusta el tamaño del icono si es necesario
                )
            }
        }

        // El resto del contenido de la pantalla, aplicando solo el padding inferior del Scaffold
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding()) // Solo aplica el padding inferior
                .padding(horizontal = 16.dp, vertical = 8.dp) // Padding interno para el contenido
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Categorias",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "ver todo",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { navController.navigate("category_screen") }
                    )
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (categories.size > 3) 400.dp else 200.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        CategoryItem(category = category) {
                            navController.navigate("product_list_screen/${category.name}")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Populares",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "ver todo",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { /* TODO: Navegar a pantalla de todos los productos populares */ }
                    )
                }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(popularProducts) { product ->
                        ProductCard(
                            product = product,
                            onClick = { /* TODO: Navegar a detalles del producto */ },
                            onAddToCart = { productToAdd ->
                                cartViewModel.addItem(productToAdd)
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    MaisonFlowersTheme {
        HomeScreen(navController = rememberNavController(), cartViewModel = CartViewModel(), paddingValues = PaddingValues(0.dp))
    }
}
