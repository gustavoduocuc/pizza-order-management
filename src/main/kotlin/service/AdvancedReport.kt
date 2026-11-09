package service

import model.Customer
import model.Order
import model.Product
import java.time.LocalDate

// Resumen integrado: demuestra en consola que las funcionalidades trabajan en conjunto.

private const val HEAVY_LINE = "=============================="
private const val LIGHT_LINE = "------------------------------"

/** Imprime el encabezado de una sección del resumen. */
private fun printSectionHeader(title: String) {
    println()
    println(LIGHT_LINE)
    println(title)
    println(LIGHT_LINE)
}

/** Imprime subtotal, descuentos, IVA y total de un pedido. */
private fun printOrderTotals(order: Order) {
    println("  Subtotal: ${formatAmount(order.calculateSubtotal())}")
    println("  Descuento por cantidad: ${formatAmount(order.calculateQuantityDiscount())}")
    println("  Descuento promoción: ${formatAmount(order.calculatePromotionDiscount())}")
    println("  IVA (${(TAX_RATE * 100).toInt()}%): ${formatAmount(order.calculateTax())}")
    println("  Total: ${formatAmount(order.calculateTotal())}")
}

/** Imprime el id, el cliente y los productos de un pedido en una línea. */
private fun printOrderDetail(title: String, order: Order) {
    println("$title (#${order.id}, ${order.customer.name}): ${describePizzas(order)}")
}

/**
 * Sección 1: combina el pedido precargado #1 con un pedido de demostración del mismo cliente
 * y luego intenta combinarlo con el pedido de otro cliente. Retorna el pedido combinado, o null si no se pudo.
 */
private fun printCombinedOrdersSection(menu: List<Product>, preloadedOrders: List<Order>): Order? {
    printSectionHeader(COMBINED_ORDERS_HEADER)
    val baseOrder = preloadedOrders.firstOrNull { it.id == COMBINATION_BASE_ORDER_ID }
    val otherCustomerOrder = preloadedOrders.firstOrNull { it.id == OTHER_CUSTOMER_ORDER_ID }
    if (baseOrder == null) {
        println("No está disponible el pedido #$COMBINATION_BASE_ORDER_ID para combinar.")
        return null
    }
    val demoOrder = createCombinationDemoOrder(menu)
    printOrderDetail("Pedido precargado", baseOrder)
    printOrderDetail("Pedido de demostración", demoOrder)
    val combined = combineOrders(baseOrder, demoOrder)
    if (combined != null) {
        println("Pedido combinado (#${combined.id}, ${combined.customer.name}): ${describePizzas(combined)}")
        printOrderTotals(combined)
    }
    if (otherCustomerOrder != null) {
        println("Intento con clientes distintos (#${baseOrder.id} + #${otherCustomerOrder.id}, " +
            "${otherCustomerOrder.customer.name}):")
        combineOrders(baseOrder, otherCustomerOrder)
    }
    return combined
}

/** Sección 2: muestra la desestructuración del cliente registrado y del pedido combinado. */
private fun printDestructuringSection(customer: Customer, combinedOrder: Order?) {
    printSectionHeader(DESTRUCTURING_HEADER)
    val (name, email, phone) = customer
    println("Cliente registrado por consola:")
    println("  Nombre: $name")
    println("  Correo: ${email ?: NO_EMAIL_LABEL}")
    println("  Teléfono: $phone")
    if (combinedOrder == null) {
        println("No hay pedido combinado para desestructurar.")
        return
    }
    val (orderCustomer, orderProducts, orderTotal) = combinedOrder
    println("Pedido combinado:")
    println("  Cliente: ${orderCustomer.name}")
    println("  Productos: ${orderProducts.size}")
    println("  Total: ${formatAmount(orderTotal)}")
}

/** Indica si la fecha está dentro o fuera del periodo de promoción. */
private fun periodLabel(date: LocalDate): String =
    if (isWithinPromotionPeriod(date)) WITHIN_PROMOTION_LABEL else OUTSIDE_PROMOTION_LABEL

/** Sección 3: productos promocionables del menú y promoción recibida por el pedido ingresado. */
private fun printPromotionalSection(menu: List<Product>, newOrder: Order?, orderDate: LocalDate) {
    printSectionHeader(PROMOTIONAL_PRODUCTS_HEADER)
    println("Detectados en el menú por la anotación:")
    for (product in findPromotionalProducts(menu)) {
        println("  ${product.name}: ${promotionDescription(product)}")
    }
    println("Periodo de promoción: ${PROMOTION_PERIOD.start} a ${PROMOTION_PERIOD.endInclusive}")
    println("Fecha del pedido ingresado: $orderDate (${periodLabel(orderDate)})")
    if (newOrder == null) {
        println(NO_ORDER_REGISTERED_MESSAGE)
        return
    }
    println("Descuento promoción aplicado: ${formatAmount(newOrder.calculatePromotionDiscount())}")
    if (findPromotionalProducts(newOrder.products).isEmpty()) {
        println(NO_PROMOTIONAL_PRODUCTS_MESSAGE)
    }
}

