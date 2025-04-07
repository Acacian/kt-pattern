package com.ktpattern.patternmatch.dsl

import com.ktpattern.patternmatch.PatternEvaluator
import com.ktpattern.patternmatch.Pattern

fun <T, R> match(value: T, evaluator: PatternEvaluator<T>, block: MatchBuilder<T, R>.() -> Unit): R? {
    val builder = MatchBuilder<T, R>(evaluator)
    builder.block()
    return builder.evaluate(value)
}
