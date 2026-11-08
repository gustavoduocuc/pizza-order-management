package model

/**
 * Usuario del sistema. Equivale a la clase Usuario
 * El correo es opcional.
 */
open class User(val name: String, val phone: String, val email: String?) {

    /** Describe al usuario en una línea de texto. */
    open fun describe(): String = "$name | Tel: $phone | Correo: ${email ?: "Sin correo"}"
}