/** Sección 4: estructura de las clases principales del dominio. */
private fun printClassStructureSection() {
    printSectionHeader(CLASS_STRUCTURE_HEADER)
    printDomainClassStructures()
}

/** Muestra la igualdad de productos con == y ===, y el intento de agregar un duplicado a un catálogo. */
private fun printProductEquality(menu: List<Product>) {
    val (pizza, featuredPizza, extraIngredient) = createEqualityDemoProducts()
    val sameReference = pizza
    println("Productos:")
    println("  $pizza == $featuredPizza: ${pizza == featuredPizza}")
    println("  $pizza == $extraIngredient: ${pizza == extraIngredient}")
    println("  Instancias distintas con == verdadero: === da ${pizza === featuredPizza}")
    println("  Misma referencia: === da ${pizza === sameReference}")
    val catalog = ProductCatalog()
    menu.forEach { catalog.add(it) }
    val added = catalog.add(featuredPizza)
    println("  Agregar $featuredPizza al catálogo: ${if (added) "agregado" else "rechazado"}")
}

/** Muestra la igualdad de clientes y el intento de registrar un cliente duplicado. */
private fun printCustomerEquality(customerRegistry: CustomerRegistry) {
    val (customer, sameCustomerOtherFormat, otherEmailCustomer) = createEqualityDemoCustomers()
    println("Clientes:")
    println("  ${customer.name} (${customer.email}) == ${sameCustomerOtherFormat.name} " +
        "(${sameCustomerOtherFormat.email}): ${customer == sameCustomerOtherFormat}")
    println("  ${customer.name} (${customer.email}) == ${otherEmailCustomer.name} " +
        "(${otherEmailCustomer.email}): ${customer == otherEmailCustomer}")
    customerRegistry.register(sameCustomerOtherFormat)
    println("  Clientes únicos registrados: ${customerRegistry.all.size}")
}

/** Sección 5: igualdad de productos y clientes. */
private fun printEqualitySection(menu: List<Product>, customerRegistry: CustomerRegistry) {
    printSectionHeader(EQUALITY_HEADER)
    printProductEquality(menu)
    printCustomerEquality(customerRegistry)
}

/** Sección 6: las 4 validaciones sobre ejemplos fijos válidos e inválidos. */
private fun printInputValidationSection() {
    printSectionHeader(INPUT_VALIDATIONS_HEADER)
    for (phone in createPhoneExamples()) {
        val formatted = formatPhone(phone)
        println("  Teléfono \"$phone\" -> ${if (formatted != null) "$VALID_LABEL: $formatted" else INVALID_LABEL}")
    }
    for (email in createEmailExamples()) {
        println("  Correo \"$email\" -> ${if (isValidEmail(email)) VALID_LABEL else INVALID_LABEL}")
    }
    for (quantity in createQuantityExamples()) {
        val result = if (isValidQuantity(quantity)) {
            "válida"
        } else {
            "inválida (rango ${QUANTITY_RANGE.first} a ${QUANTITY_RANGE.last})"
        }
        println("  Cantidad $quantity -> $result")
    }
    for (date in createDateExamples()) {
        println("  Fecha $date -> ${periodLabel(date)}")
    }
}

/**
 * Imprime el resumen integrado: pedidos combinados, desestructuración, promocionables, estructura de clases,
 * igualdad y validaciones de entrada. [newOrder] es null si el pedido ingresado no se registró.
 */
fun printIntegratedSummary(
    menu: List<Product>,
    preloadedOrders: List<Order>,
    customerRegistry: CustomerRegistry,
    customer: Customer,
    newOrder: Order?,
    orderDate: LocalDate
) {
    println()
    println(HEAVY_LINE)
    println("   $INTEGRATED_SUMMARY_TITLE")
    println(HEAVY_LINE)
    val combinedOrder = printCombinedOrdersSection(menu, preloadedOrders)
    printDestructuringSection(customer, combinedOrder)
    printPromotionalSection(menu, newOrder, orderDate)
    printClassStructureSection()
    printEqualitySection(menu, customerRegistry)
    printInputValidationSection()
    println(HEAVY_LINE)
}
