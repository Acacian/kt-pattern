package com.ktpattern.patternmatch

import com.ktpattern.patternmatch.TypePattern
import com.ktpattern.patternmatch.ValuePattern

// 🔧 타입 기반 매칭
inline fun <reified TSub : T, T : Any, R> MatchBuilder<T, R>.whenType(
    noinline action: (TSub) -> R
) {
    val typedPattern = TypePattern<TSub>(TSub::class.java)
    @Suppress("UNCHECKED_CAST")
    case(typedPattern as Pattern<T>) { value -> action(value as TSub) }
}

// 🔧 값 기반 매칭
inline fun <reified TSub : T, T : Any, R> MatchBuilder<T, R>.whenValue(
    expected: TSub,
    noinline action: (TSub) -> R
) {
    val typedPattern = ValuePattern<TSub>(expected, TSub::class.java)
    @Suppress("UNCHECKED_CAST")
    case(typedPattern as Pattern<T>) { value -> action(value as TSub) }
}

// 🔧 타입 + 조건 기반 매칭 (Predicate)
inline fun <reified TSub : T, T : Any, R> MatchBuilder<T, R>.caseOf(
    noinline predicate: (TSub) -> Boolean = { true },
    noinline action: (TSub) -> R
) {
    val typedPattern = TypePattern(TSub::class.java) { value ->
        @Suppress("UNCHECKED_CAST")
        predicate(value as TSub)
    }
    @Suppress("UNCHECKED_CAST")
    case(typedPattern as Pattern<T>) { value -> action(value as TSub) }
}