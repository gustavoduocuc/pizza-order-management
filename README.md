# Gestión de pedidos de una pizzería

Aplicación de consola escrita en Kotlin para gestionar los pedidos de una pizzería: muestra el inventario y el menú, registra a un cliente, valida los datos que se ingresan, calcula descuentos e IVA, verifica el stock, asigna un repartidor y entrega resúmenes del día. Es un proyecto de la asignatura Desarrollo APP Móviles I (PMY2201).

El código está en inglés y los textos de consola, comentarios y documentación están en español. La sección 8 relaciona ambos nombres.

## 1. Requisitos

| Requisito | Versión |
|---|---|
| JDK | 11 o superior |
| IntelliJ IDEA | cualquier versión reciente (Community o Ultimate) |
| Kotlin | 2.4.20 (`kotlin-stdlib`) |
| `kotlin-reflect` | 2.4.20 |

El proyecto no usa Gradle ni Maven: es un módulo de IntelliJ (`order-management.iml`) cuyas bibliotecas están declaradas en `.idea/libraries/`. IntelliJ descarga ambas bibliotecas desde Maven la primera vez que se abre el proyecto.

El código fuente se compila con `languageVersion` y `apiVersion` 2.4 y `jvmTarget` 1.8 (ver `.idea/kotlinc.xml`), por lo que funciona con cualquier JDK 11 o superior.

### Verificar que `kotlin-reflect` está agregado

`kotlin-reflect` se usa en `service/Promotions.kt` y `service/ClassInspector.kt`. Sin la biblioteca el proyecto no compila.

1. Abre `File > Project Structure` (atajo `Cmd+;` en macOS, `Ctrl+Alt+Shift+S` en Windows y Linux).
2. En `Modules`, elige `order-management` y abre la pestaña `Dependencies`.
3. Comprueba que aparecen `KotlinJavaRuntime` y `KotlinReflect`.

Si falta `KotlinReflect`, agrégala a mano:

1. En la misma pestaña `Dependencies`, pulsa `+` y elige `Library > From Maven...`.
2. Escribe `org.jetbrains.kotlin:kotlin-reflect:2.4.20` y pulsa `OK`.
3. Deja marcada la opción de descargar las fuentes si quieres y acepta los diálogos con `OK`.

### Verificar el JDK del proyecto

El archivo `.idea/misc.xml` guarda el nombre del JDK de quien creó el proyecto (`homebrew-26`). Si en tu equipo ese nombre no existe, IntelliJ muestra el JDK como no definido:

1. Abre `File > Project Structure > Project`.
2. En `SDK`, elige un JDK 11 o superior instalado en tu equipo (o usa `Add SDK > Download JDK`).

## 2. Cómo ejecutar

1. Abre IntelliJ IDEA y elige `File > Open...`; selecciona la carpeta raíz del repositorio (la que contiene `order-management.iml`).
2. Espera a que IntelliJ termine de indexar y de descargar las bibliotecas.
3. Verifica `kotlin-reflect` y el JDK como se explica en la sección 1.
4. Abre `src/main/kotlin/Main.kt` y pulsa el botón verde junto a `fun main()`, o usa `Run > Run 'MainKt'`.
5. Escribe las respuestas en la ventana `Run` de IntelliJ, una por línea, y pulsa `Enter` después de cada una.

### Datos que pide la consola

El programa pide, en este orden, ocho datos:

| N.º | Prompt en consola | Qué se espera | Si es inválido o está vacío |
|---|---|---|---|
| 1 | `Nombre:` | texto | usa `Sin nombre` |
| 2 | `Dirección:` | texto | usa `Sin dirección` |
| 3 | `Teléfono:` | celular chileno de 9 dígitos que empieza con 9 | 3 intentos y luego `+56 (000) 000-000` |
| 4 | `Correo (opcional):` | `nombre@dominio.ext`, o vacío | 3 intentos y luego `correo@invalido.com`; vacío significa sin correo |
| 5 | `Notas del pedido (opcional):` | texto libre | vacío significa sin notas |
| 6 | `Fecha del pedido (AAAA-MM-DD, Enter = hoy):` | fecha con ese formato | vacío usa la fecha de hoy; 3 intentos y luego hoy |
| 7 | `Elige el número de la pizza:` | número del menú (1 a 6) | 3 intentos y luego el primer producto del menú |
| 8 | `Cantidad de pizzas:` | entero entre 1 y 100 | 3 intentos y luego 1 |

