package com.example.viewmodelcounterapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.viewmodelcounterapp.viewmodel.ProductViewModel
import com.example.viewmodelcounterapp.model.Product
import com.example.viewmodelcounterapp.model.ProductStatus
import com.example.viewmodelcounterapp.model.ProductPriority

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProductScreen()
        }
    }
}

@Composable
fun ProductScreen(viewModel: ProductViewModel = viewModel()) {
    val products by viewModel.products.collectAsState()
    var name by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(ProductStatus.PENDING) }
    var priority by remember { mutableStateOf(ProductPriority.MEDIUM) }
    var triedToAdd by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Products List",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.weight(1f),
                    label = { Text("New Product") }
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (triedToAdd && name.isBlank()) {
                    Text(
                        text = "Name is required",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                DropDownMenuBox(
                    options = ProductStatus.values().toList(),
                    selected = status,
                    onSelect = { status = it }
                )
                Spacer(modifier = Modifier.width(8.dp))

                DropDownMenuBox(
                    options = ProductPriority.values().toList(),
                    selected = priority,
                    onSelect = { priority = it }
                )
                Spacer(modifier = Modifier.width(8.dp))

                Button(onClick = {
                    triedToAdd = true
                    if (name.isNotBlank() && triedToAdd) {
                        viewModel.addProduct(name, status, priority)
                        name = ""
                        triedToAdd = false
                    }
                }) {
                    Text("Add")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(products) { product ->
                    ProductItem(
                        product = product,
                        onCycleStatus = { viewModel.cycleStatus(product.id) },
                        onDelete = { viewModel.deleteProduct(product.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun <T> DropDownMenuBox(options: List<T>, selected: T, onSelect: (T) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box{
        Button(onClick = { expanded = true }) {
            Text(selected.toString())
        }

        DropdownMenu(expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(text = { Text(option.toString()) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    })
            }
        }
    }
}

@Composable
fun ProductItem(product: Product, onCycleStatus: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = product.name, style = MaterialTheme.typography.bodyLarge)
                Text(
                    "Status: ${product.status}, Priority: ${product.priority}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Row {
                IconButton(onClick = onCycleStatus) {
                    Icon(Icons.Default.Refresh, contentDescription = "Cycle Status")
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete"
                    )
                }
            }
        }
    }
}
