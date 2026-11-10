package model

/**
 * Ingrediente adicional que se agrega a un pedido. Equivale a Ingrediente adicional.
 */
class ExtraIngredient(name: String, price: Double, ingredients: Map<String, Int>) :
    Product(name, price, ingredients, ProductCategory.EXTRA_INGREDIENT)
