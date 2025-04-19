package com.ktpattern.patternmatch

abstract class SimplePattern<T : Any>(
    private val type: Class<T>
) : CustomPattern<T> {

    override fun getType(): Class<*> = type

    override fun match(value: Any): Boolean {
        return try {
            @Suppress("UNCHECKED_CAST")
            type.isInstance(value) && matches(value as T)
        } catch (e: Exception) {
            false
        }
    }

    override fun evaluate(value: T): PatternMatchResult {
        return try {
            if (matches(value)) PatternMatchResult.Success(value)
            else PatternMatchResult.Failure("❌ 매칭 실패: $value 은(는) 조건을 만족하지 않음")
        } catch (e: Exception) {
            PatternMatchResult.Failure("⚠️ 예외 발생: ${e.message}")
        }
    }

    protected abstract fun matches(value: T): Boolean
}
