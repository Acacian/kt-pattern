package com.ktpattern.patternmatch

class ValuePattern<T>(
    val value: T,
    private val type: Class<T>,
    val predicate: (T) -> Boolean = { true }
) : Pattern<T> {

    @Suppress("UNCHECKED_CAST")
    override fun match(value: Any): Boolean {
        return (value == this.value) && predicate(value as T)
    }

    override fun getType(): Class<*> = type
}