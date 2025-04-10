package com.ktpattern.patternmatch

@RestController
@RequestMapping("/api/dsl")
class DslController(
    private val dslService: DslService
) {

    @PostMapping("/match")
    fun evaluate(@RequestBody request: MatchRequest): ResponseEntity<String> {
        val patterns = request.patterns ?: listOf()
        
        val result = dslService.evaluate(request.input)
        return ResponseEntity.ok(result)
    }
}

data class MatchRequest(val input: Int)
