package com.ktpattern.patternmatch

class TypePattern<T : Any>(
    val targetType: Class<T>,
    val predicate: ((T) -> Boolean)? = null
) : Pattern<T> {

    override fun match(value: T): Boolean {
        return predicate?.invoke(value) ?: true
    }

    override fun getType(): Class<*> = targetType
}