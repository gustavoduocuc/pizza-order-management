package model

import service.DIFFERENT_CUSTOMER_ERROR_MESSAGE
import service.NOTES_SEPARATOR
import service.NOT_IN_PREPARATION_ERROR_MESSAGE
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

    /**
     * Equivale a combinar pedidos con el operador +.
     * Crea un pedido nuevo con los productos de ambos, el id, el cliente y la fecha de este pedido (el izquierdo),
     * las notas no nulas unidas con [NOTES_SEPARATOR], en preparación y sin repartidor. Los originales no cambian
     * y los totales se recalculan a partir de los productos combinados.
     * @throws IllegalArgumentException si los pedidos son de clientes distintos (comparados con ==).
     * @throws IllegalStateException si alguno de los dos no está en preparación.
     */
    operator fun plus(other: Order): Order {
        require(customer == other.customer) { DIFFERENT_CUSTOMER_ERROR_MESSAGE }
        check(status == OrderStatus.IN_PREPARATION && other.status == OrderStatus.IN_PREPARATION) {
            NOT_IN_PREPARATION_ERROR_MESSAGE
        }
        return Order(id, customer, products + other.products, combineNotes(notes, other.notes), date)
    }

    /** Une las notas no nulas con [NOTES_SEPARATOR], o retorna null si ninguna existe. */
    private fun combineNotes(first: String?, second: String?): String? =
        listOfNotNull(first, second).joinToString(NOTES_SEPARATOR).ifEmpty { null }

    /** Permite desestructurar Pedido: cliente. */
    operator fun component1(): Customer = customer

    /** Permite desestructurar Pedido: productos. */
    operator fun component2(): List<Product> = products

    /** Permite desestructurar Pedido: total calculado. */
    operator fun component3(): Double = calculateTotal()
}
