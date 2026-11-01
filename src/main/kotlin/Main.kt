package main.kotlin

const val TELEFONO_POR_DEFECTO: String = "000000000"
const val CANTIDAD_POR_DEFECTO: Int = 1
const val PIZZAS_PARA_DESCUENTO: Int = 4
const val PORCENTAJE_DESCUENTO: Double = 0.10

fun formatearMonto(monto: Double): String = "$${"%.2f".format(monto)}"

fun calcularDescuento(subtotal: Double, cantidadPizzas: Int): Double =
    if (cantidadPizzas >= PIZZAS_PARA_DESCUENTO) subtotal * PORCENTAJE_DESCUENTO else 0.0

fun asignarRepartidor(idPedido: Int): String = when (idPedido % 3) {
    0 -> "Carlos"
    1 -> "Valentina"
    else -> "Matías"
}

fun main() {
    // R1. Datos fijos de la pizza y del pedido
    val nombrePizza: String = "Pepperoni"
    val precioUnitario: Double = 8990.0
    var ingredientesDisponibles: Int = 10
    val idPedido: Int = 1

    // R2. Registro del cliente
    println("=== Registro de cliente ===")

    print("Nombre: ")
    val nombreIngresado: String? = readlnOrNull()?.trim()
    val nombre: String = if (nombreIngresado.isNullOrEmpty()) {
        println("Aviso: no se ingresó nombre, se usará \"Sin nombre\".")
        "Sin nombre"
    } else {
        nombreIngresado
    }

    print("Dirección: ")
    val direccionIngresada: String? = readlnOrNull()?.trim()
    val direccion: String = if (direccionIngresada.isNullOrEmpty()) {
        println("Aviso: no se ingresó dirección, se usará \"Sin dirección\".")
        "Sin dirección"
    } else {
        direccionIngresada
    }

    print("Teléfono: ")
    val telefonoIngresado: String = readlnOrNull()?.trim() ?: ""
    val telefono: String = try {
        telefonoIngresado.toLong()
        telefonoIngresado
    } catch (e: NumberFormatException) {
        println("Advertencia: teléfono vacío o no numérico, se usará $TELEFONO_POR_DEFECTO.")
        TELEFONO_POR_DEFECTO
    }

    print("Cantidad de pizzas: ")
    val cantidadIngresada: Int? = try {
        readlnOrNull()?.trim()?.toInt()
    } catch (e: NumberFormatException) {
        null
    }
    val cantidadPizzas: Int = if (cantidadIngresada == null || cantidadIngresada <= 0) {
        println("Advertencia: cantidad inválida, se usará $CANTIDAD_POR_DEFECTO pizza.")
        CANTIDAD_POR_DEFECTO
    } else {
        cantidadIngresada
    }

    println()
    println("Cliente registrado:")
    println("  Nombre: $nombre")
    println("  Dirección: $direccion")
    println("  Teléfono: $telefono")

    // R3. Total con descuento
    val subtotal: Double = precioUnitario * cantidadPizzas.toDouble()
    val descuento: Double = calcularDescuento(subtotal, cantidadPizzas)
    val totalPedido: Double = subtotal - descuento

    println()
    println("Subtotal: ${formatearMonto(subtotal)}")
    println("Descuento: ${formatearMonto(descuento)}")
    println("Total: ${formatearMonto(totalPedido)}")

    // R4. Verificación de stock (1 unidad por pizza)
    println()
    var estadoPedido: String = if (ingredientesDisponibles >= cantidadPizzas) {
        ingredientesDisponibles -= cantidadPizzas
        println("Stock disponible: pedido confirmado. Stock restante: $ingredientesDisponibles")
        "Preparación"
    } else {
        println("Stock insuficiente: se pidieron $cantidadPizzas pizzas y solo hay $ingredientesDisponibles disponibles.")
        println("El pedido no puede completarse y fue cancelado.")
        "Cancelado"
    }

    // R5. Asignación de repartidor (solo pedidos confirmados)
    val repartidorAsignado: String = if (estadoPedido == "Cancelado") {
        "Sin asignar"
    } else {
        asignarRepartidor(idPedido)
    }
    if (estadoPedido == "Preparación") {
        estadoPedido = "En Camino"
    }

    // R6. Resumen del pedido
    println()
    println("==============================")
    println("      RESUMEN DEL PEDIDO      ")
    println("==============================")
    println("Cliente")
    println("  Nombre: $nombre")
    println("  Dirección: $direccion")
    println("  Teléfono: $telefono")
    println("------------------------------")
    println("Pizza")
    println("  Nombre: $nombrePizza")
    println("  Precio unitario: ${formatearMonto(precioUnitario)}")
    println("------------------------------")
    println("Pedido")
    println("  Id: $idPedido")
    println("  Cantidad: $cantidadPizzas")
    println("  Subtotal: ${formatearMonto(subtotal)}")
    println("  Descuento: ${formatearMonto(descuento)}")
    println("  Total: ${formatearMonto(totalPedido)}")
    println("  Estado: $estadoPedido")
    println("  Repartidor: $repartidorAsignado")
    println("==============================")
}
