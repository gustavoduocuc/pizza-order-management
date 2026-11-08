package model

/**
 * Estados posibles de un pedido.
 * Equivale al estado del pedido (estadoPedido)
 */
enum class OrderStatus(val label: String) {
    IN_PREPARATION("En preparación"),
    READY("Listo"),
    ON_THE_WAY("En camino")
}
