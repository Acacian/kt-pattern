package com.ktpattern.patternmatch

@Service
class DslService {

    fun evaluate(input: Int): String {
        return match(input) {
            value(0) then { "zero" }
            predicate({ it % 2 == 0 }) then { "even" }
            else_ { "odd" }
        }
    }
}
