package model

import service.OrderTotals
import service.calculateTotals

/**
 * Pedido de un cliente. Equivale a la clase Pedido
 * La cantidad de pizzas se representa repitiendo el [Product] en [products].
 */
class Order(
    val id: Int,
    val customer: Customer,
    val products: List<Product>,
    val notes: String?
) {
    /** Estado actual; solo se modifica con [changeStatus]. */
    var status: OrderStatus = OrderStatus.IN_PREPARATION
        private set

    /** Repartidor asignado, o null si aún no tiene. */
    var deliveryPerson: DeliveryPerson? = null

    private fun totals(): OrderTotals = calculateTotals(calculateSubtotal(), products.size)

    /** Suma de los precios de todas las pizzas del pedido. */
    fun calculateSubtotal(): Double = products.sumOf { it.price }

    /** Descuento aplicado según la cantidad de pizzas. */
    fun calculateDiscount(): Double = totals().discount

    /** Impuesto (IVA) sobre el subtotal con descuento. */
    fun calculateTax(): Double = totals().tax

    /** Total final con descuento e impuesto. */
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
