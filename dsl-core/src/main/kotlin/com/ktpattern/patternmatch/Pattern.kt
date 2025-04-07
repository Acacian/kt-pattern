package com.ktpattern.patternmatch

interface Pattern<T> {
    fun match(value: T): Boolean
    fun getType(): Class<*> 
}