package com.ktpattern.patternmatch

class PredicateCondition<T>(
    private val type: Class<T>,
    val predicate: (T) -> Boolean
) : Pattern<T> {

    override fun match(value: T): Boolean {
        return predicate(value)
    }

    override fun getType(): Class<*> {
        return type
    }
}
