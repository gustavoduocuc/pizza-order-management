package model

import service.normalizeText
import java.util.Objects

/**
 * Cliente que realiza un pedido. Equivale a la clase Cliente.
 *
 * Dos clientes son iguales (==) si tienen el mismo nombre normalizado y el mismo correo (sin distinguir
 * mayúsculas; "sin correo" también cuenta como valor). El teléfono y la dirección no participan.
 */
class Customer(
    name: String,
    phone: String,
    email: String?,
    val address: String
) : User(name, phone, email) {

    /** Agrega la dirección a la descripción del usuario. */
    override fun describe(): String = "${super.describe()} | Dirección: $address"

    /** Permite desestructurar Cliente: nombre. */
    operator fun component1(): String = name

    /** Permite desestructurar Cliente: correo, o null si no tiene. */
    operator fun component2(): String? = email

    /** Permite desestructurar Cliente: teléfono. */
    operator fun component3(): String = phone

    /** Igualdad por nombre normalizado y correo en minúsculas. */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Customer) return false
        return normalizeText(name) == normalizeText(other.name) && email?.lowercase() == other.email?.lowercase()
    }

    /** Consistente con [equals]: usa los mismos atributos normalizados. */
    override fun hashCode(): Int = Objects.hash(normalizeText(name), email?.lowercase())
}
