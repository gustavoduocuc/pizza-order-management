package model

import service.formatAmount
import service.ingredientLabel

/**
 * Producto del menú (una pizza). Equivale a la clase Producto.
 * [ingredients] indica la cantidad de cada ingrediente necesaria por pizza.
 */
class Product(val name: String, val price: Double, val ingredients: Map<String, Int>) {

    /**
     * Retorna el nombre, el precio formateado y los ingredientes.
     * Equivale a detallarProducto() del enunciado.
     */
    fun describeProduct(): String {
        val ingredientList = ingredients.entries.joinToString(", ") { (ingredient, quantity) ->
            "${ingredientLabel(ingredient)} x$quantity"
        }
        return "$name - ${formatAmount(price)} (ingredientes: $ingredientList)"
    }
}
