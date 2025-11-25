package com.github.bcharron.algebra

class Bindings(val map: Map<String, Double>) {
    fun get(s: String) = map.get(s) ?: throw Exception("No binding defined for variable '$s'")
}
