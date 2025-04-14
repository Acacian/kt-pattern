package com.ktpattern.patternmatch

class PredicateCondition<T>(
    private val type: Class<T>,
    val predicate: (T) -> Boolean
) : Pattern<T> {

    @Suppress("UNCHECKED_CAST")
    override fun match(value: Any): Boolean {
        return if (type.isInstance(value)) {
            predicate(value as T)
        } else {
            false
        }
    }

    override fun getType(): Class<*> = type
}
