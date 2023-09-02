package com.gradle.tutorial

class FizzBuzzProcessor {
    companion object {
        fun convert(fizzBuzz: Int): String {
            return when {
                fizzBuzz % 15 == 0 -> "FizzBuzz"
                fizzBuzz % 3 == 0 -> "Fizz"
                fizzBuzz % 5 == 0 -> "Buzz"
                else -> fizzBuzz.toString()
            }
        }
    }
}