Ejemplo de entrada (cada línea es una respuesta):

```text
Ana Pérez
Av. Siempre Viva 742
9 1234 5678
ana@mail.com
Sin cebolla
2026-11-10
2
4
```

Con esa entrada se registra a Ana Pérez con el teléfono `+56 (912) 345-678`, se pide el producto 2 (Cuatro Quesos) en cantidad 4 con fecha dentro del periodo de promoción, y se crea el pedido #4 (los pedidos #1 a #3 vienen precargados).

## 3. Estructura del proyecto

```text
order-management/
├── order-management.iml          Módulo de IntelliJ (fuentes y bibliotecas)
├── .idea/libraries/              Bibliotecas de proyecto: kotlin-stdlib y kotlin-reflect 2.4.20
└── src/main/kotlin/
    ├── Main.kt
    ├── annotations/
    │   └── Promotional.kt
    ├── model/
    │   ├── Beverage.kt
    │   ├── Combo.kt
    │   ├── Customer.kt
    │   ├── DeliveryPerson.kt
    │   ├── ExtraIngredient.kt
    │   ├── FeaturedPizza.kt
    │   ├── Order.kt
    │   ├── OrderStatus.kt
    │   ├── Pizza.kt
    │   ├── Product.kt
    │   ├── ProductCategory.kt
    │   └── User.kt
    └── service/
        ├── AdvancedReport.kt
        ├── Calculations.kt
        ├── ClassInspector.kt
        ├── Constants.kt
        ├── Inventory.kt
        ├── OrderOperations.kt
        ├── Promotions.kt
        ├── Registries.kt
        ├── Reports.kt
        ├── SampleData.kt
        ├── TextUtils.kt
        └── Validations.kt
```

### Punto de entrada

| Archivo | Descripción |
|---|---|
| `Main.kt` | `main()` solo orquesta el flujo; incluye `registerCustomer` (registro por consola) y `acceptOrder` (crea el pedido, descuenta el inventario, asigna repartidor y notifica). |

### `annotations/`

| Archivo | Descripción |
|---|---|
| `Promotional.kt` | Anotación `@Promotional(description)` con retención en tiempo de ejecución; marca las clases de producto elegibles para promoción. |

### `model/`

| Archivo | Descripción |
|---|---|
| `User.kt` | Clase abierta `User` con nombre, teléfono y correo opcional; base de `Customer` y `DeliveryPerson`. |
| `Customer.kt` | `Customer`: usuario con dirección; igualdad por nombre y correo normalizados y `component1..3` manuales. |
| `DeliveryPerson.kt` | `DeliveryPerson`: usuario con distancia y disponibilidad; `deliverOrder` lleva el pedido al cliente. |
| `Product.kt` | Clase abstracta `Product` con nombre, precio, ingredientes y categoría; `describeProduct()` e igualdad por nombre y categoría. |
| `ProductCategory.kt` | Enumeración con las categorías `PIZZA`, `BEVERAGE`, `EXTRA_INGREDIENT` y `COMBO`. |
| `Pizza.kt` | `Pizza`: producto de categoría pizza (clase abierta). |
| `FeaturedPizza.kt` | `FeaturedPizza`: pizza destacada del mes, anotada con `@Promotional`. |
| `Beverage.kt` | `Beverage`: bebida; no consume ingredientes. |
| `ExtraIngredient.kt` | `ExtraIngredient`: ingrediente adicional que se agrega a un pedido. |
| `Combo.kt` | `Combo`: combo familiar con precio especial, anotado con `@Promotional`. |
| `Order.kt` | `Order`: pedido con productos, fecha, estado y repartidor; cálculo de totales, `changeStatus`, operador `plus` y `component1..3`. |
| `OrderStatus.kt` | Enumeración `IN_PREPARATION`, `READY` y `ON_THE_WAY`. |

### `service/`

