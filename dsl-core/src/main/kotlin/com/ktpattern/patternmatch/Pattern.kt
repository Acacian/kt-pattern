package com.ktpattern.patternmatch

interface Pattern<T> {
    fun match(value: Any): Boolean
    fun getType(): Class<*> 
}