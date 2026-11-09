package model

/**
 * Pizza del menú. Equivale a Pizza.
 */
open class Pizza(name: String, price: Double, ingredients: Map<String, Int>) :
    Product(name, price, ingredients, ProductCategory.PIZZA)
