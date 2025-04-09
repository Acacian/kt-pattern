package com.ktpattern.patternmatch

interface Pattern<in T> {
    fun match(value: T): Boolean
    fun getType(): Class<*> 
}