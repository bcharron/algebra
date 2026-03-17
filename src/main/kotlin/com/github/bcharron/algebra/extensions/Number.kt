package com.github.bcharron.algebra.extensions

operator fun Number.times(other: Number): Number {
    if (this is Long && other is Long) {
        return this * other
    }

    return this.toDouble() * other.toDouble()
}

operator fun Number.plus(other: Number): Number {
    if (this is Long && other is Long) {
        return this + other
    }

    return this.toDouble() + other.toDouble()
}

