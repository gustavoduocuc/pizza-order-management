package model

/**
 * Bebida del menú; no consume ingredientes. Equivale a Bebida.
 */
class Beverage(name: String, price: Double) :
    Product(name, price, emptyMap(), ProductCategory.BEVERAGE)
