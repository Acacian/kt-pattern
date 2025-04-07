package com.ktpattern.patternmatch

class ValuePattern<T>(
    val value: T,
    val predicate: (T) -> Boolean = { true }
) : Pattern<T> {
    override fun match(value: T): Boolean {
        return this.value == value && predicate(value)
    }

    override fun getType(): Class<*> {
        return value::class.java
    }
}
