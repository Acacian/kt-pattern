package com.ktpattern.patternmatch

@RestController
@RequestMapping("/api/dsl")
class DslController(
    private val dslService: DslService
) {

    @PostMapping("/match")
    fun evaluate(@RequestBody request: MatchRequest): ResponseEntity<String> {
        val customPattern = object : Pattern<Any> {
            override fun match(value: Any): Boolean =
                value is String && value.startsWith("he")
            override fun getType(): Class<*> = String::class.java
        }
        
        val result = dslService.evaluate(request.input)
        return ResponseEntity.ok(result)
    }
}

data class MatchRequest(val input: Int)