| Archivo | Descripción |
|---|---|
| `Constants.kt` | Todas las constantes: tasas, umbrales, expresiones regulares, rangos, valores por defecto y textos. |
| `Validations.kt` | Lectura de consola y validación: `readInput`, `readValidated` y las funciones de correo, teléfono, cantidad, menú y fecha. |
| `Calculations.kt` | `calculateTotals` (descuentos, IVA y total), `formatAmount` y `countPizzas`. |
| `Inventory.kt` | Ingredientes requeridos y faltantes, descuento y reabastecimiento del inventario, y asignación del repartidor más cercano. |
| `Promotions.kt` | Detección de productos promocionables leyendo `@Promotional` con reflection. |
| `ClassInspector.kt` | Descripción por reflection de las propiedades, métodos y anotaciones de una clase. |
| `Registries.kt` | `CustomerRegistry` y `ProductCatalog`: registros sin duplicados que usan `==`. |
| `OrderOperations.kt` | `combineOrders`: combina dos pedidos con `+` y muestra el error si no se pueden combinar. |
| `Reports.kt` | Impresión del inventario, menú, resumen de pedido, reporte del día, pedido rechazado y notificación. |
| `AdvancedReport.kt` | `printIntegratedSummary`: resumen integrado de seis secciones. |
| `SampleData.kt` | Datos fijos: inventario, menú, repartidores, pedidos precargados y ejemplos de las demostraciones. |
| `TextUtils.kt` | `normalizeText`: recorta espacios y pasa a minúsculas para comparar textos. |

## 4. Funcionamiento

`main()` ejecuta estos pasos, en orden:

