package service

import annotations.Promotional
import model.Product
import kotlin.reflect.full.findAnnotation

// Detección dinámica de productos promocionables mediante reflection.

/** Indica si la clase del producto está marcada con @Promotional. */
fun isPromotional(product: Product): Boolean = product::class.findAnnotation<Promotional>() != null

/** Retorna la descripción de la promoción, o null si el producto no es promocionable. */
fun promotionDescription(product: Product): String? =
    product::class.findAnnotation<Promotional>()?.description

/** Filtra los productos promocionables leyendo la anotación, sin listas fijas de nombres. */
fun findPromotionalProducts(products: List<Product>): List<Product> =
    products.filter(::isPromotional)
