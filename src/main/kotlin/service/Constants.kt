package service

import java.time.LocalDate

// Constantes de la aplicación, definidas en un solo lugar.

/** Tasa de impuesto (IVA) aplicada sobre el subtotal con descuento. */
const val TAX_RATE: Double = 0.19

/** Porcentaje de descuento aplicado a pedidos grandes. */
const val DISCOUNT_RATE: Double = 0.10

/** Cantidad de pizzas a partir de la cual (mayor que) se aplica el descuento. */
const val DISCOUNT_THRESHOLD: Int = 3

/** Teléfono asignado cuando el ingresado es inválido o está vacío. */
const val DEFAULT_PHONE: String = "+56 (000) 000-000"

/** Correo asignado cuando el ingresado tiene un formato inválido. */
const val DEFAULT_EMAIL: String = "correo@invalido.com"

/** Máximo de intentos para reingresar un dato inválido. */
const val MAX_INPUT_ATTEMPTS: Int = 3

/** Cantidad de pizzas usada cuando se agotan los intentos. */
const val DEFAULT_QUANTITY: Int = 1

/** Texto mostrado cuando un producto no consume ingredientes. */
const val NO_INGREDIENTS_LABEL: String = "sin ingredientes"

/** Descripción de la promoción de la pizza destacada. */
const val FEATURED_PIZZA_PROMOTION: String = "Pizza destacada del mes"

/** Descripción de la promoción del combo. */
const val COMBO_PROMOTION: String = "Combo familiar con precio especial"

/** Marca de los métodos que una clase hereda y no declara. */
const val INHERITED_MARKER: String = "(heredado)"

/** Encabezados del inspector de clases. */
const val CLASS_HEADER: String = "Clase"
const val ANNOTATIONS_HEADER: String = "Anotaciones"
const val PROPERTIES_HEADER: String = "Propiedades"
const val METHODS_HEADER: String = "Métodos"

/** Mensaje cuando el pedido no tiene productos promocionables. */
const val NO_PROMOTIONAL_PRODUCTS_MESSAGE: String = "El pedido no incluye productos promocionables."

/** Formato de correo: nombre@dominio.ext. */
val EMAIL_REGEX = Regex("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)*\\.[A-Za-z]{2,}$")

/** Celular chileno sin prefijo: 9 dígitos que empiezan con 9. */
val PHONE_REGEX = Regex("^9\\d{8}$")

/** Caracteres que se ignoran al leer un teléfono: espacios, guiones, paréntesis y "+". */
val PHONE_SEPARATORS_REGEX = Regex("[\\s\\-()+]")

/** Prefijo de país de Chile, sin el signo "+". */
const val PHONE_COUNTRY_PREFIX: String = "56"

/** Rango de cantidades permitidas por producto. */
val QUANTITY_RANGE: IntRange = 1..100

/** Periodo de promoción (ambos extremos incluidos). */
val PROMOTION_PERIOD: ClosedRange<LocalDate> = LocalDate.of(2026, 11, 1)..LocalDate.of(2026, 11, 30)

/** Porcentaje de descuento sobre los productos promocionables dentro del periodo de promoción. */
const val PROMOTION_DISCOUNT_RATE: Double = 0.15

/** Mensajes de error de validación (se muestran como "Error: <mensaje> (intento X de 3)"). */
const val EMAIL_ERROR_MESSAGE: String = "el correo debe tener el formato nombre@dominio.com"
const val PHONE_ERROR_MESSAGE: String = "el teléfono debe tener 9 dígitos y comenzar con 9 (ej. 9 1234 5678)"
const val DATE_ERROR_MESSAGE: String = "usa el formato AAAA-MM-DD"
val QUANTITY_ERROR_MESSAGE: String = "La cantidad debe estar entre ${QUANTITY_RANGE.first} y ${QUANTITY_RANGE.last}"

/** Textos de las líneas nuevas del resumen del pedido. */
const val WITHIN_PROMOTION_LABEL: String = "dentro del periodo de promoción"
const val OUTSIDE_PROMOTION_LABEL: String = "fuera del periodo de promoción"
