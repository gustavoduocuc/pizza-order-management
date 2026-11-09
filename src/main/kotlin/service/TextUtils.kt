package service

/** Normaliza un texto para compararlo: sin espacios en los bordes y en minúsculas. */
fun normalizeText(text: String): String = text.trim().lowercase()
