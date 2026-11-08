package service

import model.DeliveryPerson
import model.Product

// Funciones del inventario de ingredientes.
// El inventario es un MutableMap: ingrediente -> unidades disponibles.

/** Etiquetas en español de los ingredientes (las claves del inventario están en inglés). */
private val INGREDIENT_LABELS: Map<String, String> = mapOf(
    "dough" to "masa",
    "sauce" to "salsa",
    "cheese" to "queso",
    "pepperoni" to "pepperoni",
    "vegetables" to "verduras"
)

/** Retorna la etiqueta en español de un ingrediente, o su clave si no tiene. */
fun ingredientLabel(ingredient: String): String = INGREDIENT_LABELS[ingredient] ?: ingredient

/** Suma la cantidad de cada ingrediente necesaria para preparar todas las pizzas. */
fun calculateRequiredIngredients(products: List<Product>): Map<String, Int> =
    products
        .flatMap { it.ingredients.entries }
        .groupBy({ it.key }, { it.value })
        .mapValues { it.value.sum() }

/** Retorna los ingredientes que no alcanzan y cuántas unidades faltan de cada uno. */
fun findMissingIngredients(required: Map<String, Int>, inventory: Map<String, Int>): Map<String, Int> =
    required
        .mapNotNull { (ingredient, quantity) ->
            val missing = quantity - (inventory[ingredient] ?: 0)
            if (missing > 0) ingredient to missing else null
        }
        .toMap()

/** Descuenta del inventario los ingredientes usados. */
fun deductFromInventory(required: Map<String, Int>, inventory: MutableMap<String, Int>) {
    for ((ingredient, quantity) in required) {
        inventory[ingredient] = (inventory[ingredient] ?: 0) - quantity
    }
}

/**
 * Registra el ingreso de un ingrediente al inventario.
 * @throws IllegalArgumentException si la cantidad es menor o igual a 0.
 */
fun restock(inventory: MutableMap<String, Int>, ingredient: String, quantity: Int) {
    require(quantity > 0) { "La cantidad a reabastecer debe ser mayor que 0" }
    inventory[ingredient] = (inventory[ingredient] ?: 0) + quantity
}

/** Retorna el repartidor disponible más cercano, o null si no hay ninguno disponible. */
fun assignNearestDeliveryPerson(deliveryPeople: List<DeliveryPerson>): DeliveryPerson? =
    deliveryPeople.sortedBy { it.distanceKm }.find { it.isAvailable }
