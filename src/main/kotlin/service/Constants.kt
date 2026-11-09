package service

// Constantes de la aplicación, definidas en un solo lugar.

/** Tasa de impuesto (IVA) aplicada sobre el subtotal con descuento. */
const val TAX_RATE: Double = 0.19

/** Porcentaje de descuento aplicado a pedidos grandes. */
const val DISCOUNT_RATE: Double = 0.10

/** Cantidad de pizzas a partir de la cual (mayor que) se aplica el descuento. */
const val DISCOUNT_THRESHOLD: Int = 3

/** Teléfono asignado cuando el ingresado es inválido o está vacío. */
const val DEFAULT_PHONE: String = "(000) 000-0000"

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
