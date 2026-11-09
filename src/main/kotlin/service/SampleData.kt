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

// Datos fijos de la pizzería

/** Inventario inicial: ingrediente -> unidades disponibles. */
fun createInitialInventory(): MutableMap<String, Int> = mutableMapOf(
    "dough" to 20,
    "sauce" to 20,
    "cheese" to 20,
    "pepperoni" to 10,
    "vegetables" to 10
)

/** Menú de productos con los ingredientes que consume cada uno. */
fun createMenu(): List<Product> = listOf(
    Pizza("Pepperoni", 8990.0, mapOf("dough" to 1, "sauce" to 1, "cheese" to 2, "pepperoni" to 2)),
    FeaturedPizza("Cuatro Quesos", 9990.0, mapOf("dough" to 1, "sauce" to 1, "cheese" to 4)),
    Pizza("Vegetariana", 8490.0, mapOf("dough" to 1, "sauce" to 1, "cheese" to 1, "vegetables" to 3)),
    Combo("Combo Familiar", 19990.0, mapOf("dough" to 2, "sauce" to 2, "cheese" to 4, "pepperoni" to 4)),
    Beverage("Bebida 1.5L", 2490.0),
    ExtraIngredient("Pepperoni extra", 1500.0, mapOf("pepperoni" to 1))
)

/** Repartidores con distintas distancias y disponibilidad. */
fun createDeliveryPeople(): List<DeliveryPerson> = listOf(
    DeliveryPerson("Carlos", "911111111", null, 5.0, true),
    DeliveryPerson("Valentina", "922222222", null, 2.0, false),
    DeliveryPerson("Matías", "933333333", null, 3.0, true)
)

/** Tres pedidos del día ya registrados, con estados distintos. */
fun createPreloadedOrders(menu: List<Product>): List<Order> {
    val inPreparation = Order(
        1, Customer("Luis Rojas", "944444444", "luis@mail.com", "Av. Central 100"),
        listOf(menu[0], menu[0]), null
    )
    val ready = Order(
        2, Customer("Marta Silva", "955555555", null, "Calle Norte 25"),
        listOf(menu[1]), "Sin cebolla"
    )
    ready.changeStatus(OrderStatus.READY)
    val onTheWay = Order(
        3, Customer("Pedro Soto", "966666666", "pedro@mail.com", "Pasaje Sur 7"),
        listOf(menu[2], menu[2], menu[0]), null
    )
    onTheWay.deliveryPerson = DeliveryPerson("Rodrigo", "977777777", null, 1.5, false)
    onTheWay.changeStatus(OrderStatus.READY)
    onTheWay.changeStatus(OrderStatus.ON_THE_WAY)
    return listOf(inPreparation, ready, onTheWay)
}
