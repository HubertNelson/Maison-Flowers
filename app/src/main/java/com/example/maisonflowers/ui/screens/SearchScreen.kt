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
import com.example.maisonflowers.ui.components.ProductCard
import com.example.maisonflowers.ui.viewmodels.CartViewModel
import com.example.maisonflowers.models.FlowerProduct // Importar la nueva FlowerProduct del modelo
import com.google.firebase.firestore.FirebaseFirestore // Importar FirebaseFirestore
import kotlinx.coroutines.tasks.await // Importar para usar .await() en tareas de Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    paddingValues: PaddingValues
) {
    var searchQuery by remember { mutableStateOf("") }

    val allProducts = remember { mutableStateListOf<FlowerProduct>() }
    var isLoadingProducts by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Cargar todos los productos desde Firestore
    LaunchedEffect(Unit) { // Se ejecuta una vez
        try {
            val db = FirebaseFirestore.getInstance()
            val result = db.collection("products")
                .get()
                .await()

            allProducts.clear()
            for (document in result.documents) {
                val product = document.toObject(FlowerProduct::class.java)
                product?.let {
                    allProducts.add(it)
                }
            }
            isLoadingProducts = false
        } catch (e: Exception) {
            errorMessage = "Error al cargar productos para la búsqueda: ${e.message}"
            isLoadingProducts = false
            println("Error al cargar todos los productos: ${e.message}") // Para depuración
        }
    }

    val filteredProducts = remember(searchQuery, allProducts) {
        if (searchQuery.isBlank()) {
            emptyList() // No mostrar nada si la búsqueda está vacía
        } else {
            allProducts.filter { product ->
                product.name.contains(searchQuery, ignoreCase = true) ||
                        product.description.contains(searchQuery, ignoreCase = true) ||
                        product.category.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
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
            placeholder = { Text("Buscar flores, ramos, colores...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            singleLine = true,
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar", tint = MaterialTheme.colorScheme.onSurfaceVariant) },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 16.dp, vertical = 4.dp),
            shape = RoundedCornerShape(24.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                errorContainerColor = MaterialTheme.colorScheme.surface,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoadingProducts) {
            Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (errorMessage != null) {
            Text(
                text = errorMessage ?: "Error desconocido",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                textAlign = TextAlign.Center
            )
        } else if (searchQuery.isBlank()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Empieza a escribir para buscar tus flores...",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )
            }
        } else if (filteredProducts.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding()),
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
        } else {
            Text(
                text = "${filteredProducts.size} resultados encontrados",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, bottom = 16.dp),
                textAlign = TextAlign.Start
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(filteredProducts) { product ->
                    ProductCard(
                        product = product,
                        onClick = { productId -> navController.navigate("product_detail_screen/$productId") }, // Navegar al detalle del producto
                        onAddToCart = { productToAdd ->
                            cartViewModel.addItem(productToAdd)
                        }
                    )
                }
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
