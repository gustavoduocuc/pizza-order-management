package service

import model.Customer
import model.DeliveryPerson
import model.Order
import model.OrderStatus
import model.Beverage
import model.Combo
import model.ExtraIngredient
import model.FeaturedPizza
import model.Pizza
import model.Product
import java.time.LocalDate

// Datos fijos de la pizzería

/** Inventario inicial: ingrediente -> unidades disponibles. */
fun createInitialInventory(): MutableMap<String, Int> = mutableMapOf(
    "dough" to 20,
    "sauce" to 20,
    "cheese" to 20,
    "pepperoni" to 10,
    "vegetables" to 10
)

/** Menú de productos con los ingredientes que consume cada uno, cargado sin duplicados. */
fun createMenu(): List<Product> {
    val catalog = ProductCatalog()
    catalog.add(Pizza("Pepperoni", 8990.0, mapOf("dough" to 1, "sauce" to 1, "cheese" to 2, "pepperoni" to 2)))
    catalog.add(FeaturedPizza("Cuatro Quesos", 9990.0, mapOf("dough" to 1, "sauce" to 1, "cheese" to 4)))
    catalog.add(Pizza("Vegetariana", 8490.0, mapOf("dough" to 1, "sauce" to 1, "cheese" to 1, "vegetables" to 3)))
    catalog.add(Combo("Combo Familiar", 19990.0, mapOf("dough" to 2, "sauce" to 2, "cheese" to 4, "pepperoni" to 4)))
    catalog.add(Beverage("Bebida 1.5L", 2490.0))
    catalog.add(ExtraIngredient("Pepperoni extra", 1500.0, mapOf("pepperoni" to 1)))
    return catalog.all.toList()
}

/** Repartidores con distintas distancias y disponibilidad. */
fun createDeliveryPeople(): List<DeliveryPerson> = listOf(
    DeliveryPerson("Carlos", "+56 (911) 111-111", null, 5.0, true),
    DeliveryPerson("Valentina", "+56 (922) 222-222", null, 2.0, false),
    DeliveryPerson("Matías", "+56 (933) 333-333", null, 3.0, true)
)

/** Tres pedidos del día ya registrados, con estados distintos; sus clientes pasan por [customerRegistry]. */
fun createPreloadedOrders(menu: List<Product>, customerRegistry: CustomerRegistry): List<Order> {
    val inPreparation = Order(
        1, customerRegistry.register(Customer("Luis Rojas", "+56 (944) 444-444", "luis@mail.com", "Av. Central 100")),
        listOf(menu[0], menu[0]), null, LocalDate.of(2026, 11, 5)
    )
    val ready = Order(
        2, customerRegistry.register(Customer("Marta Silva", "+56 (955) 555-555", null, "Calle Norte 25")),
        listOf(menu[1]), "Sin cebolla", LocalDate.of(2026, 10, 20)
    )
    ready.changeStatus(OrderStatus.READY)
    val onTheWay = Order(
        3, customerRegistry.register(Customer("Pedro Soto", "+56 (966) 666-666", "pedro@mail.com", "Pasaje Sur 7")),
        listOf(menu[2], menu[2], menu[0]), null, LocalDate.of(2026, 11, 20)
    )
    onTheWay.deliveryPerson = DeliveryPerson("Rodrigo", "+56 (977) 777-777", null, 1.5, false)
    onTheWay.changeStatus(OrderStatus.READY)
    onTheWay.changeStatus(OrderStatus.ON_THE_WAY)
    return listOf(inPreparation, ready, onTheWay)
}

/**
 * Pedido de demostración de Luis Rojas con 2 Vegetariana, en preparación, para combinarlo con el pedido precargado #1.
 * El cliente es una instancia nueva (no pasa por el registro): es igual al del pedido #1 por nombre y correo.
 */
fun createCombinationDemoOrder(menu: List<Product>): Order = Order(
    COMBINATION_DEMO_ORDER_ID, Customer("Luis Rojas", "+56 (944) 444-444", "luis@mail.com", "Av. Central 100"),
    listOf(menu[2], menu[2]), null, LocalDate.of(2026, 11, 10)
)

/** Productos para demostrar la igualdad: pizza, pizza destacada con el mismo nombre e ingrediente adicional. */
fun createEqualityDemoProducts(): List<Product> = listOf(
    Pizza("Pepperoni", 8990.0, mapOf("dough" to 1, "sauce" to 1, "cheese" to 2, "pepperoni" to 2)),
    FeaturedPizza("pepperoni", 10990.0, mapOf("dough" to 1, "sauce" to 1, "cheese" to 2, "pepperoni" to 2)),
    ExtraIngredient("Pepperoni", 1500.0, mapOf("pepperoni" to 1))
)

/** Clientes para demostrar la igualdad: Luis, el mismo Luis con otro formato y Luis con otro correo. */
fun createEqualityDemoCustomers(): List<Customer> = listOf(
    Customer("Luis Rojas", "+56 (944) 444-444", "luis@mail.com", "Av. Central 100"),
    Customer("luis rojas", "+56 (911) 111-111", "LUIS@mail.com", "Otra calle 1"),
    Customer("Luis Rojas", "+56 (944) 444-444", "luis.rojas@mail.com", "Av. Central 100")
)

/** Teléfonos de ejemplo: válidos en distintos formatos e inválidos. */
fun createPhoneExamples(): List<String> =
    listOf("930815676", "+56 9 3081 5676", "56930815676", "12345", "812345678")

/** Correos de ejemplo: uno válido y uno inválido. */
fun createEmailExamples(): List<String> = listOf("juan@mail.com", "juan@")

/** Cantidades de ejemplo: los dos extremos válidos del rango y dos fuera de él. */
fun createQuantityExamples(): List<Int> = listOf(1, 100, 0, 101)

/** Fechas de ejemplo: los dos extremos del periodo de promoción y dos fuera de él. */
fun createDateExamples(): List<LocalDate> = listOf(
    LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30), LocalDate.of(2026, 10, 31), LocalDate.of(2026, 12, 5)
)
