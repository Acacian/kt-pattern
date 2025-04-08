package com.ktpattern.patternmatch

class TypePattern<T : Any>(
    val targetType: Class<T>,
    val predicate: ((T) -> Boolean)? = null
) : Pattern<Any> { 

    override fun match(value: Any): Boolean {
        return targetType.isInstance(value) &&
               (predicate?.invoke(value) ?: true) 
    }

    override fun getType(): Class<*> = targetType
}