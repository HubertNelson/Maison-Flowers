package com.example.maisonflowers.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Delete
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
import com.example.maisonflowers.ui.components.FlowerProduct
import com.example.maisonflowers.ui.viewmodels.CartViewModel

// Nuevo Data Class para representar un ítem en el carrito
data class CartItem(
    val product: FlowerProduct,
    var quantity: Int   // La cantidad de este producto en el carrito
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartViewModel
) {
    var selectedItem by remember { mutableIntStateOf(3) }

    val cartItems = cartViewModel.cartItems // Obtiene los ítems del ViewModel

    val subtotal = cartItems.sumOf { item ->
        val priceString = item.product.price.replace("S/", "").trim().replace(",", "")
        val price = priceString.toDoubleOrNull() ?: 0.0
        price * item.quantity
    }
    val shippingCost = if (subtotal > 0) 15.00 else 0.00 // Ejemplo: 15.00 si hay productos, 0 si no
    val total = subtotal + shippingCost

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mi Carrito",
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
                        navController.navigate("search_screen")
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
                    onClick = { selectedItem = 3 /* Ya estás en carrito */ },
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
                    onClick = {
                        selectedItem = 4
                        navController.navigate("account_screen")
                    },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (cartItems.isEmpty()) {
                // Estado de carrito vacío
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Carrito Vacío",
                        modifier = Modifier.size(96.dp),
                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "¡Tu carrito está vacío!",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Añade algunas flores hermosas para empezar tu pedido.",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 32.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { navController.navigate("home_screen") },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(56.dp)
                    ) {
                        Text("Explorar Flores", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            } else {
                // Contenido del carrito con productos
                LazyColumn(
                    modifier = Modifier.weight(1f), // Ocupa el espacio restante para la lista de ítems
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(cartItems) { item ->
                        CartItemCard(
                            cartItem = item,
                            onQuantityChange = { newQuantity ->
                                cartViewModel.updateQuantity(item, newQuantity)
                            },
                            onRemoveItem = { cartViewModel.removeItem(item) }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp)) // Espacio antes del resumen
                    }
                }

                // Resumen del pedido y botón de pago
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        OrderSummaryRow(label = "Subtotal:", value = "S/ %.2f".format(subtotal))
                        OrderSummaryRow(label = "Envío:", value = "S/ %.2f".format(shippingCost))
                        Divider(modifier = Modifier.padding(vertical = 8.dp))
                        OrderSummaryRow(label = "Total:", value = "S/ %.2f".format(total), isTotal = true)

                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { /* TODO: Navegar a pantalla de checkout/pago */
                                println("Proceder al Pago Clicked. Total: S/ %.2f".format(total))
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                        ) {
                            Text(text = "Proceder al Pago", color = MaterialTheme.colorScheme.onPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp)) // Pequeño espacio al final
            }
        }
    }
}

@Composable
fun CartItemCard(
    cartItem: CartItem,
    onQuantityChange: (Int) -> Unit,
    onRemoveItem: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del producto
            Image(
                painter = painterResource(id = cartItem.product.imageResId),
                contentDescription = cartItem.product.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Nombre del producto
                Text(
                    text = cartItem.product.name,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Precio unitario
                Text(
                    text = "Precio: ${cartItem.product.price}",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                // Selector de cantidad
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { onQuantityChange(cartItem.quantity - 1) },
                        enabled = cartItem.quantity > 0 // Deshabilitar si la cantidad es 0
                    ) {
                        Icon(Icons.Default.RemoveCircle, contentDescription = "Disminuir cantidad")
                    }
                    Text(
                        text = "${cartItem.quantity}",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    IconButton(
                        onClick = { onQuantityChange(cartItem.quantity + 1) }
                    ) {
                        Icon(Icons.Default.AddCircle, contentDescription = "Aumentar cantidad")
                    }
                }
            }

            // Subtotal del item y botón de eliminar
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.height(80.dp) // Alinea la columna al alto de la imagen
            ) {
                val priceString = cartItem.product.price.replace("S/", "").trim().replace(",", "")
                val price = priceString.toDoubleOrNull() ?: 0.0
                val itemSubtotal = price * cartItem.quantity
                Text(
                    text = "S/ %.2f".format(itemSubtotal),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onRemoveItem) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun OrderSummaryRow(label: String, value: String, isTotal: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = if (isTotal) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
            fontSize = if (isTotal) 18.sp else 16.sp,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = value,
            color = if (isTotal) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
            fontSize = if (isTotal) 18.sp else 16.sp,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal,
            textAlign = TextAlign.End
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewCartScreenWithItems() {
    MaisonFlowersTheme {
        // En preview, se crea una instancia del ViewModel directamente para la previsualización.
        val navController = rememberNavController()
        val previewCartViewModel = CartViewModel()
        previewCartViewModel.addItem(FlowerProduct("Preview Roses", "S/ 10.00", R.drawable.logomaison))
        previewCartViewModel.addItem(FlowerProduct("Preview Girasoles", "S/ 15.00", R.drawable.logomaison))

        CartScreen(navController = navController, cartViewModel = previewCartViewModel)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCartScreenEmpty() {
    MaisonFlowersTheme {
        val navController = rememberNavController()
        val previewCartViewModel = CartViewModel() // Nueva instancia, vacía por defecto, actualizar mas tarde

        CartScreen(navController = navController, cartViewModel = previewCartViewModel)
    }
}
