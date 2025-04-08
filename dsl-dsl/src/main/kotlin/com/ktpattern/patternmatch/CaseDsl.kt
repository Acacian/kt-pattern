package com.ktpattern.patternmatch

import com.ktpattern.patternmatch.pattern.TypePattern
import com.ktpattern.patternmatch.pattern.ValuePattern

inline fun <reified TSub : Any, T : Any, R> MatchBuilder<T, R>.whenType(noinline action: () -> R) {
    case(TypePattern(TSub::class.java), action) 
}

fun <T : Any, R> MatchBuilder<T, R>.whenValue(expected: T, action: () -> R) {
    case(ValuePattern(expected, expected::class.java), action) 
}

inline fun <reified TSub : Any, T : Any, R> MatchBuilder<T, R>.caseOf(
    noinline predicate: (TSub) -> Boolean = { true },
    noinline action: () -> R
) {
    val typePattern = TypePattern(TSub::class.java) { value ->
        @Suppress("UNCHECKED_CAST")
        predicate(value as TSub)
    }
    case(typePattern, action)
}
