package com.crow.mordecaix.base.extensions

const val SHIFT_64 = 64
const val SHIFT_32 = 32

fun encodeShift(input: String, shift: Int): String {
    val sb = StringBuilder(input.length)
    for (ch in input) {
        sb.append((ch.code + shift).toChar())
    }
    return sb.toString()
}

fun decodeShift(input: String, shift: Int): String {
    val sb = StringBuilder(input.length)
    for (ch in input) {
        sb.append((ch.code - shift).toChar())
    }
    return sb.toString()
}