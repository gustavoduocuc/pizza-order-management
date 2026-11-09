package service

import model.Product
import java.time.LocalDate
import java.time.format.DateTimeParseException

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
 * Lee un dato con reintentos: muestra [prompt] y aplica [parse], que retorna null si la entrada es inválida.
 * Cada fallo muestra "Error: [errorMessage] (intento X de N)"; al agotar [MAX_INPUT_ATTEMPTS]
 * retorna lo que entregue [onExhausted], que es quien avisa al usuario.
 */
fun <T> readValidated(prompt: String, parse: (String?) -> T?, errorMessage: String, onExhausted: () -> T): T {
    for (attempt in 1..MAX_INPUT_ATTEMPTS) {
        val value = parse(readInput(prompt))
        if (value != null) {
            return value
        }
        println("Error: $errorMessage (intento $attempt de $MAX_INPUT_ATTEMPTS)")
    }
    return onExhausted()
}

/** Indica si el correo tiene el formato nombre@dominio.ext. */
fun isValidEmail(email: String): Boolean = EMAIL_REGEX.matches(email)

/**
 * Lee el correo (opcional). Vacío retorna null; si el formato es inválido lo pide de nuevo
 * y, al agotar los intentos, avisa y retorna [DEFAULT_EMAIL].
 * Internamente "" representa "sin correo", porque null significa "entrada inválida".
 */
fun readEmail(): String? {
    val email = readValidated(
        prompt = "Correo (opcional): ",
        parse = { input -> if (input.isNullOrEmpty()) "" else input.takeIf(::isValidEmail) },
        errorMessage = EMAIL_ERROR_MESSAGE,
        onExhausted = {
            println("Aviso: se agotaron los intentos, se usará el correo $DEFAULT_EMAIL.")
            DEFAULT_EMAIL
        }
    )
    return email.ifEmpty { null }
}

/**
 * Quita espacios, guiones, paréntesis, "+" y el prefijo "56", y valida que queden 9 dígitos
 * que empiecen con 9. Si es válido retorna "+56 (9XX) XXX-XXX"; si no, null.
 */
fun formatPhone(input: String?): String? {
    val digits = (input ?: "").replace(PHONE_SEPARATORS_REGEX, "").removePrefix(PHONE_COUNTRY_PREFIX)
    if (!PHONE_REGEX.matches(digits)) {
        return null
    }
    return "+$PHONE_COUNTRY_PREFIX (${digits.substring(0, 3)}) ${digits.substring(3, 6)}-${digits.substring(6)}"
}

/** Lee el teléfono con reintentos; al agotar los intentos avisa y retorna [DEFAULT_PHONE]. */
fun readPhone(): String = readValidated(
    prompt = "Teléfono: ",
    parse = ::formatPhone,
    errorMessage = PHONE_ERROR_MESSAGE,
    onExhausted = {
        println("Aviso: se agotaron los intentos, se usará el teléfono $DEFAULT_PHONE.")
        DEFAULT_PHONE
    }
)

/** Indica si la cantidad está dentro de [QUANTITY_RANGE]. */
fun isValidQuantity(quantity: Int): Boolean = quantity in QUANTITY_RANGE

/** Convierte el texto en un entero, o null si no es numérico. */
private fun parseInteger(input: String?): Int? = try {
    input?.toInt()
} catch (e: NumberFormatException) {
    null
}

/**
 * Lee una cantidad dentro de [QUANTITY_RANGE], mostrando [prompt]. Si se agotan los intentos,
 * avisa y retorna [DEFAULT_QUANTITY].
 */
fun readQuantity(prompt: String): Int = readValidated(
    prompt = prompt,
    parse = { input -> parseInteger(input)?.takeIf(::isValidQuantity) },
    errorMessage = QUANTITY_ERROR_MESSAGE,
    onExhausted = {
        println("Se agotaron los intentos, se usará la cantidad $DEFAULT_QUANTITY.")
        DEFAULT_QUANTITY
    }
)

/**
 * Lee el número del producto elegido del menú. Si se agotan los intentos,
 * avisa y retorna el primer producto.
 */
fun readMenuChoice(menu: List<Product>): Product = readValidated(
    prompt = "Elige el número de la pizza: ",
    parse = { input -> parseInteger(input)?.let { menu.getOrNull(it - 1) } },
    errorMessage = "elige un número entre 1 y ${menu.size}",
    onExhausted = {
        println("Se agotaron los intentos, se usará la pizza ${menu.first().name}.")
        menu.first()
    }
)

/** Indica si la fecha está dentro de [PROMOTION_PERIOD]. */
fun isWithinPromotionPeriod(date: LocalDate): Boolean = date in PROMOTION_PERIOD

/** Convierte AAAA-MM-DD en fecha (vacío es hoy), o null si el formato o la fecha no son válidos. */
private fun parseOrderDate(input: String?): LocalDate? {
    if (input.isNullOrEmpty()) {
        return LocalDate.now()
    }
    return try {
        LocalDate.parse(input)
    } catch (e: DateTimeParseException) {
        null
    }
}

/** Lee la fecha del pedido; vacío es hoy y, al agotar los intentos, avisa y usa hoy. */
fun readOrderDate(): LocalDate = readValidated(
    prompt = "Fecha del pedido (AAAA-MM-DD, Enter = hoy): ",
    parse = ::parseOrderDate,
    errorMessage = DATE_ERROR_MESSAGE,
    onExhausted = {
        println("Aviso: se agotaron los intentos, se usará la fecha de hoy.")
        LocalDate.now()
    }
)
