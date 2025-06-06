package com.ktpattern.patternmatch

class AndPattern<T>(val patterns: List<Pattern<T>>) : Pattern<T> {
    override fun match(value: Any): Boolean =
        patterns.all { it.match(value) }

    override fun getType(): Class<*> =
        patterns.firstOrNull()?.getType() ?: Any::class.java
}

class OrPattern<T>(val patterns: List<Pattern<T>>) : Pattern<T> {
    override fun match(value: Any): Boolean =
        patterns.any { it.match(value) }

    override fun getType(): Class<*> =
        patterns.firstOrNull()?.getType() ?: Any::class.java
}