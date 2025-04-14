package com.ktpattern.patternmatch

class MatchBuilder<T, R>(
    private val evaluator: PatternEvaluator<T>,
    private val snapshotBinder: SnapshotBinder? = null
) {
    private val cases = mutableListOf<Pair<Pattern<T>, (T) -> R>>()
    private var elseCase: ((T) -> R)? = null

    @Suppress("UNCHECKED_CAST")
    fun <SubT : T> case(pattern: Pattern<SubT>, action: (SubT) -> R) {
        cases.add(pattern as Pattern<T> to { value -> action(value as SubT) })
    }

    fun else_(action: (T) -> R) {
        elseCase = action
    }

    fun evaluate(value: T): R? {
        for ((pattern, action) in cases) {
            val result = evaluator.evaluate(pattern, value)

            val status = if (result.isSuccess()) SnapshotStatus.Matched else SnapshotStatus.NotMatched

            snapshotBinder?.saveSnapshot(
                identifier = "pattern:${pattern::class.simpleName}-${pattern.hashCode()}",
                snapshot = Snapshot(
                    value = value as Any,
                    pattern = pattern.toString(),
                    status = status
                )
            )

            if (result is PatternMatchResult.Success) {
                return action(result.value as T)
            }
        }

        if (elseCase != null) {
            snapshotBinder?.saveSnapshot(
                identifier = "else-${value.hashCode()}",
                snapshot = Snapshot(
                    value = value as Any,
                    pattern = "else",
                    status = SnapshotStatus.NotMatched
                )
            )
            return elseCase?.invoke(value)
        }

        return null
    }
}
