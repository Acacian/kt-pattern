package com.ktpattern.patternmatch

import java.util.ServiceLoader

inline fun <reified T, R> match(
    value: T,
    snapshotBinder: SnapshotBinder? = null,
    block: MatchBuilder<T, R>.() -> Unit
): R? {
    val evaluators = ServiceLoader.load(PatternEvaluator::class.java).toList()

    val builder: MatchBuilder<T, R> = MatchBuilder(
        CompositeEvaluator(evaluators),
        snapshotBinder
    )

    builder.block()
    return builder.evaluate(value)
}
