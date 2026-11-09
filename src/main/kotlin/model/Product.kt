package model

import service.NO_INGREDIENTS_LABEL
import service.formatAmount
import service.ingredientLabel

/**
 * Producto del menú. Equivale a la clase Producto.
 * [ingredients] indica la cantidad de cada ingrediente necesaria por unidad
 * y [category] clasifica el producto.
 */
abstract class Product(
    val name: String,
    val price: Double,
    val ingredients: Map<String, Int>,
    val category: ProductCategory
) {

    /**
     * Retorna el nombre, la categoría, el precio formateado y los ingredientes.
     * Equivale a detallarProducto() del enunciado.
     */
    fun describeProduct(): String {
        val ingredientList = ingredients.entries
            .joinToString(", ") { (ingredient, quantity) -> "${ingredientLabel(ingredient)} x$quantity" }
            .ifEmpty { NO_INGREDIENTS_LABEL }
        return "$name [${category.label}] - ${formatAmount(price)} (ingredientes: $ingredientList)"
    }
}
