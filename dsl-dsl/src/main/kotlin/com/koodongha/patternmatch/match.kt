package com.koodongha.patternmatch.dsl

fun <T> match(value: T, block: MatchBuilder<T>.() -> Unit): Any? {
    val builder = MatchBuilder<T>()
    builder.block()
    return builder.evaluate(value)
}
