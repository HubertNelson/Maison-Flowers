package com.example.maisonflowers.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.maisonflowers.ui.components.FlowerProduct
import com.example.maisonflowers.ui.components.ProductCard
import com.example.maisonflowers.ui.viewmodels.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    navController: NavController,
    categoryName: String?,
    cartViewModel: CartViewModel,
    paddingValues: PaddingValues // ¡Nuevo parámetro!
) {
    var searchQuery by remember { mutableStateOf("") }
    // var selectedItem by remember { mutableIntStateOf(1) } // ¡Eliminado, gestionado externamente!

    val products = remember(categoryName) {
        when (categoryName) {
            "ROSAS" -> listOf(
                FlowerProduct("Classic Red Roses", "S/ 85.00", R.drawable.logomaison),
                FlowerProduct("Romantic Pink Roses", "S/ 78.00", R.drawable.logomaison),
                FlowerProduct("Elegant White Roses", "S/ 92.00", R.drawable.logomaison),
                FlowerProduct("Cheerful Yellow Roses", "S/ 80.00", R.drawable.logomaison),
                FlowerProduct("Velvet Deep Red Roses", "S/ 95.00", R.drawable.logomaison),
                FlowerProduct("Sunset Orange Roses", "S/ 88.00", R.drawable.logomaison)
            )
            "GIRASOLES" -> listOf(
                FlowerProduct("Ramo de 6 Girasoles", "S/ 65.00", R.drawable.logomaison),
                FlowerProduct("Girasoles Vibrantes", "S/ 70.00", R.drawable.logomaison),
                FlowerProduct("Alegre Ramo de Girasoles", "S/ 75.00", R.drawable.logomaison)
            )
            "LIRIOS" -> listOf(
                FlowerProduct("Lirios Blancos Puros", "S/ 60.00", R.drawable.logomaison),
                FlowerProduct("Lirios Rosados Exóticos", "S/ 68.00", R.drawable.logomaison)
            )
            else -> listOf(
                FlowerProduct("Producto Genérico 1", "S/ 50.00", R.drawable.logomaison),
                FlowerProduct("Producto Genérico 2", "S/ 55.00", R.drawable.logomaison)
            )
        }
    }

    // El Scaffold principal se ha movido a MaisonFlowersApp.
    // Aquí solo definimos el TopAppBar y el contenido de la pantalla.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues) // ¡Aplicar paddingValues del Scaffold externo!
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = categoryName ?: "Productos",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Volver a Categorías",
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
        // Campo de búsqueda movido aquí, debajo de la TopAppBar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Buscar...", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)) },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(vertical = 4.dp),
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
        Spacer(modifier = Modifier.height(16.dp)) // Espacio entre el campo de búsqueda y el texto

        Text(
            text = "Lista de Productos (${products.size})",
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(), // Ocupa el resto del espacio
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(products) { product ->
                ProductCard(
                    product = product,
                    onClick = { /* TODO: Navegar a la pantalla de detalles del producto */ },
                    onAddToCart = { productToAdd ->
                        cartViewModel.addItem(productToAdd)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProductListScreen() {
    MaisonFlowersTheme {
        ProductListScreen(navController = rememberNavController(), categoryName = "ROSAS", cartViewModel = CartViewModel(), paddingValues = PaddingValues(0.dp))
    }
}
