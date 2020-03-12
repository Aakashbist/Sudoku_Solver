package com.aakashbista.sudoku

fun String.toIntOrNull(): Int? = try {
    this.toInt()
} catch (ex: Exception) {
  null
}
