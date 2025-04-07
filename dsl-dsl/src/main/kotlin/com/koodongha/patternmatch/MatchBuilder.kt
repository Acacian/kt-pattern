package com.koodongha.patternmatch.dsl

import com.koodongha.patternmatch.MatchResult
import com.koodongha.patternmatch.Pattern
import com.koodongha.patternmatch.PatternEvaluator

class MatchBuilder<T>(
    private val evaluator: PatternEvaluator
) {
    private val cases = mutableListOf<Pair<Pattern, () -> Any>>()
    private var elseCase: (() -> Any)? = null

    fun case(pattern: Pattern, action: () -> Any) {
        cases += pattern to action
    }

    fun else_(action: () -> Any) {
        elseCase = action
    }

    fun evaluate(value: T): Any? {
        for ((pattern, action) in cases) {
            if (evaluator.evaluate(pattern, value).isSuccess()) {
                return action()
            }
        }
        return elseCase?.invoke()
    }
}
