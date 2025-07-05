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
import com.example.maisonflowers.ui.components.ProductCard
import com.example.maisonflowers.ui.viewmodels.CartViewModel
import com.example.maisonflowers.models.FlowerProduct // Importar la nueva FlowerProduct del modelo
import com.google.firebase.firestore.FirebaseFirestore // Importar FirebaseFirestore
import kotlinx.coroutines.tasks.await // Importar para usar .await() en tareas de Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    navController: NavController,
    categoryName: String?,
    cartViewModel: CartViewModel,
    paddingValues: PaddingValues
) {
    var searchQuery by remember { mutableStateOf("") }

    val products = remember { mutableStateListOf<FlowerProduct>() }
    var isLoadingProducts by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Cargar productos por categoría desde Firestore
    LaunchedEffect(categoryName) { // Se ejecuta cuando categoryName cambia
        isLoadingProducts = true
        errorMessage = null
        try {
            val db = FirebaseFirestore.getInstance()
            val query = if (categoryName.isNullOrBlank() || categoryName == "Todos") {
                db.collection("products") // Si no hay categoría o es "Todos", trae todos
            } else {
                db.collection("products").whereEqualTo("category", categoryName.uppercase()) // Filtrar por categoría
            }

            val result = query.get().await()

            products.clear()
            for (document in result.documents) {
                val product = document.toObject(FlowerProduct::class.java)
                product?.let {
                    products.add(it)
                }
            }
            isLoadingProducts = false
        } catch (e: Exception) {
            errorMessage = "Error al cargar productos: ${e.message}"
            isLoadingProducts = false
            println("Error al cargar productos de categoría $categoryName: ${e.message}") // Para depuración
        }
    }

    val filteredProducts = remember(searchQuery, products) {
        if (searchQuery.isBlank()) {
            products
        } else {
            products.filter { product ->
                product.name.contains(searchQuery, ignoreCase = true) ||
                        product.description.contains(searchQuery, ignoreCase = true)
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
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Buscar...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
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
        } else if (filteredProducts.isEmpty()) {
            Text(
                text = "No hay productos disponibles en esta categoría.",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                textAlign = TextAlign.Center
            )
        } else {
            Text(
                text = "Lista de Productos (${filteredProducts.size})",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
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
fun PreviewProductListScreen() {
    MaisonFlowersTheme {
        ProductListScreen(navController = rememberNavController(), categoryName = "ROSAS", cartViewModel = CartViewModel(), paddingValues = PaddingValues(0.dp))
    }
}
