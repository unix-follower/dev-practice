package org.example0.math

def example0(number: Double): Double = {
    val f = (x: Double) => 3 * Math.pow(x, 2) + 2 * x - 5
    f(number)
}
