package com.example.viewmodelcounterapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.viewmodelcounterapp.model.Product
import com.example.viewmodelcounterapp.model.ProductPriority
import com.example.viewmodelcounterapp.model.ProductStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProductViewModel : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    fun addProduct(name: String, productStatus: ProductStatus, productPriority: ProductPriority) {
        if (name.isBlank()) return

        val newId = (_products.value.maxOfOrNull { it.id } ?: 0) + 1
        val newProduct = Product(newId, name, productStatus, productPriority)
        _products.value += newProduct
    }

    fun cycleStatus(id: Int) {
        _products.value = _products.value.map { product ->
            if (product.id == id) {
                val next = when(product.status) {
                    ProductStatus.PENDING -> ProductStatus.IN_PROGRESS
                    ProductStatus.IN_PROGRESS -> ProductStatus.COMPLETED
                    ProductStatus.COMPLETED -> ProductStatus.PENDING
                }
                product.copy(status = next)
            } else product
        }
    }


    fun deleteProduct(id: Int) {
        _products.value = _products.value.filterNot { it.id == id }
    }


}
