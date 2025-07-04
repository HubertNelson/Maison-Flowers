package com.example.maisonflowers.ui.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.maisonflowers.models.FlowerProduct // Importar la nueva FlowerProduct del modelo
import com.example.maisonflowers.ui.screens.CartItem // Importar CartItem de ui.screens

class CartViewModel : ViewModel() {
    // Lista de ítems en el carrito
    private val _cartItems = mutableStateListOf<CartItem>()
    val cartItems: List<CartItem> get() = _cartItems

    // Añadir un producto al carrito
    fun addItem(product: FlowerProduct) { // Cambiado a FlowerProduct del modelo
        val existingItem = _cartItems.find { it.product.id == product.id }
        if (existingItem != null) {
            // Si el producto ya está en el carrito, incrementa la cantidad
            existingItem.quantity++
        } else {
            // Si no está, añade un nuevo CartItem
            _cartItems.add(CartItem(product, 1))
        }
    }

    // Actualizar la cantidad de un producto en el carrito
    fun updateQuantity(item: CartItem, newQuantity: Int) {
        val index = _cartItems.indexOf(item)
        if (index != -1) {
            if (newQuantity <= 0) {
                // Si la cantidad es 0 o menos, eliminar el ítem
                _cartItems.removeAt(index)
            } else {
                // Actualizar la cantidad
                _cartItems[index] = item.copy(quantity = newQuantity)
            }
        }
    }

    // Eliminar un producto del carrito
    fun removeItem(item: CartItem) {
        _cartItems.remove(item)
    }

    // Limpiar todo el carrito
    fun clearCart() {
        _cartItems.clear()
    }
}