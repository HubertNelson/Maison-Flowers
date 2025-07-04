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

// Data Class para representar un ítem en el carrito (mantenerlo aquí o moverlo a un archivo de modelos si lo usas en más lugares)
data class CartItem(
    val product: FlowerProduct,
    var quantity: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    paddingValues: PaddingValues // Recibe los paddingValues del Scaffold externo
) {
    val cartItems = cartViewModel.cartItems

    val subtotal = cartItems.sumOf { item ->
        val priceString = item.product.price.replace("S/", "").trim().replace(",", "")
        val price = priceString.toDoubleOrNull() ?: 0.0
        price * item.quantity
    }
    val shippingCost = if (subtotal > 0) 15.00 else 0.00
    val total = subtotal + shippingCost

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
        // No aplicar paddingValues aquí
    ) {
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
        if (cartItems.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding()), // Solo padding inferior
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
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp), // Padding interno
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
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp) // Padding interno
                    .padding(bottom = paddingValues.calculateBottomPadding()) // Solo padding inferior
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OrderSummaryRow(label = "Subtotal:", value = "S/ %.2f".format(subtotal))
                    OrderSummaryRow(label = "Envío:", value = "S/ %.2f".format(shippingCost))
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    OrderSummaryRow(label = "Total:", value = "S/ %.2f".format(total), isTotal = true)

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
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
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

// Mantener CartItemCard y OrderSummaryRow como composables auxiliares
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
                Text(
                    text = cartItem.product.name,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Precio: ${cartItem.product.price}",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { onQuantityChange(cartItem.quantity - 1) },
                        enabled = cartItem.quantity > 0
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

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.height(80.dp)
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
        val navController = rememberNavController()
        val previewCartViewModel = CartViewModel()
        previewCartViewModel.addItem(FlowerProduct("Preview Roses", "S/ 10.00", R.drawable.logomaison))
        previewCartViewModel.addItem(FlowerProduct("Preview Girasoles", "S/ 15.00", R.drawable.logomaison))

        CartScreen(navController = navController, cartViewModel = previewCartViewModel, paddingValues = PaddingValues(0.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCartScreenEmpty() {
    MaisonFlowersTheme {
        val navController = rememberNavController()
        val previewCartViewModel = CartViewModel()

        CartScreen(navController = navController, cartViewModel = previewCartViewModel, paddingValues = PaddingValues(0.dp))
    }
}
