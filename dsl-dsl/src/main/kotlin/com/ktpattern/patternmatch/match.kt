package com.ktpattern.patternmatch

import java.util.ServiceLoader

@Suppress("UNCHECKED_CAST")
fun match(
    value: Any,
    snapshotBinder: SnapshotBinder? = null,
    block: MatchBuilder.() -> Unit
): String {
    val evaluator = ServiceLoader.load(PatternEvaluator::class.java)
        .firstOrNull() as? Evaluator<Any>
        ?: error("❌ PatternEvaluator 구현체를 찾을 수 없습니다")

    val builder = MatchBuilder(evaluator, snapshotBinder).apply(block)
    return builder.evaluate(value) ?: error("❗️No pattern matched and no else_ provided.")
}