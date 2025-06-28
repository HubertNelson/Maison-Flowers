package com.example.maisonflowers.ui.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.maisonflowers.ui.components.FlowerProduct
import com.example.maisonflowers.ui.screens.CartItem

class CartViewModel : ViewModel() {

    val cartItems = mutableStateListOf<CartItem>()

    fun addItem(product: FlowerProduct) {
        val existingItem = cartItems.find { it.product == product }
        if (existingItem != null) {
            val index = cartItems.indexOf(existingItem)
            cartItems[index] = existingItem.copy(quantity = existingItem.quantity + 1)
        } else {
            cartItems.add(CartItem(product, 1))
        }
        println("Producto añadido al carrito: ${product.name}. Cantidad actual: ${existingItem?.quantity ?: 1}")
        printCartContents()
    }

    fun removeItem(item: CartItem) {
        cartItems.remove(item)
        println("Producto eliminado del carrito: ${item.product.name}")
        printCartContents()
    }

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
        printCartContents()
    }

    fun clearCart() {
        cartItems.clear()
        println("Carrito limpiado.")
        printCartContents()
    }

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
