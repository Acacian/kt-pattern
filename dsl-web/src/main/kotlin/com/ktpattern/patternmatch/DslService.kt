package com.ktpattern.patternmatch

@Service
class DslService {

    fun evaluate(input: Any, patterns: List<Pattern<Any>> = listOf()): String {
        return match(input, patterns) {
            // 기본 패턴들
            value(0) then { "zero" }
            predicate({ it % 2 == 0 }) then { "even" }
            else_ { "odd" }
        }
    }
}