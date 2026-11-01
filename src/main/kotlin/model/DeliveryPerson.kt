package model

/**
 * Repartidor de pedidos. Equivale a la clase Repartidor.
 */
class DeliveryPerson(
    name: String,
    phone: String,
    email: String?,
    val distanceKm: Double,
    var isAvailable: Boolean
) : User(name, phone, email) {

    /** Agrega la distancia y la disponibilidad a la descripción del usuario. */
    override fun describe(): String {
        val availability = if (isAvailable) "disponible" else "no disponible"
        return "${super.describe()} | Distancia: $distanceKm km | $availability"
    }

    /**
     * Lleva el pedido al cliente: lo pasa a ON_THE_WAY, marca al repartidor
     * como no disponible e imprime un mensaje.
     * Equivale a entregarPedido() del enunciado.
     * @throws IllegalStateException si el pedido no está listo para salir.
     */
    fun deliverOrder(order: Order) {
        order.changeStatus(OrderStatus.ON_THE_WAY)
        isAvailable = false
        println("$name lleva el pedido #${order.id} a ${order.customer.address}.")
    }
}
