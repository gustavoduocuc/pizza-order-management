package service

import model.Product

private val PHONE_REGEX = Regex("^\\d{9}$")
private val EMAIL_REGEX = Regex("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)*\\.[A-Za-z]{2,}$")

/** Muestra [prompt] y lee una línea de la consola, o null si no hay entrada. */
fun readInput(prompt: String): String? {
    print(prompt)
    return readlnOrNull()?.trim()
}

/** Lee un texto; si viene nulo o vacío, avisa y retorna [defaultValue]. */
fun readTextOrDefault(prompt: String, fieldName: String, defaultValue: String): String {
    val input = readInput(prompt)
    return if (input.isNullOrEmpty()) {
        println("Aviso: no se ingresó $fieldName, se usará \"$defaultValue\".")
        defaultValue
    } else {
        input
    }
}

/**
 * Quita espacios y el prefijo "+56" y valida que queden 9 dígitos.
 * Si es inválido o vacío, avisa y retorna [DEFAULT_PHONE].
 */
fun validatePhone(input: String?): String {
    val cleaned = (input ?: "").replace(" ", "").removePrefix("+56")
    return if (PHONE_REGEX.matches(cleaned)) {
        cleaned
    } else {
        println("Advertencia: teléfono vacío o inválido, se usará $DEFAULT_PHONE.")
        DEFAULT_PHONE
    }
}

/**
 * Valida el correo. Vacío retorna null (cliente sin correo);
 * con formato inválido avisa y retorna [DEFAULT_EMAIL].
 */
fun validateEmail(input: String?): String? {
    val email = input?.trim()
    return when {
        email.isNullOrEmpty() -> null
        EMAIL_REGEX.matches(email) -> email
        else -> {
            println("Advertencia: correo con formato inválido, se usará $DEFAULT_EMAIL.")
            DEFAULT_EMAIL
        }
    }
}

/**
 * Lee la cantidad de pizzas, reintentando hasta [MAX_INPUT_ATTEMPTS] veces
 * si no es un entero mayor que 0. Si se agotan, retorna [DEFAULT_QUANTITY].
 */
fun readQuantity(): Int {
    var attempts = 0
    while (attempts < MAX_INPUT_ATTEMPTS) {
        val quantity = try {
            readInput("Cantidad de pizzas: ")?.toInt()
        } catch (e: NumberFormatException) {
            null
        }
        if (quantity != null && quantity > 0) {
            return quantity
        }
        attempts++
        println("Error: ingresa un número entero mayor que 0 (intento $attempts de $MAX_INPUT_ATTEMPTS).")
    }
    println("Se agotaron los intentos, se usará la cantidad $DEFAULT_QUANTITY.")
    return DEFAULT_QUANTITY
}

/**
 * Lee el número de la pizza elegida del menú, reintentando hasta
 * [MAX_INPUT_ATTEMPTS] veces. Si se agotan, retorna la primera pizza.
 */
fun readMenuChoice(menu: List<Product>): Product {
    var attempts = 0
    while (attempts < MAX_INPUT_ATTEMPTS) {
        val choice = try {
            readInput("Elige el número de la pizza: ")?.toInt()
        } catch (e: NumberFormatException) {
            null
        }
        val selected: Product? = choice?.let { number ->
            when (number) {
                in 1..menu.size -> menu[number - 1]
                else -> null
            }
        }
        if (selected != null) {
            return selected
        }
        attempts++
        println("Error: elige un número entre 1 y ${menu.size} (intento $attempts de $MAX_INPUT_ATTEMPTS).")
    }
    println("Se agotaron los intentos, se usará la pizza ${menu.first().name}.")
    return menu.first()
}
