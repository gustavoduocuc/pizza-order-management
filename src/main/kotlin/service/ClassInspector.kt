package service

import model.Customer
import model.Order
import model.Product
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties

// Inspección de la estructura de las clases en tiempo de ejecución (reflection).

/** Describe las propiedades públicas de la clase con su tipo, ordenadas por nombre. */
private fun describeProperties(kClass: KClass<*>): List<String> =
    kClass.memberProperties
        .filter { it.visibility == KVisibility.PUBLIC }
        .sortedBy { it.name }
        .map { "  ${it.name}: ${it.returnType}" }

/** Describe un método con sus parámetros, omitiendo el receptor. */
private fun describeFunction(function: KFunction<*>, declaredNames: Set<String?>): String {
    val parameters = function.parameters
        .filter { it.kind == KParameter.Kind.VALUE }
        .joinToString(", ") { "${it.name}: ${it.type}" }
    val inherited = if (function.name in declaredNames) "" else " $INHERITED_MARKER"
    return "  ${function.name}($parameters)$inherited"
}

/** Describe los métodos públicos de la clase, ordenados por nombre y marcando los heredados. */
private fun describeFunctions(kClass: KClass<*>): List<String> {
    val declaredNames = kClass.declaredMemberFunctions.map { it.name }.toSet()
    return kClass.memberFunctions
        .filter { it.visibility == KVisibility.PUBLIC }
        .sortedBy { it.name }
        .map { describeFunction(it, declaredNames) }
}

/** Describe las anotaciones de la clase, ordenadas por texto. */
private fun describeAnnotations(kClass: KClass<*>): List<String> =
    kClass.annotations.map { "  $it" }.sorted()

/** Retorna un texto con las propiedades, métodos y anotaciones de la clase. */
fun describeClassStructure(kClass: KClass<*>): String {
    val lines = mutableListOf("$CLASS_HEADER: ${kClass.simpleName}")
    val annotations = describeAnnotations(kClass)
    if (annotations.isNotEmpty()) {
        lines += "$ANNOTATIONS_HEADER:"
        lines += annotations
    }
    lines += "$PROPERTIES_HEADER:"
    lines += describeProperties(kClass)
    lines += "$METHODS_HEADER:"
    lines += describeFunctions(kClass)
    return lines.joinToString("\n")
}

/** Imprime la estructura de las clases principales del dominio: Order, Product y Customer. */
fun printDomainClassStructures() {
    for (kClass in listOf(Order::class, Product::class, Customer::class)) {
        println(describeClassStructure(kClass))
        println()
    }
}
