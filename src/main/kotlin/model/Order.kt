package model

import service.OrderTotals
import service.calculateTotals
import service.countPizzas
import service.findPromotionalProducts
import service.isWithinPromotionPeriod
import java.time.LocalDate

/**
 * Pedido de un cliente. Equivale a la clase Pedido
 * La cantidad de pizzas se representa repitiendo el [Product] en [products].
 */
class Order(
    val id: Int,
    val customer: Customer,
    val products: List<Product>,
    val notes: String?,
    val date: LocalDate
) {
    /** Estado actual; solo se modifica con [changeStatus]. */
    var status: OrderStatus = OrderStatus.IN_PREPARATION
        private set

    /** Repartidor asignado, o null si aún no tiene. */
    var deliveryPerson: DeliveryPerson? = null

    private fun totals(): OrderTotals =
        calculateTotals(calculateSubtotal(), countPizzas(products), calculatePromotionalSubtotal())

    /** Suma de los productos promocionables, o 0 si la fecha del pedido está fuera del periodo de promoción. */
    private fun calculatePromotionalSubtotal(): Double =
        if (isWithinPromotionPeriod(date)) findPromotionalProducts(products).sumOf { it.price } else 0.0

    /** Suma de los precios de todos los productos del pedido. */
    fun calculateSubtotal(): Double = products.sumOf { it.price }

    /** Descuento aplicado según la cantidad de pizzas. */
    fun calculateQuantityDiscount(): Double = totals().quantityDiscount

    /** Descuento por promoción sobre los productos promocionables, si la fecha está en el periodo. */
    fun calculatePromotionDiscount(): Double = totals().promotionDiscount

    /** Impuesto (IVA) sobre el subtotal menos los descuentos. */
    fun calculateTax(): Double = totals().tax

    /** Total final con descuentos e impuesto. */
    fun calculateTotal(): Double = totals().total

    /**
     * Cambia el estado validando la transición:
     * solo IN_PREPARATION -> READY -> ON_THE_WAY.
     * @throws IllegalStateException si la transición no está permitida.
     */
    fun changeStatus(newStatus: OrderStatus) {
        val isAllowed = when (status) {
            OrderStatus.IN_PREPARATION -> newStatus == OrderStatus.READY
            OrderStatus.READY -> newStatus == OrderStatus.ON_THE_WAY
            OrderStatus.ON_THE_WAY -> false
        }
        if (!isAllowed) {
            throw IllegalStateException("Transición no permitida: ${status.label} -> ${newStatus.label}")
        }
        status = newStatus
    }
}
