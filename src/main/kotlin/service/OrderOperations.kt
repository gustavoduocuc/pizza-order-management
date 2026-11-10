package service

import model.Order

/**
 * Combina dos pedidos con el operador +. Si no se pueden combinar, muestra "Error: <mensaje>"
 * y retorna null sin terminar el programa.
 */
fun combineOrders(first: Order, second: Order): Order? = try {
    first + second
} catch (e: IllegalArgumentException) {
    println("Error: ${e.message}")
    null
} catch (e: IllegalStateException) {
    println("Error: ${e.message}")
    null
}
