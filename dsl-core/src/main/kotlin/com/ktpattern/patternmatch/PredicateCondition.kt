package com.ktpattern.patternmatch

class PredicateCondition<T>(
    val predicate: (T) -> Boolean
) : Pattern<T> {
    override fun match(value: T): Boolean {
        return predicate(value)
    }

    override fun getType(): Class<*> {
        return value::class.java
    }
}
