import model.Customer
import model.DeliveryPerson
import model.Order
import model.OrderStatus
import model.Product
import service.assignNearestDeliveryPerson
import service.calculateRequiredIngredients
import service.createDeliveryPeople
import service.createInitialInventory
import service.createMenu
import service.createPreloadedOrders
import service.deductFromInventory
import service.findMissingIngredients
import service.notifyCustomer
import service.printDailyReport
import service.printInventory
import service.printMenu
import service.printOrderSummary
import service.printPromotionalProducts
import service.printRejectedOrder
import service.readInput
import service.readMenuChoice
import service.readQuantity
import service.readTextOrDefault
import service.restock
import service.validateEmail
import service.validatePhone

/** Registra al cliente por consola aplicando valores por defecto y validaciones. */
private fun registerCustomer(): Customer {
    println("=== Registro de cliente ===")
    val name = readTextOrDefault("Nombre: ", "nombre", "Sin nombre")
    val address = readTextOrDefault("Dirección: ", "dirección", "Sin dirección")
    val phone = validatePhone(readInput("Teléfono: "))
    val email = validateEmail(readInput("Correo (opcional): "))
    val customer = Customer(name, phone, email, address)
    println("Cliente registrado: ${customer.describe()}")
    return customer
}

/**
 * Acepta el pedido: lo crea, lo deja listo, descuenta el inventario,
 * asigna el repartidor más cercano y notifica al cliente.
 * Retorna null si ocurre un error controlado.
 */
private fun acceptOrder(
    id: Int,
    customer: Customer,
    products: List<Product>,
    notes: String?,
    required: Map<String, Int>,
    inventory: MutableMap<String, Int>,
    deliveryPeople: List<DeliveryPerson>
): Order? = try {
    val order = Order(id, customer, products, notes)
    order.calculateTotal()
    order.changeStatus(OrderStatus.READY)
    deductFromInventory(required, inventory)
    println("Pedido confirmado.")
    printPromotionalProducts(products)
    val deliveryPerson = assignNearestDeliveryPerson(deliveryPeople)
    if (deliveryPerson != null) {
        order.deliveryPerson = deliveryPerson
        deliveryPerson.deliverOrder(order)
    } else {
        println("No hay repartidores disponibles: el pedido queda en estado ${order.status.label}.")
    }
    notifyCustomer(order)
    order
} catch (e: IllegalArgumentException) {
    println("Error: ${e.message}")
    null
} catch (e: IllegalStateException) {
    println("Error: ${e.message}")
    null
}

fun main() {
    val inventory = createInitialInventory()
    val menu = createMenu()
    val deliveryPeople = createDeliveryPeople()
    val preloadedOrders = createPreloadedOrders(menu)

    // 1. Inventario y menú
    printInventory(inventory)
    println()
    printMenu(menu)
    println()

    // 2. Registro del cliente
    val customer = registerCustomer()
    val notes = readInput("Notas del pedido (opcional): ")?.ifEmpty { null }

    // 3. Pizza y cantidad
    val product = readMenuChoice(menu)
    val quantity = readQuantity()
    val products = List(quantity) { product }

    // 4. Verificación de stock y aceptación del pedido
    println()
    val required = calculateRequiredIngredients(products)
    val missing = findMissingIngredients(required, inventory)
    val newOrder: Order? = when {
        missing.isEmpty() -> acceptOrder(
            preloadedOrders.size + 1, customer, products, notes, required, inventory, deliveryPeople
        )
        else -> {
            printRejectedOrder(missing)
            null
        }
    }

    // 5. Error controlado de inventario
    println()
    println("Demostración de error controlado de inventario:")
    try {
        restock(inventory, "cheese", -5)
    } catch (e: IllegalArgumentException) {
        println("Error: ${e.message}")
    }

    // 6. Resumen de cada pedido y reporte del día
    println()
    val dailyOrders: Array<Order> = (preloadedOrders + listOfNotNull(newOrder)).toTypedArray()
    for (order in dailyOrders) {
        printOrderSummary(order)
    }
    printDailyReport(dailyOrders)
}
