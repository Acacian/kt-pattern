package com.ktpattern.patternmatch

class DestructurePattern<T>(
    val clazz: Class<T>,
    val extractor: (T) -> Boolean
) : Pattern<T> {
    override fun match(value: T): Boolean = try { extractor(value) } catch (e: Exception) { false }
    override fun getType(): Class<*> = clazz
}
