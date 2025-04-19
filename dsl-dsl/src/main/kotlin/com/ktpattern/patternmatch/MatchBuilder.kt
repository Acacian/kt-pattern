package com.ktpattern.patternmatch

class MatchBuilder(
    private val evaluator: Evaluator<Any>,
    private val snapshotBinder: SnapshotBinder? = null
) {
    private val cases = mutableListOf<Pair<Pattern<Any>, (Any) -> String>>()
    private var elseCase: ((Any) -> String)? = null

    @Suppress("UNCHECKED_CAST")
    fun <SubT : Any> case(pattern: Pattern<SubT>, action: (SubT) -> String) {
        cases.add(pattern as Pattern<Any> to { value -> action(value as SubT) })
    }

    fun else_(action: (Any) -> String) {
        elseCase = action
    }

    fun evaluate(value: Any): String? {
        for ((pattern, action) in cases) {
            val result = evaluator.evaluate(pattern, value)

            val status = if (result.isSuccess()) SnapshotStatus.Matched else SnapshotStatus.NotMatched

            snapshotBinder?.saveSnapshot(
                Snapshot(
                    value = value,
                    pattern = pattern.toString(),
                    status = status
                )
            )

            if (result is PatternMatchResult.Success) {
                return action(result.value)
            }
        }

        // else case fallback
        elseCase?.let {
            snapshotBinder?.saveSnapshot(
                Snapshot(
                    value = value,
                    pattern = "else",
                    status = SnapshotStatus.NotMatched
                )
            )
            return it(value)
        }

        return null
    }
}
