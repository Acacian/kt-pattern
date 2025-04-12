package com.ktpattern.patternmatch

class MatchBuilder<T, R>(
    private val evaluator: PatternEvaluator<T>,
    private val snapshotBinder: SnapshotBinder<in T>? = null
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

            snapshotBinder?.saveSnapshot(
                pattern.toString(),
                Snapshot(
                    value = value as Any,
                    pattern = pattern.toString(),
                    status = if (result.isSuccess()) SnapshotStatus.Matched else SnapshotStatus.NotMatched
                )
            )

            if (result is PatternMatchResult.Success) {
                return action(result.value as T)
            }
        }
        return elseCase?.invoke()
    }
}
