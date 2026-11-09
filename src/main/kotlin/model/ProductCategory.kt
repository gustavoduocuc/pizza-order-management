package model

/**
 * Categorías de producto del menú.
 * [label] es el nombre en español que se muestra al usuario.
 */
enum class ProductCategory(val label: String) {
    PIZZA("Pizza"),
    BEVERAGE("Bebida"),
    EXTRA_INGREDIENT("Ingrediente adicional"),
    COMBO("Combo")
}
