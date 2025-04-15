package com.ktpattern.patternmatch

/**
 * 여러 PatternEvaluator를 위임 형태로 등록해서
 * 해당 패턴을 지원하는 evaluator에게 평가 책임을 넘기는 체이닝 처리기.
 *
 * - 내부적으로 순서대로 supports()를 검사하며 매칭 가능한 evaluator를 찾아 평가함.
 * - supports()가 true인 evaluator가 먼저 발견되면 해당 evaluator로 평가가 위임됨.
 */
class CompositeEvaluator<T>(
    private val delegates: List<PatternEvaluator<T>>
) : PatternEvaluator<T> {

    // 항상 true → 개별 evaluator에 위임하기 때문에 자체 supports 판단은 하지 않음
    override fun supports(pattern: Pattern<*>): Boolean = true

    override fun evaluate(pattern: Pattern<T>, value: T): PatternMatchResult {
        val evaluator = delegates.firstOrNull { it.supports(pattern) }
            ?: error("❌ No evaluator supports this pattern: $pattern")

        return evaluator.evaluate(pattern, value)
    }
}