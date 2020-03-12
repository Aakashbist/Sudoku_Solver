package com.aakashbista.sudoku

import android.content.res.Resources

fun Int.dpToPixels(resource: Resources): Int {
    return (this / resource.displayMetrics.density).toInt()
}

fun Int.pxToDips(resources: Resources): Int {
    return (this * resources.displayMetrics.density).toInt()
}

