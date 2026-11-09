package model

import annotations.Promotional
import service.FEATURED_PIZZA_PROMOTION

/**
 * Pizza destacada del mes, elegible para promociones.
 */
@Promotional(FEATURED_PIZZA_PROMOTION)
class FeaturedPizza(name: String, price: Double, ingredients: Map<String, Int>) :
    Pizza(name, price, ingredients)
