package com.ktpattern.examples  // 간단한 패키지명

import com.ktpattern.patternmatch.*  // 라이브러리에서 제공하는 패턴 매칭 관련 클래스들

fun main() {
    val shape: Shape = Circle(5.0)

    // Match shape based on its type and value
    val result = match(shape) {
        case<Circle> { it.radius > 4.0 } -> "Big Circle"
        case<Square> -> "Square"
        else -> "Unknown Shape"
    }

    println(result)  // Output: Big Circle

    // Additional example: Matching based on value
    val number = 42
    val numberResult = match(number) {
        case<5> -> "Five"
        case<42> -> "The Answer"
        else -> "Unknown Number"
    }

    println(numberResult)  // Output: The Answer
}
