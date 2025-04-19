package com.ktpattern.patternmatch

// ✅ 간결한 DSL: 타입 고정 버전 (Any, String)
fun <T : Any> MatchBuilder.withPattern(
    pattern: Pattern<T>,
    action: (T) -> String
) {
    @Suppress("UNCHECKED_CAST")
    case(pattern as Pattern<Any>) { value -> action(value as T) }
}

// 🔧 타입 기반 매칭
inline fun <reified TSub : Any> MatchBuilder.whenType(
    noinline action: (TSub) -> String
) {
    val pattern = TypePattern<TSub>(TSub::class.java)
    withPattern(pattern, action)
}


// 🔧 값 기반 매칭
inline fun <reified TSub : Any> MatchBuilder.whenValue(
    expected: TSub,
    noinline action: (TSub) -> String
) {
    val pattern = ValuePattern(expected, TSub::class.java)
    withPattern(pattern, action)
}


// 🔧 타입 + 조건 기반 매칭 (Predicate)
inline fun <reified TSub : Any> MatchBuilder.caseOf(
    noinline predicate: (TSub) -> Boolean = { true },
    noinline action: (TSub) -> String
) {
    val pattern = TypePattern(TSub::class.java, predicate)
    withPattern(pattern, action)
}
