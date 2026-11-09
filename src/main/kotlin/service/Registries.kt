package service

import model.Customer
import model.Product

// Registros que evitan duplicados usando la igualdad (==) de clientes y productos.

/** Registro de clientes sin duplicados. */
class CustomerRegistry {
    private val customers = mutableSetOf<Customer>()

    /** Clientes registrados, en orden de registro (vista de solo lectura). */
    val all: Set<Customer> get() = customers

    /**
     * Registra al cliente y lo retorna. Si ya existe uno igual, avisa y retorna el existente
     * con sus datos originales, sin agregar otro.
     */
    fun register(customer: Customer): Customer {
        if (customers.add(customer)) {
            println("$CUSTOMER_REGISTERED_MESSAGE: ${customer.name}")
            return customer
        }
        val existing = customers.first { it == customer }
        println("$CUSTOMER_ALREADY_REGISTERED_MESSAGE: ${existing.name}")
        return existing
    }
}

/** Catálogo de productos sin duplicados. */
class ProductCatalog {
    private val products = mutableSetOf<Product>()

    /** Productos del catálogo, en orden de inserción (vista de solo lectura). */
    val all: Set<Product> get() = products

    /** Agrega el producto y retorna true; si ya existe uno igual, avisa y retorna false. */
    fun add(product: Product): Boolean {
        val added = products.add(product)
        if (!added) {
            println("$DUPLICATE_PRODUCT_MESSAGE: $product")
        }
        return added
    }
}
