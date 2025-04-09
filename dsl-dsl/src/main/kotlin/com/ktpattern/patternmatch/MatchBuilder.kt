package com.ktpattern.patternmatch

// import com.ktpattern.patternmatch.PatternMatchResult
// import com.ktpattern.patternmatch.Pattern
// import com.ktpattern.patternmatch.PatternEvaluator

class MatchBuilder<T, R>(
    private val evaluator: PatternEvaluator<T>
) {
    private val cases = mutableListOf<Pair<Pattern<T>, (T) -> R>>()
    private var elseCase: (() -> R)? = null

    @Suppress("UNCHECKED_CAST")
    fun <SubT : T> case(pattern: Pattern<SubT>, action: (SubT) -> R) {
        cases.add(pattern as Pattern<T> to { value -> action(value as SubT) })
    }

    fun else_(action: () -> R) {
        elseCase = action
    }

    fun evaluate(value: T): R? {
        for ((pattern, action) in cases) {
            val result = evaluator.evaluate(pattern, value)
            if (result is PatternMatchResult.Success) {
                @Suppress("UNCHECKED_CAST")
                return action(result.value as T)
            }
        }
        return elseCase?.invoke()
    }
}
