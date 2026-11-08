package service

import java.util.Locale

/** Resultado del cálculo de un pedido: subtotal, descuento, impuesto y total. */
class OrderTotals(
    val subtotal: Double,
    val discount: Double,
    val tax: Double,
    val total: Double
)

/**
 * Calcula descuento, impuesto y total a partir del subtotal.
 * El descuento aplica solo si [pizzaCount] supera [DISCOUNT_THRESHOLD].
 * @throws IllegalArgumentException si el subtotal es negativo.
 */
fun calculateTotals(subtotal: Double, pizzaCount: Int): OrderTotals {
    require(subtotal >= 0.0) { "Costo de pedido no válido: $subtotal" }
    val discount = if (pizzaCount > DISCOUNT_THRESHOLD) subtotal * DISCOUNT_RATE else 0.0
    val tax = (subtotal - discount) * TAX_RATE
    return OrderTotals(subtotal, discount, tax, subtotal - discount + tax)
}

/** Formatea un monto con 2 decimales y punto decimal, por ejemplo "$12345.00". */
fun formatAmount(value: Double): String = "$" + String.format(Locale.US, "%.2f", value)
