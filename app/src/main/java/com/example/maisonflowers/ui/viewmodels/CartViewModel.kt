package com.example.maisonflowers.ui.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.maisonflowers.ui.components.FlowerProduct
import com.example.maisonflowers.ui.screens.CartItem // Importa el data class CartItem

// El ViewModel será un Singleton o se creará via viewModels() de Compose
// para que su instancia persista mientras la Activity o NavGraph esté viva.
class CartViewModel : ViewModel() {

    // mutableStateListOf es una lista observable por Compose
    // Esta lista mantendrá los ítems del carrito
    val cartItems = mutableStateListOf<CartItem>()

    /**
     * Añade un producto al carrito o incrementa su cantidad si ya existe.
     * @param product El FlowerProduct a añadir.
     */
    fun addItem(product: FlowerProduct) {
        val existingItem = cartItems.find { it.product == product }
        if (existingItem != null) {
            // Si el producto ya está en el carrito, incrementa la cantidad
            val index = cartItems.indexOf(existingItem)
            cartItems[index] = existingItem.copy(quantity = existingItem.quantity + 1)
        } else {
            // Si el producto no está en el carrito, añádelo con cantidad 1
            cartItems.add(CartItem(product, 1))
        }
        println("Producto añadido al carrito: ${product.name}. Cantidad actual: ${existingItem?.quantity ?: 1}")
        printCartContents() // Para depuración
    }

    /**
     * Elimina un producto del carrito.
     * @param item El CartItem a eliminar.
     */
    fun removeItem(item: CartItem) {
        cartItems.remove(item)
        println("Producto eliminado del carrito: ${item.product.name}")
        printCartContents() // Para depuración
    }

    /**
     * Actualiza la cantidad de un ítem en el carrito.
     * Si la nueva cantidad es 0 o menos, elimina el ítem.
     * @param item El CartItem a actualizar.
     * @param newQuantity La nueva cantidad.
     */
    fun updateQuantity(item: CartItem, newQuantity: Int) {
        val index = cartItems.indexOf(item)
        if (index != -1) {
            if (newQuantity > 0) {
                cartItems[index] = item.copy(quantity = newQuantity)
                println("Cantidad de ${item.product.name} actualizada a $newQuantity")
            } else {
                cartItems.removeAt(index)
                println("${item.product.name} eliminado del carrito por cantidad 0")
            }
        }
        printCartContents() // Para depuración
    }

    /**
     * Limpia completamente el carrito.
     */
    fun clearCart() {
        cartItems.clear()
        println("Carrito limpiado.")
        printCartContents() // Para depuración
    }

    // Función de depuración para ver el contenido del carrito
    private fun printCartContents() {
        println("--- Contenido del Carrito ---")
        if (cartItems.isEmpty()) {
            println("Carrito vacío.")
        } else {
            cartItems.forEachIndexed { index, cartItem ->
                println("$index: ${cartItem.product.name} - Cantidad: ${cartItem.quantity}")
            }
        }
        println("-----------------------------")
    }
}