1. **Preparación.** Crea el inventario, el menú (sin productos duplicados), los tres repartidores, el registro de clientes y tres pedidos precargados (#1 en preparación, #2 listo, #3 en camino). Registrar sus clientes imprime `Cliente registrado: ...`.
2. **Inventario y menú.** Imprime el inventario y el menú numerado; los productos anotados con `@Promotional` llevan la marca `Promocionable: <descripción>`.
3. **Registro del cliente.** Pide nombre, dirección, teléfono y correo, y registra al cliente. Si ya existe uno igual (mismo nombre y correo), reutiliza el existente. Luego pide las notas y la fecha del pedido.
4. **Pizza y cantidad.** Pide el número del producto y la cantidad; el pedido repite el producto tantas veces como la cantidad.
5. **Stock y aceptación.** Calcula los ingredientes necesarios y busca los faltantes.
   - Si alcanzan: crea el pedido, lo pasa a `READY`, descuenta el inventario, imprime `Pedido confirmado.` y los productos promocionables del pedido, asigna al repartidor disponible más cercano (el pedido pasa a `ON_THE_WAY`) y notifica al cliente si tiene correo. Si no hay repartidores disponibles, el pedido queda en `READY`.
   - Si no alcanzan: imprime `Pedido rechazado: no hay ingredientes suficientes.`, lo que falta de cada ingrediente y los pasos para reabastecer. El programa sigue con los pasos siguientes sin ese pedido.
6. **Error controlado de inventario.** Intenta reabastecer una cantidad negativa y muestra `Error: La cantidad a reabastecer debe ser mayor que 0`.
7. **Resúmenes.** Imprime el resumen de cada pedido (los precargados y el nuevo, si se aceptó) y el reporte de pedidos del día con pendientes y completados.
8. **Resumen integrado.** Imprime seis secciones: pedidos combinados con `+`, cliente desestructurado, productos promocionables, estructura de clases por reflection, validación de igualdad y validaciones de entrada con ejemplos fijos.

## 5. Reglas de negocio

Los valores vienen de `service/Constants.kt`.

| Regla | Valor | Constante |
|---|---|---|
| IVA | 19 %, sobre el subtotal menos los descuentos | `TAX_RATE` = 0.19 |
| Descuento por cantidad | 10 % del subtotal | `DISCOUNT_RATE` = 0.10 |
| Umbral del descuento por cantidad | más de 3 pizzas (solo cuentan los productos de categoría pizza) | `DISCOUNT_THRESHOLD` = 3 |
| Periodo de promoción | del 2026-11-01 al 2026-11-30, ambos incluidos | `PROMOTION_PERIOD` |
| Descuento de promoción | 15 % de la suma de los productos promocionables | `PROMOTION_DISCOUNT_RATE` = 0.15 |
| Rango de cantidades | 1 a 100 | `QUANTITY_RANGE` = 1..100 |
| Intentos máximos | 3 por dato | `MAX_INPUT_ATTEMPTS` = 3 |
| Cantidad por defecto | 1 | `DEFAULT_QUANTITY` = 1 |
| Teléfono por defecto | `+56 (000) 000-000` | `DEFAULT_PHONE` |
| Correo por defecto | `correo@invalido.com` | `DEFAULT_EMAIL` |

### Cálculo del total

```text
descuento por cantidad = subtotal * 10 %          si hay más de 3 pizzas; si no, 0
descuento de promoción = promocionables * 15 %    si la fecha está en el periodo; si no, 0
IVA                    = (subtotal - descuento por cantidad - descuento de promoción) * 19 %
total                  = subtotal - descuento por cantidad - descuento de promoción + IVA
```

Ambos descuentos parten de su base original, es decir, no se aplican uno sobre el resultado del otro. `promocionables` es la suma de los precios de los productos de clases anotadas con `@Promotional` (`FeaturedPizza` y `Combo`).

### Formatos de datos

| Dato | Formato aceptado | Resultado |
|---|---|---|
| Teléfono | 9 dígitos que empiezan con 9; se ignoran espacios, guiones, paréntesis, `+` y el prefijo `56` (`PHONE_REGEX` = `^9\d{8}$`) | se guarda como `+56 (9XX) XXX-XXX` |
| Correo | `nombre@dominio.ext` (`EMAIL_REGEX`) | se guarda tal cual; vacío significa sin correo |
| Fecha | `AAAA-MM-DD`; vacío es hoy | `LocalDate` |
| Cantidad | entero de 1 a 100 | cantidad de pizzas del pedido |

### Estados del pedido

Un pedido solo avanza `En preparación` (`IN_PREPARATION`) a `Listo` (`READY`) a `En camino` (`ON_THE_WAY`). Cualquier otra transición lanza `IllegalStateException`. Solo se pueden combinar con `+` pedidos del mismo cliente y ambos en preparación.

## 6. Conceptos de Kotlin aplicados

Todas las rutas son relativas a `src/main/kotlin/`.

### Fundamentos

| Concepto | Dónde se usa (archivo y función) |
|---|---|
| Variables y tipos | `service/Constants.kt` (`const val TAX_RATE: Double`, `DISCOUNT_THRESHOLD: Int`); `model/Order.kt` (`var status`, `val id`) |
| Operadores | `service/Calculations.kt`, `calculateTotals` (aritméticos y comparación `>`); `service/Validations.kt`, `isValidQuantity` (`in`) |
| Entrada y salida | `service/Validations.kt`, `readInput` (`print` y `readlnOrNull`); `service/Reports.kt`, `printOrderSummary` (`println`) |
| Conversiones | `service/Validations.kt`, `parseInteger` (`toInt`); `service/Reports.kt`, `printOrderSummary` (`(TAX_RATE * 100).toInt()`); `service/Calculations.kt`, `formatAmount` (`String.format`) |
| `if` como expresión | `service/Validations.kt`, `readTextOrDefault`; `service/Reports.kt`, `promotionPeriodLabel` |
| `when` como expresión | `model/Order.kt`, `changeStatus` (`val isAllowed = when (status)`); `Main.kt`, `main` (`val newOrder = when { ... }`) |
| `try` como expresión | `Main.kt`, `acceptOrder`; `service/Validations.kt`, `parseInteger`; `service/OrderOperations.kt`, `combineOrders` |

### Utilidades

| Concepto | Dónde se usa (archivo y función) |
|---|---|
| Flujos de control | `service/Validations.kt`, `readValidated` (`for` con rango); `service/Reports.kt`, `printMenu` (`for` con `withIndex`) y `printDailyReport` (`for` sobre un arreglo) |
| Arreglos | `service/Reports.kt`, `printDailyReport` (parámetro `Array<Order>`); `Main.kt`, `main` (`toTypedArray()`) |
| Strings | `service/Reports.kt`, `printOrderSummary` (plantillas `$` y `${}`); `service/Validations.kt`, `formatPhone` (`replace`, `removePrefix`, `substring`); `service/TextUtils.kt`, `normalizeText` (`trim`, `lowercase`) |
| Funciones | `service/Calculations.kt`, `calculateTotals` (parámetros y retorno); `service/Validations.kt`, `readValidated` (genérica, recibe lambdas) y `readQuantity` (argumentos con nombre); `service/Validations.kt`, `isValidEmail` (cuerpo de expresión) |
| Colecciones | `service/Inventory.kt`, `calculateRequiredIngredients` (`flatMap`, `groupBy`, `mapValues`) y `findMissingIngredients` (`mapNotNull`); `service/SampleData.kt`, `createInitialInventory` (`MutableMap`); `service/Registries.kt`, `CustomerRegistry` (`mutableSetOf`) |
| POO | `model/Order.kt`, `Order` (clase con propiedades y métodos); `service/Registries.kt`, `CustomerRegistry.register`; `model/OrderStatus.kt` (`enum class` con propiedad `label`) |
| Herencia | `model/User.kt`, `User` (clase `open`) hacia `model/Customer.kt`, `Customer.describe` y `model/DeliveryPerson.kt`, `DeliveryPerson.describe` (`override` y `super.describe()`); `model/Product.kt`, `Product` (abstracta) hacia `model/Pizza.kt` y `model/FeaturedPizza.kt` |
| Modificadores | `open` en `model/User.kt`; `abstract` en `model/Product.kt`; `private set` en `model/Order.kt`, `status`; `private` en `service/Reports.kt`, `SEPARATOR`; `const` en `service/Constants.kt`; `operator` en `model/Order.kt`, `plus` |
| `try`-`catch` | `Main.kt`, `acceptOrder` (captura `IllegalArgumentException` e `IllegalStateException`); `service/Reports.kt`, `printInventoryErrorDemo`; `service/Inventory.kt`, `restock` (`require`); `model/Order.kt`, `changeStatus` (`throw`) |
| Null safety | `model/Order.kt`, `deliveryPerson: DeliveryPerson?`; `service/Reports.kt`, `notifyCustomer` (`?.let`) y `printOrderSummary` (`?:`); `service/Validations.kt`, `readInput` (`readlnOrNull()?.trim()`) |

### Avanzado

| Concepto | Dónde se usa (archivo y función) |
|---|---|
| Regex | `service/Constants.kt`, `EMAIL_REGEX`, `PHONE_REGEX`, `PHONE_SEPARATORS_REGEX`; `service/Validations.kt`, `isValidEmail` (`matches`) y `formatPhone` (`replace`) |
| Ranges | `service/Constants.kt`, `QUANTITY_RANGE` (`1..100`) y `PROMOTION_PERIOD` (`ClosedRange<LocalDate>`); `service/Validations.kt`, `isValidQuantity` e `isWithinPromotionPeriod` (`in`) |
| Anotaciones | `annotations/Promotional.kt`, `Promotional` (`@Target`, `@Retention`); uso en `model/FeaturedPizza.kt` y `model/Combo.kt` |
| Reflection | `service/Promotions.kt`, `isPromotional` y `promotionDescription` (`findAnnotation`); `service/ClassInspector.kt`, `describeClassStructure` (`memberProperties`, `memberFunctions`) |
| Sobrecarga de operadores | `model/Order.kt`, `plus` (`operator fun plus`); `service/OrderOperations.kt`, `combineOrders` (`first + second`) |
| Desestructuración | `model/Customer.kt`, `component1..3`; `model/Order.kt`, `component1..3`; `service/AdvancedReport.kt`, `printDestructuringSection` (`val (name, email, phone) = customer`); `service/Reports.kt`, `printDailyReport` (`val (customer, _, total) = order`) |
| Igualdad `==` y `===` | `model/Product.kt`, `equals` y `hashCode`; `model/Customer.kt`, `equals` y `hashCode`; `service/AdvancedReport.kt`, `printProductEquality` (compara con `==` y `===`); `service/Registries.kt`, `CustomerRegistry.register` |

## 7. Equivalencias de nombres

El enunciado usa nombres en español y el código usa nombres en inglés. Las equivalencias están indicadas en los comentarios KDoc del código.

| Español | Código | Archivo |
|---|---|---|
| Usuario | `User` | `model/User.kt` |
| Cliente | `Customer` | `model/Customer.kt` |
| Repartidor | `DeliveryPerson` | `model/DeliveryPerson.kt` |
| Pedido | `Order` | `model/Order.kt` |
| Estado del pedido (`estadoPedido`) | `OrderStatus` | `model/OrderStatus.kt` |
| Producto | `Product` | `model/Product.kt` |
| Pizza | `Pizza` | `model/Pizza.kt` |
| Bebida | `Beverage` | `model/Beverage.kt` |
| Ingrediente adicional | `ExtraIngredient` | `model/ExtraIngredient.kt` |
| Combo | `Combo` | `model/Combo.kt` |
| `@Promocionable` | `@Promotional` | `annotations/Promotional.kt` |
| `detallarProducto()` | `describeProduct()` | `model/Product.kt` |
| `entregarPedido()` | `deliverOrder()` | `model/DeliveryPerson.kt` |
| Combinar pedidos con el operador `+` | `Order.plus` | `model/Order.kt` |

Los ingredientes del inventario usan claves en inglés y se muestran en español con `ingredientLabel` (`service/Inventory.kt`):

| Clave en el código | Etiqueta en consola |
|---|---|
| `dough` | masa |
| `sauce` | salsa |
| `cheese` | queso |
| `pepperoni` | pepperoni |
| `vegetables` | verduras |

## 8. Decisiones de diseño

- **Formato de teléfono adaptado a Chile.** El formato internacional genérico `+XX (XXX) XXX-XXXX` se adaptó a los celulares chilenos: 9 dígitos que empiezan con 9, guardados como `+56 (9XX) XXX-XXX`. La lectura acepta varias escrituras (`912345678`, `9 1234 5678`, `+56 9 1234 5678`, `56912345678`) y las deja todas en el mismo formato.
- **Descuento de promoción solo sobre productos anotados.** El 15 % se calcula únicamente sobre los productos cuya clase lleva `@Promotional`, que se detectan con reflection (`service/Promotions.kt`) en lugar de usar una lista fija de nombres. Así, una clase nueva queda en promoción con solo anotarla. El descuento por cantidad, en cambio, se calcula sobre todo el subtotal.
- **`==` implementado con `equals()`.** En Kotlin `==` llama a `equals()`, y `===` compara la referencia. `Product` y `Customer` sobrescriben `equals()` y `hashCode()` con atributos normalizados (nombre sin espacios en los bordes y en minúsculas, más categoría o correo), de modo que `CustomerRegistry` y `ProductCatalog` detectan duplicados con un `Set`. Las subclases de `Product` no sobrescriben `equals()` para no romper la simetría.
- **`componentN` manuales en lugar de `data class`.** `Customer` y `Order` declaran `operator fun component1..3` a mano para poder desestructurarlos. No son `data class` por tres razones: una `data class` solo genera `componentN` para las propiedades del constructor primario, y `Customer` recibe `name`, `phone` y `email` como parámetros sin `val` porque los hereda de `User`; `Order.component3()` devuelve el total calculado, que no es una propiedad; y una `data class` generaría `equals()` y `hashCode()` con todas las propiedades del constructor, mientras que aquí la igualdad usa solo algunas (el teléfono y la dirección no participan).

## 9. Ejemplo de salida

Ejecución con la entrada de la sección 2. Resumen del pedido #4 (pizza Cuatro Quesos, cantidad 4, fecha dentro del periodo de promoción, por eso aplica ambos descuentos):

```text
==============================
   RESUMEN DEL PEDIDO #4
==============================
Fecha: 2026-11-10 (dentro del periodo de promoción)
------------------------------
Cliente
  Nombre: Ana Pérez
  Dirección: Av. Siempre Viva 742
  Teléfono: +56 (912) 345-678
  Correo: ana@mail.com
------------------------------
Productos
  Cuatro Quesos - $9990.00 x 4
------------------------------
Totales
  Subtotal: $39960.00
  Descuento por cantidad: $3996.00
  Descuento promoción: $5994.00
  IVA (19%): $5694.30
  Total: $35664.30
------------------------------
Estado: En camino
Repartidor: Matías
Notas: Sin cebolla
==============================
```

Sección del resumen integrado:

```text
------------------------------
PEDIDOS COMBINADOS (operador +)
------------------------------
Pedido precargado (#1, Luis Rojas): Pepperoni x2
Pedido de demostración (#100, Luis Rojas): Vegetariana x2
Pedido combinado (#1, Luis Rojas): Pepperoni x2, Vegetariana x2
  Subtotal: $34960.00
  Descuento por cantidad: $3496.00
  Descuento promoción: $0.00
  IVA (19%): $5978.16
  Total: $37442.16
Intento con clientes distintos (#1 + #2, Marta Silva):
Error: Solo se pueden combinar pedidos del mismo cliente
```

Si el stock no alcanza (por ejemplo, 11 pizzas Pepperoni), la consola muestra esto en lugar de la confirmación:

```text
Pedido rechazado: no hay ingredientes suficientes.
  queso: faltan 2 unidades
  pepperoni: faltan 12 unidades
Pasos para reabastecer:
  1) Solicitar al proveedor la cantidad faltante de: queso, pepperoni
  2) Registrar el ingreso en el inventario.
  3) Reintentar el pedido.
```
