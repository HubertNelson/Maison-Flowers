package com.example.maisonflowers.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.maisonflowers.R
import com.example.maisonflowers.ui.theme.MaisonFlowersTheme
import com.example.maisonflowers.models.FlowerProduct

// El componente ProductCard ahora recibe el FlowerProduct del modelo
@Composable
fun ProductCard(
    product: FlowerProduct, // Cambiado a FlowerProduct del modelo
    onClick: (FlowerProduct) -> Unit,
    onAddToCart: (FlowerProduct) -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .width(180.dp)
            .clickable { onClick(product) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Cargar imagen desde URL usando Coil
            Image(
                painter = rememberAsyncImagePainter(model = product.imageUrl.ifEmpty { "https://placehold.co/400x300/E9C9F6/673AB7?text=No+Image" }), // Placeholder si no hay URL
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray), // Fondo temporal mientras carga o si no hay imagen
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = product.name,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.heightIn(min = 40.dp) // Asegura espacio para 2 líneas
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "S/ %.2f".format(product.price), // Formatear el precio
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = { onAddToCart(product) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AddShoppingCart,
                        contentDescription = "Añadir al carrito",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProductCard() {
    MaisonFlowersTheme {
        ProductCard(
            product = FlowerProduct(
                id = "1",
                name = "Ramo de Rosas Rojas Premium",
                description = "Un hermoso ramo de rosas rojas frescas.",
                price = 99.99,
                imageUrl = "https://placehold.co/400x300/E9C9F6/673AB7?text=Rosas", // Usar un placeholder de URL
                category = "ROSAS",
                isPopular = true
            ),
            onClick = {},
            onAddToCart = {}
        )
    }
}
