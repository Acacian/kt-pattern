package com.ktpattern.patternmatch

class TypePattern<T : Any>(
    val type: Class<T>
) : Pattern<Any> {
    override fun match(value: Any): Boolean {
        return type.isInstance(value) || type.isAssignableFrom(value::class.java)
    }

    override fun getType(): Class<*> {
        return type
    }
}
