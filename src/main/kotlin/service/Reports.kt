package service

import model.Order
import model.OrderStatus
import model.Product

private const val SEPARATOR = "=============================="

/** Muestra el inventario con las etiquetas de los ingredientes en español. */
fun printInventory(inventory: Map<String, Int>) {
    println("Inventario:")
    for ((ingredient, quantity) in inventory) {
        println("  ${ingredientLabel(ingredient)}: $quantity")
    }
}

/** Muestra el menú numerado usando describeProduct(). */
fun printMenu(menu: List<Product>) {
    println("Menú:")
    for ((index, product) in menu.withIndex()) {
        println("  ${index + 1}. ${product.describeProduct()}")
    }
}

/** Muestra los productos promocionables, sin repetir por nombre, o avisa si no hay ninguno. */
fun printPromotionalProducts(products: List<Product>) {
    val promotional = findPromotionalProducts(products).distinctBy { it.name }
    if (promotional.isEmpty()) {
        println(NO_PROMOTIONAL_PRODUCTS_MESSAGE)
        return
    }
    println("Productos promocionables del pedido:")
    for (product in promotional) {
        println("  ${product.name}: ${promotionDescription(product)}")
    }
}

/** Informa los ingredientes faltantes y los pasos para reabastecer. */
fun printRejectedOrder(missing: Map<String, Int>) {
    println("Pedido rechazado: no hay ingredientes suficientes.")
    val missingIngredients: Set<String> = missing.keys
    for (ingredient in missingIngredients) {
        println("  ${ingredientLabel(ingredient)}: faltan ${missing[ingredient]} unidades")
    }
    println("Pasos para reabastecer:")
    println("  1) Solicitar al proveedor la cantidad faltante de: " +
        missingIngredients.joinToString(", ") { ingredientLabel(it) })
    println("  2) Registrar el ingreso en el inventario.")
    println("  3) Reintentar el pedido.")
}

/** Notifica al cliente solo si tiene correo. */
fun notifyCustomer(order: Order) {
    order.customer.email?.let {
        println("Notificación enviada a $it: tu pedido #${order.id} está ${order.status.label}")
    }
}

/** Agrupa las pizzas por nombre, por ejemplo "Pepperoni x4". */
private fun describePizzas(order: Order): String =
    order.products.groupBy { it.name }.entries.joinToString(", ") { (name, items) -> "$name x${items.size}" }

/** Indica si la fecha del pedido está dentro o fuera del periodo de promoción. */
private fun promotionPeriodLabel(order: Order): String =
    if (isWithinPromotionPeriod(order.date)) WITHIN_PROMOTION_LABEL else OUTSIDE_PROMOTION_LABEL

/** Imprime el resumen de un pedido: cliente, productos, totales, estado, repartidor y notas. */
fun printOrderSummary(order: Order) {
    println(SEPARATOR)
    println("   RESUMEN DEL PEDIDO #${order.id}")
    println(SEPARATOR)
    println("Fecha: ${order.date} (${promotionPeriodLabel(order)})")
    println("------------------------------")
    println("Cliente")
    println("  Nombre: ${order.customer.name}")
    println("  Dirección: ${order.customer.address}")
    println("  Teléfono: ${order.customer.phone}")
    println("  Correo: ${order.customer.email ?: "Sin correo"}")
    println("------------------------------")
    println("Productos")
    for ((name, items) in order.products.groupBy { it.name }) {
        println("  $name - ${formatAmount(items.first().price)} x ${items.size}")
    }
    println("------------------------------")
    println("Totales")
    println("  Subtotal: ${formatAmount(order.calculateSubtotal())}")
    println("  Descuento por cantidad: ${formatAmount(order.calculateQuantityDiscount())}")
    println("  Descuento promoción: ${formatAmount(order.calculatePromotionDiscount())}")
    println("  IVA (${(TAX_RATE * 100).toInt()}%): ${formatAmount(order.calculateTax())}")
    println("  Total: ${formatAmount(order.calculateTotal())}")
    println("------------------------------")
    println("Estado: ${order.status.label}")
    println("Repartidor: ${order.deliveryPerson?.name ?: "Sin asignar"}")
    println("Notas: ${order.notes ?: "Sin notas"}")
    println(SEPARATOR)
}

/** Imprime el reporte del día con los pedidos pendientes y completados. */
fun printDailyReport(orders: Array<Order>) {
    println(SEPARATOR)
    println("   REPORTE DE PEDIDOS DEL DÍA")
    println(SEPARATOR)
    for (order in orders) {
        println("Pedido #${order.id} | Cliente: ${order.customer.name} | Pizzas: ${describePizzas(order)} " +
            "| Total: ${formatAmount(order.calculateTotal())} | Estado: ${order.status.label}")
    }
    val pendingOrders = orders.filter { it.status == OrderStatus.IN_PREPARATION }
    val completedOrders = orders.filter { it.status == OrderStatus.READY || it.status == OrderStatus.ON_THE_WAY }
    println("------------------------------")
    println("Pendientes (${pendingOrders.size}): " + pendingOrders.joinToString(", ") { "#${it.id}" })
    println("Completados (${completedOrders.size}): " + completedOrders.joinToString(", ") { "#${it.id}" })
    println(SEPARATOR)
}
