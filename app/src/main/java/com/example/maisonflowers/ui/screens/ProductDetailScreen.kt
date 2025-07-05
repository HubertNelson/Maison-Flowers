package com.example.maisonflowers.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.maisonflowers.models.FlowerProduct
import com.example.maisonflowers.ui.theme.MaisonFlowersTheme
import com.example.maisonflowers.ui.viewmodels.CartViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    navController: NavController,
    productId: String?,
    cartViewModel: CartViewModel,
    paddingValues: PaddingValues
) {
    var product by remember { mutableStateOf<FlowerProduct?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    LaunchedEffect(productId) {
        if (productId != null) {
            try {
                val db = FirebaseFirestore.getInstance()
                val document = db.collection("products").document(productId).get().await()
                if (document.exists()) {
                    product = document.toObject(FlowerProduct::class.java)
                } else {
                    errorMessage = "Producto no encontrado."
                }
                isLoading = false
            } catch (e: Exception) {
                errorMessage = "Error al cargar el producto: ${e.message}"
                isLoading = false
                Log.e("ProductDetailScreen", "Error al cargar el producto $productId: ${e.message}")
            }
        } else {
            errorMessage = "ID de producto no proporcionado."
            isLoading = false
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
                    text = product?.name ?: "Detalles del Producto",
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

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (errorMessage != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = errorMessage ?: "Error desconocido",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else if (product != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding())
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                item {
                    val imageUrl = product!!.imageUrl.ifEmpty { "https://placehold.co/600x400/E9C9F6/673AB7?text=No+Image" }
                    Log.d("ProductDetailScreen", "Cargando imagen para ${product!!.name}: $imageUrl")

                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(context)
                                .data(imageUrl)
                                .apply(fun ImageRequest.Builder.() {
                                    listener(
                                        onStart = { Log.d("CoilDebug", "ProductDetail: Carga iniciada para $imageUrl") },
                                        onSuccess = { _, _ -> Log.d("CoilDebug", "ProductDetail: Carga exitosa para $imageUrl") },
                                        onError = { _, result -> Log.e("CoilDebug", "ProductDetail: Error al cargar $imageUrl", result.throwable) }
                                    )
                                })
                                .build()
                        ),
                        contentDescription = product!!.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Text(
                        text = product!!.name,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                item {
                    Text(
                        text = "S/ %.2f".format(product!!.price),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Text(
                        text = "Descripción:",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = product!!.description,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                        fontSize = 16.sp,
                        lineHeight = 24.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item {
                    Button(
                        onClick = {
                            cartViewModel.addItem(product!!)
                            navController.navigate("cart_screen") {
                                popUpTo("cart_screen") { inclusive = true }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Icon(Icons.Default.AddShoppingCart, contentDescription = "Añadir al carrito", tint = MaterialTheme.colorScheme.onPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Añadir al Carrito", color = MaterialTheme.colorScheme.onPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProductDetailScreen() {
    MaisonFlowersTheme {
        ProductDetailScreen(
            navController = rememberNavController(),
            productId = "sample_product_id",
            cartViewModel = CartViewModel(),
            paddingValues = PaddingValues(0.dp)
        )
    }
}
