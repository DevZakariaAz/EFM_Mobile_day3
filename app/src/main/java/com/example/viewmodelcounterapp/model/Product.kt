package com.example.viewmodelcounterapp.model

data class Product(
    val id: Int,
    val name: String,
    val status: ProductStatus,
    val priority: ProductPriority
)

enum class ProductStatus { PENDING, IN_PROGRESS, COMPLETED}

enum class ProductPriority {HIGH, MEDIUM, LOW}
