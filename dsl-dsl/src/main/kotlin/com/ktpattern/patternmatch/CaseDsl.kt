package com.ktpattern.patternmatch

import com.ktpattern.patternmatch.TypePattern
import com.ktpattern.patternmatch.ValuePattern

// 🔧 타입 기반 매칭
inline fun <reified TSub : Any, T : Any, R> MatchBuilder<T, R>.whenType(noinline action: (TSub) -> R) {
    case(TypePattern(TSub::class.java), action)
}

// 🔧 값 기반 매칭
inline fun <reified T : Any, R> MatchBuilder<T, R>.whenValue(expected: T, noinline action: (T) -> R) {
    val pattern: Pattern<T> = ValuePattern(expected, T::class.java)
    case(pattern, action)
}

// 🔧 타입 + 조건 기반 매칭 (Predicate)
inline fun <reified TSub : Any, T : Any, R> MatchBuilder<T, R>.caseOf(
    noinline predicate: (TSub) -> Boolean = { true },
    noinline action: (TSub) -> R
) {
    val typePattern = TypePattern(TSub::class.java) { value ->
        @Suppress("UNCHECKED_CAST")
        predicate(value)
    }
    case(typePattern, action)
}
