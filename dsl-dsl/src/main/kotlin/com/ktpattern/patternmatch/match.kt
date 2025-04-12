package com.ktpattern.patternmatch

import java.util.ServiceLoader

inline fun <reified T, R> match(
    value: T,
    snapshotBinder: SnapshotBinder<in T>? = null,
    block: MatchBuilder<T, R>.() -> Unit
): R? {
    val rawEvaluator = ServiceLoader.load(PatternEvaluator::class.java)
        .firstOrNull() as? PatternEvaluator<Any>
        ?: error("No PatternEvaluator found via ServiceLoader")

    @Suppress("UNCHECKED_CAST")
    val evaluator = rawEvaluator as PatternEvaluator<T>

    val builder: MatchBuilder<T, R> = MatchBuilder(evaluator, snapshotBinder)
    builder.block()

    val result: R? = builder.evaluate(value)
    return result
}
