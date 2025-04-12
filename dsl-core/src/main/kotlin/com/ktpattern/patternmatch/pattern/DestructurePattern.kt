package com.ktpattern.patternmatch

class DestructurePattern<T>(
    val clazz: Class<T>,
    val extractor: (T) -> Boolean
) : Pattern<T> {

    override fun match(value: Any): Boolean {
        return if (clazz.isInstance(value)) {
            try {
                @Suppress("UNCHECKED_CAST")
                extractor(value as T)
            } catch (e: Exception) {
                false
            }
        } else {
            false
        }
    }    

    override fun getType(): Class<*> = clazz
}
