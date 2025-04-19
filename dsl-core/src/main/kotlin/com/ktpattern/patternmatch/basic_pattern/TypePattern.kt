package com.ktpattern.patternmatch

class TypePattern<T : Any>(
    val targetType: Class<T>,
    val predicate: ((T) -> Boolean)? = null
) : Pattern<T> {

    @Suppress("UNCHECKED_CAST")
    override fun match(value: Any): Boolean {
        if (!targetType.isInstance(value)) return false
        return predicate?.invoke(value as T) ?: true
    }

    override fun getType(): Class<*> = targetType
}