package com.ktpattern.patternmatch

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

// RegexPattern
private class RegexPattern(private val regex: Regex) : Pattern<Any> {
    override fun match(value: Any) = value is String && regex.matches(value)
    override fun getType(): Class<*> = RegexPattern::class.java
}

private class RegexPatternEvaluator : PatternEvaluator<Any> {
    override fun supports(pattern: Pattern<*>) = pattern is RegexPattern
    override fun evaluate(pattern: Pattern<Any>, value: Any): PatternMatchResult {
        val regex = pattern as RegexPattern
        return if (regex.match(value)) PatternMatchResult.Success(value)
        else PatternMatchResult.Failure("Regex mismatch")
    }
}

// CustomPattern
private class CustomPattern : Pattern<Any> {
    override fun match(value: Any) = value is String && value.startsWith("hey")
    override fun getType(): Class<*> = CustomPattern::class.java
}

private class CustomPatternEvaluator : PatternEvaluator<Any> {
    override fun supports(pattern: Pattern<*>) = pattern is CustomPattern
    override fun evaluate(pattern: Pattern<Any>, value: Any): PatternMatchResult {
        val custom = pattern as CustomPattern
        return if (custom.match(value)) PatternMatchResult.Success(value)
        else PatternMatchResult.Failure("Custom pattern mismatch")
    }
}

class FullDslIntegrationTest {

    sealed class Animal
    data class Dog(val name: String, val age: Int) : Animal()
    data class Cat(val name: String) : Animal()
    data class Person(val name: String, val age: Int)
    data class Box<T>(val value: T)

    private val regexPattern = RegexPattern(Regex("^hello.*"))
    private val customPattern = CustomPattern()

    private fun buildEvaluator(): CompositeEvaluator<Any> {
        return CompositeEvaluator(
            listOf(
                RegexPatternEvaluator(),
                CustomPatternEvaluator()
            )
        )
    }

    @Test
    fun `should match RegexPattern`() {
        val binder = SnapshotBinder()
        val builder = MatchBuilder<Any, String>(buildEvaluator(), binder)
        builder.case(regexPattern) { "Matched regex" }
        builder.else_ { "No match" }

        val result = builder.evaluate("hello world")
        assertEquals("Matched regex", result)
    }

    @Test
    fun `should match custom pattern`() {
        val binder = SnapshotBinder()
        val builder = MatchBuilder<Any, String>(buildEvaluator(), binder)
        builder.case(customPattern) { "Custom match!" }
        builder.else_ { "No match" }

        val result = builder.evaluate("hey!")
        assertEquals("Custom match!", result)
    }

    @Test
    fun `should fallback to else`() {
        val binder = SnapshotBinder()
        val builder = MatchBuilder<Any, String>(buildEvaluator(), binder)
        builder.case(regexPattern) { "Matched regex" }
        builder.else_ { "No match" }

        val result = builder.evaluate("bye")
        assertEquals("No match", result)
    }

    @Test
    fun `should record matched and not matched snapshots`() {
        val binder = SnapshotBinder()
        val builder = MatchBuilder<Any, String>(buildEvaluator(), binder)
        builder.case(regexPattern) { "Regex" }
        builder.case(customPattern) { "Custom" }
        builder.else_ { "Else" }

        builder.evaluate("hello world")
        builder.evaluate("hey!")
        builder.evaluate("xyz")

        val logs = binder.getAll().values
        logs.forEach { println("→ ${it.status} | ${it.value} | ${it.pattern}") }

        val matched = logs.count { it.status is SnapshotStatus.Matched }
        val notMatched = logs.count { it.status is SnapshotStatus.NotMatched }

        println("Matched: $matched / NotMatched: $notMatched")
        assertTrue(matched >= 2)
        assertTrue(notMatched >= 1)
    }
}
