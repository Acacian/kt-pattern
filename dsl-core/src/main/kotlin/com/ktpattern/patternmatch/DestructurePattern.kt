package com.ktpattern.patternmatch

class DestructurePattern<T>(
    val extractor: (T) -> Boolean
) : Pattern<T> {
    override fun match(value: T): Boolean {
        return try {
            extractor(value)
        } catch (e: Exception) {
            false
        }
    }

    override fun getType(): Class<*> {
        return value::class.java
    }
}
