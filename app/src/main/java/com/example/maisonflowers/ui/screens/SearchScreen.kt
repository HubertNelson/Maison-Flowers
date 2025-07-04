package com.example.maisonflowers.ui.screens

import androidx.compose.foundation.background
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
fun SearchScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    paddingValues: PaddingValues // Recibe los paddingValues del Scaffold externo
) {
    var searchQuery by remember { mutableStateOf("") }

    val allProducts = remember {
        listOf(
            FlowerProduct("Classic Red Roses", "S/ 85.00", R.drawable.logomaison),
            FlowerProduct("Romantic Pink Roses", "S/ 78.00", R.drawable.logomaison),
            FlowerProduct("Elegant White Roses", "S/ 92.00", R.drawable.logomaison),
            FlowerProduct("Cheerful Yellow Roses", "S/ 80.00", R.drawable.logomaison),
            FlowerProduct("Ramo de 6 Girasoles", "S/ 65.00", R.drawable.logomaison),
            FlowerProduct("Girasoles Vibrantes", "S/ 70.00", R.drawable.logomaison),
            FlowerProduct("Lirios Blancos Puros", "S/ 60.00", R.drawable.logomaison),
            FlowerProduct("Lirios Rosados Exóticos", "S/ 68.00", R.drawable.logomaison),
            FlowerProduct("Tulipanes Mixtos", "S/ 72.00", R.drawable.logomaison),
            FlowerProduct("Orquídea Phalaenopsis", "S/ 120.00", R.drawable.logomaison),
            FlowerProduct("Ramo de Lavanda", "S/ 55.00", R.drawable.logomaison)
        )
    }

    val filteredProducts = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            emptyList()
        } else {
            allProducts.filter { product ->
                product.name.contains(searchQuery, ignoreCase = true) ||
                        product.price.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
        // No aplicar paddingValues aquí
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Buscar",
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
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Buscar flores, ramos, colores...", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)) },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 16.dp, vertical = 4.dp), // Aplicar padding horizontal aquí
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

        Spacer(modifier = Modifier.height(16.dp))

        if (searchQuery.isBlank()) {
            Text(
                text = "Empieza a escribir para buscar tus flores...",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = paddingValues.calculateBottomPadding()) // Solo padding inferior
                    .padding(horizontal = 16.dp, vertical = 16.dp), // Padding interno
                textAlign = TextAlign.Center
            )
        } else if (filteredProducts.isNotEmpty()) {
            Text(
                text = "${filteredProducts.size} resultados encontrados",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, bottom = 16.dp), // Padding para el texto
                textAlign = TextAlign.Start
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding()) // Solo padding inferior
                    .padding(horizontal = 16.dp, vertical = 8.dp), // Padding interno para la grid
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredProducts) { product ->
                    ProductCard(
                        product = product,
                        onClick = { /* TODO: Navegar a la pantalla de detalles del producto */ },
                        onAddToCart = { productToAdd ->
                            cartViewModel.addItem(productToAdd)
                        }
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding()), // Solo padding inferior
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Lo sentimos, no encontramos resultados para \"$searchQuery\".",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Intenta con otras palabras o revisa la ortografía.",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchScreen() {
    MaisonFlowersTheme {
        SearchScreen(navController = rememberNavController(), cartViewModel = CartViewModel(), paddingValues = PaddingValues(0.dp))
    }
}
