package com.ktpattern.patternmatch

import java.util.ServiceLoader

@Suppress("UNCHECKED_CAST")
fun <T, R> match(
    value: T,
    snapshotBinder: SnapshotBinder? = null,
    block: MatchBuilder<T, R>.() -> Unit
): R {
    val rawEvaluators = ServiceLoader.load(PatternEvaluator::class.java).toList()
    val typedEvaluators = rawEvaluators.map { it as PatternEvaluator<T> }

    val builder: MatchBuilder<T, R> = MatchBuilder(
        CompositeEvaluator(typedEvaluators),
        snapshotBinder
    )

    builder.block()
    return builder.evaluate(value)!!
}
