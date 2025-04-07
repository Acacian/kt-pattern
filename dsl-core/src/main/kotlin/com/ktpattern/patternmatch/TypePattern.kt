package com.ktpattern.patternmatch

import kotlin.reflect.KClass

data class TypePattern<T : Any>(
    val type: KClass<T>
) : Pattern {
    override fun match(value: Any?): MatchResult {
        return if (type.isInstance(value)) {
            MatchResult.Success()
        } else {
            MatchResult.Failure
        }
    }
}
