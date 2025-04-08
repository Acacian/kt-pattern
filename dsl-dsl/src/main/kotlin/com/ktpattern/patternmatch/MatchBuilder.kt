package com.ktpattern.patternmatch

import com.ktpattern.patternmatch.PatternMatchResult
import com.ktpattern.patternmatch.Pattern
import com.ktpattern.patternmatch.PatternEvaluator

class MatchBuilder<T, R>(
    private val evaluator: PatternEvaluator<T>
) {
    private val cases = mutableListOf<Pair<Pattern<T>, () -> R>>()
    private var elseCase: (() -> R)? = null

    fun case(pattern: Pattern<T>, action: () -> R) {
        cases += pattern to action
    }

    fun else_(action: () -> R) {
        elseCase = action
    }

    fun evaluate(value: T): R? {
        for ((pattern, action) in cases) {
            val result: PatternMatchResult = evaluator.evaluate(pattern, value)
            if (result.isSuccess()) {
                return action()
            }
        }
        return elseCase?.invoke()
    }
}
