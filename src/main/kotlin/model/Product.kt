package model

import service.NO_INGREDIENTS_LABEL
import service.formatAmount
import service.ingredientLabel
import service.normalizeText
import java.util.Objects

/**
 * Producto del menú. Equivale a la clase Producto.
 * [ingredients] indica la cantidad de cada ingrediente necesaria por unidad
 * y [category] clasifica el producto.
 *
 * Dos productos son iguales (==) si tienen el mismo nombre normalizado y la misma categoría,
 * sin importar el precio, la clase concreta ni la instancia. Las subclases no deben sobrescribir
 * [equals] para no romper la simetría.
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

    /** Igualdad por nombre normalizado (sin espacios en los bordes y sin distinguir mayúsculas) y categoría. */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Product) return false
        return category == other.category && normalizeText(name) == normalizeText(other.name)
    }

    /** Consistente con [equals]: usa los mismos atributos normalizados. */
    override fun hashCode(): Int = Objects.hash(normalizeText(name), category)

    /** Texto legible, por ejemplo "Pepperoni (Pizza)". */
    override fun toString(): String = "$name (${category.label})"
}
