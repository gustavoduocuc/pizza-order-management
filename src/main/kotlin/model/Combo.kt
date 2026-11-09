package model

import annotations.Promotional
import service.COMBO_PROMOTION

/**
 * Combo de varios productos con precio especial, elegible para promociones. Equivale a Combo.
 */
@Promotional(COMBO_PROMOTION)
class Combo(name: String, price: Double, ingredients: Map<String, Int>) :
    Product(name, price, ingredients, ProductCategory.COMBO)
