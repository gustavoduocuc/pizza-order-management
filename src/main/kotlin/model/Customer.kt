package model

/**
 * Cliente que realiza un pedido. Equivale a la clase Cliente.
 */
class Customer(
    name: String,
    phone: String,
    email: String?,
    val address: String
) : User(name, phone, email) {

    /** Agrega la dirección a la descripción del usuario. */
    override fun describe(): String = "${super.describe()} | Dirección: $address"
}
