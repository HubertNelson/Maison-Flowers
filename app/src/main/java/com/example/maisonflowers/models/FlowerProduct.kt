package com.example.maisonflowers.models

import com.google.firebase.firestore.DocumentId

// Este es el modelo de datos para los productos tal como se almacenarán en Firestore.
// Usamos @DocumentId para mapear automáticamente el ID del documento de Firestore a esta propiedad.
data class FlowerProduct(
    @DocumentId
    val id: String = "", // El ID del documento en Firestore
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "", // URL de la imagen del producto
    val category: String = "",
    val isPopular: Boolean = false // Para identificar productos populares
) {
    // Constructor sin argumentos requerido por Firestore para deserialización
    constructor() : this("", "", "", 0.0, "", "", false)
}
