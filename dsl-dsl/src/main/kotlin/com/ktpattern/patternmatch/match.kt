package com.ktpattern.patternmatch

import com.ktpattern.patternmatch.Pattern
import com.ktpattern.patternmatch.PatternEvaluator
import java.util.ServiceLoader

inline fun <reified T, R> match(
    value: T,
    block: MatchBuilder<T, R>.() -> Unit
): R? {
    @Suppress("UNCHECKED_CAST")
    val evaluator = ServiceLoader.load(PatternEvaluator::class.java)
        .firstOrNull() as? PatternEvaluator<T>
        ?: error("No PatternEvaluator found via ServiceLoader")

    val builder = MatchBuilder<T, R>(evaluator) 
    builder.block()
    return builder.evaluate(value)
}
