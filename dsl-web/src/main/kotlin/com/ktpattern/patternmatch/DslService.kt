package com.ktpattern.patternmatch

@Service
class DslService {

    fun evaluate(input: Int, patterns: List<Pattern<Int>> = listOf()): String {
        return match(input) {
            // 외부에서 전달된 패턴을 모두 처리
            patterns.forEach { pattern ->
                case(pattern) { "Matched pattern: $input" }
            }

            // 기본 패턴들 (유연하게 확장 가능)
            value(0) then { "zero" }
            predicate({ it % 2 == 0 }) then { "even" }
            else_ { "odd" }
        }
    }
}