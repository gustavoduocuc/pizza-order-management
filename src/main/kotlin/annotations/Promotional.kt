package annotations

/**
 * Marca las clases de producto elegibles para promociones. Equivale a @Promocionable.
 * Se conserva en tiempo de ejecución para detectarlas con reflection.
 * [description] describe la promoción.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Promotional(val description: String)
