package service

import model.Product
import model.ProductCategory
import java.util.Locale

/** Resultado del cálculo de un pedido: subtotal, descuentos, impuesto y total. */
class OrderTotals(
    val subtotal: Double,
    val quantityDiscount: Double,
    val promotionDiscount: Double,
    val tax: Double,
    val total: Double
)

/**
 * Calcula descuentos, impuesto y total a partir del subtotal.
 * El descuento por cantidad aplica solo si [pizzaCount] supera [DISCOUNT_THRESHOLD] y se calcula sobre el subtotal.
 * El descuento por promoción es [PROMOTION_DISCOUNT_RATE] de [promotionalSubtotal], la suma de los productos
 * promocionables con derecho a promoción (0 fuera del periodo). Ambos descuentos parten de su base original
 * y el impuesto se calcula sobre el subtotal menos los dos descuentos.
 * @throws IllegalArgumentException si el subtotal es negativo.
 */
fun calculateTotals(subtotal: Double, pizzaCount: Int, promotionalSubtotal: Double): OrderTotals {
    require(subtotal >= 0.0) { "Costo de pedido no válido: $subtotal" }
    val quantityDiscount = if (pizzaCount > DISCOUNT_THRESHOLD) subtotal * DISCOUNT_RATE else 0.0
    val promotionDiscount = promotionalSubtotal * PROMOTION_DISCOUNT_RATE
    val taxableAmount = subtotal - quantityDiscount - promotionDiscount
    val tax = taxableAmount * TAX_RATE
    return OrderTotals(subtotal, quantityDiscount, promotionDiscount, tax, taxableAmount + tax)
}

/** Formatea un monto con 2 decimales y punto decimal, por ejemplo "$12345.00". */
fun formatAmount(value: Double): String = "$" + String.format(Locale.US, "%.2f", value)

/** Cuenta los productos de categoría Pizza; solo ellos cuentan para el descuento por cantidad. */
fun countPizzas(products: List<Product>): Int = products.count { it.category == ProductCategory.PIZZA }
