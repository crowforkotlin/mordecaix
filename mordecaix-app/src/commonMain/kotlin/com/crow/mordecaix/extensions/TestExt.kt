package com.crow.mordecaix.extensions

inline fun<T> measureTime(block: () -> T?) : T? {
    val result: T?
    val time = kotlin.time.measureTime { result = block() }
    println("spend time : ${time.inWholeMilliseconds} \t result : $result")
    return result
}

inline fun<T> measureTimeNotnull(block: () -> T) : T {
    val result: T
    val time = kotlin.time.measureTime { result = block() }
    println("spend time : ${time.inWholeMilliseconds} \t result : $result")
    return result
}